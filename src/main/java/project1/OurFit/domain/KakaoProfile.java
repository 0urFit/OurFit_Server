package project1.OurFit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true) // 받고 싶은 데이터만 받음
public class KakaoProfile {
    public Properties properties;
    public KakaoAccount kakao_account;

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Properties {
        public String nickname;
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class KakaoAccount {
        public Profile profile;
        public String email;
        public String gender;

        @Getter @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Profile {
            public String nickname;
        }
    }
}
