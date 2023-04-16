package project1.OurFit.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import constant.Oauth;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import project1.OurFit.domain.KakaoProfile;
import project1.OurFit.domain.OAuthToken;

public class KakaoRepository {

    public OAuthToken getToken(String code) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(Oauth.HEADER_CONTENT_NAME.getValue(), Oauth.HEADER_CONTENT_VALUE.getValue());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(Oauth.GRANT_NAME.getValue(), Oauth.GRANT_VALUE.getValue());
        params.add(Oauth.CLIENT_NAME.getValue(), Oauth.CLIENT_VALUE.getValue());
        params.add(Oauth.REDIRECT_NAME.getValue(), Oauth.REDIRECT_VALUE.getValue());
        params.add(Oauth.CODE.getValue(), code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, httpHeaders);
        String token = requestHttp(Oauth.TOKEN_URL.getValue(), rt, kakaoTokenRequest);
        return convertOAuthtokenToJson(token);
    }

    private String requestHttp(String url, RestTemplate rt, HttpEntity<MultiValueMap<String, String>> token) {
        ResponseEntity<String> response = rt.exchange(url, HttpMethod.POST, token, String.class);
        return response.getBody();
    }

    private OAuthToken convertOAuthtokenToJson(String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(token, OAuthToken.class);
        }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return oauthToken;
    }

    private KakaoProfile convertKakaoprofileToJson(String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(token, KakaoProfile.class);
        }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return kakaoProfile;
    }

    public KakaoProfile getUserInfo(OAuthToken kakao) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(Oauth.HEADER_REQUEST_NAME.getValue(),
                Oauth.HEADER_REQUEST_VALUE.getValue() + kakao.getAccess_token());
        httpHeaders.add(Oauth.HEADER_CONTENT_NAME.getValue(), Oauth.HEADER_CONTENT_VALUE.getValue());

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(httpHeaders);
        String token = requestHttp(Oauth.TOKEN_PROFILE.getValue(), rt, kakaoProfileRequest);
        return convertKakaoprofileToJson(token);
    }

}
