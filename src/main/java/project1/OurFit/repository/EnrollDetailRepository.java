package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project1.OurFit.entity.EnrollDetail;

import java.util.List;

public interface EnrollDetailRepository extends JpaRepository<EnrollDetail,Long> {

    List<EnrollDetail> findAllByExerciseDetail_ExerciseRoutine_IdAndExerciseDetail_Weeks(Long exerciseRoutineId, int weeks);

    List<EnrollDetail> findByExerciseEnrollId(Long id);
}
