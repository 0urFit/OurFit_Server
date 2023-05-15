package project1.OurFit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project1.OurFit.entity.ExerciseLike;
import project1.OurFit.entity.ExerciseRoutine;
import project1.OurFit.entity.Member;
import project1.OurFit.repository.ExerciseLikeRepository;
import project1.OurFit.repository.ExerciseRoutineRepository;
import project1.OurFit.repository.MemberRepository;
import project1.constant.exception.BaseException;

import static project1.constant.response.JsonResponseStatus.*;


@RequiredArgsConstructor
@Service
public class RoutineService {
    private final ExerciseLikeRepository exerciseLikeRepository;
    private final MemberRepository memberRepository;
    private final ExerciseRoutineRepository exerciseRoutineRepository;

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
}
