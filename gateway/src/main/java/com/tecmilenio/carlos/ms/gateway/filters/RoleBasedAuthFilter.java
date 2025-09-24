package com.tecmilenio.carlos.ms.gateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class RoleBasedAuthFilter {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Bean
    public GlobalFilter roleBasedAuthFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();
            String method = request.getMethod().toString();

            System.out.println("Role Filter - Processing: " + method + " " + path);

            // Allow auth endpoints without authentication
            if (path.startsWith("/api/v1/auth/")) {
                System.out.println("Role Filter - Allowing auth endpoint: " + path);
                return chain.filter(exchange);
            }

            // Allow test endpoints without authentication
            if (path.startsWith("/api/v1/test/")) {
                System.out.println("Role Filter - Allowing test endpoint: " + path);
                return chain.filter(exchange);
            }

            // Get token from request
            String token = getTokenFromRequest(request);
            if (token == null) {
                System.out.println("Role Filter - No token provided for: " + path);
                return onError(exchange, "No token provided", HttpStatus.UNAUTHORIZED);
            }

            // Validate token and get user info
            return validateTokenAndGetUserInfo(token)
                .flatMap(userInfo -> {
                    System.out.println("Role Filter - User: " + userInfo.get("username") + ", Role: " + userInfo.get("role"));
                    
                    // Check if user has required role for the endpoint
                    if (hasRequiredRole(path, method, userInfo)) {
                        // Token is valid and user has required role, pass through without adding headers
                        return chain.filter(exchange);
                    } else {
                        System.out.println("Role Filter - Insufficient permissions for: " + path);
                        return onError(exchange, "Insufficient permissions", HttpStatus.FORBIDDEN);
                    }
                })
                .onErrorResume(e -> {
                    System.out.println("Role Filter - Token validation error: " + e.getMessage());
                    return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
                });
        };
    }

    private String getTokenFromRequest(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private Mono<java.util.Map<String, Object>> validateTokenAndGetUserInfo(String token) {
        return webClientBuilder.build()
            .post()
            .uri("http://ms-auth/api/v1/auth/validate")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .retrieve()
            .bodyToMono(java.util.Map.class)
            .map(map -> (java.util.Map<String, Object>) map)
            .onErrorReturn(null);
    }

    private boolean hasRequiredRole(String path, String method, java.util.Map<String, Object> userInfo) {
        String role = userInfo.get("role").toString();
        
        // Admin endpoints (companies, departments management)
        if (isAdminEndpoint(path, method)) {
            return "ADMIN".equals(role);
        }
        
        // HR endpoints (employee management, request approval, company-wide data)
        if (isHREndpoint(path, method)) {
            return "HR".equals(role) || "ADMIN".equals(role);
        }
        
        // Employee endpoints (personal data, requests, attendance)
        if (isEmployeeEndpoint(path, method)) {
            return "EMPLOYEE".equals(role) || "HR".equals(role) || "ADMIN".equals(role);
        }
        
        // Public endpoints (tutorials, etc.)
        if (isPublicEndpoint(path, method)) {
            return true; // Any authenticated user can access
        }
        
        return false; // Default: deny access
    }

    private boolean isAdminEndpoint(String path, String method) {
        return (path.startsWith("/api/v1/companies") || 
                path.startsWith("/api/v1/departments")) &&
               ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method));
    }

    private boolean isHREndpoint(String path, String method) {
        return (path.startsWith("/api/v1/employees") && 
                ("GET".equals(method) || "POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method))) ||
               (path.startsWith("/api/v1/requests") && "PUT".equals(method) && path.contains("/status")) ||
               (path.startsWith("/api/v1/requests/company/")) ||
               (path.startsWith("/api/v1/attendances/company/")) ||
               (path.startsWith("/api/v1/tutorials") && 
                ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method)));
    }

    private boolean isEmployeeEndpoint(String path, String method) {
        return (path.startsWith("/api/v1/attendances") && 
                (path.contains("/employee/") || path.equals("/api/v1/attendances/check-in") || 
                 path.equals("/api/v1/attendances/check-out"))) ||
               (path.startsWith("/api/v1/requests") && 
                ("POST".equals(method) || path.contains("/employee/"))) ||
               (path.startsWith("/api/v1/employees/") && "GET".equals(method) && 
                !path.contains("/company/")) ||
               (path.startsWith("/api/v1/tutorials") && "GET".equals(method) && 
                !path.contains("/company/") && !path.contains("/category/") && !path.contains("/search"));
    }

    private boolean isPublicEndpoint(String path, String method) {
        return path.startsWith("/api/v1/tutorials") && "GET".equals(method);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json");
        
        String body = "{\"error\":\"" + error + "\"}";
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
