package project1.OurFit.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class GetPostAllDto {
    private Long id;
    private String category;
    private String gender;
    private String title;
    private Date created_at;
}
