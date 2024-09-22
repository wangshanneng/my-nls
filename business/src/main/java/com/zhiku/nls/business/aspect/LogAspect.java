package com.zhiku.nls.business.aspect;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 日志切面类，用于打印请求信息和返回结果
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * 定义切入点，匹配 com.zhiku 包下的所有 Controller 的公有方法
     */
    @Pointcut("execution(public * com.zhiku..*Controller.*(..))")
    public void pointcut() {
        // 切入点定义
    }

    /**
     * 前置通知，方法执行前调用
     *
     * @param joinPoint 切入点对象
     */
    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {
        // 可以添加前置日志逻辑
    }

    /**
     * 环绕通知，方法执行前后都会调用
     *
     * @param proceedingJoinPoint 切入点对象
     * @return 方法执行结果
     * @throws Throwable 如果有异常抛出
     */
    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("----------环绕通知开始----------");

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 获取 HttpServletRequest 对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = proceedingJoinPoint.getSignature();
        String methodName = signature.getName();

        // 打印请求基本信息
        log.info("请求地址： {} {}", request.getRequestURI(), request.getMethod());
        log.info("类名方法： {}.{}", signature.getDeclaringTypeName(), methodName);
        log.info("远程地址： {}", request.getRemoteAddr());

        // 获取请求参数并过滤特殊类型
        Object[] args = proceedingJoinPoint.getArgs();
        Object[] filteredArgs = new Object[args.length];

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof HttpServletRequest ||
                    args[i] instanceof ServletResponse ||
                    args[i] instanceof MultipartConfig) {
                continue;
            }
            filteredArgs[i] = args[i];
        }

        // 排除敏感字段
        String[] excludeProperties = {"cvv2", "idCard"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludeFilter = filters.addFilter();
        excludeFilter.addExcludes(excludeProperties);

        // 打印请求参数
        log.info("请求参数： {}", JSONObject.toJSONString(filteredArgs, excludeFilter));

        // 执行目标方法
        Object result = proceedingJoinPoint.proceed();

        // 打印返回结果
        log.info("返回结果： {}", JSONObject.toJSONString(result, excludeFilter));

        // 计算并打印执行耗时
        long elapsedTime = System.currentTimeMillis() - startTime;
        log.info("请求耗时： {}ms", elapsedTime);
        log.info("----------环绕通知结束----------");

        return result;
    }

    /**
     * 后置通知，方法执行后调用
     *
     * @param joinPoint 切入点对象
     */
    @After("pointcut()")
    public void doAfter(JoinPoint joinPoint) {
        // 可以添加后置日志逻辑
    }
}
