package project1.OurFit.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project1.OurFit.entity.Member;

import java.util.Optional;

@Getter
@NoArgsConstructor
public class TestDto {
    private String email;

    public TestDto(Member member){
        this.email=member.getEmail();
    }
}
