package project1.constant.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum JsonResponseStatus {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),
    /**
     * 400 : Request, Response 오류
     */
    LOGIN_FAIL(false, HttpStatus.UNAUTHORIZED.value(), "아이디나 비밀번호가 맞지 않습니다."),
    RESPONSE_ERROR(false, HttpStatus.NOT_FOUND.value(), "값을 불러오는데 실패하였습니다."),
    USERS_EMPTY_EMAIL(false, HttpStatus.OK.value(), "이메일 사용가능합니다."),
    NOTFOUND(false,HttpStatus.NOT_FOUND.value(),"값을 찾을 수 없습니다"),
    UNAUTHORIZED(false, HttpStatus.UNAUTHORIZED.value(), "인증에 실패하였습니다."),
    EMAIL_CONFLICT(false, HttpStatus.CONFLICT.value(), "이메일 중복입니다."),
    NICKNAME_CONFLICT(false, HttpStatus.CONFLICT.value(), "닉네임 중복입니다."),
    ALL_CONFLICT(false, HttpStatus.CONFLICT.value(), "모두 중복입니다."),
    NOT_FOUND_ROUTINE(false,HttpStatus.NOT_FOUND.value(),"루틴을 찾을 수 없습니다."),
    NOT_FOUND_MEMBER(false,HttpStatus.NOT_FOUND.value(),"유저를 찾을 수 없습니다"),
    REFRESH_TOKEN_EXPIRED(false, HttpStatus.UNAUTHORIZED.value(), "리프레시 토큰이 만료되었습니다."),
    REFRESH_TOKEN_NOT_FOUND(false, HttpStatus.NOT_FOUND.value(), "리프레시 토큰이 없습니다."),
    ACCESS_TOKEN_EXPIRED(false, HttpStatus.UNAUTHORIZED.value(), "액세스 토큰이 만료되었습니다."),
    TOO_MANY_REQUESTS(false, HttpStatus.TOO_MANY_REQUESTS.value(), "15분 후에 다시 시도하세요."),
    NOT_FOUND_ENROLL(false, HttpStatus.NOT_FOUND.value(), "등록된 운동이 없습니다."),
    INVALID_JWT(false, HttpStatus.UNAUTHORIZED.value(), "잘못된 JWT 입니다."),
    MISSING_JWT(false, HttpStatus.UNAUTHORIZED.value(), "토큰이 없습니다."),
    /**
     * 500 :  Database, Server 오류
     */
    UNEXPECTED_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "예상치 못한 에러가 발생했습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;
    JsonResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
