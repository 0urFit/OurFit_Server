package project1.OurFit.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import project1.OurFit.entity.ExerciseRoutine;

@Getter
@AllArgsConstructor
public class MyRoutineRes {
    private String routineName;
    private String imgPath;
    private int level;
    private int fewTime;
    private int period;

    public MyRoutineRes(ExerciseRoutine ex){
        this.routineName=ex.getRoutineName();
        this.imgPath=ex.getImgPath();
        this.level=ex.getLevel();
        this.fewTime=ex.getFewTime();
        this.period=ex.getPeriod();
    }


}
