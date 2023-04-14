package project1.OurFit.controller;

import constant.JsonCode;
import constant.JsonMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project1.OurFit.domain.JsonResponse;
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
}
