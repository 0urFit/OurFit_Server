package project1.OurFit.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity
@NoArgsConstructor
public class Member {
    public Member(String email, String nickname, String gender) {
        Email = email;
        Nickname = nickname;
        Gender = gender;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String Email;
    private String Password;
    private String Nickname;
    private String Gender;
    private Double Height;
    private Double Weight;
    private Double Squat;
    private Double Benchpress;
    private Double Deadlift;
    private Double Overheadpress;
}
