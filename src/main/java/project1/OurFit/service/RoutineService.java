package project1.OurFit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.entity.*;
import project1.OurFit.repository.*;
import project1.OurFit.response.ExerciseDetailDto;
import project1.OurFit.response.ExerciseViewDto;
import project1.OurFit.response.ExerciseRoutineListDto;
import project1.constant.exception.BaseException;
import project1.constant.exception.NotFoundException;


import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static project1.constant.response.JsonResponseStatus.*;


@RequiredArgsConstructor
@Service
@Transactional
public class RoutineService {
    private final ExerciseLikeRepository exerciseLikeRepository;
    private final MemberRepository memberRepository;
    private final ExerciseRoutineRepository exerciseRoutineRepository;
    private final ExerciseEnrollRepository exerciseEnrollRepository;
    private final ExerciseDetailRepository exerciseDetailRepository;
    private final ExerciseLogsRepository exerciseLogsRepository;
    private static final List<String> DAYS_ORDER = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");

    private Member findByEmail(String userEmail) {
        return memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));
    }

    private ExerciseRoutine findByExerciseRoutine(long routineId) {
        return exerciseRoutineRepository.findById(routineId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ROUTINE));
    }

    public boolean postLike(String userEmail, Long routineId) {
        Member member=memberRepository.findByEmail(userEmail)
                .orElseThrow(()-> new BaseException(NOT_FOUND_MEMBER));
        ExerciseRoutine exerciseRoutine = exerciseRoutineRepository.findById(routineId)
                .orElseThrow(()-> new BaseException(NOT_FOUND_ROUTINE));

        if (exerciseLikeRepository.existsByMemberEmailAndExerciseRoutineId(userEmail, routineId))
            return true;

        ExerciseLike like = new ExerciseLike(member,exerciseRoutine);
        exerciseLikeRepository.save(like);
        return true;
    }

    public boolean deleteLike(String userEmail, Long routineId) { //걍 좋아요 테이블
        ExerciseLike exerciseLike = exerciseLikeRepository
                .findByMemberEmailAndExerciseRoutineId(userEmail, routineId)
                .orElseThrow(() -> new BaseException(NOTFOUND));
        exerciseLikeRepository.delete(exerciseLike);
        return false;
    }

    /**
     * 카테고리별 운동루틴 조회 service
     * @param category
     * @param userEmail
     * @return
     */
    public List<ExerciseRoutineListDto> getExerciseRoutineByCategory(String category, String userEmail) {
        Member member = findByEmail(userEmail);
        List<ExerciseRoutine> exerciseRoutineList = exerciseRoutineRepository.findByCategory(category);
        return getExerciseRoutineWithEnrollmentStatus(userEmail, exerciseRoutineList);
    }

    /**
     * 운동루틴 all 조회 service
     * @param userEmail
     * @return
     */
    public List<ExerciseRoutineListDto> getExerciseRoutine(String userEmail) {
        Member member = findByEmail(userEmail);
        List<ExerciseRoutine> exerciseRoutineList = exerciseRoutineRepository.findAll();
        return getExerciseRoutineWithEnrollmentStatus(userEmail, exerciseRoutineList);
    }

    private List<ExerciseRoutineListDto> getExerciseRoutineWithEnrollmentStatus(
            String userEmail, List<ExerciseRoutine> exerciseRoutineList) {

        Set<ExerciseRoutine> enrolledRoutines = getEnrolledRoutines(userEmail);
        Set<ExerciseRoutine> likedRoutines = getLikedRoutines(userEmail);

        return exerciseRoutineList.stream()
                .map(routine -> createDtoWithStatus(routine, enrolledRoutines, likedRoutines))
                .collect(Collectors.toList());
    }

    private ExerciseRoutineListDto createDtoWithStatus(
            ExerciseRoutine routine, Set<ExerciseRoutine> enrolledRoutines, Set<ExerciseRoutine> likedRoutines) {
        boolean isEnrolled = enrolledRoutines.contains(routine);
        boolean isLiked = likedRoutines.contains(routine);
        return new ExerciseRoutineListDto(routine, isEnrolled, isLiked);
    }

    private Set<ExerciseRoutine> getLikedRoutines(String userEmail) {
        return exerciseLikeRepository.findAllByMemberEmail(userEmail).stream()
                .map(ExerciseLike::getExerciseRoutine)
                .collect(Collectors.toSet());
    }

    private Set<ExerciseRoutine> getEnrolledRoutines(String userEmail) {
        return exerciseEnrollRepository.findAllByMemberEmail(userEmail).stream()
                .map(ExerciseEnroll::getExerciseRoutine)
                .collect(Collectors.toSet());
    }


    /**
     * 주차별로 상세 루틴 가져오기 service
     * @param routineId
     * @param email
     * @param week
     * @return
     */
    public List<ExerciseDetailDto> getExerciseDetails(
            final String email, final Long routineId, final int week) {
        Member member = findByEmail(email);
        List<ExerciseDetail> exerciseDetails = exerciseDetailRepository.findByExerciseRoutineIdAndWeeks(routineId, week);
        List<ExerciseDetailDto> dto = new ArrayList<>();
        dto.add(buildExerciseDetailDto(member, exerciseDetails));
        return dto;
    }

    private ExerciseDetailDto buildExerciseDetailDto(
            Member member, List<ExerciseDetail> exerciseDetails) {
        ExerciseDetailDto dto = new ExerciseDetailDto();
        dto.setDays(constructDaysList(member, exerciseDetails));
        return dto;
    }

    private List<ExerciseDetailDto.day> constructDaysList(Member member, List<ExerciseDetail> exerciseDetails) {
        Map<String, List<ExerciseDetail>> groupedByDay = exerciseDetails.stream()
                .collect(Collectors.groupingBy(ExerciseDetail::getDay));
        return DAYS_ORDER.stream()
                .filter(groupedByDay::containsKey)
                .map(dayName -> createdayDto(member, dayName, groupedByDay.get(dayName)))
                .collect(Collectors.toList());
    }

    private ExerciseDetailDto.day createdayDto(Member member, String dayName, List<ExerciseDetail> details) {
        ExerciseDetailDto.day dayDto = new ExerciseDetailDto.day();
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
     * 운동 루틴 등록 Service
     * @param email
     * @param routineId
     */
    public void enrollExercise(String email, Long routineId) {
        Member member = findByEmail(email);

        if (!exerciseEnrollRepository.existsByMemberIdAndExerciseRoutineId(member.getId(), routineId)) { // 등록되어 있지 않다면
            ExerciseRoutine exerciseRoutine = findByExerciseRoutine(routineId);
            enrollMemberInExercise(member, exerciseRoutine);
        }
    }

    private void enrollMemberInExercise(Member member, ExerciseRoutine exerciseRoutine) {
        ExerciseEnroll exerciseEnroll = createExerciseEnrollInstance(member, exerciseRoutine);
        exerciseEnrollRepository.save(exerciseEnroll);
    }

    private ExerciseEnroll createExerciseEnrollInstance(Member member, ExerciseRoutine exerciseRoutine) {
        ExerciseEnroll exerciseEnroll = new ExerciseEnroll();
        exerciseEnroll.setMember(member);
        exerciseEnroll.setWeekProgress(1);
        exerciseEnroll.setExerciseRoutine(exerciseRoutine);
        return exerciseEnroll;
    }

    /**
     * 운동 루틴 등록 삭제 Service
     * @param email
     * @param routineId
     */
    public void deleteEnrollExercise(String email, Long routineId) {
        Member member = findByEmail(email);

        exerciseEnrollRepository.findByMemberIdAndExerciseRoutineId(member.getId(), routineId)
                .ifPresent(exerciseEnrollRepository::delete);
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            List<ExerciseLogs> logs = exerciseLogsRepository
                    .findByMemberIdAndExerciseRoutineId(member.getId(), routineId);
            exerciseLogsRepository.deleteAll(logs);
        });

    }

    public boolean inquiryLike(String userEmail, Long routineId) {
        return exerciseLikeRepository.existsByMemberEmailAndExerciseRoutineId(userEmail, routineId);
    }

    public void inquiryEnroll(String userEmail, Long routineID) {
        Member member = findByEmail(userEmail);
        if (!exerciseEnrollRepository.existsByMemberIdAndExerciseRoutineId(member.getId(), routineID))
            throw new NotFoundException(NOT_FOUND_ENROLL);
    }

    /**
     * 운동 상세 루틴 view Service
     * @param email
     * @param routineId
     * @return
     */
    public ExerciseViewDto getExerciseRoutineView(String email, long routineId) {
        Member member = findByEmail(email);
        ExerciseRoutine routine = findByExerciseRoutine(routineId);
        boolean isLiked = exerciseLikeRepository.existsByMemberIdAndExerciseRoutineId(member.getId(), routineId);
        boolean isEnrolled = exerciseEnrollRepository.existsByMemberIdAndExerciseRoutineId(member.getId(), routineId);

        return buildExerciseDetailViewDto(routine, isLiked, isEnrolled);
    }

    private ExerciseViewDto buildExerciseDetailViewDto(ExerciseRoutine routine, boolean isLiked, boolean isEnrolled) {
        return ExerciseViewDto.builder()
                .routineName(routine.getRoutineName())
                .level(routine.getLevel())
                .weeks(routine.getDaysPerWeek())
                .period(routine.getProgramLength())
                .isenrolled(isEnrolled)
                .isliked(isLiked)
                .build();
    }
}
