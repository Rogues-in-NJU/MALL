package edu.nju.mall.service;

import edu.nju.mall.entity.ProductImage;

import java.util.List;

public interface ProductImageService {
    List<ProductImage> getProductImagesByProductId(Long productId);

    Long saveProductImage(ProductImage productImage);

    Long deleteProductImage(Long productImageId);

    void deleteProductImageByImageLink(String imageLink);
}
