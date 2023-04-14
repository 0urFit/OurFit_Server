package project1.OurFit.controller;

import constant.JsonCode;
import constant.JsonMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
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
}
