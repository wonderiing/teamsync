package com.tecmilenio.ms.employees.ms_employees.controllers;

import com.tecmilenio.ms.employees.ms_employees.dto.CreateEmployeeDto;
import com.tecmilenio.ms.employees.ms_employees.dto.EmployeeDto;
import com.tecmilenio.ms.employees.ms_employees.services.EmployeeService;
import com.tecmilenio.ms.employees.ms_employees.services.EmployeeServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping()
    public ResponseEntity<List<EmployeeDto>> getAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<EmployeeDto> employeeDtos = employeeService.findAll(pageable);
        return ResponseEntity.ok(employeeDtos);

    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable @Min(1) Long id) {
        EmployeeDto employeeDto = employeeService.findEmployeeById(id);
        return ResponseEntity.ok(employeeDto);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<EmployeeDto>> getAllCompanyEmployees(
            @PathVariable @Min(1) Long companyId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        List<EmployeeDto> employeesByCompany = employeeService.findEmployeesByCompanyId(companyId,pageable);
        return ResponseEntity.ok(employeesByCompany);
    }

    @PostMapping()
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody @Valid CreateEmployeeDto createEmployeeDto) {

        EmployeeDto createdEmployee = employeeService.createEmployee(createEmployeeDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEmployee.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable @Min(1) Long id, @RequestBody CreateEmployeeDto createEmployeeDto) {
        EmployeeDto updatedEmployee = employeeService.updateEmployee(id, createEmployeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }
}
