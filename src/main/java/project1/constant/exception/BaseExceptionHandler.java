package project1.constant.exception;


import org.springframework.dao.DataIntegrityViolationException;
import project1.constant.response.JsonResponse;
import project1.constant.response.JsonResponseStatus;
import jakarta.persistence.NoResultException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BaseException.class)
    public JsonResponse<JsonResponseStatus> BaseExceptionHandle(BaseException exception) {
        // 여기는 BaseExceptionHandler 입니다. BaseException 이 발생하면 여기서 예외처리
        return new JsonResponse<>(JsonResponseStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public JsonResponse<JsonResponseStatus> ExceptionHandle(Exception exception) {
        // Exception
        return new JsonResponse<>(JsonResponseStatus.NOTFOUND);
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public JsonResponse<JsonResponseStatus> DuplicateExceptionHandle(DuplicateException exception) {
        return new JsonResponse<>(exception.getStatus());
    }
}
