package edu.nju.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 14:47
 */
@Getter
@AllArgsConstructor
public enum ClassificationStatus {

    USING(0, "使用中"),
    ABANDON(1, "已废弃");

    private int code;
    private String message;

    public static ClassificationStatus of(int code) throws Exception {
        for (ClassificationStatus item : ClassificationStatus.values()) {
            if (item.code == code) {
                return item;
            }
        }
        throw new Exception(); //todo error type
    }

}
