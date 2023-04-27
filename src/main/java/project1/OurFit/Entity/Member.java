package project1.OurFit.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter
@Entity
@NoArgsConstructor
public class Member {
    public Member(String email, String nickname, String gender) {
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
