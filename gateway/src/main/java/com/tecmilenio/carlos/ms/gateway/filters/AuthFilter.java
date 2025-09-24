package com.tecmilenio.carlos.ms.gateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class AuthFilter {
    
    @Autowired
    private WebClient.Builder webClientBuilder;
    
    // @Bean
    public GlobalFilter customGlobalFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();
            
            // Debug logging
            System.out.println("Gateway Filter - Path: " + path);
            System.out.println("Gateway Filter - Method: " + request.getMethod());
            
            // Skip auth for login endpoint and test endpoints
            if (path.startsWith("/api/v1/auth/") || path.startsWith("/api/v1/test/")) {
                System.out.println("Gateway Filter - Skipping auth for path: " + path);
                return chain.filter(exchange);
            }
            
            String token = getTokenFromRequest(request);
            
            if (token == null) {
                System.out.println("Gateway Filter - No token provided for path: " + path);
                return onError(exchange, "No token provided", HttpStatus.UNAUTHORIZED);
            }
            
            return validateToken(token)
                .flatMap(isValid -> {
                    if (isValid) {
                        System.out.println("Gateway Filter - Token valid for path: " + path);
                        return chain.filter(exchange);
                    } else {
                        System.out.println("Gateway Filter - Invalid token for path: " + path);
                        return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
                    }
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
    
    private Mono<Boolean> validateToken(String token) {
        return webClientBuilder.build()
            .post()
            .uri("http://ms-auth/api/v1/auth/validate")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .retrieve()
            .bodyToMono(String.class)
            .map(response -> true)
            .onErrorReturn(false)
            .doOnError(error -> System.err.println("Token validation error: " + error.getMessage()));
    }
    
    private Mono<Void> onError(org.springframework.web.server.ServerWebExchange exchange, String error, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json");
        
        String body = "{\"error\":\"" + error + "\"}";
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}