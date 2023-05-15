package project1.OurFit.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class GetExerciseRoutineDto {
    private Long id;
    private String category;
    private String imgpath;
    private int fewTime;
    private int level;
    private int period;
    private String routineName;
}
