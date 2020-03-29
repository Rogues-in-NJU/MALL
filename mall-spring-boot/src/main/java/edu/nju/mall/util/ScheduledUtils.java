package edu.nju.mall.util;

import edu.nju.mall.entity.Order;
import edu.nju.mall.entity.Product;
import edu.nju.mall.enums.OrderStatus;
import edu.nju.mall.service.OrderService;
import edu.nju.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-03-29 21:14
 */
@Component
public class ScheduledUtils {
    @Autowired
    OrderService orderService;
    @Autowired
    ProductService productService;

    /**
     * 每小时过滤掉未支付的订单
     */
    @Transactional
    @Scheduled(cron = "0 0 * * * ?")
    public void checkOverDue() {
        List<Order> orderList = orderService.getAllUnPayOrder();
        orderList.forEach(o -> {
            boolean overDue = DateUtils.overDue(o.getCreatedAt());
            if (overDue) {
                o.setStatus(OrderStatus.ABANDON.getCode());
                Product product = productService.getProduct(o.getProductId());
                product.setQuantity(product.getQuantity() + o.getNum());
                productService.updateProduct(product);
            }
        });
    }
}
