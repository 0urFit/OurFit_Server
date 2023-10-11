package project1.OurFit.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ExerciseDetailDto {
    private String routineName;
    private int level;
    private int weeks;
    private int period;
    private boolean isenrolled;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isliked;
    private List<day> days;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class day {
        private String day;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private boolean issuccess;
        private List<exercises> exercises;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class exercises {
            private String name;
            private List<SetDetail> sets;

            @Getter
            @Setter
            @NoArgsConstructor
            public static class SetDetail {
                private int sequence;
                private double weight;
                private int reps;
            }
        }
    }
}