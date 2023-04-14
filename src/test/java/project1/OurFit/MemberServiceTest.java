package project1.OurFit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.domain.Member;
import project1.OurFit.repository.MemberRepository;
import project1.OurFit.service.MemberService;

import java.util.Optional;
import java.util.OptionalInt;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void 회원가입() {
        //given
        Member member = new Member();
        member.setEmail("aossuper99@naver.com");
        member.setPassword("aossuper7");
        member.setNickname("aossuper99");
        member.setGender(false);
        member.setHeight(130.4);
        member.setWeight(56.0);
        member.setSquat(85.0);

        //when
        Long memberId = memberService.join(member).orElseThrow().getId();

        //then
        Optional<Member> foundMember = memberService.findEmail(member.getEmail());
        Assertions.assertThat(foundMember).isPresent();
        Assertions.assertThat(memberId).isEqualTo(foundMember.get().getId());
    }
}
