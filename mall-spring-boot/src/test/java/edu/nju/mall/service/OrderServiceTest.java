package edu.nju.mall.service;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import edu.nju.mall.entity.Order;
import edu.nju.mall.repository.OrderRepository;
import edu.nju.mall.repository.ProductRepository;
import edu.nju.mall.vo.OrderVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(value = {"dev", "devConn"})
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    Snowflake snowflake = IdUtil.getSnowflake(1, 1);

    @Test
    @Rollback
    public void testAdd() {
        Order order = Order.builder()
                .orderCode(snowflake.nextId())
                .userId("")
                .num(2)
                .price(2000)
                .consigneePhone("12345678999090")
                .status(0).build();
        orderRepository.save(order);
    }

    @Test
    public void test() {
        Page<OrderVO> orderVOS = orderService.getOrderList(0, 10, 1, 1L);
        System.out.println(orderVOS.getTotalElements());
    }

}
