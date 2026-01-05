package org.aystudios.Skincare.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log =
            LoggerFactory.getLogger(LoggingAspect.class);

    // 🔹 Service layer
    @Pointcut("execution(* org.aystudios.skincare.service..*(..))")
    public void serviceLayer() {}

    @Around("serviceLayer()")
    public Object logFullDetails(ProceedingJoinPoint joinPoint)
            throws Throwable {

        long start = System.currentTimeMillis();

        // 🔹 HTTP info
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request =
                attributes != null ? attributes.getRequest() : null;

        String httpMethod = request != null ? request.getMethod() : "N/A";
        String uri = request != null ? request.getRequestURI() : "N/A";

        // 🔹 User info
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String user =
                (auth != null && auth.isAuthenticated())
                        ? auth.getName()
                        : "ANONYMOUS";

        try {
            Object result = joinPoint.proceed();

            long timeTaken = System.currentTimeMillis() - start;

            log.info(
                    "✅ {} {} | user={} | method={} | time={}ms",
                    httpMethod,
                    uri,
                    user,
                    joinPoint.getSignature().toShortString(),
                    timeTaken
            );

            return result;

        } catch (Exception ex) {

            long timeTaken = System.currentTimeMillis() - start;

            log.error(
                    "❌ {} {} | user={} | method={} | time={}ms | error={}",
                    httpMethod,
                    uri,
                    user,
                    joinPoint.getSignature().toShortString(),
                    timeTaken,
                    ex.getMessage()
            );

            throw ex; // VERY IMPORTANT
        }
    }
}

