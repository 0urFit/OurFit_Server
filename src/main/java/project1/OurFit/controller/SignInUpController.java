package project1.OurFit.controller;

import lombok.RequiredArgsConstructor;
import project1.OurFit.entity.Member;
import project1.OurFit.response.JwtTokenDto;
import project1.OurFit.response.SignUpDto;
import project1.OurFit.service.JwtService;
import project1.OurFit.service.KakaoAccessTokenProviderService;
import project1.OurFit.service.KakaoUserInfoProviderService;
import project1.constant.Oauth;
import project1.constant.response.JsonResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project1.OurFit.request.LoginDTO;
import project1.OurFit.request.MemberDTO;
import project1.OurFit.service.MemberService;
import project1.constant.response.JsonResponseStatus;

@Controller
@RequiredArgsConstructor
public class SignInUpController {
    private final MemberService memberService;
    private final KakaoAccessTokenProviderService kakaoAccessTokenProviderService;
    private final KakaoUserInfoProviderService kakaoUserInfoProviderService;
    private final JwtService jwtService;

    /**
     * 자체 로그인 API
     * @param login
     * @return
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonResponse<JwtTokenDto> login(@RequestBody LoginDTO login) {
        Member member = memberService.authenticateMember(login);
        return new JsonResponse<>(jwtService.createToken(member));
    }

    /**
     * 이메일 중복체크 API
     * url : ?email=aossuper7@naver.com
     * @param email
     * @return
     */
    @GetMapping("/checkemail")
    @ResponseBody
    public JsonResponse<JsonResponseStatus> checkEmail(final String email) {
        memberService.validateEmail(email);
        return new JsonResponse<>(JsonResponseStatus.USERS_EMPTY_EMAIL);
    }

    /**
     * 닉네임 중복체크 API
     * url : ?nick=aossuper7
     * @param nick
     * @return
     */
    @GetMapping("/checknick")
    @ResponseBody
    public JsonResponse<JsonResponseStatus> checkNickname(final String nick) {
        memberService.validateNickname(nick);
        return new JsonResponse<>(JsonResponseStatus.USERS_EMPTY_NICKNAME);
    }

    /**
     * 회원가입 API
     * @param member
     * @return
     */
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonResponse<JsonResponseStatus> signup(@RequestBody MemberDTO member) {
        memberService.join(member);
        return new JsonResponse<>(JsonResponseStatus.SUCCESS);
    }

    /**
     * kakao 로그인 & 회원가입 API
     * @param code
     * @return
     */
    @GetMapping("/kakao")
    @ResponseBody
    public JsonResponse<JwtTokenDto> oauthKakaoLogin(
            @RequestParam("authorizationCode") final String code) {
        String accessToken = kakaoAccessTokenProviderService.getAccessToken(code);
        SignUpDto signUpDto = kakaoUserInfoProviderService.getUserInfo(accessToken);

        Member member = memberService.findKakaoId(signUpDto);
        return new JsonResponse<>(jwtService.createToken(member));
    }

    @GetMapping("/oauth/kakao")
    public String test() {
        return "redirect:" + Oauth.KAKAOLOGIN.getValue();
    }
}
