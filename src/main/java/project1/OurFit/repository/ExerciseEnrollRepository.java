package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project1.OurFit.entity.ExerciseEnroll;

import java.util.List;

public interface ExerciseEnrollRepository extends JpaRepository<ExerciseEnroll, Long> {

    List<ExerciseEnroll> findByMemberId(Long id);

}
