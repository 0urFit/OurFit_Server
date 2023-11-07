package project1.OurFit.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
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

    /**
     * 로그인 Service
     * @param login
     * @throws LoginException 아이디나 비밀번호가 틀렸을 때 발생
     */
    public Member authenticateMember(LoginDTO login) {
        Member member = findMemberByEmail(login.getEmail());
        validatePassword(login.getPassword(), member.getPassword());
        return member;
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new LoginException(JsonResponseStatus.LOGIN_FAIL));
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword))
            throw new LoginException(JsonResponseStatus.LOGIN_FAIL);
    }

    /**
     * 이메일 중복확인 여부 검사 Service
     * @param email
     * @throws DuplicateException 이메일이 이미 사용 중일 때 발생
     */
    public void validateEmail(final String email){
        if (memberRepository.existsByEmail(email))
            throw new DuplicateException(JsonResponseStatus.EMAIL_CONFLICT);
    }

    /**
     * 닉네임 중복확인 여부 검사 Service
     * @param nickname
     * @throws DuplicateException 닉네임이 이미 사용 중일 때 발생
     */
    public void validateNickname(final String nickname) {
        if (memberRepository.existsByNickname(nickname))
            throw new DuplicateException(JsonResponseStatus.NICKNAME_CONFLICT);
    }

    /**
     * 회원가입 Service
     * @param memberDTO
     * @throws DuplicateException 이메일&닉네임 중복 되었을 때 발생
     */
    public void join(MemberDTO memberDTO) {
        checkEmailAndNicknameExistance(memberDTO);

        if (memberDTO.getPassword() == null) // 카카오로 회원가입한 사람은 임시 비밀번호 생성, 자체 로그인 방지
            assignTemporaryPassword(memberDTO);

        saveMember(memberDTO);
    }

    private void checkEmailAndNicknameExistance(MemberDTO memberDTO) {
        Boolean emailExists = memberRepository.existsByEmail(memberDTO.getEmail());
        Boolean nicknameExists = memberRepository.existsByNickname(memberDTO.getNickname());

        if (emailExists && nicknameExists)
            throw new DuplicateException(JsonResponseStatus.ALL_CONFLICT);
        if (emailExists)
            throw new DuplicateException(JsonResponseStatus.EMAIL_CONFLICT);
        if (nicknameExists)
            throw new DuplicateException(JsonResponseStatus.NICKNAME_CONFLICT);
    }

    private void assignTemporaryPassword(MemberDTO memberDTO) {
        memberDTO.setPassword(UUID.randomUUID().toString());
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

    @CacheEvict(value = "token", key = "#signUpDto.email") // 기존에 있던 캐시 삭제
    public Member findKakaoId(SignUpDto signUpDto) {
        return memberRepository.findByEmail(signUpDto.getEmail())
                .orElseThrow(() -> new UnregisteredUserException(JsonResponseStatus.NOT_FOUND_MEMBER, signUpDto));
    }
}
