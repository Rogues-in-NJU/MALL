package edu.nju.mall.util;

import edu.nju.mall.entity.Order;
import edu.nju.mall.entity.Product;
import edu.nju.mall.entity.UserInfo;
import edu.nju.mall.enums.OrderStatus;
import edu.nju.mall.service.OrderService;
import edu.nju.mall.service.ProductService;
import edu.nju.mall.service.SubordinateService;
import edu.nju.mall.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-03-29 21:14
 */
@Slf4j
@Component
@Configuration
@EnableScheduling
public class ScheduledUtils {
    @Autowired
    OrderService orderService;
    @Autowired
    ProductService productService;
    @Autowired
    private SubordinateService subordinateService;
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 每小时过滤掉未支付的订单
     */
    @Transactional
    @Scheduled(cron = "0 0 * * * ?")
    public void checkOverDue() {
        List<Order> orderList = orderService.getAllUnPayOrder();
        log.info("未支付订单数量:{}", orderList.size());
        orderList.forEach(o -> {
            if (DateUtils.payOverDue(o.getCreatedAt())) {
                o.setStatus(OrderStatus.ABANDON.getCode());
                Product product = productService.getProduct(o.getProductId());
                product.setQuantity(product.getQuantity() + o.getNum());
                productService.updateProduct(product);
                orderService.updateOrder(o);
            }
        });
    }

    /**
     * 每天0点过滤掉未完成的订单,更新用户提成
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkOrderOverDue() {
        List<Order> orderList = orderService.getAllUnfinishedOrder();
        orderList.forEach(o -> {
            if (DateUtils.orderOverDue(o.getStartTime())) {
                boolean hasLeader = subordinateService.check(o.getUserId());
                Product product = productService.getProduct(o.getProductId());
                if (hasLeader) {
                    UserInfo userInfo = userInfoService.findUserInfoEntity(subordinateService.getLeaderId(o.getUserId()));
                    userInfo.setWithdrawal((userInfo.getWithdrawal() + (long) product.getPercent() * product.getPrice()));
                    userInfoService.saveUserInfo(userInfo);
                }
                o.setStatus(OrderStatus.FINISHED.getCode());
                orderService.updateOrder(o);
            }
        });
    }
}
