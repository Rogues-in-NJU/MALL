package edu.nju.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderQueryResponseDTO {

    /**
     * SUCCESS/FAIL
     * 此字段是通信标识，非交易标识，交易是否成功需要查看trade_state来判断
     *
     * */
    private String return_code;

    /**
     * 返回信息，如非空，为错误原因
     * * 签名失败
     * * 参数格式校验错误
     *
     * */
    private String return_msg;

    // 以下字段在return_code为SUCCESS的时候有返回
    /**
     * 微信分配的小程序ID
     *
     * */
    private String appid;

    /**
     * 微信支付分配的商户号
     *
     * */
    private String mch_id;

    /**
     * 随机字符串，不长于32位
     *
     * */
    private String nonce_str;

    /**
     * 签名
     *
     * */
    private String sign;

    /**
     * SUCCESS/FAIL
     *
     * */
    private String result_code;

    // 以下字段在return_code 、result_code、trade_state都为SUCCESS时有返回 ，如trade_state不为 SUCCESS，则只返回out_trade_no（必传）和attach（选传）。
    /**
     * 用户在商户appid下的唯一标识
     *
     * */
    private String openid;

    /**
     * 用户是否关注公众账号，Y-关注，N-未关注
     *
     * */
    private String is_subscribe;

    /**
     * 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，MICROPAY
     *
     * */
    private String trade_type;

    /**
     * SUCCESS—支付成功
     * REFUND—转入退款
     * NOTPAY—未支付
     * CLOSED—已关闭
     * REVOKED—已撤销（刷卡支付）
     * USERPAYING--用户支付中
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     *
     * */
    private String trade_state;

    /**
     * 银行类型，采用字符串类型的银行标识
     *
     * */
    private String bank_type;

    /**
     * 订单总金额，单位为分
     *
     * */
    private Integer total_fee;

    /**
     * 现金支付金额订单现金支付金额
     *
     * */
    private Integer cash_fee;

    /**
     * 微信支付订单号
     *
     * */
    private String transaction_id;

    /**
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     *
     * */
    private String out_trade_no;

    /**
     * 订单支付时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
     *
     * */
    private String time_end;

    /**
     * 对当前查询订单状态的描述和下一步操作的指引
     *
     * */
    private String trade_state_desc;

}
