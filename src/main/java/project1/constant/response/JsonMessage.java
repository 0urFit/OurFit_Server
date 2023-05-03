package project1.constant.response;

public enum JsonMessage {
    SUCCESS("요청에 성공하였습니다."),
    FAIL("요청에 실패하였습니다."),
    NOTFOUND("값을 찾을 수 없습니다."),
    EXISTS("존재하는 값 입니다.");

    private final String message;

    JsonMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
