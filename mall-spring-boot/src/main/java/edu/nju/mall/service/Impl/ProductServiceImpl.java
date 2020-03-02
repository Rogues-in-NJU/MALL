package edu.nju.mall.service.Impl;

import com.google.common.base.Preconditions;
import edu.nju.mall.conditionSqlQuery.ConditionFactory;
import edu.nju.mall.conditionSqlQuery.QueryContainer;
import edu.nju.mall.entity.Order;
import edu.nju.mall.entity.Product;
import edu.nju.mall.entity.ProductImage;
import edu.nju.mall.entity.ProductInfoImage;
import edu.nju.mall.enums.OrderStatus;
import edu.nju.mall.repository.ProductRepository;
import edu.nju.mall.service.ProductImageService;
import edu.nju.mall.service.ProductInfoImageService;
import edu.nju.mall.service.ProductService;
import edu.nju.mall.vo.OrderVO;
import edu.nju.mall.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductInfoImageService productInfoImageService;

    @Autowired
    ProductImageService productImageService;

    @Override
    public Page<ProductVO> getProductList(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductVO> productVOS = new ArrayList<>();
        productPage.getContent().forEach(product ->{
            productVOS.add(transfer(product));
        });
        return new PageImpl<>(productVOS);
    }

    @Override
    public ProductVO getProductById(Long id) {
        return transfer(productRepository.getOne(id.intValue()));
    }

    @Override
    public Long saveProduct(Product product) {
        return productRepository.save(product).getId();
    }

    public Page<ProductVO> getProductPage(Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "sellStartTime"));

        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductVO> productVOS = new ArrayList<>();
        productPage.getContent().forEach(product ->{
            productVOS.add(transfer(product));
        });

        return new PageImpl<>(productVOS);
    }

    @Override
    public List<ProductVO> searchByProductName(String productName) {
        Preconditions.checkNotNull(productName);

        List<Product> products = productRepository.findAll();

        List<ProductVO> productVOS = new ArrayList<>();
        products.forEach(product ->{
            productVOS.add(transfer(product));
        });

        return productVOS.stream().filter(o -> o.getName() != null && o.getName().contains(productName)).collect(Collectors.toList());
    }

    private ProductVO transfer(Product product){
        ProductVO productVO = ProductVO.builder()
                .build();
        try {
            List<String> productImagesAddresses =
                    productImageService.getProductImagesByProductId(product.getId()).stream().map(ProductImage::getImageLink).collect(Collectors.toList());
            List<String> productInfoImagesAddresses =
                    productInfoImageService.getProductInfoImagesByProductId(product.getId()).stream().map(ProductInfoImage::getImageLink).collect(Collectors.toList());

            productVO.setImageAddresses(productImagesAddresses);
            productVO.setImageInfoAddresses(productInfoImagesAddresses);
            BeanUtils.copyProperties(product, productVO);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return productVO;
    }
}
