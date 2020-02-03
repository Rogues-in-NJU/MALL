package edu.nju.mall.service.Impl;

import edu.nju.mall.entity.OrderProduct;
import edu.nju.mall.repository.OrderProductRepository;
import edu.nju.mall.repository.OrderRepository;
import edu.nju.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

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


}
