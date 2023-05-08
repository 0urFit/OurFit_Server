package project1.OurFit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project1.OurFit.jwt.JwtTokenProvider;
import project1.OurFit.service.RoutineService;
import project1.constant.response.JsonResponse;

@RequiredArgsConstructor
@RestController
public class RoutineController {
    private final RoutineService routineService;
    private final JwtTokenProvider jt;

    /**
     * 운동루틴 좋아요 등록 API
     * @param routineId
     * @return
     */
    @PostMapping("/exercise/{routineId}/likes")
    public JsonResponse<String> postLike(@PathVariable Long routineId){

        String userEmail = jt.getEmail();
        routineService.postLike(userEmail,routineId);
        return new JsonResponse<>("좋아요 등록");
    }

    /**
     * 운동루틴 좋아요 취소 API
     * @param routineId
     * @return
     */
    @DeleteMapping("/exercise/{memberId}/{routineId}/likes")
    public JsonResponse<String> deleteLike(@PathVariable Long routineId){
        String userEmail = jt.getEmail();
        routineService.deleteLike(userEmail,routineId);
        return new JsonResponse<>("좋아요 취소");
    }

}
