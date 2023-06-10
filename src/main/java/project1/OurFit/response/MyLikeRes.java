package project1.OurFit.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project1.OurFit.entity.ExerciseRoutine;

@Getter
@AllArgsConstructor
public class MyLikeRes {
    private String routineName;
    private String imgPath;
    private int level;
    private int fewTime;
    private int period;
    private Long Id;

    public MyLikeRes(ExerciseRoutine ex){
        this.routineName=ex.getRoutineName();
        this.imgPath=ex.getImgpath();
        this.level=ex.getLevel();
        this.fewTime=ex.getFewTime();
        this.period=ex.getPeriod();
        this.Id=ex.getId();
    }
}


