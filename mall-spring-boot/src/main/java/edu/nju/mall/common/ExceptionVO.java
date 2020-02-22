package edu.nju.mall.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionVO {

    private ExceptionEnum exceptionEnum;

    private String errorMessage;

    public static ExceptionVO defaultExceptionWrapper(Throwable t) {
        return ExceptionVO.builder()
                .exceptionEnum(ExceptionEnum.SERVER_ERROR)
                .errorMessage(
                        String.format("[%s] %s", ExceptionEnum.SERVER_ERROR.getMessage(),
                                t.getMessage()))
                .build();
    }

    public static ExceptionVO customExceptionWrapper(ExceptionEnum exceptionEnum, String errorMessage) {
        return ExceptionVO.builder()
                .errorMessage(String.format("[%s] %s", exceptionEnum.getMessage(),
                        errorMessage))
                .exceptionEnum(exceptionEnum)
                .build();
    }

}
