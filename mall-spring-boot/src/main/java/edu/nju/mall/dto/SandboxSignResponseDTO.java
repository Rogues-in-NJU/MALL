package edu.nju.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SandboxSignResponseDTO {

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
     * 微信支付分配的商户号
     *
     * */
    private String mch_id;

    /**
     * 沙箱秘钥
     *
     * */
    private String sandbox_signkey;

}
