package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project1.OurFit.entity.Member;

public interface JwtTokenRepository extends JpaRepository<Member, Long> {
    boolean existsByRefreshToken(String refresh_token);
}
