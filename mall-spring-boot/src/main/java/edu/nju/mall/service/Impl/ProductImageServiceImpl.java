package edu.nju.mall.service.Impl;

import edu.nju.mall.entity.ProductImage;
import edu.nju.mall.repository.ProductImageRepository;
import edu.nju.mall.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    ProductImageRepository productImageRepository;

    @Override
    public List<ProductImage> getProductImagesByProductId(Long productId) {
        return null;
    }

    @Override
    public Integer saveProductImage(ProductImage productImage) {
        return productImageRepository.save(productImage).getId();
    }

}
