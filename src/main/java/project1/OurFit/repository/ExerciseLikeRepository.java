package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project1.OurFit.entity.ExerciseEnroll;
import project1.OurFit.entity.ExerciseLike;

import java.util.List;
import java.util.Optional;

public interface ExerciseLikeRepository extends JpaRepository<ExerciseLike,Long> {
    Optional<ExerciseLike> findByMemberIdAndExerciseRoutineId(Long memberId, Long exerciseRoutineId);
    List<ExerciseLike> findAllByMemberId(Long id);

    @Query("SELECT el FROM ExerciseLike el JOIN FETCH el.member m WHERE m.email = :email")
    List<ExerciseLike> findByMemberEmail(String email);

    @Query("SELECT el From ExerciseLike el JOIN FETCH  el.member m WHERE m.email= :email and el.exerciseRoutine.id= :routineId")
    Optional<ExerciseLike> findByMemberEmailAndExerciseRoutineId(String email, Long routineId);

}
