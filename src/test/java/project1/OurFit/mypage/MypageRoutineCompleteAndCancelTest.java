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
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import project1.OurFit.jwtTest.JwtTokenProvider;
import project1.OurFit.request.ExerciseCompleteDto;
import project1.RestDocsConfiguration;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfiguration.class)
public class MypageRoutineCompleteAndCancelTest {
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
                .defaultRequest(post("/").header(HttpHeaders.HOST, "54.180.88.182").secure(true))
                .defaultRequest(delete("/").header(HttpHeaders.HOST, "54.180.88.182").secure(true))
                .alwaysDo(restDocs)
                .build();
    }

    @Test
    void routineComplete() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createAccessToken("aossuper7@naver.com");
        ExerciseCompleteDto dto = new ExerciseCompleteDto();
        dto.setWeek(1);
        dto.setDay("Wed");

        //When & Then
        mockMvc.perform(post("/mypage/exercise/{routineId}/complete", "1")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        pathParameters(
                                parameterWithName("routineId").description("루틴 번호")
                        ),
                        requestFields(
                                fieldWithPath("week").description("해당 주차"),
                                fieldWithPath("day").description("요일")
                        )
                ));
    }

    @Test
    void cancelRoutineComplete() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createAccessToken("aossuper7@naver.com");
        ExerciseCompleteDto dto = new ExerciseCompleteDto();
        dto.setWeek(1);
        dto.setDay("Wed");

        //When & Then
        mockMvc.perform(delete("/mypage/exercise/{routineId}/complete", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        pathParameters(
                                parameterWithName("routineId").description("루틴 번호")
                        ),
                        requestFields(
                                fieldWithPath("week").description("해당 주차"),
                                fieldWithPath("day").description("요일")
                        )
                ));
    }
}
