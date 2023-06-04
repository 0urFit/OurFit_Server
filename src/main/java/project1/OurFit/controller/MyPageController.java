package project1.OurFit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
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
        if(category==null){
            List<MyRoutineRes> myRoutineResList = myPageService.getMyRoutine(userEmail);
            return new JsonResponse<>(myRoutineResList);
        }
        List<MyRoutineRes> myRoutineRes = myPageService.getMyRoutineByCate(userEmail, category);
        return new JsonResponse<>(myRoutineRes);
    }

    /**
     * API 문서 5-2
     * 저장한 운동 루틴 Detail Page
     */
    @GetMapping("/mypage/exercise/{category}/{routineId}/{week}")
    @ResponseBody
    public JsonResponse<List<EnrollDetailDto>> getMyRoutineDetail(
            @PathVariable String category, @PathVariable Long routineId, @PathVariable int week){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<EnrollDetailDto> exerciseDetailDtoList =myPageService
                .getMyRoutineDetail(category,routineId,email,week);

        return new JsonResponse<>(exerciseDetailDtoList);
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

    @PatchMapping("mypage/exercise/{rouId}/complete")
    @ResponseBody
    public JsonResponse<JsonResponseStatus> completeRoutine(@PathVariable Long rouId){
        myPageService.completeRoutine(rouId);
        return new JsonResponse<>(SUCCESS);
    }
}
