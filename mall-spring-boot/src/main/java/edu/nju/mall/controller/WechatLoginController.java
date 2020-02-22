package edu.nju.mall.controller;

import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.common.aop.InvokeControl;
import edu.nju.mall.common.aop.RoleControl;
import edu.nju.mall.service.WechatLoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequestMapping("/wechat/api")
@RestController
public class WechatLoginController {

    @Autowired
    private WechatLoginService wechatLoginService;

    @InvokeControl
    @GetMapping("/login")
    public ResultVO<String> login(HttpServletRequest request) {
        String code = request.getParameter("code");
        if (StringUtils.isBlank(code)) {
            throw ExceptionEnum.ILLEGAL_PARAM.exception("code is empty");
        }
        String rawData = request.getParameter("rawData");
        if (StringUtils.isBlank(rawData)) {
            log.error("rawData is empty");
            throw ExceptionEnum.ILLEGAL_PARAM.exception("rawData is empty");
        }
        String signature = request.getParameter("signature");
        if (StringUtils.isBlank(signature)) {
            log.error("signature is empty");
            throw ExceptionEnum.ILLEGAL_PARAM.exception("signature is empty");
        }
        String token = wechatLoginService.login(code, rawData, signature);
        return ResultVO.ok(token);
    }

    @InvokeControl
    @RoleControl({"admin"})
    @GetMapping("/version")
    public ResultVO<String> version() {
        return ResultVO.ok("1.0");
    }

}
