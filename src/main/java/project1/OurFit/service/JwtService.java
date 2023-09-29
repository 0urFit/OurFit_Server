package project1.OurFit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project1.OurFit.entity.Member;
import project1.OurFit.jwt.JwtTokenProvider;
import project1.OurFit.repository.MemberRepository;
import project1.OurFit.response.PostLoginDto;
import project1.constant.exception.BaseException;
import project1.constant.response.JsonResponseStatus;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public PostLoginDto createToken(Member member) {
        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());
        saveRefreshToken(member, refreshToken);
        return new PostLoginDto(accessToken, refreshToken);
    }

    private void saveRefreshToken(Member member, String refreshToken) {
        member.setRefreshToken(refreshToken);
        memberRepository.save(member);
    }
}
