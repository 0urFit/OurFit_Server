package project1.OurFit.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project1.OurFit.entity.ExerciseRoutine;

@Getter
@AllArgsConstructor
public class MyLikeRes {
    private Long Id;
    private String routineName;
    private String imgPath;
    private String category;
    private int level;
    private int fewTime;
    private int period;
    private boolean enrolled;
    private boolean liked;

    public MyLikeRes(ExerciseRoutine ex, String category, boolean enrolled){
        this.routineName=ex.getRoutineName();
        this.imgPath=ex.getImgpath();
        this.level=ex.getLevel();
        this.fewTime=ex.getFewTime();
        this.period=ex.getPeriod();
        this.Id=ex.getId();
        this.category = category;
        this.enrolled = enrolled;
        this.liked = true;
    }
}
