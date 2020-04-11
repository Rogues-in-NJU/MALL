package edu.nju.mall.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 13:52
 */

@Data
@Builder
@Entity
@Table(name = "withdrawal_record")
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户Id
     */
    private Long userId;

    private String userNickName;

    /**
     * 提现时间
     */
    @Column(name = "withdrawal_time")
    private String withdrawalTime;

    /**
     * 提现金额
     */
    private Long cash;

    /**
     * 状态（0：待处理，1：已完成）
     */
    private int status;
}
