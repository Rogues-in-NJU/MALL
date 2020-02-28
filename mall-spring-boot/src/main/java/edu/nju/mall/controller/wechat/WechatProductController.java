package edu.nju.mall.controller.wechat;

import edu.nju.mall.common.ListResponse;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.service.ProductService;
import edu.nju.mall.util.HttpSecurity;
import edu.nju.mall.util.ListResponseUtils;
import edu.nju.mall.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/wechat/api")
public class WechatProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private HttpSecurity httpSecurity;

    @GetMapping("/product/list")
    public ResultVO<ListResponse> getProductList(@RequestParam(value = "pageIndex", defaultValue = "1") final Integer pageIndex,
                                               @RequestParam(value = "pageSize", defaultValue = "10") final Integer pageSize) {
        Page<ProductVO> products = productService.getProductPage(pageIndex,pageSize);
        return ResultVO.ok(ListResponseUtils.generateResponse(products, pageIndex, pageSize));
    }

}