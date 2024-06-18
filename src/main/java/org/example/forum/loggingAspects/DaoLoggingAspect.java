package org.example.forum.loggingAspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DaoLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(DaoLoggingAspect.class);

    @Before("execution(* org.example.forum.dao.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        logger.info("Metoda DAO => ( " + className + "." + methodName+" ) została wywołana.");
    }

    @AfterReturning(pointcut = "execution(* org.example.forum.dao.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        logger.info("Metoda DAO => ( "+ className +"."+ methodName +" ) zakończona sukcesem.");
    }

    @AfterThrowing(pointcut = "execution(* org.example.forum.dao.*.*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint,Throwable error) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        logger.error("Metoda DAO => ( "+ className +"."+ methodName +" ) ZAKOŃCZYŁA SIĘ BŁĘDEM => ", error);
    }

}
