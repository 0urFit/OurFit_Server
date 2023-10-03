package project1.OurFit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import project1.OurFit.entity.RefreshToken;
import project1.OurFit.jwt.JwtTokenProvider;
import project1.OurFit.response.JwtTokenDto;
import project1.OurFit.service.JwtService;
import project1.OurFit.service.RedisService;
import project1.constant.response.JsonResponse;

@Controller
@RequiredArgsConstructor
public class JwtTokenController {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final JwtService jwtService;

    @PostMapping("/newtoken")
    @ResponseBody
    public JsonResponse<JwtTokenDto> refreshAccessToken(@RequestBody JwtTokenDto jwtTokenDto) {
        String refreshToken = jwtTokenDto.getRefreshToken();
        String email = jwtService.extractEmailFromRefreshToken(refreshToken);
        RefreshToken storedRefreshToken = redisService.getRedisByEmail(email);
        redisService.validateRefreshToken(storedRefreshToken, refreshToken);
        String newAccessToken = jwtTokenProvider.createAccessToken(email);
        return new JsonResponse<>(new JwtTokenDto(newAccessToken, null));
    }

}
