package edu.nju.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnifiedOrderDTO {

    /**
     * 商品描述，例如"商家名称-销售商品类目"
     * 商品简单描述，该字段请按照规范传递
     * */
    private String body;

    /**
     * 商品订单号
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。
     * */
    private String out_trade_no;

    /**
     * 订单总金额，单位为分
     * */
    private Integer total_fee;

    /**
     * openId
     * */
    private String openid;

}
