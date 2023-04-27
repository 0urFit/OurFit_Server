package constant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class JsonResponse<T> {
    public JsonResponse(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

    private boolean isSuccess;
    private int code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL) // null일때는 json 변환 안함
    private T result;
}
