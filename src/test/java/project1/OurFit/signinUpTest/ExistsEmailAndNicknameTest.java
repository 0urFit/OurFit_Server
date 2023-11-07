package project1.OurFit.signinUpTest;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import project1.RestDocsConfiguration;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;


@SpringBootTest
@Transactional
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfiguration.class)
public class ExistsEmailAndNicknameTest {
    @Autowired private RestDocumentationResultHandler restDocs;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(
            final WebApplicationContext context,
            final RestDocumentationContextProvider provider
    ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .defaultRequest(get("/").header(HttpHeaders.HOST, "54.180.88.182").secure(true))
                .alwaysDo(restDocs)
                .build();
    }

    @Test
    void existEmail() throws Exception {
        //When & Then
        mockMvc.perform(get("/checkemail")
                    .queryParam("email", "goodproject@naver.com"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("email").description("이메일")
                        )
                ));
    }

    @Test
    void existNickname() throws Exception {
        //When & Then
        mockMvc.perform(get("/checknick")
                    .queryParam("nick", "goodproject"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("nick").description("닉네임")
                        )
                ));
    }
}
