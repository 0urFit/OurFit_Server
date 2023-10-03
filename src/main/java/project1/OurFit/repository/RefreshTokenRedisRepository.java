package project1.OurFit.repository;

import org.springframework.data.repository.CrudRepository;
import project1.OurFit.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByEmail(String email);
}
