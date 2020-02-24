package edu.nju.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-22 14:56
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {

    /**
     * PAYING -> SHIPPING or ABANDON
     * SHIPPING -> SHIPPED or REFUNDING
     * SHIPPED -> SIGNED or REFUNDING
     * SIGNED -> REFUNDING
     * REFUNDING -> REFUNDED
     */
    PAYING(0, "待支付"),
    TODO(1, "未完成"),
    FINISHED(2, "已完成"),
    REFUNDING(3, "申请退款中"),
    REFUNDED(4, "退款成功"),
    ABANDON(5, "已废弃");



    private int code;
    private String message;

    public static OrderStatus of(int code) throws Exception {
        for (OrderStatus item : OrderStatus.values()) {
            if (item.code == code) {
                return item;
            }
        }
        throw new Exception();
    }
}
