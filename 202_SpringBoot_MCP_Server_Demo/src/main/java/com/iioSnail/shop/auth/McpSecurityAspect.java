package com.iioSnail.shop.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
public class McpSecurityAspect {

    private static final Logger logger = LoggerFactory.getLogger(McpSecurityAspect.class);

    @Around("@annotation(mcpToolSecurity)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, McpToolSecurity mcpToolSecurity) throws Throwable {

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String token = request.getHeader("token");

        if (token == null) {
            throw new SecurityException("Authentication required");
        }

        if (!Arrays.asList("zhangsan123", "lisi123").contains(token)) {
            throw new SecurityException("Unknown User");
        }

        return joinPoint.proceed();
    }

}
