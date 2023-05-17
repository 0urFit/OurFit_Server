package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project1.OurFit.entity.ExerciseDetail;
import project1.OurFit.entity.ExerciseRoutine;

import java.util.List;

public interface ExerciseDetailRepository extends JpaRepository<ExerciseDetail, Long> {

    @Query("SELECT ed FROM ExerciseDetail ed " +
            "WHERE ed.exerciseRoutine.id = :routineId " +
            "AND ed.exerciseRoutine.category = :category " +
            "ORDER BY ed.weeks ASC, ed.day ASC, ed.sequence ASC")
    List<ExerciseDetail> findAllByWeeksAndExerciseRoutineCategory(
            @Param("routineId") Long routineId,
            @Param("category") String category
    );
}
