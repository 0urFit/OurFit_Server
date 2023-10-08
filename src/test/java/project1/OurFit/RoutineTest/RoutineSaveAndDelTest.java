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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfiguration.class)
public class RoutineSaveAndDelTest {
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
                .defaultRequest(post("/").header(HttpHeaders.HOST, "54.180.88.182"))
                .defaultRequest(delete("/").header(HttpHeaders.HOST, "54.180.88.182"))
                .alwaysDo(restDocs)
                .build();
    }

    @Test
    void saveMyRoutine() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createAccessToken("aossuper7@naver.com");

        //When & Then
        mockMvc.perform(post("/exercise/{routineId}", 4)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        pathParameters(
                                parameterWithName("routineId").description("루틴 번호")
                        )
                ));
    }

    @Test
    void deleteMyRoutine() throws Exception {
        //Given
        String accessToken = jwtTokenProvider.createAccessToken("aossuper7@naver.com");

        //When & Then
        mockMvc.perform(delete("/exercise/{routineId}", 4)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {AccessToken}")
                        ),
                        pathParameters(
                                parameterWithName("routineId").description("루틴 번호")
                        )
                ));
    }
}
