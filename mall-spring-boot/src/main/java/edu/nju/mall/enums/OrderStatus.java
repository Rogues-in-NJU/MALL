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
    SHIPPING(1, "待发货"),
    SHIPPED(2, "已发货"),
    SIGNED(3, "已签收"),
    REFUNDING(4, "申请退款中"),
    REFUNDED(5, "退款成功"),
    ABANDON(6, "已废弃");



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
