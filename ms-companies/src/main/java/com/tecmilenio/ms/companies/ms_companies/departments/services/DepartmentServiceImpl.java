package com.tecmilenio.ms.companies.ms_companies.departments.services;

import com.tecmilenio.ms.companies.ms_companies.common.exceptions.CompanyNotFound;
import com.tecmilenio.ms.companies.ms_companies.common.exceptions.DepartmentNotFoundException;
import com.tecmilenio.ms.companies.ms_companies.companies.entities.Company;
import com.tecmilenio.ms.companies.ms_companies.companies.repositories.CompanyRepository;
import com.tecmilenio.ms.companies.ms_companies.departments.dto.CreateDepartmentDto;
import com.tecmilenio.ms.companies.ms_companies.departments.dto.DepartmentDto;
import com.tecmilenio.ms.companies.ms_companies.departments.entities.Department;
import com.tecmilenio.ms.companies.ms_companies.departments.mapper.DepartmentMapper;
import com.tecmilenio.ms.companies.ms_companies.departments.repositories.DepartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    private final DepartmentRepository repository;
    private final DepartmentMapper mapper;
    private final CompanyRepository companyRepository;

    public DepartmentServiceImpl(DepartmentMapper mapper, DepartmentRepository repository, CompanyRepository companyRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.companyRepository = companyRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DepartmentDto> findAll(Pageable pageable) {
        Page<Department> departments = repository.findAll(pageable);

        return departments.stream().map(d -> mapper.toDto(d)).toList();

    }

    @Transactional
    @Override
    public DepartmentDto createDepartment(CreateDepartmentDto createDepartmentDto) {

        Long companyId = createDepartmentDto.getCompanyId();

        Company company = companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFound("Company with id " + companyId + " was not found"));

        Department department = Department.builder().company(company).fullName(createDepartmentDto.getFullName()).build();

        Department savedDepartment = repository.save(department);
        return mapper.toDto(savedDepartment);


    }

    @Transactional
    @Override
    public void deleteDepartment(Long id) {
        Department department = repository.findById(id).orElseThrow(() -> new DepartmentNotFoundException("Department with id " + id +" was not found"));

        repository.delete(department);

    }

    @Transactional(readOnly = true)
    @Override
    public List<DepartmentDto> findByCompanyId(Long id) {


        if(!companyRepository.existsById(id)) {

            throw new CompanyNotFound("Company with id " + id + " was not found");
        }

        List<Department> departments = repository.findByCompanyId(id);
        return departments.stream().map(d -> mapper.toDto(d)).toList();

    }

    @Transactional(readOnly = true)
    public DepartmentDto findById(Long id) {
        Department department = repository.findById(id).orElseThrow(() -> new DepartmentNotFoundException("Department with id " + id + " was not found"));
        return mapper.toDto(department);
    }


}
