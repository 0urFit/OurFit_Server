package project1.OurFit.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class NewPostDto {

    private String title;
    private String category;
    private String gender;
    private String content;
    private Date create_at;
}
