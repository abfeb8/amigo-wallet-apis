package com.ab.amigo.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.ab.amigo.controller..*(..))" +
            " || execution(* com.ab.amigo.service..*(..))")
    public void applicationPackagePointcut() {}

    @Before("applicationPackagePointcut()")
    public void logMethodExecutionTime(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        logger.info("Amigo_Wallet :: {} :: {}", methodSignature.getDeclaringType().getSimpleName(), methodSignature.getName());
    }

}
