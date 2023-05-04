package project1.OurFit.controller;

import project1.OurFit.response.PostLoginDto;
import project1.OurFit.service.JwtService;
import project1.constant.Oauth;
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
public class SignInUpController {
    private final MemberService memberService;
    private final KakaoService kakaoService;
    private final JwtService jwtService;

    //-> @RequiredArgsConstructor 사용하면 생략 가능하지만 학습용으로 냅둠
    public SignInUpController(MemberService memberService, KakaoService kakaoService, JwtService jwtService) {
        this.memberService = memberService;
        this.kakaoService = kakaoService;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonResponse<PostLoginDto> login(@RequestBody LoginDTO login) {
        return new JsonResponse<>(memberService.findEmailAndPassword(login.getEmail(), login.getPassword()));
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

    @GetMapping("/oauth/kakao")
    public String kakao() {
        return "redirect:" + Oauth.KAKAOLOGIN.getValue();
    }

    @GetMapping("/auth/kakao/callback")
    @ResponseBody
    public synchronized JsonResponse<PostLoginDto> oauthKakaoLogin(String code) {
        OAuthTokenDTO oAuthToken = kakaoService.getToken(code);
        PostKakaoProfile info =  kakaoService.getUserInfo(oAuthToken);

        Boolean isSuccess = memberService.findEmail(info.getKakao_account().getEmail());
        if (isSuccess)
            return new JsonResponse<>(jwtService.authorize(info.getKakao_account().getEmail()));

        return new JsonResponse<>(new PostLoginDto(info.getKakao_account().getEmail(),
                    info.getKakao_account().getGender()), JsonResponseStatus.UNAUTHORIZED);
    }
}
