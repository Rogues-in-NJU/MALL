package edu.nju.mall.security;

import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.WechatSession;
import edu.nju.mall.service.WechatApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author hermc
 */
@Slf4j
public class WechatMicroProgramAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Value("${wechat.micro-program.app-id}")
    private String appId;
    @Value("${wechat.micro-program.app-secret}")
    private String appSecret;

    private final String grantType = "authorization_code";

    @Autowired
    private WechatApiService wechatApiService;

    public WechatMicroProgramAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String code = httpServletRequest.getParameter("code");
        if (StringUtils.isBlank(code)) {
            log.error("code is empty");
            throw ExceptionEnum.ILLEGAL_PARAM.exception("code is empty");
        }
        String rawData = httpServletRequest.getParameter("rawData");
        if (StringUtils.isBlank(rawData)) {
            log.error("rawData is empty");
            throw ExceptionEnum.ILLEGAL_PARAM.exception("rawData is empty");
        }
        String signature = httpServletRequest.getParameter("signature");
        if (StringUtils.isBlank(signature)) {
            log.error("signature is empty");
            throw ExceptionEnum.ILLEGAL_PARAM.exception("signature is empty");
        }
        WechatSession wechatSession = wechatApiService.jscode2Session(appId, appSecret, code, grantType);
        if (Objects.nonNull(wechatSession.getErrcode()) && !wechatSession.getErrcode().equals(WechatSession.WechatSessionCode.SUCCESS.getCode())) {
            log.error("wechat auth failed, errcode is [{}], errmsg is [{}]", wechatSession.getErrcode(), wechatSession.getErrmsg());
            throw ExceptionEnum.ILLEGAL_USER.exception("微信登录失败!");
        }
        log.info("wechat user [{}] login", wechatSession.getOpenid());
        return this.getAuthenticationManager()
                .authenticate(new WechatMicroProgramAuthenticationToken(wechatSession.getOpenid(), wechatSession.getSession_key(), rawData, signature));
                // .authenticate(new WechatMicroProgramAuthenticationToken("testopenid", "testsessionkey", rawData, signature));
    }

}
