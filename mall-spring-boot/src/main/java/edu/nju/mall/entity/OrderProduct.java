package edu.nju.mall.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 13:59
 */

@Data
@Builder
@Entity
@Table(name = "order_product")
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 商品Id
     */
    @Column(name="product_id")
    private int productId;

    /**
     * 商品名称
     */
    @Column(name="product_name")
    private String productName;

    /**
     * 购买数量
     */
    @Column(name="product_num")
    private int productNum;

    /**
     * 订单Id
     */
    @Column(name="order_id")
    private int orderId;
}

