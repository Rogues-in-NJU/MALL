package edu.nju.mall.controller;

import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.ListResponse;
import edu.nju.mall.common.NJUException;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.common.aop.InvokeControl;
import edu.nju.mall.dto.UserDTO;
import edu.nju.mall.entity.UserInfo;
import edu.nju.mall.entity.WithdrawalCondition;
import edu.nju.mall.entity.WithdrawalRecord;
import edu.nju.mall.enums.WithDrawlRecordStatus;
import edu.nju.mall.service.UserService;
import edu.nju.mall.service.WithDrawlService;
import edu.nju.mall.util.ListResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 14:21
 */
@RestController
@RequestMapping("/api/withdrawal")
public class WithdrawalController {
    @Autowired
    private WithDrawlService withDrawlService;
    @Autowired
    private UserService userService;

    @InvokeControl
    @PostMapping(value = "saveRecord")
    public ResultVO<Integer> saveRecord(@RequestBody WithdrawalRecord withdrawalRecord) {
        Integer id = withDrawlService.saveRecord(withdrawalRecord);
        return ResultVO.ok(id);
    }

    @InvokeControl
    @PostMapping(value = "saveCondition")
    public ResultVO<Integer> saveCondition(@RequestBody WithdrawalCondition withdrawalCondition) {
        Integer id = withDrawlService.saveCondition(withdrawalCondition);
        return ResultVO.ok(id);
    }

    @InvokeControl
    @Transactional
    @GetMapping(value = "withdrawal/{id}")
    public ResultVO<Integer> withdrawal(@NotNull(message = "id不能为空") @PathVariable("id") Integer id) {
        WithdrawalRecord withdrawalRecord = withDrawlService.getRecordById(id);
        withdrawalRecord.setStatus(WithDrawlRecordStatus.DONE.getCode());
        withDrawlService.saveRecord(withdrawalRecord);
        UserInfo userInfo = userService.findUserInfo(withdrawalRecord.getUserId());
        long remain = userInfo.getWithdrawal() - withdrawalRecord.getCash();
        if(remain < 0){
            throw new NJUException(ExceptionEnum.BUSINESS_FAIL,"账户余额不足！");
        }
        userInfo.setWithdrawal(remain);
        userService.saveUserInfo(userInfo);
        return ResultVO.ok(id);
    }

    @InvokeControl
    @GetMapping(value = "withdrawalCondition")
    public ResultVO<WithdrawalCondition> withdrawalCondition() {
        WithdrawalCondition withdrawalCondition = withDrawlService.getWithdrawalCondition();
        return ResultVO.ok(withdrawalCondition);
    }

    @InvokeControl
    @GetMapping(value = "todoRecordList")
    public ResultVO<ListResponse> todoRecordList(@RequestParam(value = "pageIndex") int pageIndex,
                                                 @RequestParam(value = "pageSize") int pageSize,
                                                 @RequestParam(value = "userId", required = false) Long userId,
                                                 @RequestParam(value = "createAtStartTime", required = false) String startTime,
                                                 @RequestParam(value = "createAtEndTime", required = false) String endTime) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.ASC, "withdrawalTime"));
        return ResultVO.ok(ListResponseUtils.generateResponse(withDrawlService.getTodoRecordList(pageable, userId, startTime, endTime), pageIndex, pageSize));
    }

    @InvokeControl
    @GetMapping(value = "doneRecordList")
    public ResultVO<ListResponse> doneRecordList(@RequestParam(value = "pageIndex") int pageIndex,
                                                 @RequestParam(value = "pageSize") int pageSize,
                                                 @RequestParam(value = "userId", required = false) Long userId,
                                                 @RequestParam(value = "createAtStartTime", required = false) String startTime,
                                                 @RequestParam(value = "createAtEndTime", required = false) String endTime) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.ASC, "withdrawalTime"));
        return ResultVO.ok(ListResponseUtils.generateResponse(withDrawlService.getDoneRecordList(pageable, userId, startTime, endTime), pageIndex, pageSize));
    }
}
