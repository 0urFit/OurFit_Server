package project1.OurFit.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.entity.Member;
import project1.OurFit.request.LoginDTO;
import project1.OurFit.request.MemberDTO;
import project1.OurFit.repository.MemberRepository;
import project1.OurFit.response.SignUpDto;
import project1.constant.exception.*;
import project1.constant.response.JsonResponseStatus;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member findEmailAndPassword(LoginDTO login) {
        Member member = memberRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new LoginException(JsonResponseStatus.LOGIN_FAIL));
        if (passwordEncoder.matches(login.getPassword(), member.getPassword()))
            return member;
        throw new LoginException(JsonResponseStatus.LOGIN_FAIL);
    }

    public void findEmail(String email){
        if (checkExistence(email, memberRepository::existsByEmail))
            throw new DuplicateException(JsonResponseStatus.EMAIL_CONFLICT);
    }

    public void findNickname(String nickname) {
        if (checkExistence(nickname, memberRepository::existsByNickname))
            throw new DuplicateException(JsonResponseStatus.NICKNAME_CONFLICT);
    }

    private Boolean checkExistence(String value, Function<String, Boolean> existsFunction) {
        return existsFunction.apply(value);
    }

    public Boolean join(MemberDTO memberDTO) {
        Boolean checkEmail = memberRepository.existsByEmail(memberDTO.getEmail());
        Boolean checkNickname = memberRepository.existsByNickname(memberDTO.getNickname());

        if (memberDTO.getPassword() == null) // 카카오로 회원가입한 사람은 임시 비밀번호 생성, 자체 로그인 방지
            memberDTO.setPassword(UUID.randomUUID().toString());

        if (checkEmail && checkNickname)
            throw new DuplicateException(JsonResponseStatus.ALL_CONFLICT);
        else if (checkEmail)
            throw new DuplicateException(JsonResponseStatus.EMAIL_CONFLICT);
        else if (checkNickname)
            throw new DuplicateException(JsonResponseStatus.NICKNAME_CONFLICT);
        else {
            saveMember(memberDTO);
            return true;
        }
    }

    private void saveMember(MemberDTO memberDTO) {
        ModelMapper modelMapper = new ModelMapper();
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        Member member = modelMapper.map(memberDTO, Member.class);
        memberRepository.save(member);
    }

    /*
     * kakao login service
     */
    public Member findKakaoId(SignUpDto signUpDto) {
        return memberRepository.findByEmail(signUpDto.getEmail())
                .orElseThrow(() -> new UnregisteredUserException(JsonResponseStatus.NOT_FOUND_MEMBER, signUpDto));
    }
}
