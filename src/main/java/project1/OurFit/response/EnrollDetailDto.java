package project1.OurFit.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EnrollDetailDto {
    private String routineName;
    private String category;
    private int level;
    private int fewTime;
    private int period;
    private int weekProgress;
    private int weeks;
    private List<day> days;


    @Getter
    @Setter
    @NoArgsConstructor
    public static class day {
        private String day;
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
                private Long Id;
                private int sequence;
                private double weight;
                private int reps;
                private boolean complete = false;
            }
        }
    }
}
