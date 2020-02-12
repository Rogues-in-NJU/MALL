package edu.nju.mall.common.aop;

import edu.nju.mall.common.ExceptionVO;
import edu.nju.mall.common.NJUException;
import edu.nju.mall.common.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@Order(1)
public class InvokeControlAspect {

    @Around("@annotation(InvokeControl)")
    public Object invokeHandle(ProceedingJoinPoint joinPoint) {
        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable t) {
            if (t instanceof NJUException) {
                // 业务异常，输出业务日志
                log.error(String.format("[%s] %s", ((NJUException)t).getExceptionEnum(), t.getMessage()));
                return ResultVO.fail(((NJUException)t).getExceptionEnum(), t.getMessage());
            }
            // 系统异常，输出堆栈信息
            log.error(t.getMessage());
            return ResultVO.fail(ExceptionVO.defaultExceptionWrapper(t));
        }
    }

}
