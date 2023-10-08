package project1.OurFit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.entity.*;
import project1.OurFit.repository.*;
import project1.OurFit.request.ExerciseCompleteDto;
import project1.OurFit.response.*;
import project1.constant.exception.BaseException;
import project1.constant.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

import static project1.constant.response.JsonResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {
    private final ExerciseEnrollRepository exerciseEnrollRepository;
    private final ExerciseRoutineRepository routineRepository;
    private final ExerciseLikeRepository exerciseLikeRepository;
    private final ExerciseDetailRepository exerciseDetailRepository;
    private final MemberRepository memberRepository;
    private final ExerciseLogsRepository exerciseLogsRepository;
    private static final List<String> DAYS_ORDER = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");

    public List<MyRoutineRes> getMyRoutine(String userEmail, String category) {
        List<ExerciseEnroll> enrollRepositoryList;

        if (category.equals("all"))
            enrollRepositoryList = exerciseEnrollRepository.findByMemberEmail(userEmail);
        else
            enrollRepositoryList = exerciseEnrollRepository.findByMemberEmailAndExerciseRoutine_Category(userEmail, category);

        List<Long> routineIds = enrollRepositoryList.stream()
                .map(exerciseEnroll -> exerciseEnroll.getExerciseRoutine().getId())
                .collect(Collectors.toList());

        // check likes and enrolls for all routine IDs in one go
        List<Long> likedRoutineIds = exerciseLikeRepository.findLikedRoutineIdsByMemberEmail(userEmail, routineIds);
        List<Long> enrolledRoutineIds = exerciseEnrollRepository.findEnrolledRoutineIdsByMemberEmail(userEmail, routineIds);

        return enrollRepositoryList.stream()
                .map(exerciseEnroll -> {
                    ExerciseRoutine routine = exerciseEnroll.getExerciseRoutine();
                    boolean isLiked = likedRoutineIds.contains(routine.getId());
                    boolean isEnrolled = enrolledRoutineIds.contains(routine.getId());
                    return new MyRoutineRes(routine, exerciseEnroll.getWeekProgress(), isLiked, isEnrolled);
                })
                .collect(Collectors.toList());
    }

    public List<MyLikeRes> getMyLikeRoutine(String userEmail) {
        List<ExerciseLike> allByMemberEmail = exerciseLikeRepository.findByMemberEmail(userEmail);
        List<ExerciseEnroll> allExerciseEnrollsByMember = exerciseEnrollRepository.findByMemberEmail(userEmail);
        Set<Long> enrolledRoutineIds = allExerciseEnrollsByMember.stream()
                .map(enroll -> enroll.getExerciseRoutine().getId())
                .collect(Collectors.toSet());

        List<MyLikeRes> myRoutineRes = new ArrayList<>();
        for (ExerciseLike exerciseLike : allByMemberEmail) {
            Long routineId = exerciseLike.getExerciseRoutine().getId();
            MyLikeRes a = new MyLikeRes(
                    exerciseLike.getExerciseRoutine(),
                    exerciseLike.getExerciseRoutine().getCategory(),
                    enrolledRoutineIds.contains(routineId));
            myRoutineRes.add(a);
        }
        return myRoutineRes;
    }

    public void completeRoutine(String email, ExerciseCompleteDto completeDto, Long routineId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));
        ExerciseRoutine exerciseRoutine = routineRepository.findById(routineId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ROUTINE));

        ExerciseLogs exerciseLogs = new ExerciseLogs();
        exerciseLogs.setDay(completeDto.getDay());
        exerciseLogs.setWeek(completeDto.getWeek());
        exerciseLogs.setExerciseRoutine(exerciseRoutine);
        exerciseLogs.setMember(member);
        exerciseLogsRepository.save(exerciseLogs);
    }

    /**
     * 저장한 운동 상세 루틴 주차별로 가져오기 Service
     * @param email
     * @param routineId
     * @param week
     * @return
     */
    public List<ExerciseDetailDto> getEnrollDetails(final String email, final Long routineId, final int week) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));

        if (!exerciseEnrollRepository.existsByMemberIdAndExerciseRoutineId(member.getId(), routineId))
            throw new NotFoundException(NOT_FOUND_ENROLL);

        ExerciseRoutine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ROUTINE));

        List<ExerciseDetail> exerciseDetails = exerciseDetailRepository.findByExerciseRoutineIdAndWeeks(routineId, week);
        List<ExerciseLogs> logsForTheWeek = exerciseLogsRepository.findByExerciseRoutineAndWeek(routine, week);

        List<ExerciseDetailDto> dto = new ArrayList<>();
        dto.add(buildExerciseDetailDto(member, routine, exerciseDetails, logsForTheWeek));
        return dto;
    }

    private ExerciseDetailDto buildExerciseDetailDto(Member member, ExerciseRoutine routine,
            List<ExerciseDetail> exerciseDetails, List<ExerciseLogs> logsForTheWeek) {
        ExerciseDetailDto dto = new ExerciseDetailDto();
        dto.setRoutineName(routine.getRoutineName());
        dto.setLevel(routine.getLevel());
        dto.setWeeks(routine.getDaysPerWeek());
        dto.setPeriod(routine.getProgramLength());
        dto.setDays(constructDaysList(member, exerciseDetails, logsForTheWeek));
        return dto;
    }

    private List<ExerciseDetailDto.day> constructDaysList(
            Member member, List<ExerciseDetail> exerciseDetails, List<ExerciseLogs> logsForTheWeek) {
        Map<String, List<ExerciseDetail>> groupedByDay = exerciseDetails.stream()
                .collect(Collectors.groupingBy(ExerciseDetail::getDay));
        return DAYS_ORDER.stream()
                .filter(groupedByDay::containsKey)
                .map(dayName -> createdayDto(member, dayName, groupedByDay.get(dayName), logsForTheWeek))
                .collect(Collectors.toList());
    }

    private ExerciseDetailDto.day createdayDto(Member member, String dayName,
            List<ExerciseDetail> details, List<ExerciseLogs> logsForTheWeek) {
        ExerciseDetailDto.day dayDto = new ExerciseDetailDto.day();
        boolean logExistsForTheDay = logsForTheWeek.stream().anyMatch(log -> log.getDay().equals(dayName));
        dayDto.setIssuccess(logExistsForTheDay);
        dayDto.setDay(dayName);
        dayDto.setExercises(constructExercisesList(member, details));
        return dayDto;
    }

    private List<ExerciseDetailDto.day.exercises> constructExercisesList(Member member, List<ExerciseDetail> details) {
        return details.stream().map(detail -> {
            ExerciseDetailDto.day.exercises exerciseDto = new ExerciseDetailDto.day.exercises();
            exerciseDto.setName(detail.getName());
            exerciseDto.setSets(constructSetDetailsList(member, detail.getExerciseDetailSetList()));
            return exerciseDto;
        }).collect(Collectors.toList());
    }

    private List<ExerciseDetailDto.day.exercises.SetDetail> constructSetDetailsList(
            Member member, List<ExerciseDetailSet> setList) {
        return setList.stream().map(set -> {
            ExerciseDetailDto.day.exercises.SetDetail setDetailDto = new ExerciseDetailDto.day.exercises.SetDetail();
            setDetailDto.setSequence(set.getSequence());
            setDetailDto.setWeight(calculateWeight(member, set));
            setDetailDto.setReps(set.getReps());
            return setDetailDto;
        }).collect(Collectors.toList());
    }

    private double calculateWeight(Member member, ExerciseDetailSet set) {
        String exercise = set.getExerciseType();
        Double increase = set.getIncrease();

        if (exercise == null) {
            return set.getWeight();
        }

        Double baseWeight = getBaseWeightForMemberExercise(member, exercise);
        if (baseWeight == null)
            return set.getWeight();

        return Math.round(baseWeight * increase * 10.0) / 10.0;
    }

    private Double getBaseWeightForMemberExercise(Member member, String exercise) {
        return switch (exercise) {
            case "squat" -> member.getSquat();
            case "deadlift" -> member.getDeadlift();
            case "benchpress" -> member.getBenchpress();
            case "overheadpress" -> member.getOverheadpress();
            default -> null;
        };
    }

    /**
     * 사용자 정보 가져오기 service
     * @param userEmail
     * @return
     */
    public MemberDto getMyInfo(String userEmail) {
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));
        return new MemberDto(member);
    }

    /**
     * 개인정보 수정 Service
     * @param memberDto
     * @param email
     */
    public void saveMyInfo(MemberDto memberDto, String email) {
        Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));
        member.setHeight(memberDto.getHeight());
        member.setWeight(memberDto.getWeight());
        member.setSquat(memberDto.getSquat());
        member.setBenchpress(memberDto.getBenchpress());
        member.setDeadlift(memberDto.getDeadlift());
        member.setOverheadpress(memberDto.getOverheadpress());
        memberRepository.save(member);
    }
}
