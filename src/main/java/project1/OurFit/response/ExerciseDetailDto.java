package project1.OurFit.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ExerciseDetailDto {
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
                private int sequence;
                private double weight;
                private int reps;
            }
        }
    }
}