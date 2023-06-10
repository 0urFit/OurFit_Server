package project1.OurFit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class ExerciseDetailSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double weight;
    private int reps;
    private Double increase;
    private int sequence;
    private String exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exerciseDetailId")
    private ExerciseDetail exerciseDetail;
}
