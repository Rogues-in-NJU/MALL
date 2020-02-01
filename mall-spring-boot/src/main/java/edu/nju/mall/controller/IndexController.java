package edu.nju.mall.controller;

import edu.nju.mall.common.ResultVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IndexController {

    @GetMapping("/version")
    @PreAuthorize("hasRole('user')")
    public ResultVO<String> version() {
        return ResultVO.ok("0.1");
    }

}
