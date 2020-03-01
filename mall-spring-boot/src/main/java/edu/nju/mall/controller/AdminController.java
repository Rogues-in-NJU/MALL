package edu.nju.mall.controller;

import edu.nju.mall.common.ResultVO;
import edu.nju.mall.dto.LoginDTO;
import edu.nju.mall.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 管理员
 * @Author: qianen.yin
 * @CreateDate: 2020-03-01 17:56
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminUserService adminUserService;

    @PostMapping(value = "login")
    public ResultVO<Boolean> login(@RequestBody LoginDTO loginDTO) {
        return ResultVO.ok(adminUserService.checkLogin(loginDTO.getPhoneNumber(), loginDTO.getPassword()));
    }
}
