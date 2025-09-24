package com.tecmilenio.carlos.ms.attendance.ms_attendancce.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.AttendanceDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.CheckInDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.CheckOutDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.services.AttendanceService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/v1/attendances")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@Validated
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceDto> checkIn(@RequestBody @Valid CheckInDto checkInDto, 
                                               HttpServletRequest request) {
        AttendanceDto attendance = attendanceService.checkIn(checkInDto, request);
        return new ResponseEntity<>(attendance, HttpStatus.CREATED);
    }

    @PostMapping("/check-out")
    public ResponseEntity<AttendanceDto> checkOut(@RequestBody @Valid CheckOutDto checkOutDto,
                                                HttpServletRequest request) {
        AttendanceDto attendance = attendanceService.checkOut(checkOutDto, request);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDto> getAttendanceById(@PathVariable @Min(1) Long id) {
        AttendanceDto attendance = attendanceService.getAttendanceById(id);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/my-attendances")
    public ResponseEntity<List<AttendanceDto>> getMyAttendances(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        List<AttendanceDto> attendances = attendanceService.getEmployeeAttendances(request, pageable);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/my-attendances/range")
    public ResponseEntity<List<AttendanceDto>> getMyAttendancesByDateRange(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<AttendanceDto> attendances = attendanceService.getEmployeeAttendancesByDateRange(request, startDate, endDate);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/company-attendances")
    public ResponseEntity<List<AttendanceDto>> getCompanyAttendances(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        List<AttendanceDto> attendances = attendanceService.getCompanyAttendances(request, pageable);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/company-attendances/range")
    public ResponseEntity<List<AttendanceDto>> getCompanyAttendancesByDateRange(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<AttendanceDto> attendances = attendanceService.getCompanyAttendancesByDateRange(request, startDate, endDate);
        return ResponseEntity.ok(attendances);
    }
}
