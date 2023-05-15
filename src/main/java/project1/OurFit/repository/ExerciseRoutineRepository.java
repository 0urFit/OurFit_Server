package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project1.OurFit.entity.ExerciseRoutine;

import java.util.List;
import java.util.Optional;

public interface ExerciseRoutineRepository extends JpaRepository<ExerciseRoutine,Long> {

    Optional<ExerciseRoutine> findByIdAndCategory(Long id, String category);

    List<ExerciseRoutine> findByCategory(String category);
}
