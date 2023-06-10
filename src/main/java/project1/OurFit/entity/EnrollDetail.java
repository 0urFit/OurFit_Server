package project1.OurFit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class EnrollDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exerciseDetailId")
    private ExerciseDetail exerciseDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exerciseEnrollId")
    private ExerciseEnroll exerciseEnroll;

    @OneToMany(mappedBy = "enrollDetail", fetch = FetchType.LAZY)
    private List<EnrollDetailSet> enrollDetailSets = new ArrayList<>();
}
