package edu.nju.mall.controller;

import edu.nju.mall.common.ListResponse;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.service.OrderService;
import edu.nju.mall.util.ListResponseUtils;
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

    @GetMapping(value = "refundOrderList")
    public ResultVO<ListResponse> refundOrderList(@RequestParam(value = "pageIndex") int pageIndex,
                                                  @RequestParam(value = "pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.ASC, "refundTime"));
        return ResultVO.ok(ListResponseUtils.generateResponse(orderService.getRefundingOrderList(pageable), pageIndex, pageSize));
    }

    @GetMapping(value = "refund/{id}")
    public ResultVO<Integer> refund(@NotNull(message = "id不能为空") @PathVariable("id") Integer id) {
        return ResultVO.ok(orderService.refund(id));
    }

    @GetMapping(value = "finishRefund/{id}")
    public ResultVO<Integer> finishRefund(@NotNull(message = "id不能为空") @PathVariable("id") Integer id) {
        return ResultVO.ok(orderService.finishRefund(id));
    }

}
