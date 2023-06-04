package project1.OurFit.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EnrollDetailDto {
    private int weeks;
    private List<day> days;
    private boolean complete;
    private Long routineId; //운동상세루틴 id

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
