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

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OAuthTokenDTO requestOAuthToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(Oauth.GRANT_NAME.getValue(), Oauth.GRANT_VALUE.getValue());
        params.add(Oauth.CLIENT_NAME.getValue(), Oauth.CLIENT_VALUE.getValue());
        params.add(Oauth.REDIRECT_NAME.getValue(), Oauth.REDIRECT_VALUE.getValue());
        params.add(Oauth.CODE.getValue(), code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = createHttpEntity(params, null);
        String tokenResponse = performHttpRequest(Oauth.TOKEN_URL.getValue(), kakaoTokenRequest);
        return convertJsonToDto(tokenResponse, OAuthTokenDTO.class);
    }

    private String performHttpRequest(String url, HttpEntity<?> requestEntity) {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return response.getBody();
    }

    private <T> T convertJsonToDto(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to DTO: " + e.getMessage(), e);
        }
    }

    public PostKakaoProfile getUserProfile(String accessToken) {
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = createHttpEntity(null, accessToken);
        String userInfoResponse = performHttpRequest(Oauth.TOKEN_PROFILE.getValue(), kakaoProfileRequest);
        return convertJsonToDto(userInfoResponse, PostKakaoProfile.class);
    }

    private HttpEntity<MultiValueMap<String, String>> createHttpEntity(MultiValueMap<String, String> params, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(Oauth.HEADER_CONTENT_NAME.getValue(), Oauth.HEADER_CONTENT_VALUE.getValue());
        if (accessToken != null) {
            headers.add(Oauth.HEADER_REQUEST_NAME.getValue(), Oauth.HEADER_REQUEST_VALUE.getValue() + accessToken);
        }
        return new HttpEntity<>(params, headers);
    }
}
