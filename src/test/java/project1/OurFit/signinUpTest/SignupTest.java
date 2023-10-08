package project1.OurFit.signinUpTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import project1.OurFit.request.MemberDTO;
import project1.RestDocsConfiguration;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@SpringBootTest
@Transactional
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfiguration.class)
public class SignupTest {
    @Autowired private RestDocumentationResultHandler restDocs;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(
            final WebApplicationContext context,
            final RestDocumentationContextProvider provider
    ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .defaultRequest(post("/").header(HttpHeaders.HOST, "54.180.88.182"))
                .alwaysDo(restDocs)
                .build();
    }

    @Test
    void signUp() throws Exception {
        // Given
        MemberDTO member = new MemberDTO();
        member.setEmail("aossuper8@naver.com");
        member.setPassword("aossuper8");
        member.setNickname("aossuper8");
        member.setGender("male");
        member.setHeight(170.0);
        member.setWeight(70.0);
        member.setSquat(120.0);
        member.setBenchpress(65.0);
        member.setDeadlift(115.0);
        member.setOverheadpress(40.0);

        //When & Then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("패스워드"),
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("gender").description("성별"),
                                fieldWithPath("height").description("키"),
                                fieldWithPath("weight").description("몸무게"),
                                fieldWithPath("squat").description("스쿼트, 필수값 X").optional(),
                                fieldWithPath("benchpress").description("벤치프레스, 필수값 X").optional(),
                                fieldWithPath("deadlift").description("데드리프트, 필수값 X").optional(),
                                fieldWithPath("overheadpress").description("오버헤드프레스, 필수값 X").optional()
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공여부"),
                                fieldWithPath("code").description("HTTP 상태 코드"),
                                fieldWithPath("message").description("메시지")
                        )
                ));
    }

}
