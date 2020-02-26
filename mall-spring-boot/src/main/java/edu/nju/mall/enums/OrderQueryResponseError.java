package edu.nju.mall.enums;

import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.NJUException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderQueryResponseError {

    ORDERNOTEXIST("ORDERNOTEXIST", "此交易订单号不存在", "该API只能查提交支付交易返回成功的订单，请商户检查需要查询的订单号是否正确"),
    SYSTEMERROR("SYSTEMERROR", "系统错误", "系统异常，请再调用发起查询");

    private String name;
    private String description;
    private String solution;

    public static OrderQueryResponseError of(String name) throws Exception {
        for (OrderQueryResponseError item : OrderQueryResponseError.values()) {
            if (item.name.equals(name)) {
                return item;
            }
        }
        throw new NJUException(ExceptionEnum.SERVER_ERROR, String.format("UnifiedOrderResponseError中没有找到[%s]类型", name));
    }

}
