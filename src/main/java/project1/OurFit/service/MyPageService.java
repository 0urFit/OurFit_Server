package project1.OurFit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.entity.*;
import project1.OurFit.repository.*;
import project1.OurFit.response.EnrollDetailDto;
import project1.OurFit.response.MyLikeRes;
import project1.OurFit.response.MyRoutineRes;
import project1.constant.exception.BaseException;

import java.util.*;
import java.util.stream.Collectors;

import static project1.constant.response.JsonResponseStatus.*;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final ExerciseEnrollRepository exerciseEnrollRepository;
    private final ExerciseRoutineRepository routineRepository;
    private final ExerciseLikeRepository exerciseLikeRepository;
    private final EnrollDetailRepository enrollDetailRepository;
    private final EnrollDetailSetRepository enrollDetailSetRepository;
    private final MemberRepository memberRepository;
    private final ExerciseDetailRepository exerciseDetailRepository;


    public List<MyRoutineRes> getMyRoutine(String userEmail) {
//        Long memberId = memberRepository.findByEmail(userEmail)
//                .map(Member::getId)
//                .orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));
//
//        List<ExerciseEnroll> enrollRepositoryList =
//                exerciseEnrollRepository.findByMemberId(memberId);

        List<ExerciseEnroll> enrollRepositoryList = exerciseEnrollRepository.findByMemberEmail(userEmail);
        List<ExerciseRoutine> exerciseRoutineList = new ArrayList<>();

        for (ExerciseEnroll exerciseEnroll : enrollRepositoryList) {
            Long rouId = exerciseEnroll.getExerciseRoutine().getId();
            ExerciseRoutine exerciseRoutine = routineRepository.findById(rouId).
                    orElseThrow(()->new BaseException(NOT_FOUND_ROUTINE));
            exerciseRoutineList.add(exerciseRoutine);
        }
        return exerciseRoutineList.stream()
                .map(MyRoutineRes::new)
                .collect(Collectors.toList());
    }

    public List<MyRoutineRes> getMyRoutineByCate(String userEmail, String category) {
        List<ExerciseEnroll> enrollRepositoryList = exerciseEnrollRepository.findByMemberEmail(userEmail);
        List<ExerciseRoutine> exerciseRoutineList = new ArrayList<>();
        for (ExerciseEnroll exerciseEnroll : enrollRepositoryList) {
            Long rouId = exerciseEnroll.getExerciseRoutine().getId();
            try {
                ExerciseRoutine exerciseRoutine = routineRepository.findByIdAndCategory(rouId, category).
                        orElseThrow(() -> new BaseException(NOT_FOUND_ROUTINE));

                exerciseRoutineList.add(exerciseRoutine);
            } catch (BaseException e){
                continue;
            }
        }
        return exerciseRoutineList.stream()
                .map(MyRoutineRes::new)
                .collect(Collectors.toList());
    }

    public List<MyLikeRes> getMyLikeRoutine(String userEmail) {

        List<ExerciseLike> allByMemberEmail = exerciseLikeRepository.findByMemberEmail(userEmail);
        List<MyLikeRes> myRoutineRes = new ArrayList<>();
        for (ExerciseLike exerciseLike : allByMemberEmail) {
            MyLikeRes a = new MyLikeRes(exerciseLike.getExerciseRoutine());
            myRoutineRes.add(a);
        }
        return myRoutineRes;
    }

    //complete 하면 enroll_detail_set rate 를 바꿔야 하는데 값을 모르겠네
    public void completeRoutine(Long rouId) {
        EnrollDetail enrollDetail = enrollDetailRepository.findById(rouId)
                .orElseThrow(() -> new BaseException(NOTFOUND));
//        enrollDetail.completeRoutine();
    }

    public List<EnrollDetailDto> getEnrollDetails(String email, Long routineId, int week) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

        List<EnrollDetail> details = enrollDetailRepository.
                findAllByExerciseDetail_ExerciseRoutine_IdAndExerciseDetail_Weeks(routineId, week)
                .stream()
                .filter(detail -> detail.getExerciseEnroll().getMember().getId().equals(member.getId()))
                .collect(Collectors.toList());

        Map<Integer, Map<String, List<EnrollDetail>>> detailsByWeekAndDay = groupEnrollDetails(details);

        return buildEnrollDetailDtoList(detailsByWeekAndDay);
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
            Map<Integer, Map<String, List<EnrollDetail>>> detailsByWeekAndDay) {
        return detailsByWeekAndDay.entrySet().stream()
                .map(entry -> buildEnrollDetailDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private EnrollDetailDto buildEnrollDetailDto(
            Integer weeks,
            Map<String, List<EnrollDetail>> detailsByDay) {
        EnrollDetailDto dto = new EnrollDetailDto();
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
        setDetailDto.setComplete(set.getComplete());
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

//    @Transactional(readOnly = true)
//    public List<EnrollDetailDto> getMyRoutineDetail(String email, Long routineId, int week) {
//        Member member = memberRepository.findByEmail(email)
//                .orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));
//        List<ExerciseEnroll> exerciseEnrolls = exerciseEnrollRepository.findByMemberIdAndRoutineId(member.getId(), routineId);
//
//        List<EnrollDetailDto> response = new ArrayList<>();
//
//        for (ExerciseEnroll enroll : exerciseEnrolls) {
//            List<EnrollDetail> enrollDetails = enrollDetailRepository.findAllByExerciseEnrollId(enroll.getId());
//
//            for (EnrollDetail detail : enrollDetails) {
//                // Convert detail to EnrollDetailDto
//                EnrollDetailDto detailDto = new EnrollDetailDto();
//                detailDto.setWeeks(week);
//
//                // Add day details
//                EnrollDetailDto.day dayDto = new EnrollDetailDto.day();
//                dayDto.setDay(detail.getExerciseDetail().getDay());
//                detailDto.getDays().add(dayDto);
//
//                // Add exercise details
//                EnrollDetailDto.day.exercises exerciseDto = new EnrollDetailDto.day.exercises();
//                exerciseDto.setName(detail.getExerciseDetail().getName());
//
//                for (EnrollDetailSet detailSet : detail.getEnrollDetailSets()) {
//                    // Convert detailSet to SetDetail
//                    EnrollDetailDto.day.exercises.SetDetail setDetail = new EnrollDetailDto.day.exercises.SetDetail();
//                    setDetail.setId(detailSet.getId());
//                    setDetail.setSequence(detailSet.getSequence());
//                    setDetail.setWeight(detailSet.getWeight());
//                    setDetail.setReps(detailSet.getReps());
//                    setDetail.setComplete(detailSet.getComplete());
//
//                    exerciseDto.getSets().add(setDetail);
//                }
//
//                dayDto.getExercises().add(exerciseDto);
//                response.add(detailDto);
//            }
//        }
//
//        return response;
//    }
}
