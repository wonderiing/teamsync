package com.tecmilenio.ms.companies.ms_companies.departments.repositories;

import com.tecmilenio.ms.companies.ms_companies.departments.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByCompanyId(Long id);
}
