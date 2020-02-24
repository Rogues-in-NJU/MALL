package edu.nju.mall.controller;

import edu.nju.mall.common.ListResponse;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.entity.WithdrawalCondition;
import edu.nju.mall.entity.WithdrawalRecord;
import edu.nju.mall.enums.WithDrawlRecordStatus;
import edu.nju.mall.service.WithDrawlService;
import edu.nju.mall.util.ListResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @PostMapping(value = "saveRecord")
    public ResultVO<Integer> saveRecord(@RequestBody WithdrawalRecord withdrawalRecord) {
        Integer id = withDrawlService.saveRecord(withdrawalRecord);
        return ResultVO.ok(id);
    }

    @PostMapping(value = "saveCondition")
    public ResultVO<Integer> saveCondition(@RequestBody WithdrawalCondition withdrawalCondition) {
        Integer id = withDrawlService.saveCondition(withdrawalCondition);
        return ResultVO.ok(id);
    }

    @GetMapping(value = "withdrawal/{id}")
    public ResultVO<Integer> withdrawal(@NotNull(message = "id不能为空") @PathVariable("id") Integer id) {
        WithdrawalRecord withdrawalRecord = withDrawlService.getRecordById(id);
        withdrawalRecord.setStatus(WithDrawlRecordStatus.DONE.getCode());
        withDrawlService.saveRecord(withdrawalRecord);
        return ResultVO.ok(id);
    }

    @GetMapping(value = "todoRecordList")
    public ResultVO<ListResponse> todoRecordList(@RequestParam(value = "pageIndex") int pageIndex,
                                                 @RequestParam(value = "pageSize") int pageSize,
                                                 @RequestParam(value = "userId", required = false) String userId,
                                                 @RequestParam(value = "createAtStartTime", required = false) String startTime,
                                                 @RequestParam(value = "createAtEndTime", required = false) String endTime) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.ASC, "withdrawalTime"));
        return ResultVO.ok(ListResponseUtils.generateResponse(withDrawlService.getTodoRecordList(pageable, userId, startTime, endTime), pageIndex, pageSize));
    }

    @GetMapping(value = "doneRecordList")
    public ResultVO<ListResponse> doneRecordList(@RequestParam(value = "pageIndex") int pageIndex,
                                                 @RequestParam(value = "pageSize") int pageSize,
                                                 @RequestParam(value = "userId", required = false) String userId,
                                                 @RequestParam(value = "createAtStartTime", required = false) String startTime,
                                                 @RequestParam(value = "createAtEndTime", required = false) String endTime) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.ASC, "withdrawalTime"));
        return ResultVO.ok(ListResponseUtils.generateResponse(withDrawlService.getDoneRecordList(pageable, userId, startTime, endTime), pageIndex, pageSize));
    }
}
