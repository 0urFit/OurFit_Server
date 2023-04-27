package project1.OurFit.Request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberDTO {
    private String email;
    private String password;
    private String nickname;
    private String gender;
    private Double height;
    private Double weight;
    private Double squat;
    private Double benchpress;
    private Double deadlift;
    private Double overheadpress;
}
