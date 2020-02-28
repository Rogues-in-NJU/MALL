package edu.nju.mall.repository;

import edu.nju.mall.entity.Order;
import edu.nju.mall.entity.ProductInfoImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductInfoImageRepository extends JpaRepository<ProductInfoImage, Integer>, JpaSpecificationExecutor<ProductInfoImage> {
}
