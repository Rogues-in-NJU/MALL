package edu.nju.mall.security;

import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.dto.UserDTO;
import edu.nju.mall.entity.Role;
import edu.nju.mall.service.UserService;
import edu.nju.mall.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("processing authentication for [{}]", request.getRequestURL());
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String openid = null;
        if (Objects.nonNull(token)) {
            openid = jwtUtils.getInfo(token);
            if (Objects.isNull(openid)) {
                throw ExceptionEnum.ILLEGAL_USER.exception("非法用户!");
            }
            if (jwtUtils.isTimeout(token)) {
                throw ExceptionEnum.ILLEGAL_USER.exception("登录状态超时，请重新登录!");
            }
        } else {
            log.error("couldn't find token string");
        }
        if (Objects.nonNull(openid) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            log.debug("security context was null, so authorizing user");
            UserDTO userDTO = userService.findUser(openid);
            List<Role> roles = userService.findUserRoles(userDTO.getId());
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
            log.info("authorized user [{}], setting security context", openid);
            SecurityContextHolder.getContext().setAuthentication(new WechatMicroProgramAuthenticationToken(openid, authorities));
        }
        filterChain.doFilter(request, response);
    }

}
