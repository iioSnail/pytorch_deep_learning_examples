package com.iioSnail.shop.mcp;

import com.iioSnail.shop.auth.McpToolSecurity;
import jakarta.servlet.http.HttpServletRequest;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

@Service
public class UserInfoTools {

    @McpToolSecurity
    @McpTool(name = "getUserInfo", description = "Get the current user's basic information")
    public Map<String, String> getUserInfo() {
        ServletRequestAttributes attributes =
            (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取headers
        String token = request.getHeader("token");

        if ("zhangsan123".equals(token)) {
            return Map.of(
                    "name", "Zhang san",
                    "age", "23",
                    "balance", "$130"
            );
        } else if ("lisi123".equals(token)) {
            return Map.of(
                    "name", "Li si",
                    "age", "75",
                    "balance", "$3991"
            );
        } else {
            throw new RuntimeException("Failed to retrieve user information. Please log in again.");
        }
    }

}
