package edu.nju.mall.controller.wechat;

import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.ListResponse;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.common.aop.InvokeControl;
import edu.nju.mall.common.aop.RoleControl;
import edu.nju.mall.dto.OrderDTO;
import edu.nju.mall.dto.UnifiedOrderDTO;
import edu.nju.mall.dto.UnifiedOrderResponseDTO;
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
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/wechat/api/order")
public class WechatOrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpSecurity httpSecurity;

    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping("/list")
    public ResultVO<ListResponse> getOrderList(@RequestParam(value = "pageIndex", defaultValue = "1") final Integer pageIndex,
                                               @RequestParam(value = "pageSize", defaultValue = "10") final Integer pageSize,
                                               @RequestParam(value = "status", required = false) final Integer status) {
        Long userId = httpSecurity.getUserId();
        Page<OrderVO> orders = orderService.getOrderList(pageIndex - 1, pageSize, status, userId);
        return ResultVO.ok(ListResponseUtils.generateResponse(orders, pageIndex, pageSize));
    }

    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping("/search")
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
    @GetMapping(value = "/{id}")
    public ResultVO<Order> orderInfo(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return ResultVO.ok(orderService.getOrder(id));
    }

    /**
     * 生成订单，对应商品库存不够会失败
     *
     * @param orderDTO
     * @return
     */
    @InvokeControl
    @RoleControl({"user", "admin"})
    @PostMapping(value = "/generate")
    public ResultVO<Long> generateOrder(@RequestBody OrderDTO orderDTO) {
        Order order = orderService.generateOrder(orderDTO);
        return ResultVO.ok(order.getId());
    }

    /**
     * 支付接口，传order的id
     *
     * @param id
     * @return
     */
    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping(value = "/pay/{id}")
    public ResultVO<UnifiedOrderResponseDTO> pay(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        UnifiedOrderResponseDTO result = orderService.pay(id);
        if (Objects.nonNull(result.getReturn_code()) && result.getReturn_code().equals("SUCCESS")) {
            return ResultVO.ok(result);
        }
        return ResultVO.fail(ExceptionEnum.ILLEGAL_PARAM, "请求支付失败！");
    }

    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping(value = "/updateOrderStatus")
    public ResultVO<Long> updateOrderStatus(@RequestParam(value = "id") Long id,
                                            @RequestParam(value = "status") Integer status) {
        Order order = orderService.getOrder(id);
        order.setStatus(status);
        return ResultVO.ok(orderService.updateOrder(order));
    }

    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping(value = "/finishPay/{id}")
    public ResultVO<Boolean> finishPay(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return ResultVO.ok(orderService.finishPay(id));
    }

    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping(value = "/refund/{id}")
    public ResultVO<Long> refund(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return ResultVO.ok(orderService.refund(id));
    }

    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping(value = "/finishRefund/{id}")
    public ResultVO<Long> finishRefund(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return ResultVO.ok(orderService.finishRefund(id));
    }

    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping(value = "/cancel/{id}")
    public ResultVO<Long> cancel(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return ResultVO.ok(orderService.cancelOrder(id));
    }

}
