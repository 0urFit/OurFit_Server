package project1.OurFit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import project1.constant.Oauth;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import project1.OurFit.response.PostKakaoProfile;
import project1.OurFit.request.OAuthTokenDTO;

@Transactional
@RequiredArgsConstructor
public class KakaoService {

    private final RestTemplate rt;
    private final ObjectMapper objectMapper;

    public OAuthTokenDTO getToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(Oauth.HEADER_CONTENT_NAME.getValue(), Oauth.HEADER_CONTENT_VALUE.getValue());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(Oauth.GRANT_NAME.getValue(), Oauth.GRANT_VALUE.getValue());
        params.add(Oauth.CLIENT_NAME.getValue(), Oauth.CLIENT_VALUE.getValue());
        params.add(Oauth.REDIRECT_NAME.getValue(), Oauth.REDIRECT_VALUE.getValue());
        params.add(Oauth.CODE.getValue(), code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
        String token = requestHttp(Oauth.TOKEN_URL.getValue(), kakaoTokenRequest);
        return convertJson(token, OAuthTokenDTO.class);
    }

    private String requestHttp(String url, HttpEntity<MultiValueMap<String, String>> token) {
        ResponseEntity<String> response = rt.exchange(url, HttpMethod.POST, token, String.class);
        return response.getBody();
    }

    private <T> T convertJson(String token, Class<T> type) {
        try {
            return objectMapper.readValue(token, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public PostKakaoProfile getUserInfo(OAuthTokenDTO kakao) {
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = createHttpEntity(null, kakao.getAccess_token());
        String token = requestHttp(Oauth.TOKEN_PROFILE.getValue(), kakaoProfileRequest);
        return convertJson(token, PostKakaoProfile.class);
    }

    private HttpHeaders createHeaders(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(Oauth.HEADER_CONTENT_NAME.getValue(), Oauth.HEADER_CONTENT_VALUE.getValue());
        if (token != null)
            httpHeaders.add(Oauth.HEADER_REQUEST_NAME.getValue(), Oauth.HEADER_REQUEST_VALUE.getValue() + token);
        return httpHeaders;
    }

    private HttpEntity<MultiValueMap<String, String>> createHttpEntity(MultiValueMap<String, String> params, String token) {
        HttpHeaders httpHeaders = createHeaders(token);
        return new HttpEntity<>(params, httpHeaders);
    }
}
