package edu.nju.mall.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 13:55
 */

@Data
@Builder
@Entity
@Table(name = "order_sheet")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 购买人Id(微信号)
     */
    private String userId;

    /**
     * 下单时间
     */
    @Column(name = "order_time")
    private String orderTime;

    /**
     * 退款时间
     */
    @Column(name = "refund_time")
    private String refundTime;

    /**
     * 收货人(姓名)
     */
    private String consignee;

    /**
     * 收货人手机
     */
    @Column(name = "consignee_phone")
    private String consigneePhone;

    /**
     * 收货人地址
     */
    @Column(name = "consignee_address")
    private String consigneeAddress;

    /**
     * 总金额
     */
    private double price;

    /**
     * 状态
     */
    private int status;

}
