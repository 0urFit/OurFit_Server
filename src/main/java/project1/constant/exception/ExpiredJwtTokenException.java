package project1.constant.exception;

import project1.constant.response.JsonResponseStatus;

public class ExpiredJwtTokenException extends RuntimeException {
    private JsonResponseStatus status;

    public ExpiredJwtTokenException(JsonResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
