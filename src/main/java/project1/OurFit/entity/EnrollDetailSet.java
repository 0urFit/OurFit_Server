package project1.OurFit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class EnrollDetailSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double weight;
    private int reps;
    private int sequence;

    private String bigThree; //3대운동
    private double rate; //오운완 성공시 비율

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollDetailId")
    private EnrollDetail enrollDetail;

}
