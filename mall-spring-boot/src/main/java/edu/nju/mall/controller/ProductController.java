package edu.nju.mall.controller;

import edu.nju.mall.common.ResultVO;
import edu.nju.mall.entity.Product;
import edu.nju.mall.entity.WithdrawalCondition;
import edu.nju.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping(value = "save")
    public ResultVO<Integer> saveProduct(@RequestBody Product product) {
        Long id = productService.saveProduct(product);
        return ResultVO.ok(id.intValue());
    }

}
