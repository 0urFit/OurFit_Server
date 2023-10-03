package project1.OurFit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project1.OurFit.entity.RefreshToken;
import project1.OurFit.repository.RefreshTokenRedisRepository;
import project1.constant.exception.NotFoundException;
import project1.constant.exception.RefreshTokenException;
import project1.constant.response.JsonResponseStatus;

@Service
@RequiredArgsConstructor
public class RedisService {
    @Autowired
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    public RefreshToken getRedisByEmail(final String email) {
        return refreshTokenRedisRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(JsonResponseStatus.NOT_FOUND_MEMBER));
    }

    public void validateRefreshToken(final RefreshToken storedRefreshToken, final String refreshToken) {
        if (!storedRefreshToken.getRefreshToken().equals(refreshToken))
            throw new RefreshTokenException(JsonResponseStatus.REFRESH_TOKEN_NOT_FOUND);
    }
}
