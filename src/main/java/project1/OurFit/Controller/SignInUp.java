package project1.OurFit.Controller;

import constant.JsonCode;
import constant.JsonMessage;
import constant.JsonResponse;
import constant.Oauth;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project1.OurFit.Entity.Member;
import project1.OurFit.Request.LoginDTO;
import project1.OurFit.Request.MemberDTO;
import project1.OurFit.Request.OAuthTokenDTO;
import project1.OurFit.Response.PostKakaoProfile;
import project1.OurFit.Response.PostSignUp;
import project1.OurFit.service.KakaoService;
import project1.OurFit.service.MemberService;

import java.util.Optional;

@Controller
public class SignInUp {
    private final MemberService memberService;
    private final KakaoService kakaoService;

    public SignInUp(MemberService memberService, KakaoService kakaoService) {
        this.memberService = memberService;
        this.kakaoService = kakaoService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonResponse login(@RequestBody LoginDTO login) {
        return memberService.findEmailAndPassword(login.getEmail(), login.getPassword())
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

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonResponse signup(@RequestBody MemberDTO member) {
        return memberService.join(member)
                .map(m -> new JsonResponse(true, JsonCode.SUCCESS.getNum(), JsonMessage.SUCCESS.getMessage()))
                .orElse(new JsonResponse(false, JsonCode.FAIL.getNum(), JsonMessage.FAIL.getMessage()));
    }

    @GetMapping("/auth/kakao/callback")
    @ResponseBody
    public synchronized JsonResponse oauthKakaoLogin(String code) {
        OAuthTokenDTO oAuthToken = kakaoService.getToken(code);
        PostKakaoProfile info =  kakaoService.getUserInfo(oAuthToken);
        Optional<Member> member = memberService.findEmail(info.getKakao_account().getEmail());
        JsonResponse jsonResponse = new JsonResponse(true, JsonCode.SUCCESS.getNum(), JsonMessage.SUCCESS.getMessage());
        jsonResponse.setResult(member.map(m -> new PostSignUp(info.getKakao_account().getEmail()))
                .orElseGet(() -> new PostSignUp(info.getKakao_account().getEmail(), info.getKakao_account().getGender())));
//        if (member.isPresent())
//            jsonResponse.setResult(new PostSignUp(info.getKakao_account().getEmail()));
//        else
//            jsonResponse.setResult(new PostSignUp(info.getKakao_account().getEmail(), info.getKakao_account().getGender()));
        return jsonResponse;
    }
}
