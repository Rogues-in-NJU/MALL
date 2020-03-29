package edu.nju.mall.service;

import edu.nju.mall.entity.ProductInfoImage;

import java.util.List;

public interface ProductInfoImageService {

    List<ProductInfoImage> getProductInfoImagesByProductId(Long productId);

    Long saveProductInfoImage(ProductInfoImage productInfoImage);

    Long deleteProductInfoImage(Long productInfoImageId);

    void deleteProductImageByImageLink(String imageLink);
}
