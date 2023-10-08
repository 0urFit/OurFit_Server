package project1.OurFit.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project1.OurFit.entity.EnrollDetail;

import java.util.List;

public interface EnrollDetailRepository extends JpaRepository<EnrollDetail,Long> {

    @EntityGraph(attributePaths = {"exerciseDetail", "enrollDetailSets"})
    List<EnrollDetail> findAllByExerciseDetail_ExerciseRoutine_IdAndExerciseDetail_fewWeek(Long exerciseRoutineId, int fewWeek);

    List<EnrollDetail> findByExerciseEnrollId(Long id);
}
