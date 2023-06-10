package project1.OurFit.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project1.OurFit.entity.ExerciseDetail;

import java.util.List;

public interface ExerciseDetailRepository extends JpaRepository<ExerciseDetail, Long> {

    @EntityGraph(attributePaths= "exerciseDetailSetList")
    @Query("SELECT ed FROM ExerciseDetail ed " +
            "LEFT JOIN FETCH ed.exerciseDetailSetList " +
            "WHERE ed.exerciseRoutine.id = :routineId " +
            "AND ed.weeks = :week " +
            "ORDER BY ed.weeks ASC, ed.day ASC, ed.sequence ASC")
    List<ExerciseDetail> findAllByWeekAndExerciseRoutine(
            @Param("routineId") Long routineId,
            @Param("week") int week
    );

    @Query("select distinct ed from ExerciseDetail ed join fetch ed.exerciseDetailSetList eds where ed.exerciseRoutine.id = :routineId")
    List<ExerciseDetail> findAllByExerciseRoutineIdWithSets(@Param("routineId") Long routineId);
}
