package project1.OurFit.signinUpTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import project1.OurFit.entity.Member;
import project1.OurFit.response.JwtTokenDto;
import project1.OurFit.response.SignUpDto;
import project1.OurFit.service.JwtService;
import project1.OurFit.service.KakaoAccessTokenProviderService;
import project1.OurFit.service.KakaoUserInfoProviderService;
import project1.OurFit.service.MemberService;
import project1.RestDocsConfiguration;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfiguration.class)
public class KakaoLoginTest {
    @Autowired private RestDocumentationResultHandler restDocs;
    private MockMvc mockMvc;
    @MockBean private KakaoAccessTokenProviderService kakaoAccessTokenProviderService;
    @MockBean private KakaoUserInfoProviderService kakaoUserInfoProviderService;
    @MockBean private MemberService memberService;
    @MockBean private JwtService jwtService;

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
    void loginKakaoSuccessTest() throws Exception {
        //Given
        String kakaoCode = "EdSngKE6VUVrYZkkzdHgu1BNXC7D5B_HzOa5TqD2aPG_qZ7PpGMvoYKIVmaH1NnLFr7tVgo9dZoAAAGK6rhCZA";
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setEmail("aossuper7@naver.com");
        signUpDto.setGender("male");
        JwtTokenDto jwtTokenDto = new JwtTokenDto(
                "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhb3NzdXBlcjdAbmF2ZXIuY29tIiwiZXhwIjoxNjkwMTI3NDk5fQ.R2j5PVz57YpOzTthYzNcrbRlYgy4ddSFv5hxm14o1PzxRhYOX3AVH2wNkDZrLJzdoTgPoZ67CYgNTlOyjdXcOw",
                "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhb3NzdXBlcjdAbmF2ZXIuY29tIiwiZXhwIjoxNjkxMzM1Mjk5fQ.c-NdhuORzcMBWAr2NscETTeI8uujEuMVmg8NFA0HjA9oS6kO8nECnlO31hOPm8sw0LJVcIMK1yPAlCtJTh29Yw");

        //When
        Mockito.when(kakaoAccessTokenProviderService.getAccessToken(any())).thenReturn("test_access_token");
        Mockito.when(kakaoUserInfoProviderService.getUserInfo(any())).thenReturn(signUpDto);
        Mockito.when(memberService.findKakaoId(any())).thenReturn(new Member());
        Mockito.when(jwtService.createToken(any())).thenReturn(jwtTokenDto);

        //Then
        mockMvc.perform(get("/kakao")
                    .queryParam("authorizationCode", "code"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("authorizationCode").description("카카오 인가코드")
                        ),
                        responseFields(
                                fieldWithPath("code").description("Http 상태코드"),
                                fieldWithPath("message").description("상태 메시지"),
                                fieldWithPath("success").description("성공 여부")
                        ).andWithPrefix("result.",
                                fieldWithPath("accessToken").description("회원일 경우에만 제공"),
                                fieldWithPath("refreshToken").description("회원일 경우에만 제공"),
                                fieldWithPath("email").description("회원이 없을 경우에만 제공")
                                        .type(JsonFieldType.STRING).optional(),
                                fieldWithPath("gender").description("회원이 없을 경우에만 제공")
                                        .type(JsonFieldType.STRING).optional())
                ));
    }
}
