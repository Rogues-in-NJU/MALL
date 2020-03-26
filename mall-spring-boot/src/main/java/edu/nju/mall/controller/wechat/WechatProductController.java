package edu.nju.mall.controller.wechat;

import edu.nju.mall.common.ListResponse;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.common.aop.InvokeControl;
import edu.nju.mall.common.aop.RoleControl;
import edu.nju.mall.service.ProductService;
import edu.nju.mall.util.HttpSecurity;
import edu.nju.mall.util.ListResponseUtils;
import edu.nju.mall.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/wechat/api")
public class WechatProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private HttpSecurity httpSecurity;

    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping("/product/list")
    public ResultVO<ListResponse> getProductList(@RequestParam(value = "pageIndex", defaultValue = "1") final Integer pageIndex,
                                               @RequestParam(value = "pageSize", defaultValue = "10") final Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.ASC, "createdAt"));
        return ResultVO.ok(ListResponseUtils.generateResponse(productService.getProductList(pageable), pageIndex, pageSize));
    }

    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping("/product/search")
    public ResultVO<List<ProductVO>> searchProduct(@RequestParam(value = "productName") final String productName) {
        List<ProductVO> results = productService.searchByProductName(productName);
        return ResultVO.ok(results);
    }

    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping("/product/get")
    public ResultVO<ProductVO> getProductById(@RequestParam(value = "id") final long id) {
        ProductVO results = productService.getProductById(id);
        return ResultVO.ok(results);
    }

}
