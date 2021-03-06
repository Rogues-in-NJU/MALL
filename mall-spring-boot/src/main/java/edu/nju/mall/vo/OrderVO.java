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

    private Long id;

    private Long userId;

    private String transactionNumber;

    private String createdAt;

    private String payTime;

    private String startTime;

    private String refundTime;

    private Long productId;

    private String productName;

    private String productImage;

    private int num;

    private String consignee;

    private String consigneePhone;

    private String consigneeAddress;

    private Integer price;

    private Integer status;

    private String statusName;
}
