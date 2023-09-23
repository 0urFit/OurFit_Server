package project1.constant.exception;


import io.jsonwebtoken.JwtException;
import org.springframework.web.bind.annotation.ResponseBody;
import project1.constant.response.JsonResponse;
import project1.constant.response.JsonResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BaseException.class)
    public JsonResponse<JsonResponseStatus> BaseExceptionHandle(BaseException exception) {
        return new JsonResponse<>(JsonResponseStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public JsonResponse<JsonResponseStatus> DuplicateExceptionHandle(DuplicateException exception) {
        return new JsonResponse<>(exception.getStatus());
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public JsonResponse<JsonResponseStatus> JwtExceptionHandle(JwtException exception) {
        return new JsonResponse<>(JsonResponseStatus.REFRESH_TOKEN_EXPIRED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RefreshTokenException.class)
    @ResponseBody
    public JsonResponse<JsonResponseStatus> RefreshTokenHandle(RefreshTokenException exception) {
        return new JsonResponse<>(JsonResponseStatus.REFRESH_TOKEN_NOT_FOUND);
    }

    @ExceptionHandler(AccessTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public JsonResponse<JsonResponseStatus> AccessTokenHandle(AccessTokenException exception) {
        return new JsonResponse<>(JsonResponseStatus.ACCESS_TOKEN_EXPIRED);
    }

    @ExceptionHandler(TooManyRequestException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ResponseBody
    public JsonResponse<JsonResponseStatus> TooManyRequestHandle(TooManyRequestException exception) {
        return new JsonResponse<>(JsonResponseStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(ExpiredJwtTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public JsonResponse<JsonResponseStatus> ExpiredJwtTokenException(ExpiredJwtTokenException exception) {
        return new JsonResponse<>(JsonResponseStatus.ACCESS_TOKEN_EXPIRED);
    }
}
