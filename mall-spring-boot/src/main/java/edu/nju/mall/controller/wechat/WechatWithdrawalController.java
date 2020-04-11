package edu.nju.mall.controller.wechat;

import edu.nju.mall.common.ListResponse;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.common.aop.InvokeControl;
import edu.nju.mall.common.aop.RoleControl;
import edu.nju.mall.entity.WithdrawalRecord;
import edu.nju.mall.service.WithDrawlService;
import edu.nju.mall.util.HttpSecurity;
import edu.nju.mall.util.ListResponseUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/wechat/api")
public class WechatWithdrawalController {

    @Autowired
    private HttpSecurity httpSecurity;

    @Autowired
    private WithDrawlService withDrawlService;

    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping("/withdrawal/history/list")
    public ResultVO<List<WithdrawalRecord>> getWithdrawalHistoryList(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                     @RequestParam(value = "pageSize", defaultValue = "10000") Integer pageSize) {
        // Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.DESC, "withdrawalTime"));

        // Page<WithdrawalRecord> todoList = withDrawlService.getTodoRecordList(pageable, userId, null, null);
        // Page<WithdrawalRecord> doneList = withDrawlService.getDoneRecordList(pageable, userId, null, null);
        //
        // List<WithdrawalRecord> records = new LinkedList<>();
        // records.addAll(todoList.getContent());
        // records.addAll(doneList.getContent());

        Long userId = httpSecurity.getUserId();

        return ResultVO.ok(withDrawlService.getRecordList(userId));
    }

    @InvokeControl
    @RoleControl({"user", "admin"})
    @PostMapping("/withdrawal/apply")
    public ResultVO<Long> applyWithdrawal(@RequestBody WithdrawalApplyCommand command) {
        Long userId = httpSecurity.getUserId();
        return ResultVO.ok(withDrawlService.applyWithdrawal(userId, command.applyValue).longValue());
    }

    @Data
    public static class WithdrawalApplyCommand {

        private Long applyValue;

    }

}
