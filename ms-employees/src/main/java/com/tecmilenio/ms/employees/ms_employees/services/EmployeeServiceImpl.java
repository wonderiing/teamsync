package com.tecmilenio.ms.employees.ms_employees.services;

import com.tecmilenio.ms.employees.ms_employees.dto.CompanyDto;
import com.tecmilenio.ms.employees.ms_employees.dto.CreateEmployeeDto;
import com.tecmilenio.ms.employees.ms_employees.dto.DepartmentDto;
import com.tecmilenio.ms.employees.ms_employees.dto.EmployeeDto;
import com.tecmilenio.ms.employees.ms_employees.entities.Employee;
import com.tecmilenio.ms.employees.ms_employees.exceptions.EmployeeNotFound;
import com.tecmilenio.ms.employees.ms_employees.mapper.EmployeeMapper;
import com.tecmilenio.ms.employees.ms_employees.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeMapper mapper;
    private final CompanyService companyService;
    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeMapper mapper, CompanyService companyService, EmployeeRepository repository) {
        this.repository = repository;
        this.mapper = mapper;
        this.companyService = companyService;
    }

    @Transactional(readOnly = true)
    @Override
    public List<EmployeeDto> findAll(Pageable pageable) {
        Page<Employee> employees = repository.findAll(pageable);
        return employees.stream().map(e -> mapper.toDto(e)).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public EmployeeDto findEmployeeById(Long id) {
        Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFound("Employee with id " + id + " was not found"));
        return mapper.toDto(employee);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EmployeeDto> findEmployeesByCompanyId(Long companyId, Pageable pageable) {
        CompanyDto company = validateCompany(companyId);

        Page<Employee> employeesByCompany = repository.findAllByCompanyId(companyId, pageable);

        return employeesByCompany.stream().map(e -> mapper.toDto(e)).toList();


    }

    @Transactional(readOnly = true)
    @Override
    public EmployeeDto findEmployeeByEmail(String email) {
        Employee employee = repository.findByEmail(email).orElse(null);
        if (employee != null) {
            return mapper.toDto(employee);
        }
        return null;
    }

    @Transactional
    @Override
    public void deleteEmployee(Long id) {

        Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFound("Employee with id " + id + " was not found"));
        employee.setStatus("inactive");
        repository.save(employee);

    }

    @Transactional()
    @Override
    public EmployeeDto createEmployee(CreateEmployeeDto createEmployeeDto) {

        Long companyId = createEmployeeDto.getCompanyId();
        Long departmentId = createEmployeeDto.getDepartmentId();

        validateCompany(companyId);
        validateDepartment(departmentId);

        Employee employee = mapper.toEntity(createEmployeeDto);
        Employee createdEmployee = repository.save(employee);
        return mapper.toDto(createdEmployee);

    }

    @Transactional
    @Override
    public EmployeeDto updateEmployee(Long id, CreateEmployeeDto dto) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFound("Employee with id " + id + " was not found"));

        validateCompany(dto.getCompanyId());
        validateDepartment(dto.getDepartmentId());

        employee.setCompanyId(dto.getCompanyId());
        employee.setDepartmentId(dto.getDepartmentId());
        employee.setFullName(dto.getFullName());
        employee.setEmail(dto.getEmail());
        employee.setTelephone(dto.getTelephone());
        employee.setPosition(dto.getPosition());

        Employee updatedEmployee = repository.save(employee);

        return mapper.toDto(updatedEmployee);
    }

    @Override
    public CompanyDto validateCompany(Long companyId) {
        CompanyDto company = companyService.findCompanyById(companyId);

        if (company == null) {
            throw new EntityNotFoundException("Company with id " + companyId + " doesnt exists");
        }
        return company;
    }

    @Override
    public DepartmentDto validateDepartment(Long departmentId) {
        DepartmentDto department = companyService.findDepartmentById(departmentId);

        if (department == null) {
            throw new EntityNotFoundException("Department with id " + departmentId + " was not found");
        }
        return department;
    }
}
