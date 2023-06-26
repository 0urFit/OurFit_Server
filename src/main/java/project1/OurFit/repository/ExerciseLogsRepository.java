package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project1.OurFit.entity.ExerciseLogs;
import project1.OurFit.entity.ExerciseRoutine;
import project1.OurFit.entity.Member;

import java.util.List;
import java.util.Optional;

public interface ExerciseLogsRepository extends JpaRepository<ExerciseLogs, Long> {

    Optional<ExerciseLogs> findByMemberAndExerciseRoutineAndWeekAndDay(Member member, ExerciseRoutine exerciseRoutine, int week, String day);

    List<ExerciseLogs> findByExerciseRoutineIdAndWeek(long routineId, int week);
}
