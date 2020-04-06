package edu.nju.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnifiedOrderResponseDTO {

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
    private String retmsg;

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
     * 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，MICROPAY
     *
     * */
    private String trade_type;

    /**
     * 微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
     *
     * */
    private String prepay_id;

    /**
     * 时间戳
     */
    private String timeStamp;

}
