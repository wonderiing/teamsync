package com.tecmilenio.carlos.ms.requests.repository;

import com.tecmilenio.carlos.ms.requests.entities.Request;
import com.tecmilenio.carlos.ms.requests.entities.RequestStatus;
import com.tecmilenio.carlos.ms.requests.entities.RequestType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Page<Request> findByIdEmployeeOrderByCreatedAtDesc(Long idEmployee, Pageable pageable);

    Page<Request> findByIdEmployeeInOrderByCreatedAtDesc(List<Long> employeeIds, Pageable pageable);

    Page<Request> findByIdEmployeeAndRequestTypeOrderByCreatedAtDesc(Long idEmployee, RequestType requestType, Pageable pageable);

    Page<Request> findByIdEmployeeAndStatusOrderByCreatedAtDesc(Long idEmployee, RequestStatus status, Pageable pageable);

    Page<Request> findByIdEmployeeInAndRequestTypeOrderByCreatedAtDesc(List<Long> employeeIds, RequestType requestType, Pageable pageable);

    Page<Request> findByIdEmployeeInAndStatusOrderByCreatedAtDesc(List<Long> employeeIds, RequestStatus status, Pageable pageable);

    long countByIdEmployeeAndStatus(Long idEmployee, RequestStatus status);

    long countByIdEmployeeInAndStatus(List<Long> employeeIds, RequestStatus status);
}
