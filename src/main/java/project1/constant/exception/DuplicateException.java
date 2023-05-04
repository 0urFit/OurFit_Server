package project1.constant.exception;

import lombok.Getter;
import lombok.Setter;
import project1.constant.response.JsonResponseStatus;

@Getter @Setter
public class DuplicateException extends RuntimeException {

    private JsonResponseStatus status;

    public DuplicateException(JsonResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
