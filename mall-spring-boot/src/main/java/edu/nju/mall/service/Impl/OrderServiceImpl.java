package edu.nju.mall.service.Impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.google.common.base.Preconditions;
import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.NJUException;
import edu.nju.mall.conditionSqlQuery.ConditionFactory;
import edu.nju.mall.conditionSqlQuery.QueryContainer;
import edu.nju.mall.dto.UserDTO;
import edu.nju.mall.entity.Order;
import edu.nju.mall.enums.OrderStatus;
import edu.nju.mall.repository.OrderRepository;
import edu.nju.mall.service.OrderService;
import edu.nju.mall.service.ProductService;
import edu.nju.mall.service.UserService;
import edu.nju.mall.util.DateUtils;
import edu.nju.mall.vo.OrderSummaryVO;
import edu.nju.mall.vo.OrderVO;
import edu.nju.mall.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 19:27
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;

    private List<Integer> dealStatus = new ArrayList<Integer>() {{
        add(OrderStatus.TODO.getCode());
        add(OrderStatus.FINISHED.getCode());
    }};

    private List<Integer> badDealStatus = new ArrayList<Integer>() {{
        add(OrderStatus.REFUNDING.getCode());
        add(OrderStatus.REFUNDED.getCode());
//        add(OrderStatus.ABANDON.getCode());
    }};

    private Snowflake snowflake = IdUtil.getSnowflake(1, 1);

    private DecimalFormat df = new DecimalFormat("0.00");

    /**
     * 用户申请退款接口
     *
     * @param orderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long refund(long orderId) {
        Order order = orderRepository.getOne(orderId);
//        if (order == null) {
//            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "没有找到该订单!");
//        }
        if (!dealStatus.contains(order.getStatus())) {
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "该订单目前状态无法申请退款!");
        }
        order.setStatus(OrderStatus.REFUNDING.getCode());
        return orderId;
    }

    @Override
    public long finishRefund(long orderId) {
        Order order = orderRepository.getOne(orderId);
        if (order.getStatus() != OrderStatus.REFUNDING.getCode()) {
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
        return new PageImpl<>(transfer(orderPage.getContent()));
    }

    @Override
    public Page<OrderVO> getOrderList(Pageable pageable, Long userId, Integer status, String startTime, String endTime) {
        QueryContainer<Order> sp = new QueryContainer<>();
        try {
            if (userId != null) {
                sp.add(ConditionFactory.equal("userId", userId));
            }
            if (status != null) {
                sp.add(ConditionFactory.equal("status", status));
            }
            if (startTime != null) {
                sp.add(ConditionFactory.greatThanEqualTo("createdAt", startTime));
            }
            if (endTime != null) {
                sp.add(ConditionFactory.lessThanEqualTo("createdAt", endTime));
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        Page<Order> orderPage = orderRepository.findAll(sp, pageable);
        return new PageImpl<>(transfer(orderPage.getContent()));
    }

    @Override
    public Page<OrderVO> getOrderList(Integer pageIndex, Integer pageSize, Integer status, Long userId) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return this.getOrderList(pageable, userId, status, null, null);
    }

    @Override
    public List<OrderVO> searchByProductName(String productName, Integer status, Long userId) {
        Preconditions.checkNotNull(productName);

        QueryContainer<Order> sp = new QueryContainer<>();
        try {
            sp.add(ConditionFactory.equal("userId", userId));
            if (status != null) {
                sp.add(ConditionFactory.equal("status", status));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        List<Order> orders = orderRepository.findAll(sp);
        List<OrderVO> orderVOS = transfer(orders);
        return orderVOS.stream().filter(o -> o.getProductName() != null && o.getProductName().contains(productName)).collect(Collectors.toList());
    }

    private List<OrderVO> transfer(List<Order> orderList) {
        List<OrderVO> orderVOList = new ArrayList<>();
        orderList.forEach(o -> {
            try {
                OrderVO orderVO = OrderVO.builder()
                        .build();
                ProductVO productVO = productService.getProductById(o.getProductId());
                if (productVO != null) {
                    List<String> productImg = productVO.getImageAddresses();
                    if (!CollectionUtils.isEmpty(productImg)) {
                        orderVO.setProductImage(productImg.get(0));
                    }
                    orderVO.setProductName(productVO.getName());
                }
                orderVO.setStatusName(OrderStatus.of(o.getStatus()).getMessage());
                BeanUtils.copyProperties(o, orderVO);
                orderVOList.add(orderVO);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
        return orderVOList;
    }

    @Override
    public int generateOrder() {
        // todo 接收OrderDTO待创建，order信息

        Order order = Order.builder().orderCode(snowflake.nextId()).build();
        //todo 插入order表，修改product库存
        return 0;
    }

    @Override
    public OrderSummaryVO getSummaryInfo() {
        List<Order> totalDeal = getOrder(false, null);
        List<Order> todayDeal = getOrder(false, DateUtils.getToday());
        List<Order> totalBadDeal = getOrder(true, null);
        List<Order> todayBadDeal = getOrder(true, DateUtils.getToday());
        int totalIncome = totalDeal.stream().mapToInt(Order::getPrice).sum();
        int todayIncome = todayDeal.stream().mapToInt(Order::getPrice).sum();
        //单位是分，下方需转换为元
        OrderSummaryVO orderSummaryVO = OrderSummaryVO.builder()
                .totalInCome(Double.valueOf(df.format((float) totalIncome / 100)))
                .totalDeal(totalDeal.size())
                .totalBadDeal(totalBadDeal.size())
                .todayInCome(Double.valueOf(df.format((float) todayIncome / 100)))
                .todayDeal(todayDeal.size())
                .todayBadDeal(todayBadDeal.size())
                .build();
        return orderSummaryVO;
    }

    /**
     * @param date
     * @return
     */
    private List<Order> getOrder(boolean isBad, String date) {
        QueryContainer<Order> sp = new QueryContainer<>();
        try {
            sp.add(ConditionFactory.In("status", isBad ? badDealStatus : dealStatus));
            if (date != null) {
                //因为当天所以只考虑下界
                sp.add(ConditionFactory.greatThanEqualTo("payTime", date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderRepository.findAll(sp);
    }

}
