package edu.nju.mall.service.Impl;

import com.google.common.base.Preconditions;
import edu.nju.mall.conditionSqlQuery.ConditionFactory;
import edu.nju.mall.conditionSqlQuery.QueryContainer;
import edu.nju.mall.entity.Order;
import edu.nju.mall.entity.ProductImage;
import edu.nju.mall.repository.ProductImageRepository;
import edu.nju.mall.service.ProductImageService;
import edu.nju.mall.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    ProductImageRepository productImageRepository;

    @Override
    public List<ProductImage> getProductImagesByProductId(Long productId) {
        Preconditions.checkNotNull(productId);

        QueryContainer<ProductImage> sp = new QueryContainer<>();
        try {
            sp.add(ConditionFactory.equal("productId", productId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        List<ProductImage> productImages = productImageRepository.findAll(sp);
        return productImages;
    }

    @Override
    public Long saveProductImage(ProductImage productImage) {
        return productImageRepository.save(productImage).getId();
    }

    @Override
    public int deleteProductImage(Integer productImageId) {
        return 0;
    }

}
