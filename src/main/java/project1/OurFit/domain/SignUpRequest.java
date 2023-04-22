package project1.OurFit.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class SignUpRequest {

    public SignUpRequest(String email) {
        this.email = email;
    }

    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String gender;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;
}
