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
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;

    public PostLoginDto authorize(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.createAccessToken(authentication.getName());
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication.getName());
        saveRefreshToken(email, refreshToken);
        return new PostLoginDto(token, refreshToken, null, null);
    }

    public PostLoginDto authorize(String email) {
        return authorize(email, "");
    }

    private void saveRefreshToken(String email, String refreshToken) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(JsonResponseStatus.UNAUTHORIZED));
        member.setRefreshToken(refreshToken);
        memberRepository.save(member);
    }
}
