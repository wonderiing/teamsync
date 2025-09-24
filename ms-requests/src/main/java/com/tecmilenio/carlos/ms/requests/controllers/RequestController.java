package com.tecmilenio.carlos.ms.requests.controllers;

import com.tecmilenio.carlos.ms.requests.dto.CreateRequestDto;
import com.tecmilenio.carlos.ms.requests.dto.RequestDto;
import com.tecmilenio.carlos.ms.requests.dto.UpdateStatusDto;
import com.tecmilenio.carlos.ms.requests.entities.RequestStatus;
import com.tecmilenio.carlos.ms.requests.entities.RequestType;
import com.tecmilenio.carlos.ms.requests.services.RequestService;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/v1/requests")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@Validated
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<RequestDto> createRequest(@RequestBody @Valid CreateRequestDto createRequestDto,
                                                  HttpServletRequest request) {
        RequestDto requestDto = requestService.createRequest(createRequestDto, request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(requestDto.getIdRequest())
                .toUri();
        return ResponseEntity.created(location).body(requestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestDto> getRequestById(@PathVariable @Min(1) Long id) {
        RequestDto request = requestService.getRequestById(id);
        return ResponseEntity.ok(request);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<RequestDto> updateRequestStatus(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid UpdateStatusDto updateStatusDto) {
        RequestDto request = requestService.updateRequestStatus(id, updateStatusDto);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/my-requests")
    public ResponseEntity<List<RequestDto>> getMyRequests(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) RequestType type,
            @RequestParam(required = false) RequestStatus status) {
        
        Pageable pageable = PageRequest.of(page, size);
        List<RequestDto> requests;
        
        if (type != null) {
            requests = requestService.getEmployeeRequestsByType(request, type, pageable);
        } else if (status != null) {
            requests = requestService.getEmployeeRequestsByStatus(request, status, pageable);
        } else {
            requests = requestService.getEmployeeRequests(request, pageable);
        }
        
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/company-requests")
    public ResponseEntity<List<RequestDto>> getCompanyRequests(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) RequestType type,
            @RequestParam(required = false) RequestStatus status) {
        
        Pageable pageable = PageRequest.of(page, size);
        List<RequestDto> requests;
        
        if (type != null) {
            requests = requestService.getCompanyRequestsByType(request, type, pageable);
        } else if (status != null) {
            requests = requestService.getCompanyRequestsByStatus(request, status, pageable);
        } else {
            requests = requestService.getCompanyRequests(request, pageable);
        }
        
        return ResponseEntity.ok(requests);
    }
}
