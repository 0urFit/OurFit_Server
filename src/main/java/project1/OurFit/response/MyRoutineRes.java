package project1.OurFit.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project1.OurFit.entity.ExerciseRoutine;

@Getter
@AllArgsConstructor
public class MyRoutineRes {
    private String routineName;
    private int level;
    private int fewTime;
    private int period;
    private String imgpath;
    private String category;
    private int weekProgress;
    private boolean isLiked;
    private boolean isEnrolled;
    private Long Id;

    public MyRoutineRes(ExerciseRoutine ex, int weekProgress, boolean isLiked, boolean isEnrolled){
        this.routineName=ex.getRoutineName();
        this.level=ex.getLevel();
        this.fewTime=ex.getDaysPerWeek();
        this.period=ex.getProgramLength();
        this.imgpath=ex.getImgpath();
        this.category=ex.getCategory();
        this.weekProgress = weekProgress;
        this.isLiked = isLiked;
        this.isEnrolled = isEnrolled;
        this.Id=ex.getId();
    }
}
