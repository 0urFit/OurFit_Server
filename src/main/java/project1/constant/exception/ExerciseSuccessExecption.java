package project1.constant.exception;

import lombok.Getter;
import project1.constant.response.JsonResponseStatus;

@Getter
public class ExerciseSuccessExecption extends RuntimeException {
    private JsonResponseStatus status;

    public ExerciseSuccessExecption(JsonResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
