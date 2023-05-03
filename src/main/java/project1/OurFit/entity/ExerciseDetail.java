package project1.OurFit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class ExerciseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String day;
    private int sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exerciseRoutineId")
    private ExerciseRoutine exerciseRoutine;

    @OneToMany(mappedBy = "exerciseDetail")
    List<ExerciseDetailSet> exerciseDetailSetList = new ArrayList<>();

}
