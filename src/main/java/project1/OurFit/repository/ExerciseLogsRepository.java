package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project1.OurFit.entity.ExerciseLogs;

import java.util.Optional;

public interface ExerciseLogsRepository extends JpaRepository<ExerciseLogs, Long> {

    Optional<ExerciseLogs> findByMemberIdAndExerciseRoutineIdAndWeekAndDay(Long memberId, Long exerciseRoutineId, int week, String day);
}
