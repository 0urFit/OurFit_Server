package project1.OurFit.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true) // 받고 싶은 데이터만 받음
public class PostKakaoProfile {
    public KakaoAccount kakao_account;

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {
        public String email;
        public String gender;
    }
}
