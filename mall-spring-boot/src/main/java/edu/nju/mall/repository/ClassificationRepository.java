package edu.nju.mall.repository;

import edu.nju.mall.entity.Classification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 14:19
 */
public interface ClassificationRepository extends JpaRepository<Classification, Integer>, JpaSpecificationExecutor<Classification> {
    List<Classification> findAllByStatus(Integer status);
}
