package project1.OurFit.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {

    public LoginRequest(String email) {
        this.email = email;
    }

    private String email;
    private String password;
}
