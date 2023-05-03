package project1.OurFit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@NoArgsConstructor
public class Member {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true)
    private String nickname;
    private String gender;
    private Double height;
    private Double weight;
    private Double squat;
    private Double benchpress;
    private Double deadlift;
    private Double overheadpress;

    @OneToMany(mappedBy = "member")
    List<PostReply> postReplyList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    List<ExerciseLike> exerciseLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    List<ExerciseEnroll> exerciseEnrollList = new ArrayList<>();

    public Member(String email, String nickname, String gender) {
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
    }

    public Member(Long id, String email, String password, String nickname, String gender, Double height, Double weight, Double squat, Double benchpress, Double deadlift, Double overheadpress) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.squat = squat;
        this.benchpress = benchpress;
        this.deadlift = deadlift;
        this.overheadpress = overheadpress;
    }
}
