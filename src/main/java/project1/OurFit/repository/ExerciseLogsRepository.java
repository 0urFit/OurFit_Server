package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project1.OurFit.entity.ExerciseLogs;
import project1.OurFit.entity.ExerciseRoutine;

import java.util.List;
import java.util.Optional;

public interface ExerciseLogsRepository extends JpaRepository<ExerciseLogs, Long> {

    List<ExerciseLogs> findByExerciseRoutineAndWeek(ExerciseRoutine exerciseRoutine, int week);
    Optional<ExerciseLogs> findByExerciseRoutineAndWeekAndDay(ExerciseRoutine exerciseRoutine, int week, String day);
    List<ExerciseLogs> findByMemberIdAndExerciseRoutineId(Long memberId, Long exerciseRoutine);
}
