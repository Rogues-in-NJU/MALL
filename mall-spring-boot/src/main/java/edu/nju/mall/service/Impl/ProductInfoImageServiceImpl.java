package edu.nju.mall.service.Impl;

import edu.nju.mall.entity.ProductInfoImage;
import edu.nju.mall.service.ProductInfoImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoImageServiceImpl  implements ProductInfoImageService {
    @Override
    public List<ProductInfoImage> getProductInfoImagesByProductId(Long productId) {
        return null;
    }
}
