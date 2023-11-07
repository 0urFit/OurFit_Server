package project1.constant.exception;

import lombok.Getter;
import project1.constant.response.JsonResponseStatus;

@Getter
public class NotFoundException extends RuntimeException {
    private JsonResponseStatus status;

    public NotFoundException(JsonResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
