package project1.OurFit.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoAccessTokenProviderService {

    @Autowired private RestTemplate restTemplate;
    @Value("${kakao.client.id}")
    private String clidentId;
    @Value("${kakao.redirect.url}")
    private String redirectUrl;

    public String getAccessToken(final String code) {
        JsonNode responseBody = requestAccessToken(code).getBody();
        return responseBody.get("access_token").asText();
    }

    private HttpHeaders createContentTypeHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    private MultiValueMap<String, String> createTokenRequestParameters(final String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clidentId);
        params.add("redirect_url", redirectUrl);
        params.add("code", code);
        return params;
    }

    private HttpEntity<MultiValueMap<String, String>> createTokenRequestEntity(final String code) {
        return new HttpEntity<>(createTokenRequestParameters(code), createContentTypeHeaders());
    }

    private ResponseEntity<JsonNode> requestAccessToken(final String code) {
        return restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                createTokenRequestEntity(code),
                JsonNode.class
        );
    }
}
