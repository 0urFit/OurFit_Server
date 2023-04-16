package project1.OurFit.service;

import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.domain.KakaoProfile;
import project1.OurFit.domain.OAuthToken;
import project1.OurFit.domain.Member;
import project1.OurFit.repository.KakaoRepository;
import project1.OurFit.repository.MemberRepository;

import java.util.Optional;

@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final KakaoRepository kakaoRepository;

    public MemberService(MemberRepository memberRepository, KakaoRepository kakaoRepository) {
        this.memberRepository = memberRepository;
        this.kakaoRepository = kakaoRepository;
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

    public Optional<Member> join(Member member) {
        return memberRepository.save(member);
    }

    public KakaoProfile loginKakao(String code) {
        OAuthToken OAuthToken = kakaoRepository.getToken(code);
        return kakaoRepository.getUserInfo(OAuthToken);
    }
}
