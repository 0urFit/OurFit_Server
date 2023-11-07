package project1.OurFit.RoutineTest;

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
public class RoutineByCategoryTest {
    @Autowired private RestDocumentationResultHandler restDocs;
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
                .defaultRequest(get("/").header(HttpHeaders.HOST, "54.180.88.182").secure(true))
                .alwaysDo(restDocs)
                .build();
    }

    @Test
    void getRoutineByCategory() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createAccessToken("aossuper7@naver.com");

        //When & Then
        mockMvc.perform(get("/exercise/{category}", "all")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer "+ accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        pathParameters(
                                parameterWithName("category").description("""
                                        모두 : all +
                                        바디빌딩 : bodybuilding +
                                        스트렝스 : strength""")
                        ),
                        responseFields(
                                fieldWithPath("code").description("Http 상태코드"),
                                fieldWithPath("message").description("상태 메시지"),
                                fieldWithPath("success").description("성공 여부")
                        ).andWithPrefix("result[].",
                                fieldWithPath("id").description("운동 루틴 번호"),
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("imgpath").description("이미지 경로"),
                                fieldWithPath("fewTime").description("일주일 운동 횟수"),
                                fieldWithPath("level").description("운동 강도 (1~10)"),
                                fieldWithPath("period").description("운동 기간 (주 단위)"),
                                fieldWithPath("routineName").description("운동 루틴 이름"),
                                fieldWithPath("liked").description("사용자 좋아요 클릭 여부"),
                                fieldWithPath("enrolled").description("사용자 루틴 저장 여부")
                        )
                ));
    }
}
