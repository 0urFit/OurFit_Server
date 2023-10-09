package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project1.OurFit.entity.ExerciseRoutine;

import java.util.List;
import java.util.Optional;

public interface ExerciseRoutineRepository extends JpaRepository<ExerciseRoutine,Long> {

    List<ExerciseRoutine> findByCategory(String category);

    @Query("select e from ExerciseRoutine e join fetch e.exerciseEnrollList el where el.member.id = :memberId and el.exerciseRoutine.id = :routineId")
    Optional<ExerciseRoutine> findByExerciseRoutineWithEnrollByMemberIdAndRoutineId(@Param("memberId") Long memberId, @Param("routineId") Long routineId);
}
