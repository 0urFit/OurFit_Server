package project1.OurFit.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class JsonResponse {
    private boolean isSuccess;
    private int code;
    private String message;
}
