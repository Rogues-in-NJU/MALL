package edu.nju.mall.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.google.common.base.Preconditions;
import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.NJUException;
import edu.nju.mall.conditionSqlQuery.ConditionFactory;
import edu.nju.mall.conditionSqlQuery.QueryContainer;
import edu.nju.mall.dto.*;
import edu.nju.mall.entity.Order;
import edu.nju.mall.entity.OrderSecurity;
import edu.nju.mall.entity.Product;
import edu.nju.mall.entity.UserInfo;
import edu.nju.mall.enums.OrderStatus;
import edu.nju.mall.repository.OrderRepository;
import edu.nju.mall.repository.OrderSecurityRepository;
import edu.nju.mall.service.*;
import edu.nju.mall.util.DateUtils;
import edu.nju.mall.util.HttpSecurity;
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

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private WechatPayService wechatPayService;
    @Autowired
    private OrderSecurityRepository orderSecurityRepository;
    @Autowired
    private SubordinateService subordinateService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private HttpSecurity httpSecurity;

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
        updateQuantity(order, OrderStatus.REFUNDED.getCode());
        boolean hasLeader = subordinateService.check(order.getUserId());
        Product product = productService.getProduct(order.getProductId());
        if (product == null) {
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "商品信息获取失败！");
        }
        product.setQuantity(product.getQuantity() + order.getNum());
        product.setSaleVolume(product.getSaleVolume() - order.getNum());
        if (hasLeader) {
            UserInfo userInfo = userInfoService.findUserInfoEntity(subordinateService.getLeaderId(order.getUserId()));
            userInfo.setWithdrawal((userInfo.getWithdrawal() - (long) product.getPercent() * product.getPrice()));
            userInfoService.saveUserInfo(userInfo);
        }
        return orderId;
    }

    @Override
    public long cancelOrder(long orderId) {
        Order order = orderRepository.getOne(orderId);
        if (order.getStatus() != OrderStatus.PAYING.getCode()) {
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "该订单已完成支付，无法取消!");
        }
        updateQuantity(order, OrderStatus.ABANDON.getCode());
        return orderId;
    }

    private void updateQuantity(Order order, int status) {
        Product product = productService.getProduct(order.getProductId());
        if (product == null) {
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "订单异常!");
        }
        product.setQuantity(product.getQuantity() + order.getNum());
        order.setStatus(status);
        orderRepository.save(order);
        productService.updateProduct(product);
    }

    @Override
    public List<Order> getAllUnPayOrder() {
        QueryContainer<Order> sp = new QueryContainer<>();
        try {
            sp.add(ConditionFactory.equal("status", OrderStatus.PAYING.getCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderRepository.findAll(sp);
    }

    @Override
    public List<Order> getAllUnfinishedOrder() {
        QueryContainer<Order> sp = new QueryContainer<>();
        try {
            sp.add(ConditionFactory.equal("status", OrderStatus.TODO.getCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderRepository.findAll(sp);
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

    @Transactional
    @Override
    public Order generateOrder(OrderDTO orderDTO) {
        Order order = Order.builder()
                .userId(httpSecurity.getUserId())
                .createdAt(DateUtils.getTime())
                .orderCode(snowflake.nextId())
                .status(OrderStatus.PAYING.getCode())
                .build();
        BeanUtils.copyProperties(orderDTO, order);
        Product product = productService.getProduct(orderDTO.getProductId());
        if (product == null) {
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "错误的商品Id！");
        }
        int remain = product.getQuantity() - orderDTO.getNum();
        if (remain < 0) {
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "库存不够，下单失败！");
        }
        product.setQuantity(remain);
        productService.updateProduct(product);
        order.setPrice(product.getPrice() * orderDTO.getNum());
        Long orderId = orderRepository.saveAndFlush(order).getId();
        order.setId(orderId);
        return order;
    }

    @Override
    public long updateOrder(Order order) {
        return orderRepository.save(order).getId();
    }

    @Override
    public Order getOrder(long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public UnifiedOrderResponseDTO pay(Long id) {
        Order order = getOrder(id);
        if (order.getStatus() != OrderStatus.PAYING.getCode()) {
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "该订单已支付或被废弃！");
        }
        UnifiedOrderDTO unifiedOrderDTO = UnifiedOrderDTO.builder()
                .body(productService.getProduct(order.getProductId()).getName())
                .out_trade_no(String.valueOf(order.getOrderCode()))
                .total_fee(order.getPrice())
                .openid(httpSecurity.getUserOpenId())
                .build();
        UnifiedOrderResponseDTO unifiedOrderResponseDTO = wechatPayService.unifiedOrder(unifiedOrderDTO);
        log.info("ans:{}", unifiedOrderResponseDTO);

        if ("FAIL".equals(unifiedOrderResponseDTO.getReturn_code())
                || "FAIL".equals(unifiedOrderResponseDTO.getResult_code())) {
            log.error("微信下单失败: return_msg {}, err_code_des: {}", unifiedOrderResponseDTO.getReturn_msg(), unifiedOrderResponseDTO.getErr_code_des());
            throw new NJUException(ExceptionEnum.SERVER_ERROR, "微信下单失败!");
        }

        OrderSecurity orderSecurity = OrderSecurity.builder()
                .nonceStr(unifiedOrderResponseDTO.getNonce_str())
                .sign(unifiedOrderResponseDTO.getSign())
                .prepayId(unifiedOrderResponseDTO.getPrepay_id())
                .orderCode(order.getOrderCode())
                .orderId(order.getId())
                .build();

        orderSecurityRepository.save(orderSecurity);

        return unifiedOrderResponseDTO;
    }


    @Override
    @Transactional
    /**
     * 支付完成只更新订单和商品信息，上级用户的提成在订单完成后更新
     */
    public Boolean finishPay(Long id) {
        Order order = getOrder(id);
        if (order == null) {
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "未找到该订单！");
        }
        if (order.getStatus() != OrderStatus.PAYING.getCode()) {
            return true;
        }
        OrderQueryResponseDTO orderQueryResponseDTO = wechatPayService.orderQuery(String.valueOf(order.getOrderCode()));

        if (orderQueryResponseDTO == null) {
            throw new NJUException(ExceptionEnum.SERVER_ERROR, "支付失败!");
        }
        if ("FAIL".equals(orderQueryResponseDTO.getReturn_code())) {
            throw new NJUException(ExceptionEnum.SERVER_ERROR, orderQueryResponseDTO.getReturn_msg());
        }
        if ("FAIL".equals(orderQueryResponseDTO.getResult_code())) {
            throw new NJUException(ExceptionEnum.SERVER_ERROR, "支付失败!");
        }

        order.setTransactionNumber(orderQueryResponseDTO.getTransaction_id());
        order.setStatus(OrderStatus.TODO.getCode());
        order.setPayTime(DateUtils.getTime());
        order.setUpdatedAt(DateUtils.getTime());
        updateOrder(order);
        Product product = productService.getProduct(order.getProductId());
        if (product == null) {
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "商品信息获取失败！");
        }
        if (product.getQuantity() < order.getNum()) {
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST, "商品库存不够，支付失败！");
        }
        //更新商品数据（库存和销量）
        product.setQuantity(product.getQuantity() - order.getNum());
        product.setSaleVolume(product.getSaleVolume() + order.getNum());
        productService.updateProduct(product);
        return true;
    }

    @Override
    public Boolean finishPay(@Nonnull Map<String, Object> payResult) {
        try {
            if (!wechatPayService.checkSign(payResult, String.valueOf(payResult.get("sign")))) {
                return false;
            }
            PayResultDTO payResultDTO = new PayResultDTO();
            BeanUtil.copyProperties(payResult, payResultDTO);

            if ("FAIL".equals(payResultDTO.getReturn_code()) || "FAIL".equals(payResultDTO.getResult_code())) {
                return false;
            }

            Order order = orderRepository.findByOrderCode(Long.parseLong(payResultDTO.getOut_trade_no())).orElse(null);
            order.setTransactionNumber(payResultDTO.getTransaction_id());
            order.setStatus(OrderStatus.TODO.getCode());
            updateOrder(order);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
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
