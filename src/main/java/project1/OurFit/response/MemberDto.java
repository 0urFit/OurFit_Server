package project1.OurFit.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project1.OurFit.entity.Member;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;
    private Double height;
    private Double weight;
    private Double squat;
    private Double benchpress;
    private Double deadlift;
    private Double overheadpress;

    public MemberDto(Member member) {
        this.nickname = member.getNickname();
        this.height = member.getHeight();
        this.weight = member.getWeight();
        this.squat = member.getSquat();
        this.benchpress = member.getBenchpress();
        this.deadlift = member.getDeadlift();
        this.overheadpress = member.getOverheadpress();
    }
}
