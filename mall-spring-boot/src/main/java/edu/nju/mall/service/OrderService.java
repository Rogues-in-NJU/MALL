package edu.nju.mall.service;

import edu.nju.mall.dto.OrderDTO;
import edu.nju.mall.dto.UnifiedOrderResponseDTO;
import edu.nju.mall.entity.Order;
import edu.nju.mall.vo.OrderSummaryVO;
import edu.nju.mall.vo.OrderVO;
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

    long cancelOrder(long orderId);

    List<Order> getAllUnPayOrder();

    Page<OrderVO> getRefundingOrderList(Pageable pageable);

    Page<OrderVO> getOrderList(Pageable pageable, Long userId, Integer status, String startTime, String endTime);

    Page<OrderVO> getOrderList(Integer pageIndex, Integer pageSize, Integer status, Long userId);

    List<OrderVO> searchByProductName(String productName, Integer status, Long userId);

    OrderSummaryVO getSummaryInfo();

    Order generateOrder(OrderDTO orderDTO);

    long updateOrder(Order order);

    Order getOrder(long id);

    UnifiedOrderResponseDTO pay(Long id);

    Boolean finishPay(Long id);
}
