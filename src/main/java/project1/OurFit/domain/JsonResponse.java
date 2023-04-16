package project1.OurFit.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class JsonResponse {
    public JsonResponse(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

    private boolean isSuccess;
    private int code;
    private String message;

    private Object result;
}
