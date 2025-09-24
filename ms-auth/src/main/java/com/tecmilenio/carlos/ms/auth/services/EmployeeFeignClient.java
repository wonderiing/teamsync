package com.tecmilenio.carlos.ms.auth.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "ms-employees", url = "http://localhost:8081")
public interface EmployeeFeignClient {
    
    @GetMapping("/api/v1/employees/email")
    Map<String, Object> findEmployeeByEmail(@RequestParam("email") String email);
    
    @PostMapping("/api/v1/employees")
    Map<String, Object> createEmployee(@RequestBody Map<String, Object> employeeData);
}
