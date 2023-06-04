package project1.OurFit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project1.OurFit.entity.*;
import project1.OurFit.repository.*;
import project1.OurFit.response.EnrollDetailDto;
import project1.OurFit.response.MyLikeRes;
import project1.OurFit.response.MyRoutineRes;
import project1.constant.exception.BaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static project1.constant.response.JsonResponseStatus.*;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final ExerciseEnrollRepository exerciseEnrollRepository;
    private final ExerciseRoutineRepository routineRepository;
    private final ExerciseLikeRepository exerciseLikeRepository;
    private final EnrollDetailRepository enrollDetailRepository;
    private final MemberRepository memberRepository;


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
                .orElseThrow(()->new BaseException(NOTFOUND));
//        enrollDetail.completeRoutine();
    }

    public List<EnrollDetailDto> getMyRoutineDetail(String category, Long routineId, String email, int week) {
         Member member=memberRepository.findByEmail(email)
                 .orElseThrow(()-> new BaseException(NOT_FOUND_MEMBER));


        return null;
    }
}
