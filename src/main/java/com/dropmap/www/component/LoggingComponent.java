package com.dropmap.www.component;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingComponent {

    @Before("execution(* com.dropmap.www.service..*(..)) || execution(* com.dropmap.www.web..*(..))")
    public void logMethodName(JoinPoint joinPoint) throws Throwable {
        log.info("=============================[ " + joinPoint.getSignature().getName() + " ]=============================");
    }

    @Around("execution(* com.dropmap.www.service.DataProcessorService.init(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();  // 실제 메서드 실행
        long end = System.currentTimeMillis();

        log.info("[실행 시간] {}.{}: {}ms",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                end - start);

        return result;
    }
}
