package project1.OurFit.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OAuthToken {
    private String id_token;
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String scope;
    private int expires_in;
    private int refresh_token_expires_in;
}
