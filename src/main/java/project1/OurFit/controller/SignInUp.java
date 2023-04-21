package project1.OurFit.controller;

import constant.JsonCode;
import constant.JsonMessage;
import constant.Oauth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project1.OurFit.domain.*;
import project1.OurFit.service.KakaoService;
import project1.OurFit.service.MemberService;

@Controller
public class SignInUp {
    private final MemberService memberService;
    private final KakaoService kakaoService;

    public SignInUp(MemberService memberService, KakaoService kakaoService) {
        this.memberService = memberService;
        this.kakaoService = kakaoService;
    }

    @PostMapping("/login")
    @ResponseBody
    public JsonResponse login(@RequestBody LoginRequest value) {
        return memberService.findEmailAndPassword(value.getEmail(), value.getPassword())
                .map(m -> new JsonResponse(true, JsonCode.SUCCESS.getNum(), JsonMessage.SUCCESS.getMessage()))
                .orElse(new JsonResponse(false, JsonCode.FAIL.getNum(), JsonMessage.FAIL.getMessage()));
    }

    @GetMapping("/checkemail/{email}")
    @ResponseBody
    public JsonResponse checkEmail(@PathVariable String email) {
        return memberService.findEmail(email)
                .map(m -> new JsonResponse(false, JsonCode.FAIL.getNum(), JsonMessage.FAIL.getMessage()))
                .orElse(new JsonResponse(true, JsonCode.SUCCESS.getNum(), JsonMessage.SUCCESS.getMessage()));
    }

    @GetMapping("/checknick/{nickname}")
    @ResponseBody
    public JsonResponse checkNickname(@PathVariable String nickname) {
        return memberService.findNickname(nickname)
                .map(m -> new JsonResponse(false, JsonCode.FAIL.getNum(), JsonMessage.FAIL.getMessage()))
                .orElse(new JsonResponse(true, JsonCode.SUCCESS.getNum(), JsonMessage.SUCCESS.getMessage()));
    }

    @PostMapping("/signup")
    @ResponseBody
    public JsonResponse signup(@RequestBody Member member) {
        return memberService.join(member)
                .map(m -> new JsonResponse(true, JsonCode.SUCCESS.getNum(), JsonMessage.SUCCESS.getMessage()))
                .orElse(new JsonResponse(false, JsonCode.FAIL.getNum(), JsonMessage.FAIL.getMessage()));
    }

    @GetMapping("/oauth/kakao")
    public String kakao() {
        return "redirect:" + Oauth.KAKAOLOGIN.getValue();
    }

    @GetMapping("/auth/kakao/callback")
    @ResponseBody
    public synchronized JsonResponse oauthKakaoLogin(String code) {
        OAuthToken oAuthToken = kakaoService.getToken(code);
        KakaoProfile info =  kakaoService.getUserInfo(oAuthToken);
        return memberService.findEmail(info.getKakao_account().getEmail())
                .map(m -> new JsonResponse(true, JsonCode.SUCCESS.getNum(), JsonMessage.SUCCESS.getMessage(),
                        new LoginRequest(info.getKakao_account().getEmail())))
                .orElseGet(() -> new JsonResponse(false, JsonCode.FAIL.getNum(), JsonMessage.FAIL.getMessage(),
                        new SignUpRequest(info.getKakao_account().getEmail(), info.getKakao_account().getGender(),
                                info.getProperties().getNickname())));
    }
}
