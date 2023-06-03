package project1.OurFit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class EnrollDetailSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double weight;
    private int reps;
    private int sequence;
    private Boolean complete = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollDetailId")
    private EnrollDetail enrollDetail;

    public void completeRoutine(){
        this.complete=true;
    }
}
