package com.tecmilenio.carlos.ms.requests.services;

import com.tecmilenio.carlos.ms.requests.dto.CreateRequestDto;
import com.tecmilenio.carlos.ms.requests.dto.RequestDto;
import com.tecmilenio.carlos.ms.requests.dto.UpdateStatusDto;
import com.tecmilenio.carlos.ms.requests.entities.RequestStatus;
import com.tecmilenio.carlos.ms.requests.entities.RequestType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RequestService {

    RequestDto createRequest(CreateRequestDto createRequestDto, HttpServletRequest request);

    RequestDto getRequestById(Long requestId);

    RequestDto updateRequestStatus(Long requestId, UpdateStatusDto updateStatusDto);

    List<RequestDto> getEmployeeRequests(HttpServletRequest request, Pageable pageable);

    List<RequestDto> getEmployeeRequestsByType(HttpServletRequest request, RequestType requestType, Pageable pageable);

    List<RequestDto> getEmployeeRequestsByStatus(HttpServletRequest request, RequestStatus status, Pageable pageable);

    List<RequestDto> getCompanyRequests(HttpServletRequest request, Pageable pageable);

    List<RequestDto> getCompanyRequestsByType(HttpServletRequest request, RequestType requestType, Pageable pageable);

    List<RequestDto> getCompanyRequestsByStatus(HttpServletRequest request, RequestStatus status, Pageable pageable);
}
