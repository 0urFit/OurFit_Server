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
    RESPONSE_ERROR(false, HttpStatus.NOT_FOUND.value(), "값을 불러오는데 실패하였습니다."),
    USERS_EMPTY_EMAIL(false, HttpStatus.OK.value(), "이메일 사용가능합니다."),
    NOTFOUND(false,HttpStatus.NOT_FOUND.value(),"값을 찾을 수 없습니다"),
    UNAUTHORIZED(false, HttpStatus.UNAUTHORIZED.value(), "인증에 실패하였습니다."),
    EMAIL_CONFLICT(false, HttpStatus.CONFLICT.value(), "이메일 중복입니다."),
    NICKNAME_CONFLICT(false, HttpStatus.CONFLICT.value(), "닉네임 중복입니다."),
    ALL_CONFLICT(false, HttpStatus.CONFLICT.value(), "모두 중복입니다."),
    NOT_FOUND_ROUTINE(false,HttpStatus.NOT_FOUND.value(),"루틴을 찾을 수 없습니다."),
    NOT_FOUND_MEMBER(false,HttpStatus.NOT_FOUND.value(),"유저를 찾을 수 없습니다"),
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
