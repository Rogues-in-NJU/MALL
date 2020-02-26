package edu.nju.mall.service;

import edu.nju.mall.entity.Product;
import edu.nju.mall.vo.ProductVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<ProductVO> getProductList(Pageable pageable);

    ProductVO getProductById(Long id);

}
