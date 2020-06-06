package edu.nju.mall.controller;

import edu.nju.mall.common.ListResponse;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.common.aop.InvokeControl;
import edu.nju.mall.entity.CoverImage;
import edu.nju.mall.entity.ProductImage;
import edu.nju.mall.entity.ProductInfoImage;
import edu.nju.mall.service.CoverService;
import edu.nju.mall.service.FileService;
import edu.nju.mall.service.ProductImageService;
import edu.nju.mall.service.ProductInfoImageService;
import edu.nju.mall.util.ListResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    CoverService coverService;

    @InvokeControl
    @RequestMapping("/upload-product-image")
    public Map<String, String> upload(@RequestParam("upload_file")MultipartFile file,
                                      @RequestParam("product_id")Long productId) {

        Map<String, String> result = fileService.saveImage(file);

        ProductImage productImage = new ProductImage();
        productImage.setProductId(productId);
        productImage.setImageLink(result.get("url"));

        Long id = productImageService.saveProductImage(productImage);
        result.put("uid", id.toString());
        return result;
    }

    @InvokeControl
    @RequestMapping("/upload-product-info")
    public Map<String, String> uploadInfo(@RequestParam("upload_file")MultipartFile file,
                                      @RequestParam("product_id")Long productId) {

        Map<String, String> result = fileService.saveInfoImage(file);

        ProductInfoImage productInfoImage = new ProductInfoImage();
        productInfoImage.setProductId(productId);
        productInfoImage.setImageLink(result.get("url"));

        Long id = productInfoImageService.saveProductInfoImage(productInfoImage);
        result.put("uid", id.toString());
        return result;
    }

    @InvokeControl
    @RequestMapping("/upload-cover-image")
    public Map<String, String> uploadCoverImage(@RequestParam("upload_file")MultipartFile file) {

        Map<String, String> result = fileService.saveCoverImage(file);

        CoverImage coverImage = CoverImage.builder().imageLink(result.get("url")).build();

        Long id = coverService.saveCoverImage(coverImage);
        result.put("uid", id.toString());
        return result;
    }


    @GetMapping("deleteImage/{productImageId}")
    public ResultVO<Integer> deleteProductImage(@PathVariable("productImageId") Long productImageId){
        return ResultVO.ok(productImageService.deleteProductImage(productImageId).intValue());

    }

    @GetMapping("deleteInfoImage/{productInfoImageId}")
    public ResultVO<Integer> deleteProductInfoImage(@PathVariable("productInfoImageId") Long productInfoImageId){
        return ResultVO.ok(productInfoImageService.deleteProductInfoImage(productInfoImageId).intValue());

    }

    @GetMapping("deleteImageByImageLink/{imageLink}")
    public ResultVO<Integer> deleteProductImageByImageLink(@PathVariable("imageLink") String imageLink){
        productImageService.deleteProductImageByImageLink(imageLink);
        return ResultVO.ok(0);
    }

    @GetMapping("deleteInfoImageByImageLink/{imageLink}")
    public ResultVO<Integer> deleteProductInfoImageByImageLink(@PathVariable("imageLink") String imageLink){
        productInfoImageService.deleteProductImageByImageLink(imageLink);
        return ResultVO.ok(0);
    }

    @GetMapping("deleteCoverImage/{coverImageId}")
    public ResultVO<Integer> deleteCoverImage(@PathVariable("coverImageId") Long coverImageId){
        return ResultVO.ok(coverService.deleteCoverImage(coverImageId).intValue());

    }

    @GetMapping("deleteCoverImageByImageLink/{imageLink}")
    public ResultVO<Integer> deleteCoverImageByImageLink(@PathVariable("imageLink") String imageLink){
        coverService.deleteCoverImageByImageLink(imageLink);
        return ResultVO.ok(0);
    }

}