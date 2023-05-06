package project1.OurFit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project1.OurFit.response.MyLikeRes;
import project1.OurFit.response.MyRoutineRes;
import project1.OurFit.service.MyPageService;
import project1.constant.response.JsonResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MyPageController {
    private final MyPageService myPageService;

    /**
     * MyPage 들어갔을 때 등록한 루틴 조회
     * @param userId
     * @return
     */
    @GetMapping("/mypage/{userId}")
    public JsonResponse<List<MyRoutineRes>> getMyRoutine(@PathVariable Long userId){
        List<MyRoutineRes> myRoutineResList = myPageService.getMyRoutine(userId);
        return new JsonResponse<>(myRoutineResList);

    }

    /**
     * MyPage 들어갔을 때 카테고리로 등록한 루틴 조회
     * @param userId
     * @param category
     * @return
     */
    @GetMapping("/mypage/{userId}/exercise/{category}")
    public JsonResponse<List<MyRoutineRes>> getMyRoutineByCate(@PathVariable Long userId,
                                             @PathVariable String category){

        List<MyRoutineRes> myRoutineRes = myPageService.getMyRoutineByCate(userId, category);
        return new JsonResponse<>(myRoutineRes);
    }

    @GetMapping("mypage/{memberId}/like")
    public JsonResponse<List<MyLikeRes>> getMyLikeRoutine(@PathVariable Long memberId){
        List<MyLikeRes> myLikeRes = myPageService.getMyLikeRoutine(memberId);
        return new JsonResponse<>(myLikeRes);
    }

}
