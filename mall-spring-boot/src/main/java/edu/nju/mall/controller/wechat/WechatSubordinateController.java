package edu.nju.mall.controller.wechat;

import edu.nju.mall.common.ResultVO;
import edu.nju.mall.common.aop.InvokeControl;
import edu.nju.mall.common.aop.RoleControl;
import edu.nju.mall.dto.InfoSecurityUserDTO;
import edu.nju.mall.service.SubordinateService;
import edu.nju.mall.util.HttpSecurity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/wechat/api")
public class WechatSubordinateController {

    @Autowired
    private HttpSecurity httpSecurity;

    @Autowired
    private SubordinateService subordinateService;

    @InvokeControl
    @RoleControl({"user", "admin"})
    @GetMapping("/subordinate/list")
    public ResultVO<List<InfoSecurityUserDTO>> getSubordinateList() {
        Long userId = httpSecurity.getUserId();
        List<InfoSecurityUserDTO> subordinates = subordinateService.getSubordinates(userId);
        return ResultVO.ok(subordinates);
    }

    @InvokeControl
    @RoleControl({"admin", "user"})
    @GetMapping("/subordinate/add/{sharedUserId}")
    public ResultVO<Boolean> addSubordinate(@PathVariable("sharedUserId") final Long sharedUserId) {
        Long userId = httpSecurity.getUserId();
        log.info("sharedUserId: {}, userId: {}", sharedUserId, userId);
        subordinateService.addSubordinate(sharedUserId, userId);
        return ResultVO.ok(true);
    }

}
