package project1.OurFit.service;

import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.Entity.Member;
import project1.OurFit.Request.MemberDTO;
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

    public Optional<Member> findNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    public Optional<Member> join(MemberDTO memberDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Member member = modelMapper.map(memberDTO, Member.class);
        return memberRepository.save(member);
    }
}
