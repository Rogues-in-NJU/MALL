package edu.nju.mall.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author hermc
 *
 * 承接访问微信登录接口auth.code2Session的返回值
 *
 */
@Data
public class WechatSession {

    private String openid;

    private String session_key;

    private String unionid;

    private Integer errcode;

    private String errmsg;

    @Getter
    @AllArgsConstructor
    public static enum WechatSessionCode {

        SYSTEM_BUSY(-1, "系统繁忙"),
        SUCCESS(0, "请求成功"),
        INVALID_CODE(40029, "code无效"),
        FREQUENCY_LIMIT(45011, "频率限制");

        private int code;
        private String message;

    }

}
