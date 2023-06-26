package project1.OurFit.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseCompleteDto {

    private long routineId;
    private int week;
    private String day;
    private double percent_rate;
    private boolean lastday;
}
