package edu.nju.mall.controller;

import edu.nju.mall.common.ResultVO;
import edu.nju.mall.common.aop.InvokeControl;
import edu.nju.mall.service.CoverService;
import edu.nju.mall.vo.Cover;
import edu.nju.mall.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cover")
public class CoverController {

    @Autowired
    CoverService coverService;

    @InvokeControl
    @GetMapping("info")
    public ResultVO<Cover> getCover(){
        return ResultVO.ok(coverService.getCover());
    }
}
