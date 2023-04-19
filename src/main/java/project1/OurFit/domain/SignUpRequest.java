package project1.OurFit.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class SignUpRequest {

    private String email;
    private String gender;
    private String nickname;
}
