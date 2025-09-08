package com.tecmilenio.ms.employees.ms_employees.repository;

import com.tecmilenio.ms.employees.ms_employees.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Page<Employee> findAllByCompanyId(Long companyId, Pageable pageable);
}
