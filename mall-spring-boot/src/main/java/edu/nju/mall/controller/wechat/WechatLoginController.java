package edu.nju.mall.controller.wechat;

import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.common.RoleEnum;
import edu.nju.mall.common.aop.InvokeControl;
import edu.nju.mall.common.aop.RoleControl;
import edu.nju.mall.service.WechatLoginService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/wechat/api")
@RestController
public class WechatLoginController {

    @Autowired
    private WechatLoginService wechatLoginService;

    @InvokeControl
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultVO<String> login(@RequestBody final LoginCommand command) {
        String code = command.getCode();
        if (StringUtils.isBlank(code)) {
            throw ExceptionEnum.ILLEGAL_PARAM.exception("code is empty");
        }
        String rawData = command.getRawData();
        if (StringUtils.isBlank(rawData)) {
            log.error("rawData is empty");
            throw ExceptionEnum.ILLEGAL_PARAM.exception("rawData is empty");
        }
        String signature = command.getSignature();
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

    @Data
    private static class LoginCommand {
        private String code;
        private String rawData;
        private String signature;
    }

}
