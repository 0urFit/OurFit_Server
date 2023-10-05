package project1.OurFit.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.entity.*;
import project1.OurFit.repository.*;
import project1.OurFit.request.ExerciseCompleteDto;
import project1.OurFit.request.MemberDTO;
import project1.OurFit.response.EnrollDetailDto;
import project1.OurFit.response.MemberDto;
import project1.OurFit.response.MyLikeRes;
import project1.OurFit.response.MyRoutineRes;
import project1.constant.exception.BaseException;
import project1.constant.exception.NotFoundException;
import project1.constant.exception.UnregisteredUserException;
import project1.constant.response.JsonResponseStatus;

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
    private final EnrollDetailRepository enrollDetailRepository;
    private final MemberRepository memberRepository;
    private final ExerciseLogsRepository exerciseLogsRepository;

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

    public void completeRoutine(String email, ExerciseCompleteDto completeDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));


    }

    public List<EnrollDetailDto> getEnrollDetails(String email, Long routineId, int week) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

        ExerciseRoutine exerciseRoutine = routineRepository.findById(routineId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_ROUTINE));

        List<EnrollDetail> details = enrollDetailRepository.
                findAllByExerciseDetail_ExerciseRoutine_IdAndExerciseDetail_Weeks(routineId, week)
                .stream()
                .filter(detail -> detail.getExerciseEnroll().getMember().getId().equals(member.getId()))
                .collect(Collectors.toList());

        Map<Integer, Map<String, List<EnrollDetail>>> detailsByWeekAndDay = groupEnrollDetails(details);

        return buildEnrollDetailDtoList(detailsByWeekAndDay, exerciseRoutine);
    }

    private Map<Integer, Map<String, List<EnrollDetail>>> groupEnrollDetails(List<EnrollDetail> details) {
        return details.stream()
                .collect(Collectors.groupingBy(
                        detail -> detail.getExerciseDetail().getWeeks(),
                        Collectors.groupingBy(
                                detail -> detail.getExerciseDetail().getDay(),
                                Collectors.toList()
                        )
                ));
    }

    private List<EnrollDetailDto> buildEnrollDetailDtoList(
            Map<Integer, Map<String, List<EnrollDetail>>> detailsByWeekAndDay,
            ExerciseRoutine exerciseRoutine) {
        return detailsByWeekAndDay.entrySet().stream()
                .map(entry -> buildEnrollDetailDto(entry.getKey(), entry.getValue(), exerciseRoutine))
                .collect(Collectors.toList());
    }

    private EnrollDetailDto buildEnrollDetailDto(
            Integer weeks,
            Map<String, List<EnrollDetail>> detailsByDay,
            ExerciseRoutine exerciseRoutine) {
        EnrollDetailDto dto = new EnrollDetailDto();
        dto.setRoutineName(exerciseRoutine.getRoutineName());
        dto.setCategory(exerciseRoutine.getCategory());
        dto.setLevel(exerciseRoutine.getLevel());
        dto.setFewTime(exerciseRoutine.getFewTime());
        dto.setPeriod(exerciseRoutine.getPeriod());
        dto.setWeeks(weeks);

        List<EnrollDetailDto.day> days = detailsByDay.entrySet().stream()
                .sorted(Comparator.comparingInt(dayEntry -> getDayOrder(dayEntry.getKey())))
                .map(dayEntry -> buildDayDto(dayEntry.getKey(), dayEntry.getValue()))
                .collect(Collectors.toList());

        dto.setDays(days);
        return dto;
    }

    private EnrollDetailDto.day buildDayDto(
            String day,
            List<EnrollDetail> details) {
        EnrollDetailDto.day dayDto = new EnrollDetailDto.day();
        dayDto.setDay(day);

        List<EnrollDetailDto.day.exercises> exercises = details.stream()
                .map(this::buildExercisesDto)
                .collect(Collectors.toList());

        dayDto.setExercises(exercises);
        return dayDto;
    }

    private EnrollDetailDto.day.exercises buildExercisesDto(EnrollDetail detail) {
        EnrollDetailDto.day.exercises exercisesDto = new EnrollDetailDto.day.exercises();
        exercisesDto.setName(detail.getExerciseDetail().getName());

        List<EnrollDetailDto.day.exercises.SetDetail> setDetails = detail.getEnrollDetailSets().stream()
                .sorted(Comparator.comparingInt(EnrollDetailSet::getSequence))
                .map(this::buildSetDetailDto)
                .collect(Collectors.toList());

        exercisesDto.setSets(setDetails);
        return exercisesDto;
    }

    private EnrollDetailDto.day.exercises.SetDetail buildSetDetailDto(EnrollDetailSet set) {
        EnrollDetailDto.day.exercises.SetDetail setDetailDto = new EnrollDetailDto.day.exercises.SetDetail();
        setDetailDto.setId(set.getId());
        setDetailDto.setSequence(set.getSequence());
        setDetailDto.setReps(set.getReps());
        setDetailDto.setWeight(set.getWeight());
        return setDetailDto;
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

    public MemberDto getMyInfo(String userEmail) {
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));
        return new MemberDto(member);
    }

    public void saveMyInfo(MemberDto memberDto, String email) {
        Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));
        member.setNickname(memberDto.getNickname());
        member.setHeight(memberDto.getHeight());
        member.setWeight(memberDto.getWeight());
        member.setSquat(memberDto.getSquat());
        member.setBenchpress(memberDto.getBenchpress());
        member.setDeadlift(memberDto.getDeadlift());
        member.setOverheadpress(memberDto.getOverheadpress());
        memberRepository.save(member);
    }
}
