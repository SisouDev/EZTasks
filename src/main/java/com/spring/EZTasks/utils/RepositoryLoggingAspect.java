package com.spring.EZTasks.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryLoggingAspect.class);

    @Before("execution(* com.spring.EZTasks.model.repositories.user.UserRepository*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        logger.info("Chamando método: {}", joinPoint.getSignature().getName());
        logger.info("Com argumentos: {}", joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(* com.spring.EZTasks.model.repositories.user.UserRepository.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Método {} retornou: {}", joinPoint.getSignature().getName(), result);
    }
}
