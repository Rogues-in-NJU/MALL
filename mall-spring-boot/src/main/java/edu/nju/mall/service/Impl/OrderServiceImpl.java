package edu.nju.mall.service.Impl;

import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.NJUException;
import edu.nju.mall.conditionSqlQuery.ConditionFactory;
import edu.nju.mall.conditionSqlQuery.QueryContainer;
import edu.nju.mall.entity.Order;
import edu.nju.mall.entity.OrderProduct;
import edu.nju.mall.enums.OrderStatus;
import edu.nju.mall.repository.OrderProductRepository;
import edu.nju.mall.repository.OrderRepository;
import edu.nju.mall.service.OrderService;
import edu.nju.mall.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 19:27
 */
@Service
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

    /**
     * 用户申请退款接口
     * @param orderId
     * @return
     */
    @Override
    @Transactional
    public int refund(int orderId) {
        Order order = orderRepository.getOne(orderId);
//        if (order == null) {
//            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "没有找到该订单!");
//        }
        if (!refundSet.contains(order.getStatus())) {
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "该订单目前状态无法申请退款!");
        }
        order.setStatus(OrderStatus.REFUNDING.getCode());
        List<OrderProduct> orderProductList = orderProductRepository.findAllByOrderId(order.getId());
        orderProductList.forEach(orderProduct -> {
            //todo 修改商品库存
        });
        return orderId;
    }

    @Override
    public int finishRefund(int orderId) {
        Order order = orderRepository.getOne(orderId);
        if(order.getStatus() != OrderStatus.REFUNDING.getCode()){
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "该订单目前状态无法退款!");
        }
        order.setStatus(OrderStatus.REFUNDED.getCode());
        orderRepository.save(order);
        return orderId;
    }


    @Override
    public Page<OrderVO> getRefundingOrderList(Pageable pageable) {
        QueryContainer<Order> sp = new QueryContainer<>();
        try {
            sp.add(ConditionFactory.equal("status", OrderStatus.REFUNDING.getCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Page<Order> orderPage = orderRepository.findAll(sp, pageable);
        List<Order> orderList = orderPage.getContent();
        List<OrderVO> orderVOList = new ArrayList<>();
        orderList.forEach(o -> {
            try {
                OrderVO orderVO = OrderVO.builder()
                        .status(OrderStatus.of(o.getStatus()).getMessage())
                        .build();
                BeanUtils.copyProperties(o,orderVO);
                orderVOList.add(orderVO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return new PageImpl<>(orderVOList);
    }
}
