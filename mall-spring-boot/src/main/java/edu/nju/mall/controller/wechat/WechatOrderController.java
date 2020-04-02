package edu.nju.mall.controller.wechat;

import edu.nju.mall.common.ListResponse;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.common.aop.InvokeControl;
import edu.nju.mall.common.aop.RoleControl;
import edu.nju.mall.entity.Order;
import edu.nju.mall.service.OrderService;
import edu.nju.mall.util.HttpSecurity;
import edu.nju.mall.util.ListResponseUtils;
import edu.nju.mall.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/wechat/api")
public class WechatOrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpSecurity httpSecurity;

    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping("/order/list")
    public ResultVO<ListResponse> getOrderList(@RequestParam(value = "pageIndex", defaultValue = "1") final Integer pageIndex,
                                               @RequestParam(value = "pageSize", defaultValue = "10") final Integer pageSize,
                                               @RequestParam(value = "status", required = false) final Integer status) {
        Long userId = httpSecurity.getUserId();
        Page<OrderVO> orders = orderService.getOrderList(pageIndex - 1, pageSize, status, userId);
        return ResultVO.ok(ListResponseUtils.generateResponse(orders, pageIndex, pageSize));
    }

    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping("/order/search")
    public ResultVO<List<OrderVO>> searchOrder(@RequestParam(value = "productName") final String productName,
                                               @RequestParam(value = "status", required = false) final Integer status) {
        Long userId = httpSecurity.getUserId();
        List<OrderVO> results = orderService.searchByProductName(productName, status, userId);
        return ResultVO.ok(results);
    }

    /**
     * 获取订单信息
     *
     * @param id
     * @return
     */
    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping(value = "/order/{id}")
    public ResultVO<Order> orderInfo(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return ResultVO.ok(orderService.getOrder(id));
    }

}
