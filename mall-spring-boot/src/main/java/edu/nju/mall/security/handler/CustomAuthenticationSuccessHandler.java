package edu.nju.mall.security.handler;

import cn.hutool.http.ContentType;
import com.alibaba.fastjson.JSON;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.security.WechatMicroProgramAuthenticationToken;
import edu.nju.mall.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        WechatMicroProgramAuthenticationToken wechatToken = (WechatMicroProgramAuthenticationToken) authentication;
        String token = jwtUtils.createToken(wechatToken.getOpenid());
        ResultVO<String> result = ResultVO.ok(token);
        response.setContentType(ContentType.JSON.toString(StandardCharsets.UTF_8));
        response.getWriter().write(JSON.toJSONString(result));
    }

}
