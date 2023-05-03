package project1.OurFit.service;

import project1.constant.exception.BaseException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.entity.Member;
import project1.OurFit.request.MemberDTO;
import project1.OurFit.repository.MemberRepository;
import project1.OurFit.response.TestDto;
import project1.OurFit.vo.DuplicateCheckResult;

import java.util.Optional;

import static project1.constant.response.JsonResponseStatus.NOTFOUND;
import static project1.constant.response.JsonResponseStatus.USERS_EMPTY_EMAIL;

@Transactional
@Service
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

    //로그인 메서드 -> 이메일 존재하면 패스워드 꺼내기
    public String findEmailAndPassword(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(NOTFOUND));
        if (passwordEncoder.matches(password, member.getPassword()))
            return "로그인에 성공하였습니다.";
        return "로그인에 실패하였습니다.";
    }

    public TestDto findEmail(String email){
        System.out.println("@Service");
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(USERS_EMPTY_EMAIL));
        return new TestDto(member);
    }

    public Optional<Member> findNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }
    public Boolean checkMember(String email){
        return memberRepository.existsByEmail(email);
    }

    public DuplicateCheckResult join(MemberDTO memberDTO) {
        Boolean checkEmail = memberRepository.existsByEmail(memberDTO.getEmail());
        Boolean checkNickname = memberRepository.existsByNickname(memberDTO.getNickname());
//        Optional<Member> checkEmail = findEmail(memberDTO.getEmail()); -> Repository 에 check하는 코드 만들기
//        Optional<Member> checkNickname = findNickname(memberDTO.getNickname());

        if (checkEmail && checkNickname)
            return new DuplicateCheckResult(true, ALL);
        else if (checkEmail)
            return new DuplicateCheckResult(true, EMAIL);
        else if (checkNickname)
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
