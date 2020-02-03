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

    @Override
    public Integer save(Classification classification) {
        classification.setUpdatedTime(DateUtils.getTime());
        if (classification.getId() == null) {
            classification.setStatus(ClassificationStatus.USING.getCode());
            classification.setCreatedTime(DateUtils.getTime());
            return classificationRepository.saveAndFlush(classification).getId();
        } else {
            return classificationRepository.save(classification).getId();
        }
    }

    @Override
    public List<Classification> getClassificationList() {
        return classificationRepository.findAllByStatus(ClassificationStatus.USING.getCode());
    }

    @Override
    public List<Classification> getAbandonedClassificationList() {
        return classificationRepository.findAllByStatus(ClassificationStatus.ABANDON.getCode());
    }

    @Override
    public Integer delete(Classification classification) {
        if (classification.getId() == null) {
            throw new NJUException(ExceptionEnum.ILLEGAL_PARAM, "分类Id为空!");
        }
        classification.setDeletedTime(DateUtils.getTime());
        classification.setStatus(ClassificationStatus.ABANDON.getCode());
        return classificationRepository.save(classification).getId();
    }
}
