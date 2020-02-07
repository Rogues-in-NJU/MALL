package edu.nju.mall.repository;

import edu.nju.mall.entity.WithdrawalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 14:08
 */
public interface WithdrawalRecordRepository extends JpaRepository<WithdrawalRecord, Integer>, JpaSpecificationExecutor<WithdrawalRecord> {
}
