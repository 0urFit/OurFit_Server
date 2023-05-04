package project1.OurFit.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.entity.Member;
import project1.OurFit.request.MemberDTO;
import project1.OurFit.repository.MemberRepository;
import project1.OurFit.response.PostLoginDto;
import project1.constant.exception.BaseException;
import project1.constant.exception.DuplicateException;
import project1.constant.response.JsonResponseStatus;

import java.util.function.Function;

@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public PostLoginDto findEmailAndPassword(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(JsonResponseStatus.UNAUTHORIZED));
        if (passwordEncoder.matches(password, member.getPassword()))
            return jwtService.authorize(email, password);
        throw new BaseException(JsonResponseStatus.UNAUTHORIZED);
    }

    public Boolean findEmail(String email){
        return checkExistence(email, memberRepository::existsByEmail, JsonResponseStatus.EMAIL_CONFLICT);
    }

    public Boolean findNickname(String nickname) {
        return checkExistence(nickname, memberRepository::existsByNickname, JsonResponseStatus.NICKNAME_CONFLICT);
    }

    private Boolean checkExistence(String value, Function<String, Boolean> existsFunction, JsonResponseStatus status) {
        Boolean isExist = existsFunction.apply(value);
        if (isExist)
            throw new DuplicateException(status);
        return true;
    }

    public Boolean checkMember(String email){
        return memberRepository.existsByEmail(email);
    }

    public Boolean join(MemberDTO memberDTO) {
        Boolean checkEmail = memberRepository.existsByEmail(memberDTO.getEmail());
        Boolean checkNickname = memberRepository.existsByNickname(memberDTO.getNickname());

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
}
