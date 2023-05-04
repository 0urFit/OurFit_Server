package project1.OurFit.repository;

import project1.OurFit.entity.Member;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByEmail(String email);

    Optional<Member> save(Member member);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
