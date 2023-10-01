package project1.OurFit.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import project1.OurFit.response.SignUpDto;

@Service
@RequiredArgsConstructor
public class KakaoUserInfoProviderService {

    @Autowired private RestTemplate restTemplate;

    public SignUpDto getUserInfo(final String accessToken) {
        JsonNode responseBody = requestUserInfo(accessToken).getBody();
        return extractUserInfo(responseBody);
    }

    private HttpEntity<MultiValueMap<String, String>> createUserRequestHeaders(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return new HttpEntity<>(headers);
    }

    private ResponseEntity<JsonNode> requestUserInfo(final String accessToken) {
        return restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                createUserRequestHeaders(accessToken),
                JsonNode.class
        );
    }

    private SignUpDto extractUserInfo(final JsonNode responseBody) {
        SignUpDto signUpDto = new SignUpDto();
        JsonNode kakaoAccount = responseBody.get("kakao_account");

        JsonNode gender = kakaoAccount.get("gender");
        if (gender != null && !gender.asText().isEmpty()) {
            signUpDto.setGender(gender.asText());
        }

        signUpDto.setEmail(kakaoAccount.get("email").asText());
        return signUpDto;
    }
}
