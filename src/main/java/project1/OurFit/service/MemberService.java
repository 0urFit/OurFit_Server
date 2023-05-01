package project1.OurFit.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.entity.Member;
import project1.OurFit.request.MemberDTO;
import project1.OurFit.repository.MemberRepository;
import project1.OurFit.vo.DuplicateCheckResult;

import java.util.Optional;

@Transactional
public class MemberService {

    private final String ALL = "모두 ";
    private final String EMAIL = "이메일 ";
    private final String NICKNAME = "닉네임 ";

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Optional<Member> findEmailAndPassword(String email, String password) {
        Optional<Member> member = findEmail(email);
        if (member.isPresent())
            if (passwordEncoder.matches(password, member.get().getPassword()))
                return member;
        return Optional.empty();
    }

    public Optional<Member> findEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Optional<Member> findNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    public DuplicateCheckResult join(MemberDTO memberDTO) {
        Optional<Member> checkEmail = findEmail(memberDTO.getEmail());
        Optional<Member> checkNickname = findNickname(memberDTO.getNickname());

        if (checkEmail.isPresent() && checkNickname.isPresent())
            return new DuplicateCheckResult(true, ALL);
        else if (checkEmail.isPresent())
            return new DuplicateCheckResult(true, EMAIL);
        else if (checkNickname.isPresent())
            return new DuplicateCheckResult(true, NICKNAME);
        else {
            boolean isSuccess = saveMember(memberDTO);
            return new DuplicateCheckResult(!isSuccess, null);
        }
    }

    private boolean saveMember(MemberDTO memberDTO) {
        ModelMapper modelMapper = new ModelMapper();
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        Member member = modelMapper.map(memberDTO, Member.class);
        Optional<Member> savedMember = memberRepository.save(member);
        return savedMember.isPresent();
    }
}
