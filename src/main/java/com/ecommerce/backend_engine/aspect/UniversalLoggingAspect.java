package com.ecommerce.backend_engine.aspect;

import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class UniversalLoggingAspect {

    // قمنا بتعديل الـ Pointcut ليشمل الـ Controller والـ Service معاً
    @Around("execution(* com.ecommerce.backend_engine.service.*.*(..)) || execution(* com.ecommerce.backend_engine.controller.*.*(..))")
    public Object monitorAllServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        long start = System.currentTimeMillis();

        log.info(">>> START EXECUTION THE FUNCTION: {}", methodName);

        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - start;
            log.info("<<< FINISH : {} | IT TAKES: {} ms", methodName, executionTime);

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletResponse response = attributes.getResponse();
                // التأكد من أن الاستجابة لم تُرسل بعد ليتمكن الـ Aspect من إضافة الهيدر
                if (response != null && !response.isCommitted()) {
                    // سيظهر هذا الهيدر الآن في JMeter في كل الحالات
                    response.addHeader("X-Internal-Time", executionTime + "ms");
                    response.addHeader("X-Observed-Method", methodName);
                }
            }
        }
        return proceed;
    }
}