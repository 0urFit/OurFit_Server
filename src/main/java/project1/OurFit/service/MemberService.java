package project1.OurFit.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.Entity.Member;
import project1.OurFit.Request.MemberDTO;
import project1.OurFit.repository.MemberRepository;

import java.util.Optional;

@Transactional
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
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
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        Member member = modelMapper.map(memberDTO, Member.class);
        return memberRepository.save(member);
    }
}
