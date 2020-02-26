package edu.nju.mall.repository;

import edu.nju.mall.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductImageRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {
}
