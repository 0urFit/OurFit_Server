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
    private Long exerciseId;

    public MyRoutineRes(ExerciseRoutine ex){
        this.routineName=ex.getRoutineName();
        this.level=ex.getLevel();
        this.fewTime=ex.getFewTime();
        this.period=ex.getPeriod();
        this.imgpath=ex.getImgpath();
        this.category=ex.getCategory();
        this.exerciseId=ex.getId();
    }
}
