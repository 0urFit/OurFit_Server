//package project1.OurFit;
//
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//import project1.OurFit.entity.Member;
//import project1.OurFit.request.MemberDTO;
//import project1.OurFit.repository.MemberRepository;
//import project1.OurFit.service.MemberService;
//import project1.OurFit.vo.DuplicateCheckResult;
//
//import java.util.Optional;
//
//
//@SpringBootTest
//@Transactional
//@AutoConfigureMockMvc
//public class MemberServiceTest {
//
//    @Autowired MemberService memberService;
//    @Autowired MemberRepository memberRepository;
//
//    @Test
//    void 정상_회원가입() {
//        MemberDTO member = new MemberDTO();
//        member.setEmail("test@test.com");
//        member.setPassword("test");
//        member.setNickname("test");
//        member.setGender("male");
//
//        DuplicateCheckResult checkResult = memberService.join(member);
//
//        Assertions.assertThat(checkResult.isDuplicate()).isFalse();
//    }
//
//    @Test
//    void 이메일_닉네임_중복_회원가입() {
//        MemberDTO member = new MemberDTO();
//        member.setEmail("aossuper7@naver.com");
//        member.setPassword("test");
//        member.setNickname("aossuper7");
//        member.setGender("male");
//
//        DuplicateCheckResult checkResult = memberService.join(member);
//
//        Assertions.assertThat(checkResult.getField()).isEqualTo("모두 ");
//    }
//
//    @Test
//    void 이메일_중복_회원가입() {
//        MemberDTO member = new MemberDTO();
//        member.setEmail("aossuper7@naver.com");
//        member.setPassword("test");
//        member.setNickname("aossuper77");
//        member.setGender("male");
//
//        DuplicateCheckResult checkResult = memberService.join(member);
//
//        Assertions.assertThat(checkResult.getField()).isEqualTo("이메일 ");
//    }
//
//    @Test
//    void 닉네임_중복_회원가입() {
//        MemberDTO member = new MemberDTO();
//        member.setEmail("aossuper77@naver.com");
//        member.setPassword("test");
//        member.setNickname("aossuper7");
//        member.setGender("male");
//
//        DuplicateCheckResult checkResult = memberService.join(member);
//
//        Assertions.assertThat(checkResult.getField()).isEqualTo("닉네임 ");
//    }
//
//    @Test
//    void 로그인() {
//        String email = "test@test.com";
//        String password = "testpassword";
//
//        Optional<Member> login = memberService.findEmailAndPassword(email, password);
//
//        Assertions.assertThat(login.isPresent()).isTrue();
//    }
//
//    @Test
//    void 로그인_실패() {
//        String email = "testtest@testtest.com";
//        String password = "testpassword";
//
//        Optional<Member> login = memberService.findEmailAndPassword(email, password);
//
//        Assertions.assertThat(login.isPresent()).isFalse();
//    }
//
//    @Test
//    void 이메일_확인() {
//        String email = "test@test.com";
//
//        Optional<Member> findEmail = memberService.findEmail(email);
//
//        Assertions.assertThat(findEmail.isPresent()).isTrue();
//    }
//
//    @Test
//    void 없는_이메일_확인() {
//        String email = "testtest@testtest.com";
//
//        Optional<Member> findEmail = memberService.findEmail(email);
//
//        Assertions.assertThat(findEmail.isPresent()).isFalse();
//    }
//
//    @Test
//    void 닉네임_확인() {
//        String nickname = "testnickname";
//
//        Optional<Member> findNickname = memberService.findNickname(nickname);
//
//        Assertions.assertThat(findNickname.isPresent()).isTrue();
//    }
//
//    @Test
//    void 없는_닉네임_확인() {
//        String nickname = "testtestnickname";
//
//        Optional<Member> findNickname = memberService.findNickname(nickname);
//
//        Assertions.assertThat(findNickname.isPresent()).isFalse();
//    }
//}
