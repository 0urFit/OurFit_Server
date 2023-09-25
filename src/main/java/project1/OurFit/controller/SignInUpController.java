package project1.OurFit.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project1.OurFit.entity.Member;
import project1.OurFit.response.PostLoginDto;
import project1.OurFit.service.JwtService;
import project1.constant.Oauth;
import project1.constant.exception.BaseException;
import project1.constant.exception.DuplicateException;
import project1.constant.response.JsonResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project1.OurFit.request.LoginDTO;
import project1.OurFit.request.MemberDTO;
import project1.OurFit.request.OAuthTokenDTO;
import project1.OurFit.response.PostKakaoProfile;
import project1.OurFit.service.KakaoService;
import project1.OurFit.service.MemberService;
import project1.constant.response.JsonResponseStatus;

@Controller
@RequiredArgsConstructor
public class SignInUpController {
    private final MemberService memberService;
    private final KakaoService kakaoService;
    private final JwtService jwtService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonResponse<PostLoginDto> login(@RequestBody LoginDTO login, HttpServletRequest request) {
        Member member = memberService.findEmailAndPassword(login);
        return new JsonResponse<>(jwtService.createToken(member));
    }

    @GetMapping("/checkemail/{email}")
    @ResponseBody
    public JsonResponse<JsonResponseStatus> checkEmail(@PathVariable String email) {
        memberService.findEmail(email);
        return new JsonResponse<>(JsonResponseStatus.SUCCESS);
    }

    @GetMapping("/checknick/{nickname}")
    @ResponseBody
    public JsonResponse<JsonResponseStatus> checkNickname(@PathVariable String nickname) {
        memberService.findNickname(nickname);
        return new JsonResponse<>(JsonResponseStatus.SUCCESS);
    }

    //회원가입
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonResponse<JsonResponseStatus> signup(@RequestBody MemberDTO member) {
        memberService.join(member);
        return new JsonResponse<>(JsonResponseStatus.SUCCESS);
    }

//    @GetMapping("/kakao")
//    @ResponseBody
//    public synchronized ResponseEntity<JsonResponse<PostLoginDto>> oauthKakaoLogin(
//            @RequestParam("authorizationCode") String code) {
//        OAuthTokenDTO oAuthToken = kakaoService.requestOAuthToken(code);
//        PostKakaoProfile info =  kakaoService.getUserProfile(oAuthToken.getAccess_token());
//
//        Boolean isEmailExist = memberService.findEmail(info.getKakao_account().getEmail());
//        if (isEmailExist)
//            return ResponseEntity.ok(new JsonResponse<>(jwtService.createToken(info.getKakao_account().getEmail())));
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
//                new JsonResponse<>(
//                        new PostLoginDto(null, null,
//                            info.getKakao_account().getEmail(), info.getKakao_account().getGender()),
//                    JsonResponseStatus.UNAUTHORIZED));
//    }

    @GetMapping("/oauth/kakao")
    public String test() {
        return "redirect:" + Oauth.KAKAOLOGIN.getValue();
    }
}
