package project1.OurFit.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PostSignUp {

    public PostSignUp(String email) {
        this.email = email;
    }

    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String gender;
}
