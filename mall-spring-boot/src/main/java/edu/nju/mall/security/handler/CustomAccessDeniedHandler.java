package edu.nju.mall.security.handler;

import edu.nju.mall.common.ExceptionEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 删除框架抛出的异常，改为自定义异常
        request.removeAttribute("org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR");
        throw ExceptionEnum.AUTH_FAIL.exception("没有访问权限!");
    }
}
