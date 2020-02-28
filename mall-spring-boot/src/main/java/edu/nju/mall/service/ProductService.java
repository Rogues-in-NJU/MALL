package edu.nju.mall.service;

import edu.nju.mall.entity.Product;
import edu.nju.mall.vo.ProductVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<ProductVO> getProductList(Pageable pageable);

    ProductVO getProductById(Long id);

    Long saveProduct(Product product);

    Page<ProductVO> getProductPage(Integer pageIndex, Integer pageSize);

    List<ProductVO> searchByProductName(String productName);

}
