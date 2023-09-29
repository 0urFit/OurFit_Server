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

@Service("kakaoUserInfoService")
@RequiredArgsConstructor
public class KakaoUserInfoProviderService {

    private final RestTemplate restTemplate = new RestTemplate();

    public SignUpDto getUserInfo(String accessToken) {
        JsonNode root = sendUserInfoRequest(accessToken).getBody();
        return extractUserInfo(root);
    }

    private HttpEntity<MultiValueMap<String, String>> createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return new HttpEntity<>(headers);
    }

    private ResponseEntity<JsonNode> sendUserInfoRequest(String accessToken) {
        return restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                createHeaders(accessToken),
                JsonNode.class
        );
    }

    private SignUpDto extractUserInfo(JsonNode root) {
        SignUpDto signUpDto = new SignUpDto();
        JsonNode gender = root.get("kakao_account").get("gender");
        if (!(gender == null || gender.asText().isEmpty()))
            signUpDto.setGender(root.get("kakao_account").get("gender").asText());
        signUpDto.setEmail(root.get("kakao_account").get("email").asText());
        return signUpDto;
    }
}
