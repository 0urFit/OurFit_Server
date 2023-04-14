package project1.OurFit.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String Email;
    private String Pwd;
    private String Nickname;
    private boolean Gender;
    private Double Height;
    private Double Weight;
    private Double Squat;
    private Double Benchpress;
    private Double Deadlift;
    private Double Overheadpress;
}
