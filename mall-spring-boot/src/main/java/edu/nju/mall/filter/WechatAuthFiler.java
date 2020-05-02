package edu.nju.mall.filter;

import com.alibaba.fastjson.JSON;
import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.common.ResponseWrapper;
import edu.nju.mall.common.ResultVO;
import edu.nju.mall.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class WechatAuthFiler extends OncePerRequestFilter {

    private JwtUtils jwtUtils;

    public WechatAuthFiler(final JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.debug("processing authentication for [{}]", request.getRequestURL());

        if (Objects.equals("/wechat/api/login", request.getRequestURI()) || request.getRequestURI().contains("/wechat/api/product")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(token)) {
            doNoAuth(response, "请登陆后再访问该接口");
            return;
        }
        String openid = jwtUtils.getInfo(token);
        if (Objects.isNull(openid)) {
            doNoAuth(response, "非法用户!");
        }
        if (jwtUtils.isTimeout(token)) {
            doNoAuth(response, "登录状态超时，请重新登录!");
        }
        filterChain.doFilter(request, response);
    }

    private void doNoAuth(HttpServletResponse response, String message) throws IOException {
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        responseWrapper.setStatus(200);
        responseWrapper.setHeader("Content-Type", "application/json;charset=UTF-8");

        String result = JSON.toJSONString(ResultVO.fail(ExceptionEnum.ILLEGAL_USER, message));
        responseWrapper.getOutputStream().write(result.getBytes());
        responseWrapper.getOutputStream().flush();
    }

}
