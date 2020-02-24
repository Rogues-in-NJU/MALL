package edu.nju.mall.service.Impl;

import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.NJUException;
import edu.nju.mall.entity.Order;
import edu.nju.mall.entity.OrderProduct;
import edu.nju.mall.enums.OrderStatus;
import edu.nju.mall.repository.OrderProductRepository;
import edu.nju.mall.repository.OrderRepository;
import edu.nju.mall.service.OrderService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 19:27
 */
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;

    private Set<Integer> refundSet = new HashSet<Integer>() {
        {
            add(OrderStatus.TODO.getCode());
            add(OrderStatus.FINISHED.getCode());
        }
    };

    @Override
    @Transactional
    public int refund(int orderId) {
        Order order = orderRepository.getOne(orderId);
        if (order == null) {
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "没有找到该订单!");
        }
        if (!refundSet.contains(order.getStatus())) {
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "该订单目前状态无法申请退款!");
        }
        order.setStatus(OrderStatus.REFUNDING.getCode());
        List<OrderProduct> orderProductList = orderProductRepository.findAllByOrderId(order.getId());
        orderProductList.forEach(orderProduct -> {
            //todo 修改商品库存
        });
        return 0;
    }
}
