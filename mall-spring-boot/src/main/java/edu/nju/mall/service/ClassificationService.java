package edu.nju.mall.service;

import edu.nju.mall.entity.Classification;

import java.util.List;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 14:32
 */
public interface ClassificationService {
    Integer save(Classification classification);

    List<Classification> getClassificationList();

}
