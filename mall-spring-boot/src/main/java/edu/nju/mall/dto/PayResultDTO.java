package edu.nju.mall.dto;

import lombok.Data;

@Data
public class PayResultDTO {

    /**
     * SUCCESS/FAIL
     * 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
     * */
    private String return_code;

    /**
     * 返回信息，如非空，为错误原因
     * 签名失败
     * 参数格式校验错误
     * */
    private String return_msg;

    /**
     * 微信分配的小程序ID
     * */
    private String appid;

    /**
     * 微信支付分配的商户号
     * */
    private String mch_id;

    /**
     * 随机字符串，不长于32位
     * */
    private String nonce_str;

    /**
     * 签名
     * */
    private String sign;

    /**
     * SUCCESS/FAIL
     * */
    private String result_code;

    /**
     * 用户在商户appid下的唯一标识
     * */
    private String openid;

    /**
     * 用户是否关注公众账号，Y-关注，N-未关注
     * */
    private String is_subscribe;

    /**
     * JSAPI、NATIVE、APP
     * */
    private String trade_type;

    /**
     * 银行类型，采用字符串类型的银行标识，银行类型见银行列表
     * */
    private String bank_type;

    /**
     * 订单总金额，单位为分
     * */
    private Long total_fee;

    /**
     * 现金支付金额订单现金支付金额，详见支付金额
     * */
    private Long cash_fee;

    /**
     * 微信支付订单号
     * */
    private String transaction_id;

    /**
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     * */
    private String out_trade_no;

    /**
     * 支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
     * */
    private String time_end;

}
