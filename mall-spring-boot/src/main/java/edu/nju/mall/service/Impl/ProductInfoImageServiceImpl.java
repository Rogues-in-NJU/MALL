package edu.nju.mall.service.Impl;

import com.google.common.base.Preconditions;
import edu.nju.mall.conditionSqlQuery.ConditionFactory;
import edu.nju.mall.conditionSqlQuery.QueryContainer;
import edu.nju.mall.entity.ProductInfoImage;
import edu.nju.mall.repository.ProductInfoImageRepository;
import edu.nju.mall.service.ProductInfoImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class ProductInfoImageServiceImpl  implements ProductInfoImageService {
    @Autowired
    ProductInfoImageRepository productInfoImageRepository;

    @Override
    public List<ProductInfoImage> getProductInfoImagesByProductId(Long productId) {
        Preconditions.checkNotNull(productId);

        QueryContainer<ProductInfoImage> sp = new QueryContainer<>();
        try {
            sp.add(ConditionFactory.equal("productId", productId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        List<ProductInfoImage> productInfoImages = productInfoImageRepository.findAll(sp);
        return productInfoImages;
    }

    @Override
    public Long saveProductInfoImage(ProductInfoImage productInfoImage) {
        return productInfoImageRepository.save(productInfoImage).getId();
    }

    @Override
    public Long deleteProductInfoImage(Long productInfoImageId) {
        productInfoImageRepository.deleteById(productInfoImageId);
        return 0l;
    }

    @Transactional
    @Override
    public void deleteProductImageByImageLink(String imageLink) {
        //todo "/"临时做法
        productInfoImageRepository.deleteProductInfoImagesByImageLink("/" + imageLink);
    }
}
