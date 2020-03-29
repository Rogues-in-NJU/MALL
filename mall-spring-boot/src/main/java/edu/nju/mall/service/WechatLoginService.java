package edu.nju.mall.service;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.alibaba.fastjson.JSON;
import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.WechatSession;
import edu.nju.mall.dto.UserDTO;
import edu.nju.mall.entity.UserInfo;
import edu.nju.mall.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class WechatLoginService {

    @Value("${wechat.micro-program.app-id}")
    private String appId;
    @Value("${wechat.micro-program.app-secret}")
    private String appSecret;

    private final String grantType = "authorization_code";

    @Autowired
    private WechatApiService wechatApiService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private JwtUtils jwtUtils;

    public String login(String code, String rawData, String signature) {
        log.info("Login code: {}", code);
        WechatSession wechatSession = wechatApiService.jscode2Session(appId, appSecret, code, grantType);
        if (Objects.nonNull(wechatSession.getErrcode())
                && !Objects.equals(wechatSession.getErrcode(), WechatSession.WechatSessionCode.SUCCESS.getCode())) {
            log.error("wechat auth failed, errcode is [{}], errmsg is [{}]", wechatSession.getErrcode(), wechatSession.getErrmsg());
            throw ExceptionEnum.ILLEGAL_USER.exception("微信登录失败!");
        }
        if (!userService.isUserExisted(wechatSession.getOpenid())) {
            log.debug("user account not exist, begin to register. openid is [{}]", wechatSession.getOpenid());
            log.debug("rawData is {}", rawData);
            // 微信签名校验
            Digester digester = new Digester(DigestAlgorithm.SHA1);
            String data = rawData + wechatSession.getSession_key();
            String digestedSignature = digester.digestHex(data);
            if (!digestedSignature.equals(signature)) {
                log.error("signature is invalid, got: [{}] vs cal: [{}]", signature, digestedSignature);
                throw ExceptionEnum.ILLEGAL_USER.exception("用户签名不正确，请确认账号安全!");
            }
            UserDTO userDTO = JSON.parseObject(rawData, UserDTO.class);
            userDTO.setOpenid(wechatSession.getOpenid());
            userDTO.setSessionKey(wechatSession.getSession_key());
            log.info("user account is [{}]", userDTO);
            userDTO = userService.register(userDTO);
            // 需要初始化一下用户的其他信息
            UserInfo userInfo = userInfoService.initUserInfo(userDTO.getId());
        } else {
            UserDTO userDTO = userService.findUser(wechatSession.getOpenid());
            userDTO.setSessionKey(wechatSession.getSession_key());
            userDTO = userService.saveUser(userDTO);
        }
        return jwtUtils.createToken(wechatSession.getOpenid());
    }

}
