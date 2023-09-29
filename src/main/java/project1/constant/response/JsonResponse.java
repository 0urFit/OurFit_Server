package project1.constant.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static project1.constant.response.JsonResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
public class JsonResponse<T> {
    private final boolean isSuccess;
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL) // null일때는 json 변환 안함
    private T result;

    public JsonResponse(JsonResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public JsonResponse(boolean isSuccess, String code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

    //요청에 성공
    public JsonResponse(T result) {
        this.isSuccess = SUCCESS.isSuccess();
        this.code = SUCCESS.getCode();
        this.message = SUCCESS.getMessage();
        this.result=result;
    }

    public JsonResponse(T result, JsonResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.code = status.getCode();
        this.message = status.getMessage();
        this.result = result;
    }

    //상태코드
    public JsonResponse(T result, String code) {
        this.isSuccess = SUCCESS.isSuccess();
        this.code = code;
        this.message = SUCCESS.getMessage();
        this.result=result;
    }


}
