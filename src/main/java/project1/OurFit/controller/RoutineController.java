package project1.OurFit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project1.OurFit.response.ExerciseDetailDto;
import project1.OurFit.response.ExerciseRoutineWithEnrollmentStatusDto;
import project1.OurFit.service.RoutineService;
import project1.constant.response.JsonResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RoutineController {
    private final RoutineService routineService;

    /**
     * 운동루틴 좋아요 등록 API
     * @param routineId
     * @return
     */
    @PostMapping("/exercise/{routineId}/likes")
    public JsonResponse<String> postLike(@PathVariable Long routineId){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        routineService.postLike(userEmail,routineId);
        return new JsonResponse<>("좋아요 등록");
    }

    /**
     * 운동루틴 좋아요 취소 API
     * @param routineId
     * @return
     */
    @DeleteMapping("/exercise/{routineId}/likes")
    public JsonResponse<String> deleteLike(@PathVariable Long routineId){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        routineService.deleteLike(userEmail,routineId);
        return new JsonResponse<>("좋아요 취소");
    }

    /**
     * 운동 카테고리 조회 API
     * @param category
     * @return
     */
    @GetMapping("/exercise/{category}")
    @ResponseBody
    public JsonResponse<List<ExerciseRoutineWithEnrollmentStatusDto>> getExerciseRoutine(
            @PathVariable String category) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if (category.equals("all")) {
            List<ExerciseRoutineWithEnrollmentStatusDto> exercises = routineService.getExerciseRoutine(userEmail);
            return new JsonResponse<>(exercises);
        }
        List<ExerciseRoutineWithEnrollmentStatusDto> exerciseRoutineWithEnrollmentStatusDtoList =
                routineService.getExerciseRoutineByCategory(category,userEmail);
        return new JsonResponse<>(exerciseRoutineWithEnrollmentStatusDtoList);
    }

    @GetMapping("/exercise/{category}/{routineId}/{week}")
    @ResponseBody
    public JsonResponse<List<ExerciseDetailDto>> getExerciseDetails(
            @PathVariable String category, @PathVariable Long routineId, @PathVariable int week) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new JsonResponse<>(routineService.getExerciseDetails(category, routineId, email, week));
    }
}
