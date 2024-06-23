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
public class RepositoryLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(DaoLoggingAspect.class);

    @Before("execution(* org.example.forum.repositories.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        logger.info("Metoda REPOZYTORIUM ( "+className+"."+methodName+" )jest wywoływana.");
    }

    @AfterReturning(pointcut = "execution(* org.example.forum.repositories.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        logger.info("Metoda REPOZYTORIUM ( "+className+"."+methodName+" ) zakończona sukcesem.");
    }

    @AfterThrowing(pointcut = "execution(* org.example.forum.repositories.*.*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        logger.error("Wystąpił błąd w metodzie REPOZYTORIUM => ", error);
    }

}
