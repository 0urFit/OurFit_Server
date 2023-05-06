package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project1.OurFit.entity.ExerciseLike;

import java.util.List;
import java.util.Optional;

public interface ExerciseLikeRepository extends JpaRepository<ExerciseLike,Long> {
    Optional<ExerciseLike> findByMemberIdAndExerciseRoutineId(Long memberId, Long exerciseRoutineId);
    List<ExerciseLike> findAllByMemberId(Long id);

}
