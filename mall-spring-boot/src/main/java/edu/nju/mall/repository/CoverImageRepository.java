package edu.nju.mall.repository;

import edu.nju.mall.entity.CoverImage;
import edu.nju.mall.entity.Product;
import edu.nju.mall.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoverImageRepository extends JpaRepository<CoverImage, Long>, JpaSpecificationExecutor<CoverImage> {

    @Modifying
    @Query("delete from CoverImage p where p.imageLink =:imageLink")
    void deleteCoverImagesByImageLink(@Param("imageLink")String imageLink);

    CoverImage findByImageLink(String imageLink);


}
