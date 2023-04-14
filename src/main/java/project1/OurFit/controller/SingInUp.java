package project1.OurFit.controller;

import constant.JsonCode;
import constant.JsonMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project1.OurFit.domain.JsonResponse;
import project1.OurFit.domain.LoginRequest;
import project1.OurFit.service.MemberService;

@Controller
public class SingInUp {
    private final MemberService memberService;

    public SingInUp(MemberService memberService) {
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
}
