package com.iioSnail.mcp_client.config;

import io.modelcontextprotocol.client.transport.customizer.McpSyncHttpClientRequestCustomizer;
import io.modelcontextprotocol.common.McpTransportContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.net.http.HttpRequest;

@Configuration
public class McpConfig {

    @Bean
    McpSyncHttpClientRequestCustomizer requestCustomizer() {
        McpSyncHttpClientRequestCustomizer mcpSyncHttpClientRequestCustomizer = new McpSyncHttpClientRequestCustomizer() {
            @Override
            public void customize(HttpRequest.Builder builder, String method, URI endpoint, String body, McpTransportContext context) {
                if (RequestContextHolder.getRequestAttributes() == null) {
                    return;
                }

                ServletRequestAttributes attributes =
                        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                HttpServletRequest request = attributes.getRequest();
                String token = request.getHeader("token");

                if (token != null) {
                    builder.header("token", token);
                }
            }
        };

        return mcpSyncHttpClientRequestCustomizer;
    }

}
