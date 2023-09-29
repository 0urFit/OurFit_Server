package project1.constant.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum JsonResponseStatus {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, "200", "요청에 성공하였습니다."),
    /**
     * 400 : Request, Response 오류
     */
    LOGIN_FAIL(false, "401", "아이디나 비밀번호가 맞지 않습니다."),
    RESPONSE_ERROR(false, "404", "값을 불러오는데 실패하였습니다."),
    USERS_EMPTY_EMAIL(false, "200", "이메일 사용가능합니다."),
    NOTFOUND(false,"404","값을 찾을 수 없습니다"),
    UNAUTHORIZED(false, "401", "인증에 실패하였습니다."),
    EMAIL_CONFLICT(false, "409", "이메일 중복입니다."),
    NICKNAME_CONFLICT(false, "409", "닉네임 중복입니다."),
    ALL_CONFLICT(false, "409", "모두 중복입니다."),
    NOT_FOUND_ROUTINE(false, "404","루틴을 찾을 수 없습니다."),
    NOT_FOUND_MEMBER(false, "404","유저를 찾을 수 없습니다"),
    REFRESH_TOKEN_EXPIRED(false, "401", "리프레시 토큰이 만료되었습니다."),
    REFRESH_TOKEN_NOT_FOUND(false, "404", "리프레시 토큰이 없습니다."),
    ACCESS_TOKEN_EXPIRED(false, "401", "액세스 토큰이 만료되었습니다."),
    NOT_FOUND_ENROLL(false, "404", "등록된 운동이 없습니다."),
    INVALID_JWT(false, "401", "잘못된 JWT 입니다."),
    MISSING_JWT(false, "401", "토큰이 없습니다."),
    ACCOUNT_CONFLICT(false, "409", "이미 자체 회원가입을 통해 등록된 계정이 있습니다. 해당 계정으로 로그인 해주세요."),
    /**
     * 500 :  Database, Server 오류
     */
    UNEXPECTED_ERROR(false, "500", "예상치 못한 에러가 발생했습니다.");

    private final boolean isSuccess;
    private final String code;
    private final String message;
    JsonResponseStatus(boolean isSuccess, String code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
