package project1.OurFit.jwtTest;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import project1.constant.exception.ExpiredJwtTokenException;
import project1.constant.exception.InvalidJwtException;
import project1.constant.exception.RefreshTokenException;
import project1.constant.response.JsonResponseStatus;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtTokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final String accessSecret;
    private final String refreshSecret;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;
    private Key accessSecretKey;
    private Key refreshSecretKey;

    public JwtTokenProvider(
            @Value("${jwt.access-secret}") String accessSecret,
            @Value("${jwt.refresh-secret}") String refreshSecret,
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds) {
        this.accessSecret = accessSecret;
        this.refreshSecret = refreshSecret;
        this.accessTokenValidity = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidity = refreshTokenValidityInSeconds * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(accessSecret);
        this.accessSecretKey = Keys.hmacShaKeyFor(keyBytes);
        keyBytes = Decoders.BASE64.decode(refreshSecret);
        this.refreshSecretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String username) {
        return buildJwtToken(username, accessSecretKey, accessTokenValidity);
    }

    public String createRefreshToken(String username) {
        return buildJwtToken(username, refreshSecretKey, refreshTokenValidity);
    }

    private String buildJwtToken(String username, Key secretKey, long validityPeriod) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + validityPeriod);

        return Jwts.builder()
                .setSubject(username)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(accessSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        User principal = new User(claims.getSubject(), "N/A", new ArrayList<>());

        return new UsernamePasswordAuthenticationToken(principal, token, new ArrayList<>());
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(accessSecretKey).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT token.");
            throw new ExpiredJwtTokenException(JsonResponseStatus.ACCESS_TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtException();
        }
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(refreshSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtTokenException(JsonResponseStatus.REFRESH_TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new RefreshTokenException(JsonResponseStatus.INVALID_JWT);
        }
    }
}
