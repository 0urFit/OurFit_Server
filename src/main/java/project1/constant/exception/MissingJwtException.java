package project1.constant.exception;

import project1.constant.response.JsonResponseStatus;

public class MissingJwtException extends RuntimeException {
    private JsonResponseStatus status;

    public MissingJwtException(JsonResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
