package com.tecmilenio.carlos.ms.attendance.ms_attendancce.clients;

import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.EmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-employees", url = "http://localhost:8081")
public interface EmployeeFeignClient {

    @GetMapping("/api/v1/employees/{id}")
    EmployeeDto getEmployeeById(@PathVariable Long id);

    @GetMapping("/api/v1/employees/company/{companyId}")
    List<EmployeeDto> getEmployeesByCompanyId(@PathVariable Long companyId);
}
