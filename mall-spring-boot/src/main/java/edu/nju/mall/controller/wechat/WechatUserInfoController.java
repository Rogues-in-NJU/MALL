package edu.nju.mall.controller.wechat;

import edu.nju.mall.common.ResultVO;
import edu.nju.mall.common.aop.InvokeControl;
import edu.nju.mall.common.aop.RoleControl;
import edu.nju.mall.dto.UserInfoDTO;
import edu.nju.mall.service.UserInfoService;
import edu.nju.mall.util.HttpSecurity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/wechat/api")
public class WechatUserInfoController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private HttpSecurity security;

    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping("/userInfo")
    public ResultVO<UserInfoDTO> getUserInfo() {
        System.out.println("UserInfoController : /userInfo");
        String openId = security.getUserOpenId();
        UserInfoDTO userInfo = userInfoService.findUserInfo(openId);
        System.out.println("UserInfoController : /userinfo -> " + userInfo.toString());
        return ResultVO.ok(userInfo);
    }

}
