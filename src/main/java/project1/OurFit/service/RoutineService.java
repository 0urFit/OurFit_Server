package project1.OurFit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project1.OurFit.entity.*;
import project1.OurFit.repository.*;
import project1.OurFit.response.ExerciseDetailDto;
import project1.OurFit.response.ExerciseRoutineWithEnrollmentStatusDto;
import project1.constant.exception.BaseException;


import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static project1.constant.response.JsonResponseStatus.*;


@RequiredArgsConstructor
@Service
public class RoutineService {
    private final ExerciseLikeRepository exerciseLikeRepository;
    private final MemberRepository memberRepository;
    private final ExerciseRoutineRepository exerciseRoutineRepository;
    private final ExerciseEnrollRepository exerciseEnrollRepository;
    private final ExerciseDetailRepository exerciseDetailRepository;
    private final ExerciseDetailSetRepository exerciseDetailSetRepository;

    public void postLike(String userEmail, Long routineId) {
        Member member=memberRepository.findByEmail(userEmail)
                .orElseThrow(()-> new BaseException(NOT_FOUND_MEMBER));
        ExerciseRoutine exerciseRoutine = exerciseRoutineRepository.findById(routineId)
                .orElseThrow(()-> new BaseException(NOT_FOUND_ROUTINE));
        ExerciseLike like = new ExerciseLike(member,exerciseRoutine);
        exerciseLikeRepository.save(like);
    }

    public void deleteLike(String userEmail, Long routineId) { //걍 좋아요 테이블
        ExerciseLike exerciseLike = exerciseLikeRepository
                .findByMemberEmailAndExerciseRoutineId(userEmail, routineId)
                .orElseThrow(() -> new BaseException(NOTFOUND));
        exerciseLikeRepository.delete(exerciseLike);
    }

    public List<ExerciseRoutineWithEnrollmentStatusDto> getExerciseRoutineByCategory(String category, String userEmail) {
        List<ExerciseRoutine> exerciseRoutineList = exerciseRoutineRepository.findByCategory(category);
        return getExerciseRoutineWithEnrollmentStatus(userEmail, exerciseRoutineList);
    }

    public List<ExerciseRoutineWithEnrollmentStatusDto> getExerciseRoutine(String userEmail) {
        List<ExerciseRoutine> exerciseRoutineList = exerciseRoutineRepository.findAll();
        return getExerciseRoutineWithEnrollmentStatus(userEmail, exerciseRoutineList);
    }

    private List<ExerciseRoutineWithEnrollmentStatusDto> getExerciseRoutineWithEnrollmentStatus(
            String userEmail, List<ExerciseRoutine> exerciseRoutineList) {
        List<ExerciseRoutineWithEnrollmentStatusDto> result = new ArrayList<>();

        List<ExerciseEnroll> exerciseEnrolls = exerciseEnrollRepository.findAllByMemberEmail(userEmail);

        Set<ExerciseRoutine> enrolledRoutines = exerciseEnrolls.stream()
                .map(ExerciseEnroll::getExerciseRoutine)
                .collect(Collectors.toSet());

        for (ExerciseRoutine exerciseRoutine : exerciseRoutineList) {
            boolean isEnrolled = enrolledRoutines.contains(exerciseRoutine);
            ExerciseRoutineWithEnrollmentStatusDto dto =
                    new ExerciseRoutineWithEnrollmentStatusDto(exerciseRoutine, isEnrolled);
            result.add(dto);
        }

        return result;
    }

    public List<ExerciseDetailDto> getExerciseDetails(String category, Long id, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

        List<ExerciseDetail> details = exerciseDetailRepository.findAllByWeeksAndExerciseRoutineCategory(id, category);

        Map<Long, List<ExerciseDetailSet>> setsMap = getExerciseDetailSetsMap(details);

        Map<Integer, Map<String, Map<String, List<ExerciseDetail>>>> detailsByWeekAndDayAndName = groupExerciseDetails(details);

        return buildExerciseDetailDtoList(detailsByWeekAndDayAndName, member, setsMap);
    }

    private Map<Long, List<ExerciseDetailSet>> getExerciseDetailSetsMap(List<ExerciseDetail> details) {
        List<Long> exerciseDetailIds = details.stream()
                .map(ExerciseDetail::getId)
                .collect(Collectors.toList());

        List<ExerciseDetailSet> sets = exerciseDetailSetRepository
                .findAllByExerciseDetailIdInOrderBySequence(exerciseDetailIds);

        return sets.stream()
                .collect(Collectors.groupingBy(set -> set.getExerciseDetail().getId()));
    }

    private Map<Integer, Map<String, Map<String, List<ExerciseDetail>>>> groupExerciseDetails(List<ExerciseDetail> details) {
        return details.stream()
                .collect(Collectors.groupingBy(
                        ExerciseDetail::getWeeks,
                        Collectors.groupingBy(
                                ExerciseDetail::getDay,
                                Collectors.collectingAndThen(
                                        Collectors.groupingBy(ExerciseDetail::getName),
                                        map -> map.entrySet().stream().collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                entry -> entry.getValue().isEmpty() ? Collections.emptyList() : entry.getValue()
                                        ))
                                )
                        )
                ));
    }

    private List<ExerciseDetailDto> buildExerciseDetailDtoList(
            Map<Integer, Map<String, Map<String, List<ExerciseDetail>>>> detailsByWeekAndDayAndName,
            Member member,
            Map<Long, List<ExerciseDetailSet>> setsMap) {
        return detailsByWeekAndDayAndName.entrySet().stream()
                .map(entry -> buildExerciseDetailDto(entry.getKey(), entry.getValue(), member, setsMap))
                .collect(Collectors.toList());
    }

    private ExerciseDetailDto buildExerciseDetailDto(
            Integer weeks,
            Map<String, Map<String, List<ExerciseDetail>>> detailsByDayAndName,
            Member member,
            Map<Long, List<ExerciseDetailSet>> setsMap) {
        ExerciseDetailDto dto = new ExerciseDetailDto();
        dto.setWeeks(weeks);

        List<ExerciseDetailDto.day> days = detailsByDayAndName.entrySet().stream()
                .sorted(Comparator.comparingInt(dayEntry -> getDayOrder(dayEntry.getKey())))
                .map(dayEntry -> buildDayDto(dayEntry.getKey(), dayEntry.getValue(), member, setsMap))
                .collect(Collectors.toList());

        dto.setDays(days);
        return dto;
    }

    private ExerciseDetailDto.day buildDayDto(
            String day,
            Map<String, List<ExerciseDetail>> detailsByName,
            Member member,
            Map<Long, List<ExerciseDetailSet>> setsMap) {
        ExerciseDetailDto.day dayDto = new ExerciseDetailDto.day();
        dayDto.setDay(day);

        List<ExerciseDetailDto.day.exercises> exercisesList = detailsByName.entrySet().stream()
                .sorted(Comparator.comparingInt(exerciseEntry -> exerciseEntry.getValue().get(0).getSequence()))
                .map(nameEntry -> buildExerciseDto(nameEntry.getKey(), nameEntry.getValue(), member, setsMap))
                .collect(Collectors.toList());
        dayDto.setExercises(exercisesList);
        return dayDto;
    }

    private ExerciseDetailDto.day.exercises buildExerciseDto(
            String name,
            List<ExerciseDetail> exerciseDetails,
            Member member,
            Map<Long, List<ExerciseDetailSet>> setsMap) {
        ExerciseDetailDto.day.exercises exercisesDto = new ExerciseDetailDto.day.exercises();
        exercisesDto.setName(name);

        List<ExerciseDetailDto.day.exercises.SetDetail> setsList = exerciseDetails.stream()
                .flatMap(detail -> buildSetDtoList(detail, member, setsMap))
                .collect(Collectors.toList());

        exercisesDto.setSets(setsList);
        return exercisesDto;
    }

    private Stream<ExerciseDetailDto.day.exercises.SetDetail> buildSetDtoList(
            ExerciseDetail detail,
            Member member,
            Map<Long, List<ExerciseDetailSet>> setsMap) {
        List<ExerciseDetailSet> sets = setsMap.getOrDefault(detail.getId(), Collections.emptyList());
        return sets.stream().map(set -> buildSetDetailDto(set, member));
    }

    private ExerciseDetailDto.day.exercises.SetDetail buildSetDetailDto(
            ExerciseDetailSet set,
            Member member) {
        ExerciseDetailDto.day.exercises.SetDetail setDto = new ExerciseDetailDto.day.exercises.SetDetail();
        setDto.setReps(set.getReps());
        setDto.setSequence(set.getSequence());
        setDto.setWeight(calculateWeight(member, set));
        return setDto;
    }

    private int getDayOrder(String day) {
        return switch (day) {
            case "Mon" -> 0;
            case "Tue" -> 1;
            case "Wed" -> 2;
            case "Thu" -> 3;
            case "Fri" -> 4;
            case "Sat" -> 5;
            default -> Integer.MAX_VALUE;
        };
    }

    private double calculateWeight(Member member, ExerciseDetailSet set) {
        String exercise = set.getExercise();
        double increase = set.getIncrease();

        if (exercise == null)
            return set.getWeight();

        Double memberWeight = switch (exercise) {
            case "squat" -> member.getSquat();
            case "deadlift" -> member.getDeadlift();
            case "benchpress" -> member.getBenchpress();
            case "overheadpress" -> member.getOverheadpress();
            default -> null;
        };

        if (memberWeight == null) {
            return set.getWeight();
        } else {
            return memberWeight * increase;
        }
    }
}
