package com.tecmilenio.carlos.ms.attendance.ms_attendancce.controllers;

import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.AttendanceDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.CheckInDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.CheckOutDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.services.AttendanceService;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/secure/attendances")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@Validated
public class SecureAttendanceController {

    private final AttendanceService attendanceService;
    private final JwtUtils jwtUtils;

    @Autowired
    public SecureAttendanceController(AttendanceService attendanceService, JwtUtils jwtUtils) {
        this.attendanceService = attendanceService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceDto> checkIn(@RequestBody @Valid CheckInDto checkInDto, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long employeeId = jwtUtils.extractEmployeeId(token);
        
        // Set employeeId from token
        checkInDto.setIdEmployee(employeeId);
        
        AttendanceDto attendance = attendanceService.checkIn(checkInDto);
        return new ResponseEntity<>(attendance, HttpStatus.CREATED);
    }

    @PostMapping("/check-out")
    public ResponseEntity<AttendanceDto> checkOut(@RequestBody @Valid CheckOutDto checkOutDto, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long employeeId = jwtUtils.extractEmployeeId(token);
        
        // Set employeeId from token
        checkOutDto.setIdEmployee(employeeId);
        
        AttendanceDto attendance = attendanceService.checkOut(checkOutDto);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/my-attendances")
    public ResponseEntity<List<AttendanceDto>> getMyAttendances(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            HttpServletRequest request) {
        
        String token = getTokenFromRequest(request);
        Long employeeId = jwtUtils.extractEmployeeId(token);
        
        Pageable pageable = PageRequest.of(page, size);
        List<AttendanceDto> attendances = attendanceService.getEmployeeAttendances(employeeId, pageable);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/my-attendances/range")
    public ResponseEntity<List<AttendanceDto>> getMyAttendancesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpServletRequest request) {
        
        String token = getTokenFromRequest(request);
        Long employeeId = jwtUtils.extractEmployeeId(token);
        
        List<AttendanceDto> attendances = attendanceService.getEmployeeAttendancesByDateRange(employeeId, startDate, endDate);
        return ResponseEntity.ok(attendances);
    }

    // HR endpoints (require HR role)
    @GetMapping("/company-attendances")
    public ResponseEntity<List<AttendanceDto>> getCompanyAttendances(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            HttpServletRequest request) {
        
        String token = getTokenFromRequest(request);
        String role = jwtUtils.extractRole(token);
        Long companyId = jwtUtils.extractCompanyId(token);
        
        if (!"HR".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Pageable pageable = PageRequest.of(page, size);
        List<AttendanceDto> attendances = attendanceService.getCompanyAttendances(companyId, pageable);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/company-attendances/range")
    public ResponseEntity<List<AttendanceDto>> getCompanyAttendancesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpServletRequest request) {
        
        String token = getTokenFromRequest(request);
        String role = jwtUtils.extractRole(token);
        Long companyId = jwtUtils.extractCompanyId(token);
        
        if (!"HR".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        List<AttendanceDto> attendances = attendanceService.getCompanyAttendancesByDateRange(companyId, startDate, endDate);
        return ResponseEntity.ok(attendances);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
