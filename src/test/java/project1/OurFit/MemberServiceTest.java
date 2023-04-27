package project1.OurFit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.Entity.Member;
import project1.OurFit.Request.MemberDTO;
import project1.OurFit.repository.MemberRepository;
import project1.OurFit.service.MemberService;

import java.util.Optional;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void 회원가입() {
        MemberDTO member = new MemberDTO();
        member.setEmail("test@test.com");
        member.setPassword("test");
        member.setNickname("test");
        member.setGender("male");
        member.setHeight(170.0);
        member.setWeight(79.0);

        Optional<Member> savedMember = memberService.join(member);

        Assertions.assertThat(savedMember).isNotNull();
    }

    @Test
    void 로그인() {
        String email = "test@test.com";
        String password = "testpassword";

        Optional<Member> login = memberService.findEmailAndPassword(email, password);

        Assertions.assertThat(login.isPresent()).isTrue();
    }

    @Test
    void 로그인_실패() {
        String email = "testtest@testtest.com";
        String password = "testpassword";

        Optional<Member> login = memberService.findEmailAndPassword(email, password);

        Assertions.assertThat(login.isPresent()).isFalse();
    }

    @Test
    void 이메일_확인() {
        String email = "test@test.com";

        Optional<Member> findEmail = memberService.findEmail(email);

        Assertions.assertThat(findEmail.isPresent()).isTrue();
    }

    @Test
    void 없는_이메일_확인() {
        String email = "testtest@testtest.com";

        Optional<Member> findEmail = memberService.findEmail(email);

        Assertions.assertThat(findEmail.isPresent()).isFalse();
    }

    @Test
    void 닉네임_확인() {
        String nickname = "testnickname";

        Optional<Member> findNickname = memberService.findNickname(nickname);

        Assertions.assertThat(findNickname.isPresent()).isTrue();
    }

    @Test
    void 없는_닉네임_확인() {
        String nickname = "testtestnickname";

        Optional<Member> findNickname = memberService.findNickname(nickname);

        Assertions.assertThat(findNickname.isPresent()).isFalse();
    }
}
