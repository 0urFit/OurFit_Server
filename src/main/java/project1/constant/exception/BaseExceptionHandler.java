package project1.constant.exception;


import project1.constant.response.JsonResponse;
import project1.constant.response.JsonResponseStatus;
import jakarta.persistence.NoResultException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public JsonResponse<JsonResponseStatus> BaseExceptionHandle(BaseException exception) {
        System.out.println("여기는 BaseExceptionHandler 입니다. BaseException 이 발생하면 여기서 예외처리");
        return new JsonResponse<>(exception.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public JsonResponse<JsonResponseStatus> ExceptionHandle(Exception exception) {
        System.out.println("Exception");
        return new JsonResponse<>(JsonResponseStatus.NOTFOUND);
    }
}
