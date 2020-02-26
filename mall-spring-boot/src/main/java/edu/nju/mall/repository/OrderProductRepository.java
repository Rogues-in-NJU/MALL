package edu.nju.mall.repository;

import edu.nju.mall.entity.Order;
import edu.nju.mall.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 14:06
 */
public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer>, JpaSpecificationExecutor<OrderProduct> {

    List<OrderProduct> findAllByOrderId(Long orderId);
}
