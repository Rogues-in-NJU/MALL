package edu.nju.mall.common.aop;

import edu.nju.mall.common.ExceptionEnum;
import edu.nju.mall.dto.UserDTO;
import edu.nju.mall.entity.Role;
import edu.nju.mall.service.UserService;
import edu.nju.mall.util.CommonUtils;
import edu.nju.mall.util.HttpSecurity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

@Aspect
@Slf4j
@Component
@Order(100)
public class RoleControlAspect {

    @Autowired
    private UserService userService;
    @Autowired
    private HttpSecurity httpSecurity;

    @Around("@annotation(RoleControl)")
    public Object invokeHandle(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Class<?> classTarget= joinPoint.getTarget().getClass();
        Class<?>[] par = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        Method objMethod = classTarget.getMethod(methodName, par);

        RoleControl roleControl = objMethod.getAnnotation(RoleControl.class);

        String openid = httpSecurity.getUserOpenId();
        if (Objects.isNull(openid)) {
            log.error("openid is null");
            throw ExceptionEnum.ILLEGAL_USER.exception("非法用户!");
        }
        UserDTO userDTO = userService.findUser(openid);
        if (Objects.isNull(userDTO)) {
            log.error("user is null");
            throw ExceptionEnum.ILLEGAL_USER.exception("非法用户!");
        }
        List<Role> roles = userDTO.getRoles();
        for (Role r : roles) {
            for (String s : roleControl.value()) {
                if (Objects.equals(r.getName(), s)) {
                    return joinPoint.proceed();
                }
            }
        }
        throw ExceptionEnum.AUTH_FAIL.exception("没有权限!");
    }

}
