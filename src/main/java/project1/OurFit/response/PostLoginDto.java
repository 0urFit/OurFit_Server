package project1.OurFit.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PostLoginDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String gender;

    public PostLoginDto(String token) {
        this.token = token;
    }

    public PostLoginDto(String email, String gender) {
        this.email = email;
        this.gender = gender;
    }
}
