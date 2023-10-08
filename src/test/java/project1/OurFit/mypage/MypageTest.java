package project1.OurFit.mypage;

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
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import project1.OurFit.jwtTest.JwtTokenProvider;
import project1.OurFit.response.MemberDto;
import project1.RestDocsConfiguration;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;

@SpringBootTest
@Transactional
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfiguration.class)
public class MypageTest {
    @Autowired
    private RestDocumentationResultHandler restDocs;
    private MockMvc mockMvc;
    @Autowired private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(
            final WebApplicationContext context,
            final RestDocumentationContextProvider provider
    ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .defaultRequest(get("/").header(HttpHeaders.HOST, "54.180.88.182"))
                .defaultRequest(patch("/").header(HttpHeaders.HOST, "54.180.88.182"))
                .alwaysDo(restDocs)
                .build();
    }

    @Test
    void getMyInfo() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createAccessToken("aossuper7@naver.com");

        //When & Then
        mockMvc.perform(get("/mypage")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        responseFields(
                                fieldWithPath("code").description("http 상태코드"),
                                fieldWithPath("message").description("상태 메시지"),
                                fieldWithPath("success").description("성공 여부")
                        ).andWithPrefix("result.",
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("height").description("키"),
                                fieldWithPath("weight").description("몸무게"),
                                fieldWithPath("squat").description("스쿼트"),
                                fieldWithPath("benchpress").description("벤치프레스"),
                                fieldWithPath("deadlift").description("데드리프트"),
                                fieldWithPath("overheadpress").description("오버헤드프레스")
                        )
                ));
    }

    @Test
    void updateMyInfo() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createAccessToken("aossuper7@naver.com");
        MemberDto memberDto = new MemberDto();
        memberDto.setHeight(180.0);
        memberDto.setWeight(72.0);
        memberDto.setSquat(120.0);
        memberDto.setBenchpress(65.0);
        memberDto.setDeadlift(115.0);
        memberDto.setOverheadpress(45.0);


        //When & Then
        mockMvc.perform(patch("/mypage/m")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        requestFields(
                                fieldWithPath("height").description("키"),
                                fieldWithPath("weight").description("몸무게"),
                                fieldWithPath("squat").description("스쿼트"),
                                fieldWithPath("benchpress").description("벤치프레스"),
                                fieldWithPath("deadlift").description("데드리프트"),
                                fieldWithPath("overheadpress").description("오버헤드프레스")
                        )
                ));
    }
}
