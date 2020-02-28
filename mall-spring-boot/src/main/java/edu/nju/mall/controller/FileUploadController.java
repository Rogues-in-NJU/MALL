package edu.nju.mall.controller;

import edu.nju.mall.entity.ProductInfoImage;
import edu.nju.mall.service.FileService;
import edu.nju.mall.service.ProductImageService;
import edu.nju.mall.service.ProductInfoImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class FileUploadController {

    @Autowired
    FileService fileService;

    @Autowired
    ProductImageService productImageService;

    @Autowired
    ProductInfoImageService productInfoImageService;

    @RequestMapping("/upload-product-info")
    public Map<String, String> upload(@RequestParam("upload_file")MultipartFile file,
                                      @RequestParam("product_id")String productId) {

        Map<String, String> result = fileService.saveImage(file);

        //todo 绑定图片与产品
        ProductInfoImage productInfoImage = new ProductInfoImage();
        productInfoImage.setProductId(productId);
        productInfoImage.setImageLink(result.get("url"));

        productInfoImageService.saveProductInfoImage(productInfoImage);

        return result;
    }
}