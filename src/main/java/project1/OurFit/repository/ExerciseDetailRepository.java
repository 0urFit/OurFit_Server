package project1.OurFit.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project1.OurFit.entity.ExerciseDetail;

import java.util.List;

public interface ExerciseDetailRepository extends JpaRepository<ExerciseDetail, Long> {

    @Query("select ed from ExerciseDetail ed join fetch ed.exerciseDetailSetList where ed.exerciseRoutine.id = :exerciseRoutineId AND ed.fewWeek = :week")
    List<ExerciseDetail> findByExerciseRoutineIdAndWeeks(@Param("exerciseRoutineId") Long exerciseRoutineId, @Param("week") int week);

    @Query("select distinct ed from ExerciseDetail ed join fetch ed.exerciseDetailSetList eds where ed.exerciseRoutine.id = :routineId")
    List<ExerciseDetail> findAllByExerciseRoutineIdWithSets(@Param("routineId") Long routineId);
}
