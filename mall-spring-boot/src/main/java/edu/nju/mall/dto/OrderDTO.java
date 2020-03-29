package edu.nju.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-03-29 20:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    /**
     * 微信号ID
     */
    private String userId;

    private Long productId;

    private int num;

    private String consignee;

    private String consigneePhone;

    private String consigneeAddress;
}