package edu.nju.mall.controller;

import edu.nju.mall.common.ResultVO;
import edu.nju.mall.entity.Classification;
import edu.nju.mall.service.ClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 作用描述
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 14:21
 */
@RestController
@RequestMapping("/api/classification")
public class ClassificationController {
    @Autowired
    private ClassificationService classificationService;

    @PostMapping(value = "")
    public ResultVO<Integer> save(@RequestBody Classification classification) {
        Integer id = classificationService.save(classification);
        return ResultVO.ok(id);
    }

    @GetMapping(value = "/list")
    public ResultVO<List<Classification>> getClassificationList() {
        List<Classification> classificationList = classificationService.getClassificationList();
        return ResultVO.ok(classificationList);
    }
}
