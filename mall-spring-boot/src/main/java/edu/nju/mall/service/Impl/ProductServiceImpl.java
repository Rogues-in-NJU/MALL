package edu.nju.mall.service.Impl;

import edu.nju.mall.entity.ProductImage;
import edu.nju.mall.entity.ProductInfoImage;
import edu.nju.mall.repository.ProductRepository;
import edu.nju.mall.service.ProductImageService;
import edu.nju.mall.service.ProductInfoImageService;
import edu.nju.mall.service.ProductService;
import edu.nju.mall.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductInfoImageService productInfoImageService;

    @Autowired
    ProductImageService productImageService;

    @Override
    public List<ProductVO> getProductList(Pageable pageable) {
        return null;
    }

    @Override
    public ProductVO getProductById(Long id) {
        return null;
    }
}
