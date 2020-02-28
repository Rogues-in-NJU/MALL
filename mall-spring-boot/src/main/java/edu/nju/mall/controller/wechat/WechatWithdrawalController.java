package edu.nju.mall.controller.wechat;

import edu.nju.mall.common.ListResponse;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.common.aop.InvokeControl;
import edu.nju.mall.common.aop.RoleControl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/wechat/api")
public class WechatWithdrawalController {

    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping("/withdrawal/history/list")
    public ResultVO<ListResponse> getWithdrawalHistoryList(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.ASC, "withdrawalTime"));

        return null;
    }

    @InvokeControl
    @RoleControl({"user", "admin"})
    @PostMapping("/withdrawal/apply")
    public ResultVO<Long> applyWithdrawal(@RequestBody WithdrawalApplyCommand command) {
        return null;
    }

    @Data
    public static class WithdrawalApplyCommand {

        private Long applyValue;

    }

}
