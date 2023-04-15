package project1.OurFit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.domain.Member;
import project1.OurFit.repository.MemberRepository;
import project1.OurFit.service.MemberService;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired MockMvc mockMvc;

    @Test
    void 회원가입() throws Exception {
        Member member = new Member();
        member.setEmail("aossuper11@naver.com");
        member.setPassword("aossuper1");
        member.setNickname("aossuper1");
        member.setGender(false);
        member.setHeight(170.0);
        member.setWeight(78.0);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(member);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 이메일_확인() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/checkemail/aossuper7@naver.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 없는_이메일_확인() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/checkemail/aossup@naver.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 닉네임_확인() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/checknick/aossuper7"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 없는_닉네임_확인() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/checknick/aoss"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
