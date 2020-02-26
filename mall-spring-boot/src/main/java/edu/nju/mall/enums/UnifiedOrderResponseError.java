package edu.nju.mall.enums;

import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.NJUException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UnifiedOrderResponseError {

    INVALID_REQUEST("INVALID_REQUEST", "参数错误", "订单重入时，要求参数值与原请求一致，请确认参数问题"),
    NOAUTH("NOAUTH", "商户无此接口权限", "请商户前往申请此接口权限"),
    NOTENOUGH("NOTENOUGH", "余额不足", "用户帐号余额不足，请用户充值或更换支付卡后再支付"),
    ORDERPAID("ORDERPAID", "商户订单已支付", "商户订单已支付，无需更多操作"),
    ORDERCLOSED("ORDERCLOSED", "订单已关闭", "当前订单已关闭，请重新下单"),
    SYSTEMERROR("SYSTEMERROR", "系统错误", "系统异常，请用相同参数重新调用"),
    APPID_NOT_EXIST("APPID_NOT_EXIST", "APPID不存在", "请检查APPID是否正确"),
    MCHID_NOT_EXIST("MCHID_NOT_EXIST", "MCHID不存在", "请检查MCHID是否正确"),
    APPID_MCHID_NOT_MATCH("APPID_MCHID_NOT_MATCH", "appid和mch_id不匹配", "请确认appid和mch_id是否匹配"),
    LACK_PARAMS("LACK_PARAMS", "缺少参数", "请检查参数是否齐全"),
    OUT_TRADE_NO_USED("OUT_TRADE_NO_USED", "商户订单号重复", "请核实商户订单号是否重复提交"),
    SIGNERROR("SIGNERROR", "签名错误", "请检查签名参数和方法是否都符合签名算法要求"),
    XML_FORMAT_ERROR("XML_FORMAT_ERROR", "XML格式错误", "请检查XML参数格式是否正确"),
    REQUIRE_POST_METHOD("REQUIRE_POST_METHOD", "请使用post方法", "请检查请求参数是否通过post方法提交"),
    POST_DATA_EMPTY("POST_DATA_EMPTY", "post数据为空", "请检查post数据是否为空"),
    NOT_UTF8("NOT_UTF8", "编码格式错误", "请使用UTF-8编码格式");

    private String name;
    private String description;
    private String solution;

    public static UnifiedOrderResponseError of(String name) throws Exception {
        for (UnifiedOrderResponseError item : UnifiedOrderResponseError.values()) {
            if (item.name.equals(name)) {
                return item;
            }
        }
        throw new NJUException(ExceptionEnum.SERVER_ERROR, String.format("UnifiedOrderResponseError中没有找到[%s]类型", name));
    }

}
