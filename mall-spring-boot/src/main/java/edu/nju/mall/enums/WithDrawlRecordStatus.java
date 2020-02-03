package edu.nju.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 20:28
 */
@Getter
@AllArgsConstructor
public enum WithDrawlRecordStatus {
    TODO(0, "待处理"),
    DONE(1, "已处理");

    private int code;
    private String message;

    public static WithDrawlRecordStatus of(int code) throws Exception {
        for (WithDrawlRecordStatus item : WithDrawlRecordStatus.values()) {
            if (item.code == code) {
                return item;
            }
        }
        throw new Exception(); //todo error type
    }
}
