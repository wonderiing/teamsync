package com.tecmilenio.ms.employees.ms_employees.controllers;

import com.tecmilenio.ms.employees.ms_employees.dto.EmployeeDto;
import com.tecmilenio.ms.employees.ms_employees.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class EmployeeSearchController {
    
    @Autowired
    private EmployeeService employeeService;
    
    /**
     * Buscar empleado por email
     * GET /api/v1/employees/email?email=test@example.com
     */
    @GetMapping("/email")
    public ResponseEntity<EmployeeDto> findEmployeeByEmail(@RequestParam("email") String email) {
        try {
            EmployeeDto employee = employeeService.findEmployeeByEmail(email);
            if (employee != null) {
                return ResponseEntity.ok(employee);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
