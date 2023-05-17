package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project1.OurFit.entity.ExerciseDetailSet;

import java.util.Arrays;
import java.util.List;

public interface ExerciseDetailSetRepository extends JpaRepository<ExerciseDetailSet, Long> {

    List<ExerciseDetailSet> findAllByExerciseDetailIdInOrderBySequence(List<Long> exerciseDetailIds);
}
