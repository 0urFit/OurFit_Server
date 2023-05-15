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
import project1.OurFit.response.ExerciseRoutineWithEnrollmentStatusDto;
import project1.constant.exception.BaseException;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static project1.constant.response.JsonResponseStatus.*;


@RequiredArgsConstructor
@Service
public class RoutineService {
    private final ExerciseLikeRepository exerciseLikeRepository;
    private final MemberRepository memberRepository;
    private final ExerciseRoutineRepository exerciseRoutineRepository;
    private final ExerciseEnrollRepository exerciseEnrollRepository;

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

    private List<ExerciseRoutineWithEnrollmentStatusDto> getExerciseRoutineWithEnrollmentStatus(String userEmail, List<ExerciseRoutine> exerciseRoutineList) {
        List<ExerciseRoutineWithEnrollmentStatusDto> result = new ArrayList<>();

        List<ExerciseEnroll> exerciseEnrolls = exerciseEnrollRepository.findAllByMemberEmail(userEmail);

        Set<ExerciseRoutine> enrolledRoutines = exerciseEnrolls.stream()
                .map(ExerciseEnroll::getExerciseRoutine)
                .collect(Collectors.toSet());

        for (ExerciseRoutine exerciseRoutine : exerciseRoutineList) {
            boolean isEnrolled = enrolledRoutines.contains(exerciseRoutine);
            ExerciseRoutineWithEnrollmentStatusDto dto = new ExerciseRoutineWithEnrollmentStatusDto(exerciseRoutine, isEnrolled);
            result.add(dto);
        }

        return result;
    }
}
