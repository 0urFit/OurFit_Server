package project1.OurFit.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.entity.*;
import project1.OurFit.repository.*;
import project1.OurFit.response.ExerciseDetailDto;
import project1.OurFit.response.ExerciseRoutineWithEnrollmentStatusDto;
import project1.constant.exception.BaseException;
import project1.constant.exception.NotFoundException;
import project1.constant.response.JsonResponse;
import project1.constant.response.JsonResponseStatus;


import java.time.DayOfWeek;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final EnrollDetailRepository enrollDetailRepository;
    private final EnrollDetailSetRepository enrollDetailSetRepository;
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
    public List<ExerciseRoutineWithEnrollmentStatusDto> getExerciseRoutineByCategory(String category, String userEmail) {
        findByEmail(userEmail);
        List<ExerciseRoutine> exerciseRoutineList = exerciseRoutineRepository.findByCategory(category);
        return getExerciseRoutineWithEnrollmentStatus(userEmail, exerciseRoutineList);
    }

    /**
     * 운동루틴 all 조회 service
     * @param userEmail
     * @return
     */
    public List<ExerciseRoutineWithEnrollmentStatusDto> getExerciseRoutine(String userEmail) {
        findByEmail(userEmail);
        List<ExerciseRoutine> exerciseRoutineList = exerciseRoutineRepository.findAll();
        return getExerciseRoutineWithEnrollmentStatus(userEmail, exerciseRoutineList);
    }

    private List<ExerciseRoutineWithEnrollmentStatusDto> getExerciseRoutineWithEnrollmentStatus(
            String userEmail, List<ExerciseRoutine> exerciseRoutineList) {

        Set<ExerciseRoutine> enrolledRoutines = getEnrolledRoutines(userEmail);
        Set<ExerciseRoutine> likedRoutines = getLikedRoutines(userEmail);

        return exerciseRoutineList.stream()
                .map(routine -> createDtoWithStatus(routine, enrolledRoutines, likedRoutines))
                .collect(Collectors.toList());
    }

    private ExerciseRoutineWithEnrollmentStatusDto createDtoWithStatus(
            ExerciseRoutine routine, Set<ExerciseRoutine> enrolledRoutines, Set<ExerciseRoutine> likedRoutines) {
        boolean isEnrolled = enrolledRoutines.contains(routine);
        boolean isLiked = likedRoutines.contains(routine);
        return new ExerciseRoutineWithEnrollmentStatusDto(routine, isEnrolled, isLiked);
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
     * 주차&날짜별로 상세 루틴 가져오기 service
     * @param routineId
     * @param email
     * @param week
     * @return
     */
    public List<ExerciseDetailDto> getExerciseDetails(
            final String email, final Long routineId, final int week) {
        Member member = findByEmail(email);
        ExerciseRoutine routine = findByExerciseRoutine(routineId);
        boolean isLiked = exerciseLikeRepository.existsByMemberIdAndExerciseRoutineId(member.getId(), routineId);

        List<ExerciseDetail> exerciseDetails = exerciseDetailRepository.findByExerciseRoutineIdAndWeeks(routineId, week);
        List<ExerciseDetailDto> dto = new ArrayList<>();
        dto.add(buildExerciseDetailDto(member, routine, isLiked, exerciseDetails));
        return dto;
    }

    private ExerciseDetailDto buildExerciseDetailDto(
            Member member, ExerciseRoutine routine, boolean isLiked, List<ExerciseDetail> exerciseDetails) {
        ExerciseDetailDto dto = new ExerciseDetailDto();
        dto.setRoutineName(routine.getRoutineName());
        dto.setLevel(routine.getLevel());
        dto.setWeeks(routine.getDaysPerWeek());
        dto.setPeriod(routine.getProgramLength());
        dto.setIsliked(isLiked);
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
        exerciseEnroll.setWeekProgress(1);

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
                enrollDetailSet.setWeight(calculateWeight(member, set));
                enrollDetailSet.setReps(set.getReps());
                enrollDetailSet.setSequence(set.getSequence());
                enrollDetailSets.add(enrollDetailSet);
            }
        }
        CompletableFuture<Void> exerciseEnrollFuture = CompletableFuture.runAsync(() -> {
            exerciseEnrollRepository.save(exerciseEnroll);
        });
        CompletableFuture<Void> enrollDetailFuture = CompletableFuture.runAsync(() -> {
            enrollDetailRepository.saveAll(enrollDetails);
        });
        CompletableFuture<Void> enrollDetailSetFuture = CompletableFuture.runAsync(() -> {
            enrollDetailSetRepository.saveAll(enrollDetailSets);
        });

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

    public boolean inquiryLike(String userEmail, Long routineId) {
        return exerciseLikeRepository.existsByMemberEmailAndExerciseRoutineId(userEmail, routineId);
    }
}
