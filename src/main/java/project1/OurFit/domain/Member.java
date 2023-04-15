package project1.OurFit.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String Email;
    private String Password;
    private String Nickname;
    private boolean Gender;
    private Double Height;
    private Double Weight;
    private Double Squat;
    private Double Benchpress;
    private Double Deadlift;
    private Double Overheadpress;
}
