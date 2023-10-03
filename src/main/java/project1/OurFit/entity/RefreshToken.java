package project1.OurFit.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 1209600) // 2주
public class RefreshToken {

    @Id @Indexed
    private String email;
    private String refreshToken;
    private LocalDateTime createdAt;

    public RefreshToken(String email, String refreshToken) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.createdAt = LocalDateTime.now();
    }
}
