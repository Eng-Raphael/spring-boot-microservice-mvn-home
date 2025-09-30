package com.axa.core_lib.aop;


import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    public LoggingAspect() {
        LOGGER.info(">>> LoggingAspect bean created! , it's rafy for test");
    }

    private String buildLog(JoinPoint joinPoint, String severity, String content) {
        // Try to get request context
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()) != null
                        ? ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                        : null;

        String entity = "CORE_LIB"; // or pass from config
        String application = "TEST_SERVICE"; // ideally use spring.application.name
        String applicationElement = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        String securityFlag = "NO_AUTH";
        String dateTime = LocalDateTime.now().toString();
        String ip = (request != null) ? request.getRemoteAddr() : "N/A";
        String geo = "N/A"; // placeholder unless GeoIP is added
        String sessionId = (request != null && request.getSession(false) != null) ? request.getSession().getId() : "N/A";
        String userId = "ANONYMOUS"; // integrate later with security
        String serviceAccessed = (request != null) ? request.getRequestURI() : "N/A";
        String rawRequest = (request != null) ? request.getMethod() + " " + request.getRequestURI() : "N/A";

        return String.join("|",
                entity,
                application,
                applicationElement,
                severity,
                securityFlag,
                dateTime,
                ip,
                geo,
                sessionId,
                userId,
                serviceAccessed,
                content,
                rawRequest
        );
    }

    @Before("execution (* com..*.*(..)) && !within(com.axa.core_lib..*)")
    public void logBefore(JoinPoint joinPoint) {
        LOGGER.info(buildLog(joinPoint, "INFO", "Before execution"));
    }

    @AfterThrowing(pointcut = "execution (* com..*.*(..)) && !within(com.axa.core_lib..*)", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        LOGGER.error(buildLog(joinPoint, "ERROR", "Exception: " + ex.getMessage()));
    }

    @AfterReturning(pointcut = "execution (* com..*.*(..)) && !within(com.axa.core_lib..*)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        LOGGER.info(buildLog(joinPoint, "INFO", "Returned: " + String.valueOf(result)));
    }

}
