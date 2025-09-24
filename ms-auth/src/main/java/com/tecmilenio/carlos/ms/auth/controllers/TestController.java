package com.tecmilenio.carlos.ms.auth.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class TestController {
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "OK",
            "service", "ms-auth",
            "message", "Service is running"
        ));
    }
    
    @PostMapping("/echo")
    public ResponseEntity<Map<String, Object>> echo(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(Map.of(
            "received", request,
            "status", "success"
        ));
    }
    
    @PostMapping("/test-json")
    public ResponseEntity<Map<String, Object>> testJson(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(Map.of(
            "message", "JSON recibido correctamente",
            "data", request,
            "timestamp", System.currentTimeMillis()
        ));
    }
}