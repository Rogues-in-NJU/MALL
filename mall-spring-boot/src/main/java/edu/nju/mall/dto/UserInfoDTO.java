package edu.nju.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO extends BaseDTO {

    private Long userId;

    /**
     * 提现额度
     * */
    private Double withdrawal;

    /**
     * 下级用户数量
     * */
    private Long subordinateNum;

    /**
     * 待付款
     * */
    private Long unpaidNum;

    /**
     * 待完成
     * */
    private Long undoneNum;

    /**
     * 已结束
     * */
    private Long doneNum;

}
