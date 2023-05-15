package project1.constant;

public enum Oauth {
    HEADER_CONTENT_NAME("Content-type"),
    HEADER_CONTENT_VALUE("application/x-www-form-urlencoded;charset=utf-8"),
    HEADER_REQUEST_NAME("Authorization"),
    HEADER_REQUEST_VALUE("Bearer "),
    GRANT_NAME("grant_type"),
    GRANT_VALUE("authorization_code"),
    CLIENT_NAME("client_id"),
    CLIENT_VALUE("2be5601c4dcce1fe89db958b51887271"),
    REDIRECT_NAME("redirect_uri"),
    REDIRECT_VALUE("http://localhost:3000/verifying"),
    CODE("code"),
    TOKEN_URL("https://kauth.kakao.com/oauth/token"),
    TOKEN_PROFILE("https://kapi.kakao.com/v2/user/me"),
    KAKAOLOGIN("https://kauth.kakao.com/oauth/authorize?client_id=2be5601c4dcce1fe89db958b51887271"
            + "&redirect_uri=http://localhost:3000/verifying&response_type=code");

    private final String value;

    Oauth(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
