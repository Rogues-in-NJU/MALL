package edu.nju.mall.service.Impl;

import edu.nju.mall.entity.ProductImage;
import edu.nju.mall.service.ProductImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService {
    @Override
    public List<ProductImage> getProductImagesByProductId(Long productId) {
        return null;
    }
}
