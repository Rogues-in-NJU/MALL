package edu.nju.mall.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 13:55
 */

@Data
@SuperBuilder
@Entity
@Table(name = "order_sheet")
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 购买人Id(微信号)
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 微信交易号
     */
    @Column(name = "transaction_number")
    private String transactionNumber;

    /**
     * 支付时间
     */
    @Column(name = "pay_time")
    private String payTime;

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
     * 总金额(单位：分)
     */
    private int price;

    /**
     * 状态
     */
    private int status;

}
