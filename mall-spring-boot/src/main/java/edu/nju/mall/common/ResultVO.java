package edu.nju.mall.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultVO<T> {

    private String message;

    private int code;

    private T data;

    public static ResultVO<String> ok() {
        return ok("success", ExceptionEnum.SUCCESS);
    }

    public static <T> ResultVO<T> ok(T data) {
        return ok(data, ExceptionEnum.SUCCESS);
    }

    public static <T> ResultVO<T> ok(T data, ExceptionEnum exceptionEnum) {
        return ResultVO.<T>builder()
                .code(exceptionEnum.getCode())
                .message(exceptionEnum.getMessage())
                .data(data)
                .build();
    }

    public static <T> ResultVO<T> fail(ExceptionEnum exceptionEnum,
                                             String message) {
        return ResultVO.<T>builder()
                .code(exceptionEnum.getCode())
                .message(String.format("[%s]%s", exceptionEnum.getMessage(), message))
                .build();
    }

    public static <T> ResultVO<T> fail(ExceptionVO exceptionWrapper) {
        return ResultVO.<T>builder()
                .code(exceptionWrapper.getExceptionEnum().getCode())
                .message(exceptionWrapper.getErrorMessage())
                .build();
    }

}
