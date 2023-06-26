package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project1.OurFit.entity.ExerciseLike;

import java.util.List;
import java.util.Optional;

public interface ExerciseLikeRepository extends JpaRepository<ExerciseLike,Long> {

    List<ExerciseLike> findAllByMemberEmail(String email);

    @Query("SELECT el FROM ExerciseLike el JOIN FETCH el.member m WHERE m.email = :email")
    List<ExerciseLike> findByMemberEmail(@Param("email") String email);

    @Query("SELECT el From ExerciseLike el JOIN FETCH  el.member m WHERE m.email= :email and el.exerciseRoutine.id= :routineId")
    Optional<ExerciseLike> findByMemberEmailAndExerciseRoutineId(@Param("email") String email, @Param("routineId") Long routineId);

    boolean existsByMemberEmailAndExerciseRoutineId(String userEmail, Long routineId);

    @Query("SELECT el.exerciseRoutine.id FROM ExerciseLike el WHERE el.member.email = :email AND el.exerciseRoutine.id IN :routineIds")
    List<Long> findLikedRoutineIdsByMemberEmail(@Param("email") String email, @Param("routineIds") List<Long> routineIds);
}
