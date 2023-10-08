package project1.OurFit.mypage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import project1.OurFit.jwtTest.JwtTokenProvider;
import project1.RestDocsConfiguration;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfiguration.class)
public class MypageRoutineDetailTest {
    @Autowired
    private RestDocumentationResultHandler restDocs;
    private MockMvc mockMvc;
    @Autowired private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp(
            final WebApplicationContext context,
            final RestDocumentationContextProvider provider
    ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .defaultRequest(get("/").header(HttpHeaders.HOST, "54.180.88.182"))
                .alwaysDo(restDocs)
                .build();
    }

    @Test
    void getRoutineDetailTest() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createAccessToken("aossuper7@naver.com");

        //When & Then
        mockMvc.perform(get("/mypage/exercise/{routineId}/{week}", "1", "1")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        pathParameters(
                                parameterWithName("routineId").description("루틴 번호"),
                                parameterWithName("week").description("해당 주차")
                        ),
                        responseFields(
                                fieldWithPath("code").description("Http 상태코드"),
                                fieldWithPath("message").description("상태 메시지"),
                                fieldWithPath("success").description("성공 여부")
                        ).andWithPrefix("result.",
                                fieldWithPath("routineName").description("루틴 이름"),
                                fieldWithPath("level").description("운동 난이도 (1~10)"),
                                fieldWithPath("weeks").description("일주일에 몇번 운동 하는지"),
                                fieldWithPath("period").description("운동 기간 (주 단위)"),
                                fieldWithPath("isliked").description("사용자가 좋아요 눌렀는지 여부")
                        ).andWithPrefix("result.days[].",
                                fieldWithPath("day").description("요일"),
                                fieldWithPath("issuccess").description("요일 성공 여부")
                        ).andWithPrefix("result.days[].exercises[].",
                                fieldWithPath("name").description("운동 이름")
                        ).andWithPrefix("result.days[].exercises[].sets[].",
                                fieldWithPath("sequence").description("운동 순서"),
                                fieldWithPath("weight").description("사용자 4대 운동요소에 맞춰진 무게"),
                                fieldWithPath("reps").description("반복 횟수")
                        )
                ));
    }
}
