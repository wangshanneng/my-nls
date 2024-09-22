package com.zhiku.nls.business.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(public * com.zhiku..*Controller.*(..))")
    public void pointcut() {

    }

    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("前置通知");
    }

    @Around("pointcut()")
    public  Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("around-开始");
        Object result = proceedingJoinPoint.proceed();
        log.info("around-结束");

        return result;
    }

    @After("pointcut()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("后置通知");
    }
}
