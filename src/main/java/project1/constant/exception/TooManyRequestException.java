package project1.constant.exception;

import lombok.Getter;
import lombok.Setter;
import project1.constant.response.JsonResponseStatus;

@Getter
@Setter
public class TooManyRequestException extends RuntimeException {
    private JsonResponseStatus status;

    public TooManyRequestException(JsonResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
