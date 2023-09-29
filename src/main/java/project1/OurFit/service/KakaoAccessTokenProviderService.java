package project1.OurFit.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken(String code) {
        JsonNode root = sendAccessTokenRequest(code).getBody();
        return root.get("access_token").asText();
    }

    private HttpHeaders createHeadersWithContentType() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    private MultiValueMap<String, String> createAccessTokenParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "2be5601c4dcce1fe89db958b51887271");
        params.add("redirect_url", "http://localhost:3000/verifying");
        params.add("code", code);
        return params;
    }

    private HttpEntity<MultiValueMap<String, String>> createAccessTokenRequest(String code) {
        HttpHeaders headers = createHeadersWithContentType();
        MultiValueMap<String, String> params = createAccessTokenParams(code);
        return new HttpEntity<>(params, headers);
    }

    private ResponseEntity<JsonNode> sendAccessTokenRequest(String code) {
        return restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                createAccessTokenRequest(code),
                JsonNode.class
        );
    }
}
