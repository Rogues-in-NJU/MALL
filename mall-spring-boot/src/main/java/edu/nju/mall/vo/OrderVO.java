package edu.nju.mall.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-24 17:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderVO {

    private Integer id;

    private String userId;

    private String orderTime;

    private String refundTime;

    private String consignee;

    private String consigneePhone;

    private String consigneeAddress;

    private double price;

    private String status;
}
