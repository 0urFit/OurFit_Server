package project1.OurFit.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ExerciseViewDto {
    private String routineName;
    private int level;
    private int weeks;
    private int period;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer currentWeek;
    private boolean isenrolled;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isliked;
}
