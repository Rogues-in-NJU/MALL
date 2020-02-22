package edu.nju.mall.aop;

import edu.nju.mall.common.ExceptionVO;
import edu.nju.mall.common.NJUException;
import edu.nju.mall.common.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 环切，目前主要是业务异常和系统异常处理
 *
 * @author luhailong
 * @date 2019/12/29
 */
@Aspect
@Component
@Slf4j
public class InvokeControlAspect {

    /**
     * 环切，目前主要是业务异常和系统异常处理
     * @param joinPoint
     * @return
     */
    @Around("@annotation(InvokeControl)")
    public Object invokeHandle(ProceedingJoinPoint joinPoint) {
        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable t) {
            if (t instanceof NJUException) {
                // 业务异常，输出业务日志
                log.error(String.format("[%s]%s", ((NJUException)t).getExceptionEnum(), t.getMessage()));
                return ResultVO.fail(((NJUException)t).getExceptionEnum(), t.getMessage());
            }
            // 系统异常，输出堆栈信息
            log.error(t.getMessage());
            return ResultVO.fail(ExceptionVO.defaultExceptionWrapper(t));
        }
    }
}
