package project1.OurFit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project1.OurFit.entity.*;
import project1.OurFit.repository.*;
import project1.OurFit.response.ExerciseDetailDto;
import project1.OurFit.response.ExerciseRoutineWithEnrollmentStatusDto;
import project1.constant.exception.BaseException;
import project1.constant.response.JsonResponse;
import project1.constant.response.JsonResponseStatus;


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
    private final EnrollDetailRepository enrollDetailRepository;
    private final EnrollDetailSetRepository enrollDetailSetRepository;



    public void postLike(String userEmail, Long routineId) {
        Member member=memberRepository.findByEmail(userEmail)
                .orElseThrow(()-> new BaseException(NOT_FOUND_MEMBER));
        ExerciseRoutine exerciseRoutine = exerciseRoutineRepository.findById(routineId)
                .orElseThrow(()-> new BaseException(NOT_FOUND_ROUTINE));

        if (exerciseLikeRepository.existsByMemberEmailAndExerciseRoutineId(userEmail, routineId))
            return;

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
        List<ExerciseLike> exerciseLikes = exerciseLikeRepository.findAllByMemberEmail(userEmail);

        Set<ExerciseRoutine> enrolledRoutines = exerciseEnrolls.stream()
                .map(ExerciseEnroll::getExerciseRoutine)
                .collect(Collectors.toSet());

        Set<ExerciseRoutine> likedRoutines = exerciseLikes.stream()
                .map(ExerciseLike::getExerciseRoutine)
                .collect(Collectors.toSet());

        for (ExerciseRoutine exerciseRoutine : exerciseRoutineList) {
            boolean isEnrolled = enrolledRoutines.contains(exerciseRoutine);
            boolean isLiked = likedRoutines.contains(exerciseRoutine);
            ExerciseRoutineWithEnrollmentStatusDto dto =
                    new ExerciseRoutineWithEnrollmentStatusDto(exerciseRoutine, isEnrolled, isLiked);
            result.add(dto);
        }

        return result;
    }

    public List<ExerciseDetailDto> getExerciseDetails(String category, Long routineId, String email, int week) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

        List<ExerciseDetail> details = exerciseDetailRepository.findAllByWeekAndExerciseRoutineCategory(routineId, category, week);

        Map<Integer, Map<String, List<ExerciseDetail>>> detailsByWeekAndDay = groupExerciseDetails(details);

        return buildExerciseDetailDtoList(detailsByWeekAndDay, member);
    }

    private Map<Integer, Map<String, List<ExerciseDetail>>> groupExerciseDetails(List<ExerciseDetail> details) {
        return details.stream()
                .collect(Collectors.groupingBy(
                        ExerciseDetail::getWeeks,
                        Collectors.groupingBy(
                                ExerciseDetail::getDay,
                                Collectors.toList()
                        )
                ));
    }

    private List<ExerciseDetailDto> buildExerciseDetailDtoList(
            Map<Integer, Map<String, List<ExerciseDetail>>> detailsByWeekAndDay,
            Member member) {
        return detailsByWeekAndDay.entrySet().stream()
                .map(entry -> buildExerciseDetailDto(entry.getKey(), entry.getValue(), member))
                .collect(Collectors.toList());
    }

    private ExerciseDetailDto buildExerciseDetailDto(
            Integer weeks,
            Map<String, List<ExerciseDetail>> detailsByDay,
            Member member) {
        ExerciseDetailDto dto = new ExerciseDetailDto();
        dto.setWeeks(weeks);

        List<ExerciseDetailDto.day> days = detailsByDay.entrySet().stream()
                .sorted(Comparator.comparingInt(dayEntry -> getDayOrder(dayEntry.getKey())))
                .map(dayEntry -> buildDayDto(dayEntry.getKey(), dayEntry.getValue(), member))
                .collect(Collectors.toList());

        dto.setDays(days);
        return dto;
    }

    private ExerciseDetailDto.day buildDayDto(
            String day,
            List<ExerciseDetail> details,
            Member member) {
        ExerciseDetailDto.day dayDto = new ExerciseDetailDto.day();
        dayDto.setDay(day);

        List<ExerciseDetailDto.day.exercises> exercisesList = details.stream()
                .sorted(Comparator.comparingInt(ExerciseDetail::getSequence))
                .map(detail -> buildExerciseDto(detail, member))
                .collect(Collectors.toList());

        dayDto.setExercises(exercisesList);
        return dayDto;
    }

    private ExerciseDetailDto.day.exercises buildExerciseDto(
            ExerciseDetail detail,
            Member member) {
        ExerciseDetailDto.day.exercises exercisesDto = new ExerciseDetailDto.day.exercises();
        exercisesDto.setName(detail.getName());

        List<ExerciseDetailDto.day.exercises.SetDetail> setsList = buildSetDtoList(detail, member);

        exercisesDto.setSets(setsList);
        return exercisesDto;
    }

    private List<ExerciseDetailDto.day.exercises.SetDetail> buildSetDtoList(
            ExerciseDetail detail,
            Member member) {
        List<ExerciseDetailSet> sets = detail.getExerciseDetailSetList();
        return sets.stream()
                .map(set -> buildSetDetailDto(set, member))
                .collect(Collectors.toList());
    }

    private ExerciseDetailDto.day.exercises.SetDetail buildSetDetailDto(
            ExerciseDetailSet set,
            Member member) {
        ExerciseDetailDto.day.exercises.SetDetail setDto = new ExerciseDetailDto.day.exercises.SetDetail();
        setDto.setSequence(set.getSequence());
        setDto.setWeight(calculateWeight(member, set));
        setDto.setReps(set.getReps());
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

        if (exercise == null) {
            return set.getWeight();
        }

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

    public void enrollExercise(String email, Long routineId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

        ExerciseRoutine routine = exerciseRoutineRepository.findById(routineId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_ROUTINE));

        if (exerciseEnrollRepository.existsByMemberIdAndExerciseRoutineId(member.getId(), routineId)) {
            return;
        }

        ExerciseEnroll exerciseEnroll = new ExerciseEnroll();
        exerciseEnroll.setMember(member);
        exerciseEnroll.setExerciseRoutine(routine);

        List<ExerciseDetail> details = exerciseDetailRepository.findAllByExerciseRoutineIdWithSets(routine.getId());

        List<EnrollDetail> enrollDetails = new ArrayList<>();
        List<EnrollDetailSet> enrollDetailSets = new ArrayList<>();

        for (ExerciseDetail detail : details) {
            EnrollDetail enrollDetail = new EnrollDetail();
            enrollDetail.setExerciseEnroll(exerciseEnroll);
            enrollDetail.setExerciseDetail(detail);
            enrollDetails.add(enrollDetail);

            List<ExerciseDetailSet> sets = detail.getExerciseDetailSetList();

            for (ExerciseDetailSet set : sets) {
                EnrollDetailSet enrollDetailSet = new EnrollDetailSet();
                enrollDetailSet.setEnrollDetail(enrollDetail);
                enrollDetailSet.setWeight(set.getWeight());
                enrollDetailSet.setReps(set.getReps());
                enrollDetailSet.setSequence(set.getSequence());
                enrollDetailSets.add(enrollDetailSet);
            }
        }

        exerciseEnrollRepository.save(exerciseEnroll);
        enrollDetailRepository.saveAll(enrollDetails);
        enrollDetailSetRepository.saveAll(enrollDetailSets);
    }

    public void deleteEnrollExercise(String email, Long routineId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

        if (!exerciseEnrollRepository.existsByMemberIdAndExerciseRoutineId(member.getId(), routineId)) {
            return; // 루틴이 등록되지 않았을 경우 함수 종료
        }

        ExerciseEnroll exerciseEnroll = (ExerciseEnroll) exerciseEnrollRepository.findByMemberIdAndExerciseRoutineId(member.getId(), routineId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_ENROLL));

        List<EnrollDetail> enrollDetails = enrollDetailRepository.findByExerciseEnrollId(exerciseEnroll.getId());
        List<Long> enrollDetailIds = enrollDetails.stream().map(EnrollDetail::getId).collect(Collectors.toList());
        List<EnrollDetailSet> enrollDetailSets = enrollDetailSetRepository.findByEnrollDetailIdIn(enrollDetailIds);

        enrollDetailSetRepository.deleteAll(enrollDetailSets);
        enrollDetailRepository.deleteAll(enrollDetails);
        exerciseEnrollRepository.delete(exerciseEnroll);
    }
}
