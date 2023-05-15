package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project1.OurFit.entity.ExerciseEnroll;
import project1.OurFit.entity.ExerciseRoutine;

import java.util.List;
import java.util.Map;

public interface ExerciseEnrollRepository extends JpaRepository<ExerciseEnroll, Long> {

    List<ExerciseEnroll> findByMemberId(Long id);

    @Query("SELECT ee FROM ExerciseEnroll ee JOIN FETCH ee.member m WHERE m.email = :email")
    List<ExerciseEnroll> findByMemberEmail(String email);

    boolean existsByExerciseRoutineAndMemberEmail(ExerciseRoutine exerciseRoutine, String userEmail);

    List<ExerciseEnroll> findAllByMemberEmail(String userEmail);
}
