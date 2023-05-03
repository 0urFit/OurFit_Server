package project1.OurFit.controller;

import project1.constant.response.JsonMessage;
import project1.constant.response.JsonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project1.OurFit.request.LoginDTO;
import project1.OurFit.request.MemberDTO;
import project1.OurFit.request.OAuthTokenDTO;
import project1.OurFit.response.PostKakaoProfile;
import project1.OurFit.response.PostSignUp;
import project1.OurFit.response.TestDto;
import project1.OurFit.service.KakaoService;
import project1.OurFit.service.MemberService;
import project1.OurFit.vo.DuplicateCheckResult;

@Controller
public class SignInUpController {
    private final MemberService memberService;
    private final KakaoService kakaoService;

    //-> @RequiredArgsConstructor 사용하면 생략 가능하지만 학습용으로 냅둠
    public SignInUpController(MemberService memberService, KakaoService kakaoService) {
        this.memberService = memberService;
        this.kakaoService = kakaoService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonResponse<String> login(@RequestBody LoginDTO login) {

        return new JsonResponse<>(memberService.findEmailAndPassword(login.getEmail(), login.getPassword()));

    }

    @GetMapping("/checkemail/{email}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public JsonResponse<TestDto> checkEmail(@PathVariable String email) {
        TestDto testDto = memberService.findEmail(email);
        return new JsonResponse<>(testDto);
    }

    @GetMapping("/checknick/{nickname}")
    @ResponseBody
    public ResponseEntity<JsonResponse> checkNickname(@PathVariable String nickname) {
        return memberService.findNickname(nickname)
                .map(m -> ResponseEntity.ok(
                        new JsonResponse(false, HttpStatus.OK.value(), JsonMessage.SUCCESS.getMessage())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(
                        new JsonResponse(true, HttpStatus.NOT_FOUND.value(), JsonMessage.NOTFOUND.getMessage())));
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<JsonResponse> signup(@RequestBody MemberDTO member) {
        DuplicateCheckResult checkResult;
        try {
            checkResult = memberService.join(member);
        } catch (Exception e) { // 동시성 문제
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(
                    new JsonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            JsonMessage.FAIL.getMessage())
            );
        }

        if (checkResult.isDuplicate()) {
            System.out.println("true");
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(
                    new JsonResponse(false, HttpStatus.CONFLICT.value(),
                            checkResult.getField() + JsonMessage.EXISTS.getMessage()));
        } else {
            System.out.println("false");
            return ResponseEntity.ok(
                    new JsonResponse(true, HttpStatus.OK.value(), JsonMessage.SUCCESS.getMessage()));
        }
    }

    @GetMapping("/auth/kakao/callback")
    @ResponseBody
    public synchronized ResponseEntity<JsonResponse> oauthKakaoLogin(String code) {
        OAuthTokenDTO oAuthToken = kakaoService.getToken(code);
        PostKakaoProfile info =  kakaoService.getUserInfo(oAuthToken);

        PostSignUp postSignUp;
        HttpStatus httpStatus;
        String message;

        if (memberService.checkMember(info.getKakao_account().getEmail())) {
            postSignUp = new PostSignUp(info.getKakao_account().getEmail());
            httpStatus = HttpStatus.OK;
            message = JsonMessage.SUCCESS.getMessage();
        } else {
            postSignUp = new PostSignUp(info.getKakao_account().getEmail(), info.getKakao_account().getGender());
            httpStatus = HttpStatus.NOT_FOUND;
            message = JsonMessage.NOTFOUND.getMessage();
        }

        JsonResponse jsonResponse = new JsonResponse(true, httpStatus.value(), message, postSignUp);
        return ResponseEntity.status(httpStatus).body(jsonResponse);
    }
}
