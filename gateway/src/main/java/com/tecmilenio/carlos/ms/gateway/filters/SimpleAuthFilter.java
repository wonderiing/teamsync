package com.tecmilenio.carlos.ms.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class SimpleAuthFilter {

    // @Bean
    public GlobalFilter simpleGlobalFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();
            
            System.out.println("Simple Filter - Processing path: " + path);
            
            // Allow all auth endpoints without authentication
            if (path.startsWith("/api/v1/auth/")) {
                System.out.println("Simple Filter - Allowing auth endpoint: " + path);
                return chain.filter(exchange);
            }
            
            // Allow all test endpoints without authentication
            if (path.startsWith("/api/v1/test/")) {
                System.out.println("Simple Filter - Allowing test endpoint: " + path);
                return chain.filter(exchange);
            }
            
            // For all other endpoints, require authentication
            System.out.println("Simple Filter - Requiring auth for: " + path);
            
            // For now, let's just allow everything to test
            return chain.filter(exchange);
        };
    }
}
