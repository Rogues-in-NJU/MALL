package edu.nju.mall.controller;

import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.ListResponse;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.common.aop.InvokeControl;
import edu.nju.mall.dto.OrderDTO;
import edu.nju.mall.dto.UnifiedOrderResponseDTO;
import edu.nju.mall.entity.Order;
import edu.nju.mall.service.OrderService;
import edu.nju.mall.service.ProductService;
import edu.nju.mall.service.WechatPayService;
import edu.nju.mall.util.ListResponseUtils;
import edu.nju.mall.vo.OrderSummaryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 14:20
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    ProductService productService;
    @Autowired
    WechatPayService wechatPayService;

    @InvokeControl
    @GetMapping(value = "refundOrderList")
    public ResultVO<ListResponse> refundOrderList(@RequestParam(value = "pageIndex") int pageIndex,
                                                  @RequestParam(value = "pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.ASC, "refundTime"));
        return ResultVO.ok(ListResponseUtils.generateResponse(orderService.getRefundingOrderList(pageable), pageIndex, pageSize));
    }


    @InvokeControl
    @GetMapping(value = "refund/{id}")
    public ResultVO<Long> refund(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return ResultVO.ok(orderService.refund(id));
    }

    @InvokeControl
    @GetMapping(value = "cancel/{id}")
    public ResultVO<Long> cancel(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return ResultVO.ok(orderService.cancelOrder(id));
    }

    @InvokeControl
    @GetMapping(value = "finishRefund/{id}")
    public ResultVO<Long> finishRefund(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return ResultVO.ok(orderService.finishRefund(id));
    }

    @InvokeControl
    @GetMapping(value = "summaryInfo")
    public ResultVO<OrderSummaryVO> summaryInfo() {
        return ResultVO.ok(orderService.getSummaryInfo());
    }

    @InvokeControl
    @GetMapping(value = "orderList")
    public ResultVO<ListResponse> orderList(@RequestParam(value = "pageIndex") int pageIndex,
                                            @RequestParam(value = "pageSize") int pageSize,
                                            @RequestParam(value = "status", required = false) Integer status,
                                            @RequestParam(value = "startTime", required = false) String startTime,
                                            @RequestParam(value = "endTime", required = false) String endTime) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResultVO.ok(ListResponseUtils.generateResponse(orderService.getOrderList(pageable, null, status, startTime, endTime), pageIndex, pageSize));
    }


    /**
     * 获取订单信息
     *
     * @param id
     * @return
     */
    @InvokeControl
    @GetMapping(value = "orderInfo/{id}")
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
    @PostMapping(value = "generateOrder")
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
//    @InvokeControl
//    @GetMapping(value = "pay/{id}")
//    public ResultVO<UnifiedOrderResponseDTO> pay(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
//        UnifiedOrderResponseDTO result = orderService.pay(id);
//        if (Objects.nonNull(result.getReturn_code()) && result.getReturn_code().equals("SUCCESS")) {
//            return ResultVO.ok(result);
//        }
//        return ResultVO.fail(ExceptionEnum.ILLEGAL_PARAM, "请求支付失败！");
//    }

}
