package project1.OurFit.controller;

import constant.JsonCode;
import constant.JsonMessage;
import constant.Oauth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import project1.OurFit.domain.JsonResponse;
import project1.OurFit.domain.KakaoProfile;
import project1.OurFit.domain.LoginRequest;
import project1.OurFit.domain.Member;
import project1.OurFit.service.MemberService;

@Controller
public class SignInUp {
    private final MemberService memberService;

    public SignInUp(MemberService memberService) {
        this.memberService = memberService;
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
    public JsonResponse oauthKakaoLogin(String code) {
        KakaoProfile info = memberService.loginKakao(code);
        return memberService.findEmail(info.getKakao_account().getEmail())
                .map(m -> new JsonResponse(true, JsonCode.SUCCESS.getNum(), JsonMessage.SUCCESS.getMessage(),
                        new LoginRequest(info.getKakao_account().getEmail())))
                .orElseGet(() -> {
                    return new JsonResponse(false, JsonCode.FAIL.getNum(), JsonMessage.FAIL.getMessage(),
                            new Member(info.getKakao_account().getEmail(), info.getProperties().getNickname(),
                                    info.getKakao_account().getGender()));
                });
    }
}
