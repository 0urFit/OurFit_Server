package project1.constant.exception;

import lombok.Getter;
import project1.OurFit.response.SignUpDto;
import project1.constant.response.JsonResponseStatus;

@Getter
public class UnregisteredUserException extends RuntimeException {
    private JsonResponseStatus status;
    private SignUpDto signUpDto;

    public UnregisteredUserException(JsonResponseStatus status, SignUpDto signUpDto) {
        super(status.getMessage());
        this.status = status;
        this.signUpDto = signUpDto;
    }

    public SignUpDto getSignUpDto() {
        return signUpDto;
    }
}
