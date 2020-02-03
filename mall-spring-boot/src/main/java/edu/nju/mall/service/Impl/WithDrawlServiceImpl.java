package edu.nju.mall.service.Impl;

import edu.nju.mall.conditionSqlQuery.ConditionFactory;
import edu.nju.mall.conditionSqlQuery.QueryContainer;
import edu.nju.mall.entity.WithdrawalCondition;
import edu.nju.mall.entity.WithdrawalRecord;
import edu.nju.mall.enums.WithDrawlRecordStatus;
import edu.nju.mall.repository.WithdrawalConditionRepository;
import edu.nju.mall.repository.WithdrawalRecordRepository;
import edu.nju.mall.service.WithDrawlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 19:28
 */
@Slf4j
public class WithDrawlServiceImpl implements WithDrawlService {
    @Autowired
    private WithdrawalConditionRepository withdrawalConditionRepository;
    @Autowired
    private WithdrawalRecordRepository withdrawalRecordRepository;


    @Override
    public void updateCondition(WithdrawalCondition withdrawalCondition) {
        withdrawalConditionRepository.save(withdrawalCondition);
    }

    @Override
    public void addRecord(WithdrawalRecord withdrawalRecord) {
        withdrawalRecordRepository.save(withdrawalRecord);
    }

    @Override
    public Page<WithdrawalRecord> getDoneRecordList(Pageable pageable, String userId, String startTime, String endTime) {
        QueryContainer<WithdrawalRecord> sp = new QueryContainer<>();
        try {
            sp.add(ConditionFactory.equal("status", WithDrawlRecordStatus.DONE.getCode()));
            if (userId != null) {
                sp.add(ConditionFactory.like("userId", userId));
            }
            if (!"".equals(startTime)) {
                sp.add(ConditionFactory.greatThanEqualTo("withdrawalTime", startTime));
            }
            if (!"".equals(endTime)) {
                sp.add(ConditionFactory.lessThanEqualTo("withdrawalTime", endTime));
            }
        } catch (Exception e) {
            log.error("Value is null", e);
        }
        return withdrawalRecordRepository.findAll(sp, pageable);
    }

    @Override
    public Page<WithdrawalRecord> getTodoRecordList(Pageable pageable) {
        QueryContainer<WithdrawalRecord> sp = new QueryContainer<>();
        try {
            sp.add(ConditionFactory.equal("status", WithDrawlRecordStatus.TODO.getCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return withdrawalRecordRepository.findAll(sp, pageable);
    }
}
