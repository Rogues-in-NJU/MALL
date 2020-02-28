package edu.nju.mall.controller.wechat;

import edu.nju.mall.common.ListResponse;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.service.OrderService;
import edu.nju.mall.util.HttpSecurity;
import edu.nju.mall.util.ListResponseUtils;
import edu.nju.mall.vo.OrderVO;
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
public class WechatOrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpSecurity httpSecurity;

    @GetMapping("/order/list")
    public ResultVO<ListResponse> getOrderList(@RequestParam(value = "pageIndex", defaultValue = "1") final Integer pageIndex,
                                               @RequestParam(value = "pageSize", defaultValue = "10") final Integer pageSize) {
        String openId = httpSecurity.getUserOpenId();
        Page<OrderVO> orders = orderService.getOrderList(pageIndex - 1, pageSize, openId);
        return ResultVO.ok(ListResponseUtils.generateResponse(orders, pageIndex, pageSize));
    }

}
