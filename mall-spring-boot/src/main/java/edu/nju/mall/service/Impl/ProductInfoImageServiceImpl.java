package edu.nju.mall.service.Impl;

import edu.nju.mall.entity.ProductInfoImage;
import edu.nju.mall.repository.ProductInfoImageRepository;
import edu.nju.mall.service.ProductInfoImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoImageServiceImpl  implements ProductInfoImageService {
    @Autowired
    ProductInfoImageRepository productInfoImageRepository;

    @Override
    public List<ProductInfoImage> getProductInfoImagesByProductId(Long productId) {
        return null;
    }

    @Override
    public Integer saveProductInfoImage(ProductInfoImage productInfoImage) {
        return productInfoImageRepository.save(productInfoImage).getId();
    }
}
