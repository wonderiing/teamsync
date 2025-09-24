package com.tecmilenio.ms.companies.ms_companies.departments.controllers;

import com.tecmilenio.ms.companies.ms_companies.departments.dto.CreateDepartmentDto;
import com.tecmilenio.ms.companies.ms_companies.departments.dto.DepartmentDto;
import com.tecmilenio.ms.companies.ms_companies.departments.services.DepartmentService;
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
@RequestMapping("/api/v1/departments")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@Validated
public class DepartmentController {


    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }


    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
            ) {

        Pageable pageable = PageRequest.of(page, size);
        List<DepartmentDto> departmentDtos = departmentService.findAll(pageable);
        return ResponseEntity.ok(departmentDtos);

    }


    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<DepartmentDto>> getAllByCompanyId(@PathVariable @Min(0) Long companyId) {
        List<DepartmentDto> departmentsByCompany = departmentService.findByCompanyId(companyId);
        return ResponseEntity.ok(departmentsByCompany);

    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getById(@PathVariable @Min(0) Long id) {
        DepartmentDto departmentDto = departmentService.findById(id);
        return ResponseEntity.ok(departmentDto);
    }

    @PostMapping
    public ResponseEntity<DepartmentDto> create(@RequestBody @Valid CreateDepartmentDto createDepartmentDto) {
        DepartmentDto createdDepartment = departmentService.createDepartment(createDepartmentDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdDepartment.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdDepartment);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Min(1) Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

}
