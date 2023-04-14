package project1.OurFit.service;

import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.domain.Member;
import project1.OurFit.repository.MemberRepository;

import java.util.Optional;

@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Optional<Member> findEmailAndPassword(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password);
    }

    public Optional<Member> findEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
