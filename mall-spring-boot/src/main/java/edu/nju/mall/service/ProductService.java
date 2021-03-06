package edu.nju.mall.service;

import edu.nju.mall.entity.Product;
import edu.nju.mall.vo.ProductVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Page<ProductVO> getProductList(Pageable pageable);

    ProductVO getProductById(Long id);

    Long addProduct(Product product);

    Long updateProduct(Product product);

    Product getProduct(Long id);

    Page<ProductVO> getProductPage(Integer pageIndex, Integer pageSize);

    List<ProductVO> searchByProductName(String productName);

    Integer deleteProductById(Long productId);

}
