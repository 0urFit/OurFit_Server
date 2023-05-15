package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project1.OurFit.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findById(Long id);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
