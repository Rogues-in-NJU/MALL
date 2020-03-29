package edu.nju.mall.repository;

import edu.nju.mall.entity.Order;
import edu.nju.mall.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long>, JpaSpecificationExecutor<ProductImage> {

    @Modifying
    @Query("delete from ProductImage p where p.imageLink =:imageLink")
    void deleteProductImagesByImageLink(@Param("imageLink")String imageLink);

    ProductImage findByImageLink(String imageLink);
}
