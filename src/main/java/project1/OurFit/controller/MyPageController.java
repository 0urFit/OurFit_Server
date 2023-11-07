package project1.OurFit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project1.OurFit.request.ExerciseCompleteDto;
import project1.OurFit.response.*;
import project1.OurFit.service.MyPageService;
import project1.constant.exception.ExerciseSuccessExecption;
import project1.constant.response.JsonResponse;
import project1.constant.response.JsonResponseStatus;

import java.util.List;

import static project1.constant.response.JsonResponseStatus.SUCCESS;
import static project1.constant.response.JsonResponseStatus.SUCCESS_EXERCISE;

@RequiredArgsConstructor
@RestController
public class MyPageController {
    private final MyPageService myPageService;

    /**
     * 사용자 개인정보 조회
     * @return
     */
    @GetMapping("/mypage")
    @ResponseBody
    public JsonResponse<MemberDto> getMyInfo() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return new JsonResponse<>(myPageService.getMyInfo(userEmail));
    }

    /**
     * API문서 5-1
     * url : mypage?category=diet성
     * MyPage 들어갔을 때 등록한 루틴 조회
     * @return
     */
    @GetMapping("/mypage/exercise")
    @ResponseBody
    public JsonResponse<List<MyRoutineRes>> getMyRoutine(String category){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return new JsonResponse<>(myPageService.getMyRoutine(userEmail, category));
    }

    /**
     * 저장한 운동 상세 루틴 가져오기 API
     * @param routineId
     * @return
     */
    @GetMapping("/mypage/exercise/{routineId}")
    @ResponseBody
    public JsonResponse<ExerciseDetailDto> getMyRoutineDetail(@PathVariable final Long routineId){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new JsonResponse<>(myPageService.getEnrollDetails(email, routineId).get(0));
    }

    @GetMapping("/mypage/exercise/{routineId}/view")
    @ResponseBody
    public JsonResponse<ExerciseViewDto> getMyRoutineDetailView(@PathVariable final long routineId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new JsonResponse<>(myPageService.getEnrollDetailView(email, routineId));
    }

    @GetMapping("/mypage/like")
    @ResponseBody
    public JsonResponse<List<MyLikeRes>> getMyLikeRoutine(){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MyLikeRes> myLikeRes = myPageService.getMyLikeRoutine(userEmail);
        return new JsonResponse<>(myLikeRes);
    }

    /**
     * 운동 완료 API
     * @param completeDto
     * @param routineId
     * @return
     */
    @PostMapping("/mypage/exercise/{routineId}/complete")
    @ResponseBody
    public JsonResponse<JsonResponseStatus> completeRoutine(
            @RequestBody ExerciseCompleteDto completeDto, @PathVariable Long routineId){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if(myPageService.completeRoutine(userEmail, completeDto, routineId))
            return new JsonResponse<>(SUCCESS);
        throw new ExerciseSuccessExecption(SUCCESS_EXERCISE);
    }

    /**
     * 운동 완료 취소 API
     * @param completeDto
     * @param routineId
     * @return
     */
    @DeleteMapping("/mypage/exercise/{routineId}/complete")
    @ResponseBody
    public JsonResponse<JsonResponseStatus> deleteCompleteRoutine(
            @RequestBody ExerciseCompleteDto completeDto, @PathVariable Long routineId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        myPageService.deleteCompleteRoutine(userEmail, completeDto, routineId);
        return new JsonResponse<>(SUCCESS);
    }

    /**
     * 개인정보 수정 API
     * @param memberDto
     * @return
     */
    @PatchMapping("/mypage/u")
    @ResponseBody
    public JsonResponse<JsonResponseStatus> setMyInfo(@RequestBody MemberDto memberDto) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        myPageService.saveMyInfo(memberDto, userEmail);
        return new JsonResponse<>(SUCCESS);
    }
}
