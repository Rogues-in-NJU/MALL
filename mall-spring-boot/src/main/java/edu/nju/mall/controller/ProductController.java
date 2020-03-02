package edu.nju.mall.controller;

import edu.nju.mall.common.ListResponse;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.entity.Product;
import edu.nju.mall.entity.WithdrawalCondition;
import edu.nju.mall.service.ProductService;
import edu.nju.mall.util.ListResponseUtils;
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
        Long id = productService.saveProduct(product);
        return ResultVO.ok(id.intValue());
    }

    @GetMapping
    public ResultVO<ListResponse> getProductList(@RequestParam(value = "pageIndex") int pageIndex,
                                                 @RequestParam(value = "pageSize") int pageSize){
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.ASC, "sellStartTime"));
        return ResultVO.ok(ListResponseUtils.generateResponse(productService.getProductList(pageable), pageIndex, pageSize));

    }

}
