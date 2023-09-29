package project1.OurFit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project1.OurFit.request.ExerciseCompleteDto;
import project1.OurFit.request.MemberDTO;
import project1.OurFit.response.*;
import project1.OurFit.service.MyPageService;
import project1.constant.response.JsonResponse;
import project1.constant.response.JsonResponseStatus;

import java.util.List;

import static project1.constant.response.JsonResponseStatus.SUCCESS;

@RequiredArgsConstructor
@RestController
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/mypage")
    @ResponseBody
    public JsonResponse<MemberDto> getMyInfo() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return new JsonResponse<>(myPageService.getMyInfo(userEmail));
    }

    /**
     * API문서 5-1
     * url : mypage?category=diet
     * MyPage 들어갔을 때 등록한 루틴 조회
     * @return
     */
    @GetMapping("/mypage/exercise")
    @ResponseBody
    public JsonResponse<List<MyRoutineRes>> getMyRoutine(String category){
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
    public JsonResponse<EnrollDetailDto> getMyRoutineDetail(
            @PathVariable Long routineId, @PathVariable int week){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new JsonResponse<>(myPageService.getEnrollDetails(email, routineId, week).get(0));
    }


    /**
     * MyPage 들어갔을 때 좋아요 한 루틴 조회
     * @return
     */
    @GetMapping("/mypage/like")
    @ResponseBody
    public JsonResponse<List<MyLikeRes>> getMyLikeRoutine(){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MyLikeRes> myLikeRes = myPageService.getMyLikeRoutine(userEmail);
        return new JsonResponse<>(myLikeRes);
    }

    @PatchMapping("/mypage/exercise/complete")
    @ResponseBody
    public JsonResponse<JsonResponseStatus> completeRoutine(@RequestBody ExerciseCompleteDto completeDto){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        myPageService.completeRoutine(userEmail, completeDto);
        return new JsonResponse<>(SUCCESS);
    }

    @PatchMapping("/mypage/m")
    @ResponseBody
    public JsonResponse<JsonResponseStatus> setMyInfo(@RequestBody MemberDto memberDto) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(userEmail);
        myPageService.saveMyInfo(memberDto, userEmail);
        return new JsonResponse<>(SUCCESS);
    }
}
