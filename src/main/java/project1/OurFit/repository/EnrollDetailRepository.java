package project1.OurFit.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project1.OurFit.entity.EnrollDetail;
import project1.OurFit.entity.ExerciseEnroll;

import java.util.List;

public interface EnrollDetailRepository extends JpaRepository<EnrollDetail,Long> {

    List<EnrollDetail> findByExerciseEnrollId(Long id);
}
