package project1.OurFit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project1.OurFit.entity.Member;
import project1.OurFit.jwt.JwtTokenProvider;
import project1.OurFit.repository.MemberRepository;
import project1.OurFit.response.JwtTokenDto;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public JwtTokenDto createToken(Member member) {
        String accessToken = generateAccessToken(member);
        String refreshToken = generateRefreshToken(member);
        saveRefreshToken(member, refreshToken);
        return new JwtTokenDto(accessToken, refreshToken);
    }

    private String generateAccessToken(Member member) {
        return jwtTokenProvider.createAccessToken(member.getEmail());
    }

    private String generateRefreshToken(Member member) {
        return jwtTokenProvider.createRefreshToken(member.getEmail());
    }

    private void saveRefreshToken(Member member, String refreshToken) {
        member.setRefreshToken(refreshToken);
        memberRepository.save(member);
    }
}
