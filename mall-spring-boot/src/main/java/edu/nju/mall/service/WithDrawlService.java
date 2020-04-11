package edu.nju.mall.service;

import edu.nju.mall.entity.WithdrawalCondition;
import edu.nju.mall.entity.WithdrawalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 19:28
 */
public interface WithDrawlService {
    Integer saveCondition(WithdrawalCondition withdrawalCondition);

    Integer saveRecord(WithdrawalRecord withdrawalRecord);

    Integer applyWithdrawal(Long userId, Long cash);

    WithdrawalRecord getRecordById(Integer id);

    WithdrawalCondition getWithdrawalCondition();

    List<WithdrawalRecord> getRecordList(Long userId);

    Page<WithdrawalRecord> getDoneRecordList(Pageable pageable, Long userId, String startTime, String endTime);

    Page<WithdrawalRecord> getTodoRecordList(Pageable pageable, Long userId, String startTime, String endTime);
}
