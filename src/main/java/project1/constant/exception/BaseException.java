package project1.constant.exception;

import project1.constant.response.JsonResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {
    private JsonResponseStatus status;

    public BaseException(JsonResponseStatus status) {
        super(status.getMessage());
        this.status = status;
        System.out.println("@Service 에서 넘어옴 -> BaseExceptionHandler 에서 예외처리할꺼임");
    }
}
