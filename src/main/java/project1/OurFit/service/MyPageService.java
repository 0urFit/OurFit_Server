package project1.OurFit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project1.OurFit.entity.ExerciseEnroll;
import project1.OurFit.entity.ExerciseLike;
import project1.OurFit.entity.ExerciseRoutine;
import project1.OurFit.entity.Member;
import project1.OurFit.repository.ExerciseEnrollRepository;
import project1.OurFit.repository.ExerciseLikeRepository;
import project1.OurFit.repository.ExerciseRoutineRepository;
import project1.OurFit.repository.MemberRepository;
import project1.OurFit.response.MyLikeRes;
import project1.OurFit.response.MyRoutineRes;
import project1.constant.exception.BaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static project1.constant.response.JsonResponseStatus.NOT_FOUND_MEMBER;
import static project1.constant.response.JsonResponseStatus.NOT_FOUND_ROUTINE;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;
    private final ExerciseEnrollRepository exerciseEnrollRepository;
    private final ExerciseRoutineRepository routineRepository;
    private final ExerciseLikeRepository exerciseLikeRepository;

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
            ExerciseRoutine exerciseRoutine = routineRepository.findByIdAndCategory(rouId,category).
                    orElseThrow(()->new BaseException(NOT_FOUND_ROUTINE));
            exerciseRoutineList.add(exerciseRoutine);
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
}
