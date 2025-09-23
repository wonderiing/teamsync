package com.tecmilenio.carlos.ms.requests.services;

import com.tecmilenio.carlos.ms.requests.dto.CreateRequestDto;
import com.tecmilenio.carlos.ms.requests.dto.RequestDto;
import com.tecmilenio.carlos.ms.requests.dto.UpdateStatusDto;
import com.tecmilenio.carlos.ms.requests.entities.RequestStatus;
import com.tecmilenio.carlos.ms.requests.entities.RequestType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RequestService {

    RequestDto createRequest(CreateRequestDto createRequestDto);

    RequestDto getRequestById(Long requestId);

    RequestDto updateRequestStatus(Long requestId, UpdateStatusDto updateStatusDto);

    List<RequestDto> getEmployeeRequests(Long employeeId, Pageable pageable);

    List<RequestDto> getEmployeeRequestsByType(Long employeeId, RequestType requestType, Pageable pageable);

    List<RequestDto> getEmployeeRequestsByStatus(Long employeeId, RequestStatus status, Pageable pageable);

    List<RequestDto> getCompanyRequests(Long companyId, Pageable pageable);

    List<RequestDto> getCompanyRequestsByType(Long companyId, RequestType requestType, Pageable pageable);

    List<RequestDto> getCompanyRequestsByStatus(Long companyId, RequestStatus status, Pageable pageable);
}
