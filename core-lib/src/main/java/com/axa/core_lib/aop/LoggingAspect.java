package com.axa.core_lib.aop;


import com.axa.core_lib.http.UserServiceClient;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Value("${spring.application.name:UNKNOWN_SERVICE}")
    private String applicationName;

    private final UserServiceClient userServiceClient;

    public LoggingAspect(@Lazy() UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
        LOGGER.info(">>> LoggingAspect bean created! , it's rafy for test2");
    }

    private String buildLog(JoinPoint joinPoint, String severity, String content) {
        // Try to get request context
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()) != null
                        ? ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                        : null;

        String entity = "CORE_LIB";
        String application = applicationName;
        String applicationElement = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        String dateTime = LocalDateTime.now().toString();


        String securityFlag = "NO_AUTH";
        String userId = "ANONYMOUS";
        String userName = "ANONYMOUS";

        try {
            Class<?> ctxClass = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
            Object context = ctxClass.getMethod("getContext").invoke(null);
            Object auth = context.getClass().getMethod("getAuthentication").invoke(context);

            if (auth != null && (Boolean) auth.getClass().getMethod("isAuthenticated").invoke(auth)) {
                securityFlag = "AUTH";
                userId = (String) auth.getClass().getMethod("getName").invoke(auth);
            }
        } catch (ClassNotFoundException e) {
            LOGGER.debug("Spring Security not found on classpath → falling back to NO_AUTH.");
        } catch (Exception e) {
            LOGGER.warn("Could not resolve authentication context, using defaults: NO_AUTH/ANONYMOUS", e);
        }

        if ("NO_AUTH".equals(securityFlag) && request != null && request.getHeader("Authorization") != null) {
            try{
                userName = userServiceClient.validateToken(request.getHeader("Authorization"));
                    securityFlag = "AUTH";
            } catch (Exception e) {
                userName = "Invaid_User_Name";
                securityFlag = "TOKEN_PRESENT";
            }

        }




        String ip = (request != null) ? request.getRemoteAddr() : "N/A";
        String geo = "N/A"; // placeholder unless GeoIP is added
        String sessionId = (request != null && request.getSession(false) != null) ? request.getSession().getId() : "N/A";
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
                userName,
                serviceAccessed,
                content,
                rawRequest
        );
    }

    @Before("execution (* com..*.*(..)) && !within(com.axa.core_lib..*) ")
    public void logBefore(JoinPoint joinPoint) {
        LOGGER.info(buildLog(joinPoint, "INFO", "Before execution"));
    }

    @Before("execution(* com..service..*(..)) ")
    public void logBeforeService(JoinPoint joinPoint) {
        LOGGER.info(buildLog(joinPoint, "INFO", "Before execution"));
    }

    @AfterThrowing(pointcut = "execution (* com..*.*(..)) && !within(com.axa.core_lib..*) ", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        LOGGER.error(buildLog(joinPoint, "ERROR", "Exception: " + ex.getMessage()));
    }

    @AfterThrowing(pointcut = "execution(* com..service..*(..))", throwing = "ex")
    public void logAfterThrowingService(JoinPoint joinPoint, Throwable ex) {
        LOGGER.error(buildLog(joinPoint, "ERROR", "Exception: " + ex.getMessage()));
    }

    @AfterReturning(pointcut = "execution (* com..*.*(..)) && !within(com.axa.core_lib..*) ", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        LOGGER.info(buildLog(joinPoint, "INFO", "Returned: " + String.valueOf(result)));
    }

    @AfterReturning(pointcut = "execution(* com..service..*(..))", returning = "result")
    public void logAfterReturningService(JoinPoint joinPoint, Object result) {
        LOGGER.info(buildLog(joinPoint, "INFO", "Returned: " + String.valueOf(result)));
    }

    @Around("execution(* *(..)) && !execution(* com.axa.core_lib.UserServiceClient.validateToken(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }



//    @Before("within(@org.springframework.web.bind.annotation.RestController *)")
//    public void logBeforeController(JoinPoint joinPoint) {
//        LOGGER.info(buildLog(joinPoint, "INFO", "Before execution"));
//    }
//
//    @AfterReturning(pointcut = "within(@org.springframework.web.bind.annotation.RestController *)", returning = "result")
//    public void logAfterReturningController(JoinPoint joinPoint, Object result) {
//        LOGGER.info(buildLog(joinPoint, "INFO", "Returned: " + String.valueOf(result)));
//    }
//
//    @AfterThrowing(pointcut = "within(@org.springframework.web.bind.annotation.RestController *)", throwing = "ex")
//    public void logAfterThrowingController(JoinPoint joinPoint, Throwable ex) {
//        LOGGER.error(buildLog(joinPoint, "ERROR", "Exception: " + ex.getMessage()));
//    }

}





//
//package com.axa.core_lib.aop;
//
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.time.LocalDateTime;
//@Aspect
//@Component
//@Order(Ordered.LOWEST_PRECEDENCE)
//public class LoggingAspect {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);
//
//    @Value("${spring.application.name:UNKNOWN_SERVICE}")
//    private String applicationName;
//
//    public LoggingAspect() {
//        LOGGER.info(">>> LoggingAspect bean created! , it's rafy for test2");
//    }
//
//    // ---- Full buildLog with HttpServletRequest
//    private String buildLog(JoinPoint joinPoint, String severity, String content, HttpServletRequest request) {
//        String entity = "CORE_LIB";
//        String application = applicationName;
//        String applicationElement = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
//        String dateTime = LocalDateTime.now().toString();
//
//        String securityFlag = "NO_AUTH";
//        String userId = "ANONYMOUS";
//
//        try {
//            Class<?> ctxClass = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
//            Object context = ctxClass.getMethod("getContext").invoke(null);
//            Object auth = context.getClass().getMethod("getAuthentication").invoke(context);
//
//            if (auth != null && (Boolean) auth.getClass().getMethod("isAuthenticated").invoke(auth)) {
//                securityFlag = "AUTH";
//                userId = (String) auth.getClass().getMethod("getName").invoke(auth);
//            }
//        } catch (ClassNotFoundException e) {
//
//        } catch (Throwable t) {
//
//            LOGGER.debug("Security context not available, falling back to defaults", t);
//        }
//
//
//        if ("NO_AUTH".equals(securityFlag) && request != null && request.getHeader("Authorization") != null) {
//            securityFlag = "TOKEN_PRESENT"; // Token header exists, but no authenticated user
//        }
//
//        String ip = (request != null) ? request.getRemoteAddr() : "N/A";
//        String geo = "N/A"; // placeholder unless GeoIP is added
//        String sessionId = (request != null && request.getSession(false) != null) ? request.getSession().getId() : "N/A";
//        String serviceAccessed = (request != null) ? request.getRequestURI() : "N/A";
//        String rawRequest = (request != null) ? request.getMethod() + " " + request.getRequestURI() : "N/A";
//
//        return String.join("|",
//                entity,
//                application,
//                applicationElement,
//                severity,
//                securityFlag,
//                dateTime,
//                ip,
//                geo,
//                sessionId,
//                userId,
//                serviceAccessed,
//                content,
//                rawRequest
//        );
//    }
//
//    // ---- Overload: suppress AUTH/NO_AUTH only for @Before logs
//    private String buildLog(JoinPoint joinPoint, String severity, String content) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()) != null
//                ? ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
//                : null;
//
//        return buildLog(joinPoint, severity, content, request)
//                .replaceFirst("\\|(AUTH|NO_AUTH|TOKEN_PRESENT)", "|-");
//    }
//
//    // ---- Advices
//    @Before("execution(* com..service..*(..)) || within(@org.springframework.web.bind.annotation.RestController *)")
//    public void logBefore(JoinPoint joinPoint) {
//        LOGGER.info(buildLog(joinPoint, "INFO", "Before execution"));
//    }
//
//    @AfterReturning(pointcut = "execution(* com..service..*(..)) || within(@org.springframework.web.bind.annotation.RestController *)", returning = "result")
//    public void logAfterReturning(JoinPoint joinPoint, Object result) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()) != null
//                ? ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
//                : null;
//        LOGGER.info(buildLog(joinPoint, "INFO", "Returned: " + String.valueOf(result), request));
//    }
//
//    @AfterThrowing(pointcut = "execution(* com..service..*(..)) || within(@org.springframework.web.bind.annotation.RestController *)", throwing = "ex")
//    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()) != null
//                ? ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
//                : null;
//        LOGGER.error(buildLog(joinPoint, "ERROR", "Exception: " + ex.getMessage(), request));
//    }
//}
