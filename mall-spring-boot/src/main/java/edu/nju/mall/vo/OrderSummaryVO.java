package edu.nju.mall.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-28 20:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummaryVO {
    /**
     * 总收入（订单状态为已完成的）
     */
    private double totalInCome;

    /**
     * 订单总数量（除去退款成功以及已废弃）
     */
    private int totalDeal;

    /**
     * 未成功订单总数量（退款成功以及已废弃）
     */
    private int totalBadDeal;
    /**
     * 下面三个字段为当天的情况
     */
    private double todayInCome;

    private int todayDeal;

    private int todayBadDeal;
}
