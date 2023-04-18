package project1.OurFit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import constant.Oauth;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import project1.OurFit.domain.KakaoProfile;
import project1.OurFit.domain.OAuthToken;

import java.util.Optional;

@Transactional
public class KakaoService {

    private final RestTemplate rt;
    private final ObjectMapper objectMapper;
    private final OAuthToken oAuthToken;

    public KakaoService() {
        this.rt = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.oAuthToken = new OAuthToken();
    }

    public OAuthToken getToken(String code) {
        if (oAuthToken.getAccess_token() != null) {
            return oAuthToken;
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(Oauth.HEADER_CONTENT_NAME.getValue(), Oauth.HEADER_CONTENT_VALUE.getValue());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(Oauth.GRANT_NAME.getValue(), Oauth.GRANT_VALUE.getValue());
        params.add(Oauth.CLIENT_NAME.getValue(), Oauth.CLIENT_VALUE.getValue());
        params.add(Oauth.REDIRECT_NAME.getValue(), Oauth.REDIRECT_VALUE.getValue());
        params.add(Oauth.CODE.getValue(), code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, httpHeaders);
        String token = requestHttp(Oauth.TOKEN_URL.getValue(), rt, kakaoTokenRequest);
        return convertJson(token, OAuthToken.class, oAuthToken);
    }

    private String requestHttp(String url , RestTemplate rt, HttpEntity<MultiValueMap<String, String>> token) {
        ResponseEntity<String> response = rt.exchange(url, HttpMethod.POST, token, String.class);
        return response.getBody();
    }

    private <T> T convertJson(String token, Class<T> type, Object dto) {
        try {
            dto = objectMapper.readValue(token, type);
            return (T) dto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public KakaoProfile getUserInfo(OAuthToken kakao) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(Oauth.HEADER_REQUEST_NAME.getValue(),
                Oauth.HEADER_REQUEST_VALUE.getValue() + kakao.getAccess_token());
        httpHeaders.add(Oauth.HEADER_CONTENT_NAME.getValue(), Oauth.HEADER_CONTENT_VALUE.getValue());

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(httpHeaders);
        String token = requestHttp(Oauth.TOKEN_PROFILE.getValue(), rt, kakaoProfileRequest);
        return convertJson(token, KakaoProfile.class, new KakaoProfile());
    }
}
