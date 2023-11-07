package project1.OurFit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@AllArgsConstructor
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
    private String refreshToken;

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
}
