package edu.nju.mall.controller;

import edu.nju.mall.common.ListResponse;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.dto.OrderDTO;
import edu.nju.mall.dto.UnifiedOrderDTO;
import edu.nju.mall.entity.Order;
import edu.nju.mall.entity.Product;
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

    @GetMapping(value = "refundOrderList")
    public ResultVO<ListResponse> refundOrderList(@RequestParam(value = "pageIndex") int pageIndex,
                                                  @RequestParam(value = "pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.ASC, "refundTime"));
        return ResultVO.ok(ListResponseUtils.generateResponse(orderService.getRefundingOrderList(pageable), pageIndex, pageSize));
    }

    @GetMapping(value = "refund/{id}")
    public ResultVO<Long> refund(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return ResultVO.ok(orderService.refund(id));
    }

    @GetMapping(value = "finishRefund/{id}")
    public ResultVO<Long> finishRefund(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return ResultVO.ok(orderService.finishRefund(id));
    }

    @GetMapping(value = "summaryInfo")
    public ResultVO<OrderSummaryVO> summaryInfo() {
        return ResultVO.ok(orderService.getSummaryInfo());
    }

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
    @PostMapping(value = "generateOrder")
    public ResultVO<String> generateOrder(@RequestBody OrderDTO orderDTO) {
        Order order = orderService.generateOrder(orderDTO);
        UnifiedOrderDTO unifiedOrderDTO = UnifiedOrderDTO.builder()
                .body(productService.getProduct(orderDTO.getProductId()).getName())
                .out_trade_no(String.valueOf(order.getOrderCode()))
                .total_fee(order.getPrice())
                .build();
        return ResultVO.ok(wechatPayService.unifiedOrder(unifiedOrderDTO));
    }

}
