package project1.OurFit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import project1.OurFit.service.RoutineService;
import project1.constant.response.JsonResponse;

@RequiredArgsConstructor
@RestController
public class RoutineController {
    private final RoutineService routineService;

    /**
     * 운동루틴 좋아요 등록 API
     * @param memberId
     * @param routineId
     * @return
     */
    @PostMapping("/exercise/{memberId}/{routineId}/likes")
    public JsonResponse<String> postLike(@PathVariable Long memberId,
                                         @PathVariable Long routineId){
        routineService.postLike(memberId,routineId);
        return new JsonResponse<>("좋아요 등록");
    }

    /**
     * 운동루틴 좋아요 취소 API
     * @param memberId
     * @param routineId
     * @return
     */
    @DeleteMapping("/exercise/{memberId}/{routineId}/likes")
    public JsonResponse<String> deleteLike(@PathVariable Long memberId,
                                           @PathVariable Long routineId){
        routineService.deleteLike(memberId,routineId);
        return new JsonResponse<>("좋아요 취소");
    }

}
