package project1.constant.exception;


import io.jsonwebtoken.JwtException;
import org.springframework.web.bind.annotation.ResponseBody;
import project1.OurFit.response.SignUpDto;
import project1.constant.response.JsonResponse;
import project1.constant.response.JsonResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BaseException.class)
    public JsonResponse<JsonResponseStatus> baseExceptionHandle(BaseException exception) {
        return new JsonResponse<>(JsonResponseStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public JsonResponse<JsonResponseStatus> loginExceptionHandle(LoginException exception) {
        return new JsonResponse<>(exception.getStatus());
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public JsonResponse<JsonResponseStatus> duplicateExceptionHandle(DuplicateException exception) {
        return new JsonResponse<>(exception.getStatus());
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public JsonResponse<JsonResponseStatus> jwtExceptionExceptionHandle(JwtException exception) {
        return new JsonResponse<>(JsonResponseStatus.REFRESH_TOKEN_EXPIRED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RefreshTokenException.class)
    @ResponseBody
    public JsonResponse<JsonResponseStatus> refreshTokenExceptionHandle(RefreshTokenException exception) {
        return new JsonResponse<>(exception.getStatus());
    }

    @ExceptionHandler(AccessTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public JsonResponse<JsonResponseStatus> accessTokenExceptionHandle(AccessTokenException exception) {
        return new JsonResponse<>(exception.getStatus());
    }

    @ExceptionHandler(ExpiredJwtTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public JsonResponse<JsonResponseStatus> expiredJwtTokenExceptionHandle(ExpiredJwtTokenException exception) {
        return new JsonResponse<>(JsonResponseStatus.ACCESS_TOKEN_EXPIRED);
    }

    @ExceptionHandler(UnregisteredUserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public JsonResponse<SignUpDto> UnregisteredUserExceptionHandle(UnregisteredUserException exception) {
        return new JsonResponse<>(exception.getSignUpDto(), exception.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public JsonResponse<JsonResponseStatus> NotFoundExceptionHandle(NotFoundException exception) {
        return new JsonResponse<>(exception.getStatus());
    }
}
