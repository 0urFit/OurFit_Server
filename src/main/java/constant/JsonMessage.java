package constant;

public enum JsonMessage {
    SUCCESS("요청에 성공하였습니다."),
    FAIL("요청에 실패하였습니다.");

    private final String message;

    JsonMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
