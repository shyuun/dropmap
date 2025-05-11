package com.dropmap.www.component;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
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
}
