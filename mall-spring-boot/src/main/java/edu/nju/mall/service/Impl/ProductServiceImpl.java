package edu.nju.mall.service.Impl;

import com.google.common.base.Preconditions;
import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.NJUException;
import edu.nju.mall.conditionSqlQuery.ConditionFactory;
import edu.nju.mall.conditionSqlQuery.QueryContainer;
import edu.nju.mall.entity.*;
import edu.nju.mall.enums.OrderStatus;
import edu.nju.mall.enums.ProductStatus;
import edu.nju.mall.repository.ProductRepository;
import edu.nju.mall.service.ClassificationService;
import edu.nju.mall.service.ProductImageService;
import edu.nju.mall.service.ProductInfoImageService;
import edu.nju.mall.service.ProductService;
import edu.nju.mall.util.DateUtils;
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

    @Autowired
    ClassificationService classificationService;

    @Override
    public Page<ProductVO> getProductList(Pageable pageable) {
        QueryContainer<Product> sp = new QueryContainer<>();
        try {
            sp.add(ConditionFactory.equal("status", ProductStatus.NORMAL.getCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Page<Product> productPage = productRepository.findAll(sp, pageable);
        List<ProductVO> productVOS = new ArrayList<>();
        productPage.getContent().forEach(product ->{
            productVOS.add(transfer(product));
        });
        return new PageImpl<>(productVOS);
    }

    @Override
    public ProductVO getProductById(Long id) {
        return transfer(productRepository.getOne(id));
    }

    @Override
    public Long addProduct(Product product) {
        try{
            product.setStatus(ProductStatus.NORMAL.getCode());
            product.setCreatedAt(DateUtils.getTime());
            return productRepository.save(product).getId();
        } catch (Exception e){
            throw new NJUException(ExceptionEnum.OTHER_ERROR, e.getMessage());
        }
    }

    @Override
    public Long updateProduct(Product product) {
        if(product == null || product.getId() == null){
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST,"没有id");
        }
        product.setUpdatedAt(DateUtils.getTime());
        productRepository.save(product);
        return product.getId();
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.getOne(id);
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

    @Override
    public Integer deleteProductById(Long productId) {
        Product product = productRepository.getOne(productId);
        if(product == null){
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST,"未找到该产品");
        }
        product.setDeletedAt(DateUtils.getTime());
        product.setStatus(ProductStatus.DELETED.getCode());
        productRepository.save(product);
        return 0;
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
            productVO.setClassificationName(getClassificationNameById(product.getClassificationId()));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return productVO;
    }

    private String getClassificationNameById(Integer classidicationId){
        List<Classification> classifications = classificationService.getClassificationList();
        for(Classification c : classifications){
            if(c.getId().equals(classidicationId)){
                return c.getName();
            }
        }
        return "";
    }
}
