package edu.nju.mall.controller;

import edu.nju.mall.common.ListResponse;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.entity.Product;
import edu.nju.mall.service.ProductService;
import edu.nju.mall.util.ListResponseUtils;
import edu.nju.mall.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping(value = "save")
    public ResultVO<Integer> saveProduct(@RequestBody Product product) {
        Long id = productService.addProduct(product);
        return ResultVO.ok(id.intValue());
    }

    @PostMapping(value = "update")
    public ResultVO<Integer> updateProduct(@RequestBody Product product) {
        Long id = productService.updateProduct(product);
        return ResultVO.ok(id.intValue());
    }


    @GetMapping("list")
    public ResultVO<ListResponse> getProductList(@RequestParam(value = "pageIndex") int pageIndex,
                                                 @RequestParam(value = "pageSize") int pageSize){
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.ASC, "createdAt"));
        return ResultVO.ok(ListResponseUtils.generateResponse(productService.getProductList(pageable), pageIndex, pageSize));

    }

    @GetMapping("info/{productId}")
    public ResultVO<ProductVO> getProduct(@PathVariable("productId") long productId){
        return ResultVO.ok(productService.getProductById(productId));
    }

    @GetMapping("delete/{productId}")
    public ResultVO<Integer> deleteProduct(@PathVariable("productId") long productId){
        return ResultVO.ok(productService.deleteProductById(productId));
    }

}
