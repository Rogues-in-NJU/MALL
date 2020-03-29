package edu.nju.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatus {

    NORMAL(1, "正常"),
    DELETED(2, "已删除");


    private int code;
    private String message;

    public static ProductStatus of(int code) throws Exception {
        for (ProductStatus item : ProductStatus.values()) {
            if (item.code == code) {
                return item;
            }
        }
        throw new Exception();
    }
}
