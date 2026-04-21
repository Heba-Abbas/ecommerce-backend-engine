package com.ecommerce.backend_engine.aspect;

import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class PerformanceLoggingAspect {

    @Around("execution(* com.ecommerce.backend_engine.service.ProductService.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed;

        try {
            proceed = joinPoint.proceed();
        } finally {

            long executionTime = System.currentTimeMillis() - start;

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletResponse response = attributes.getResponse();
                if (response != null && !response.isCommitted()) {
                    response.addHeader("X-Internal-Time", executionTime + "ms");
                }
            }
        }
        return proceed;
    }
}