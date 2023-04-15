package project1.OurFit;

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
        String Json = "{\"email\": \"aossuper1@naver.com\"," +
                "\"password\": \"aossuper1\"," +
                "\"nickname\": \"aossuper1\"," +
                "\"gender\": false," +
                "\"height\": 170.1," +
                "\"weight\": 80.0}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
