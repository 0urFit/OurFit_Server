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

    private double oneWeight;
    private int oneReps;

    private double twoWeight;
    private int twoReps;

    private double thrWeight;
    private int thrReps;

    private double fouWeight;
    private int fouReps;

    private double fivWeight;
    private int fivReps;

    private double sixWeight;
    private int sizReps;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollDetailId")
    private EnrollDetail enrollDetail;


}
