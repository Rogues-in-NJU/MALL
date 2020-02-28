package edu.nju.mall.service;

import edu.nju.mall.repository.OrderRepository;
import edu.nju.mall.vo.OrderSummaryVO;
import edu.nju.mall.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 19:27
 */
public interface OrderService {
    long refund(long orderId);

    long finishRefund(long orderId);

    Page<OrderVO> getRefundingOrderList(Pageable pageable);

    Page<OrderVO> getOrderList(Pageable pageable, Long userId, Integer status, String startTime, String endTime);

    Page<OrderVO> getOrderList(Integer pageIndex, Integer pageSize, String openId);

    int generateOrder();

    OrderSummaryVO getSummaryInfo();
}
