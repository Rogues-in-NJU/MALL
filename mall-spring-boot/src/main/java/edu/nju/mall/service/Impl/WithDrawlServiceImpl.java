package edu.nju.mall.service.Impl;

import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.NJUException;
import edu.nju.mall.conditionSqlQuery.ConditionFactory;
import edu.nju.mall.conditionSqlQuery.QueryContainer;
import edu.nju.mall.dto.UserDTO;
import edu.nju.mall.dto.UserInfoDTO;
import edu.nju.mall.entity.WithdrawalCondition;
import edu.nju.mall.entity.WithdrawalRecord;
import edu.nju.mall.enums.WithDrawlRecordStatus;
import edu.nju.mall.repository.WithdrawalConditionRepository;
import edu.nju.mall.repository.WithdrawalRecordRepository;
import edu.nju.mall.service.UserInfoService;
import edu.nju.mall.service.UserService;
import edu.nju.mall.service.WithDrawlService;
import edu.nju.mall.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 19:28
 */
@Slf4j
@Service
public class WithDrawlServiceImpl implements WithDrawlService {

    @Autowired
    private WithdrawalConditionRepository withdrawalConditionRepository;
    @Autowired
    private WithdrawalRecordRepository withdrawalRecordRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;


    @Override
    public Integer saveCondition(WithdrawalCondition withdrawalCondition) {
        return withdrawalConditionRepository.save(withdrawalCondition).getId();
    }

    @Override
    public Integer saveRecord(WithdrawalRecord withdrawalRecord) {
        if (withdrawalRecord.getId() == null) {
            withdrawalRecord.setStatus(WithDrawlRecordStatus.TODO.getCode());
            withdrawalRecord.setWithdrawalTime(DateUtils.getTime());
            return withdrawalRecordRepository.saveAndFlush(withdrawalRecord).getId();
        } else {
            return withdrawalRecordRepository.save(withdrawalRecord).getId();
        }
    }

    @Override
    public Integer applyWithdrawal(@Nonnull Long userId, @Nonnull Long cash) {
        UserDTO userDTO = userService.findUser(userId);
        UserInfoDTO userInfoDTO = userInfoService.findUserInfo(userId);

        List<WithdrawalRecord> todoRecords = withdrawalRecordRepository.findByUserIdAndStatus(userId, 0);
        AtomicReference<Long> todoCash = new AtomicReference<>(cash);
        todoRecords.forEach(t -> todoCash.updateAndGet(v -> v + t.getCash()));

        if (userInfoDTO.getWithdrawal() <= todoCash.get()) {
            throw new NJUException(ExceptionEnum.BUSINESS_FAIL, "待处理金额已达上限!");
        }

        WithdrawalRecord withdrawalRecord = WithdrawalRecord.builder()
                .withdrawalTime(DateUtils.getTime())
                .cash(cash)
                .userId(userId)
                .userNickName(userDTO.getNickname())
                .status(0)
                .build();
        return withdrawalRecordRepository.save(withdrawalRecord).getId();
    }

    @Override
    public WithdrawalRecord getRecordById(Integer id) {
        return withdrawalRecordRepository.getOne(id);
    }

    @Override
    public WithdrawalCondition getWithdrawalCondition() {
        return withdrawalConditionRepository.getOne(1);
    }

    @Override
    public List<WithdrawalRecord> getRecordList(Long userId) {
        return withdrawalRecordRepository.findByUserIdOrderByWithdrawalTimeDesc(userId);
    }

    @Override
    public Page<WithdrawalRecord> getDoneRecordList(Pageable pageable, Long userId, String startTime, String endTime) {
        QueryContainer<WithdrawalRecord> sp = new QueryContainer<>();
        try {
            sp.add(ConditionFactory.equal("status", WithDrawlRecordStatus.DONE.getCode()));
            setQueryContainer(sp, userId, startTime, endTime);
        } catch (Exception e) {
            log.error("Value is null", e);
        }
        return withdrawalRecordRepository.findAll(sp, pageable);
    }

    @Override
    public Page<WithdrawalRecord> getTodoRecordList(Pageable pageable, Long userId, String startTime, String endTime) {
        QueryContainer<WithdrawalRecord> sp = new QueryContainer<>();
        try {
            sp.add(ConditionFactory.equal("status", WithDrawlRecordStatus.TODO.getCode()));
            setQueryContainer(sp, userId, startTime, endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return withdrawalRecordRepository.findAll(sp, pageable);
    }

    private void setQueryContainer(QueryContainer<WithdrawalRecord> sp, Long userId, String startTime, String endTime) {
        try {
            if (userId != null) {
                sp.add(ConditionFactory.equal("userId", userId));
            }
            if (startTime != null) {
                sp.add(ConditionFactory.greatThanEqualTo("withdrawalTime", startTime));
            }
            if (endTime != null) {
                sp.add(ConditionFactory.lessThanEqualTo("withdrawalTime", endTime));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
