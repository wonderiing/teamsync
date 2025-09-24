package com.tecmilenio.carlos.ms.requests.services;

import com.tecmilenio.carlos.ms.requests.clients.EmployeeFeignClient;
import com.tecmilenio.carlos.ms.requests.dto.*;
import com.tecmilenio.carlos.ms.requests.entities.Request;
import com.tecmilenio.carlos.ms.requests.entities.RequestStatus;
import com.tecmilenio.carlos.ms.requests.entities.RequestType;
import com.tecmilenio.carlos.ms.requests.exceptions.EmployeeNotFoundException;
import com.tecmilenio.carlos.ms.requests.exceptions.InvalidStatusTransitionException;
import com.tecmilenio.carlos.ms.requests.exceptions.RequestNotFoundException;
import com.tecmilenio.carlos.ms.requests.mapper.RequestMapper;
import com.tecmilenio.carlos.ms.requests.repository.RequestRepository;
import com.tecmilenio.carlos.ms.requests.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final EmployeeFeignClient employeeFeignClient;
    private final JwtUtils jwtUtils;

    public RequestServiceImpl(RequestRepository requestRepository,
                            RequestMapper requestMapper,
                            EmployeeFeignClient employeeFeignClient,
                            JwtUtils jwtUtils) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
        this.employeeFeignClient = employeeFeignClient;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public RequestDto createRequest(CreateRequestDto createRequestDto, HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long employeeId = jwtUtils.extractEmployeeId(token);
        
        if (employeeId == null) {
            throw new RuntimeException("No se pudo extraer el ID del empleado del token");
        }
        
        createRequestDto.setIdEmployee(employeeId);
        
        try {
            EmployeeDto employee = employeeFeignClient.getEmployeeById(employeeId);
            if (!"active".equals(employee.getStatus())) {
                throw new EmployeeNotFoundException("El empleado no está activo");
            }
        } catch (Exception e) {
            throw new EmployeeNotFoundException("Empleado no encontrado con ID: " + employeeId);
        }

        Request requestEntity = requestMapper.toEntity(createRequestDto);
        Request savedRequest = requestRepository.save(requestEntity);
        return requestMapper.toDto(savedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public RequestDto getRequestById(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Solicitud no encontrada con ID: " + requestId));
        return requestMapper.toDto(request);
    }

    @Override
    public RequestDto updateRequestStatus(Long requestId, UpdateStatusDto updateStatusDto) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Solicitud no encontrada con ID: " + requestId));

        // Validar transición de estado
        if (!isValidStatusTransition(request.getStatus(), updateStatusDto.getStatus())) {
            throw new InvalidStatusTransitionException(
                    String.format("No se puede cambiar de %s a %s", 
                            request.getStatus().getDisplayName(), 
                            updateStatusDto.getStatus().getDisplayName()));
        }

        request.setStatus(updateStatusDto.getStatus());
        
        // Si hay una razón, agregarla a la descripción
        if (updateStatusDto.getReason() != null && !updateStatusDto.getReason().trim().isEmpty()) {
            String currentDescription = request.getDescription() != null ? request.getDescription() : "";
            request.setDescription(currentDescription + 
                    (currentDescription.isEmpty() ? "" : "\n\n") + 
                    "Comentario: " + updateStatusDto.getReason());
        }

        Request savedRequest = requestRepository.save(request);
        return requestMapper.toDto(savedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getEmployeeRequests(HttpServletRequest request, Pageable pageable) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long employeeId = jwtUtils.extractEmployeeId(token);
        
        if (employeeId == null) {
            throw new RuntimeException("No se pudo extraer el ID del empleado del token");
        }
        
        // Validar que el empleado existe
        try {
            employeeFeignClient.getEmployeeById(employeeId);
        } catch (Exception e) {
            throw new EmployeeNotFoundException("Empleado no encontrado con ID: " + employeeId);
        }

        Page<Request> requests = requestRepository.findByIdEmployeeOrderByCreatedAtDesc(employeeId, pageable);
        return requestMapper.toDtoList(requests.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getEmployeeRequestsByType(HttpServletRequest request, RequestType requestType, Pageable pageable) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long employeeId = jwtUtils.extractEmployeeId(token);
        
        if (employeeId == null) {
            throw new RuntimeException("No se pudo extraer el ID del empleado del token");
        }
        
        // Validar que el empleado existe
        try {
            employeeFeignClient.getEmployeeById(employeeId);
        } catch (Exception e) {
            throw new EmployeeNotFoundException("Empleado no encontrado con ID: " + employeeId);
        }

        Page<Request> requests = requestRepository.findByIdEmployeeAndRequestTypeOrderByCreatedAtDesc(employeeId, requestType, pageable);
        return requestMapper.toDtoList(requests.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getEmployeeRequestsByStatus(HttpServletRequest request, RequestStatus status, Pageable pageable) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long employeeId = jwtUtils.extractEmployeeId(token);
        
        if (employeeId == null) {
            throw new RuntimeException("No se pudo extraer el ID del empleado del token");
        }
        
        // Validar que el empleado existe
        try {
            employeeFeignClient.getEmployeeById(employeeId);
        } catch (Exception e) {
            throw new EmployeeNotFoundException("Empleado no encontrado con ID: " + employeeId);
        }

        Page<Request> requests = requestRepository.findByIdEmployeeAndStatusOrderByCreatedAtDesc(employeeId, status, pageable);
        return requestMapper.toDtoList(requests.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getCompanyRequests(HttpServletRequest request, Pageable pageable) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long companyId = jwtUtils.extractCompanyId(token);
        
        if (companyId == null) {
            throw new RuntimeException("No se pudo extraer el ID de la empresa del token");
        }
        
        // Obtener todos los empleados de la empresa
        List<EmployeeDto> employees = employeeFeignClient.getEmployeesByCompanyId(companyId);
        List<Long> employeeIds = employees.stream().map(EmployeeDto::getId).toList();
        
        if (employeeIds.isEmpty()) {
            return List.of();
        }

        Page<Request> requests = requestRepository.findByIdEmployeeInOrderByCreatedAtDesc(employeeIds, pageable);
        return requestMapper.toDtoList(requests.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getCompanyRequestsByType(HttpServletRequest request, RequestType requestType, Pageable pageable) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long companyId = jwtUtils.extractCompanyId(token);
        
        if (companyId == null) {
            throw new RuntimeException("No se pudo extraer el ID de la empresa del token");
        }
        
        // Obtener todos los empleados de la empresa
        List<EmployeeDto> employees = employeeFeignClient.getEmployeesByCompanyId(companyId);
        List<Long> employeeIds = employees.stream().map(EmployeeDto::getId).toList();
        
        if (employeeIds.isEmpty()) {
            return List.of();
        }

        Page<Request> requests = requestRepository.findByIdEmployeeInAndRequestTypeOrderByCreatedAtDesc(employeeIds, requestType, pageable);
        return requestMapper.toDtoList(requests.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getCompanyRequestsByStatus(HttpServletRequest request, RequestStatus status, Pageable pageable) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long companyId = jwtUtils.extractCompanyId(token);
        
        if (companyId == null) {
            throw new RuntimeException("No se pudo extraer el ID de la empresa del token");
        }
        
        // Obtener todos los empleados de la empresa
        List<EmployeeDto> employees = employeeFeignClient.getEmployeesByCompanyId(companyId);
        List<Long> employeeIds = employees.stream().map(EmployeeDto::getId).toList();
        
        if (employeeIds.isEmpty()) {
            return List.of();
        }

        Page<Request> requests = requestRepository.findByIdEmployeeInAndStatusOrderByCreatedAtDesc(employeeIds, status, pageable);
        return requestMapper.toDtoList(requests.getContent());
    }

    private boolean isValidStatusTransition(RequestStatus currentStatus, RequestStatus newStatus) {
        // Lógica básica de transiciones válidas
        switch (currentStatus) {
            case RECEIVED:
                return newStatus == RequestStatus.IN_REVIEW || newStatus == RequestStatus.REJECTED;
            case IN_REVIEW:
                return newStatus == RequestStatus.APPROVED || newStatus == RequestStatus.REJECTED;
            case APPROVED:
                return newStatus == RequestStatus.CLOSED;
            case REJECTED:
                return newStatus == RequestStatus.CLOSED || newStatus == RequestStatus.IN_REVIEW;
            case CLOSED:
                return false; // No se puede cambiar desde cerrado
            default:
                return false;
        }
    }
}
