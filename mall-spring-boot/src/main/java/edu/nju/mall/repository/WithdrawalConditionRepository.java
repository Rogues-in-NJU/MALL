package edu.nju.mall.repository;

import edu.nju.mall.entity.Order;
import edu.nju.mall.entity.OrderProduct;
import edu.nju.mall.entity.WithdrawalCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 14:07
 */
public interface WithdrawalConditionRepository extends JpaRepository<WithdrawalCondition, Integer>, JpaSpecificationExecutor<WithdrawalCondition> {
}
