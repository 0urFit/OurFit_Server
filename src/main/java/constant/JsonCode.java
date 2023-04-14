package constant;

public enum JsonCode {
    SUCCESS(200),
    FAIL(204);

    private final int num;

    JsonCode(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
