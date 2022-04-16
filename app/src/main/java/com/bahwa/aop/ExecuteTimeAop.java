package com.bahwa.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.log4j.Log4j2;

@Aspect
@Component
@Log4j2
public class ExecuteTimeAop {
    
    @Around("@within(com.bahwa.aop.ExecuteTimeCheck)")
    public Object executeTimer(ProceedingJoinPoint joinPoint) throws Throwable {
        
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed();

        stopWatch.stop();

        log.debug("ExecuteTime ::: ", stopWatch.getTotalTimeMillis() + "ms");

        return result;
    }
}
