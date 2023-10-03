package project1.OurFit.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project1.OurFit.entity.Member;
import project1.OurFit.entity.RefreshToken;
import project1.OurFit.jwt.JwtTokenProvider;
import project1.OurFit.repository.MemberRepository;
import project1.OurFit.repository.RefreshTokenRedisRepository;
import project1.OurFit.response.JwtTokenDto;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    /**
     * AccessToken, refreshToken 생성하고
     * redis에 refreshToken 저장하는 service
     * @param member
     * @return
     */
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
        RefreshToken refreshToken1 = new RefreshToken(member.getEmail(), refreshToken);
        refreshTokenRedisRepository.save(refreshToken1);
    }


    public String extractEmailFromRefreshToken(String refreshToken) {
        Claims claims = jwtTokenProvider.parseToken(refreshToken);
        return claims.getSubject();
    }
}
