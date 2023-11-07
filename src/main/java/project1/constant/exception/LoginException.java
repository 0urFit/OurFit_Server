package project1.constant.exception;

import lombok.Getter;
import project1.constant.response.JsonResponseStatus;

@Getter
public class LoginException extends RuntimeException {
    JsonResponseStatus status;

    public LoginException(JsonResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
