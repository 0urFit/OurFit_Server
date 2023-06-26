package project1.OurFit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project1.OurFit.request.ExerciseCompleteDto;
import project1.OurFit.response.EnrollDetailDto;
import project1.OurFit.response.ExerciseDetailDto;
import project1.OurFit.response.MyLikeRes;
import project1.OurFit.response.MyRoutineRes;
import project1.OurFit.service.MyPageService;
import project1.constant.response.JsonResponse;
import project1.constant.response.JsonResponseStatus;

import java.util.List;

import static project1.constant.response.JsonResponseStatus.SUCCESS;

@RequiredArgsConstructor
@RestController
public class MyPageController {
    private final MyPageService myPageService;

    /**
     * API문서 5-1
     * url : mypage?category=diet
     * MyPage 들어갔을 때 등록한 루틴 조회
     * @return
     */
    @GetMapping("/mypage")
    @ResponseBody
    public JsonResponse<List<MyRoutineRes>> getMyRoutine(
            @RequestParam(required = false) String category){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MyRoutineRes> myRoutineResList = myPageService.getMyRoutine(userEmail, category);
        return new JsonResponse<>(myRoutineResList);
    }

    /**
     * API 문서 5-2
     * 저장한 운동 루틴 Detail Page
     */
    @GetMapping("/mypage/exercise/{routineId}/{week}")
    @ResponseBody
    public JsonResponse<List<EnrollDetailDto>> getMyRoutineDetail(
            @PathVariable Long routineId, @PathVariable int week){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new JsonResponse<>(myPageService.getEnrollDetails(email, routineId, week));
    }


    /**
     * MyPage 들어갔을 때 좋아요 한 루틴 조회
     * @return
     */
    @GetMapping("mypage/like")
    @ResponseBody
    public JsonResponse<List<MyLikeRes>> getMyLikeRoutine(){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MyLikeRes> myLikeRes = myPageService.getMyLikeRoutine(userEmail);
        return new JsonResponse<>(myLikeRes);
    }

    @PatchMapping("mypage/exercise/complete")
    @ResponseBody
    public JsonResponse<JsonResponseStatus> completeRoutine(@RequestBody ExerciseCompleteDto completeDto){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        myPageService.completeRoutine(userEmail, completeDto);
        return new JsonResponse<>(SUCCESS);
    }
}
