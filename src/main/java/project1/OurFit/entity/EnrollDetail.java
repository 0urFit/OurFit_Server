package project1.OurFit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class EnrollDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean complete;

    @UpdateTimestamp
    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime completeDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exerciseDetailId")
    private ExerciseDetail exerciseDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exerciseEnrollId")
    private ExerciseEnroll exerciseEnroll;

    @OneToMany(mappedBy = "enrollDetail")
    List<EnrollDetailSet> enrollDetailSetList = new ArrayList<>();

}
