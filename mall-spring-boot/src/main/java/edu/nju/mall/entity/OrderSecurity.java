package edu.nju.mall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@SuperBuilder
@Entity
@Table(name = "order_security")
@NoArgsConstructor
@AllArgsConstructor
public class OrderSecurity extends BaseEntity {

    @Column(name = "nonce_str", unique = true)
    private String nonceStr;

    @Column(name = "sign")
    private String sign;

    @Column(name = "prepay_id")
    private String prepayId;

    @Column(name = "time_stamp")
    private String timeStamp;

    @Column(name = "orderId")
    private Long orderId;

    @Column(name = "order_code")
    private Long orderCode;

}
