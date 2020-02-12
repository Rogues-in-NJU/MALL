package edu.nju.mall.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component
public class HttpSecurity {

    @Autowired
    private JwtUtils jwtUtils;

    public HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
    }

    public String getUserOpenId() {
        HttpServletRequest request = getHttpServletRequest();
        if (Objects.isNull(request)) {
            return null;
        }
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return jwtUtils.getInfo(token);
    }

}
