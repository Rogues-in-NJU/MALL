package edu.nju.mall.service.Impl;

import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.NJUException;
import edu.nju.mall.entity.Classification;
import edu.nju.mall.enums.ClassificationStatus;
import edu.nju.mall.repository.ClassificationRepository;
import edu.nju.mall.service.ClassificationService;
import edu.nju.mall.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 14:32
 */
@Service
public class ClassificationServiceImpl implements ClassificationService {
    @Autowired
    private ClassificationRepository classificationRepository;

    /**
     * 目前分类的保存 固定是4个，只能更新，无法新增。
     * @param classification
     * @return
     */
    @Override
    public Integer save(Classification classification) {
        classification.setUpdatedAt(DateUtils.getTime());
        if (classification.getId() == null) {
            throw new NJUException(ExceptionEnum.ILLEGAL_REQUEST,"分类未找到！");
        } else {
            return classificationRepository.save(classification).getId();
        }
    }

    @Override
    public List<Classification> getClassificationList() {
        return classificationRepository.findAll();
    }


}
