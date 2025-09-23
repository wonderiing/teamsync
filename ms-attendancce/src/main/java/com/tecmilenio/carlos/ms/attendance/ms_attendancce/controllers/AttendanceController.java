package com.tecmilenio.carlos.ms.attendance.ms_attendancce.controllers;

import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.AttendanceDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.CheckInDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.CheckOutDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.services.AttendanceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
@RequestMapping("/api/v1/attendances")
@Validated
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceDto> checkIn(@RequestBody @Valid CheckInDto checkInDto) {
        AttendanceDto attendance = attendanceService.checkIn(checkInDto);
        return new ResponseEntity<>(attendance, HttpStatus.CREATED);
    }

    @PostMapping("/check-out")
    public ResponseEntity<AttendanceDto> checkOut(@RequestBody @Valid CheckOutDto checkOutDto) {
        AttendanceDto attendance = attendanceService.checkOut(checkOutDto);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDto> getAttendanceById(@PathVariable @Min(1) Long id) {
        AttendanceDto attendance = attendanceService.getAttendanceById(id);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AttendanceDto>> getEmployeeAttendances(
            @PathVariable @Min(1) Long employeeId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        List<AttendanceDto> attendances = attendanceService.getEmployeeAttendances(employeeId, pageable);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/employee/{employeeId}/range")
    public ResponseEntity<List<AttendanceDto>> getEmployeeAttendancesByDateRange(
            @PathVariable @Min(1) Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<AttendanceDto> attendances = attendanceService.getEmployeeAttendancesByDateRange(employeeId, startDate, endDate);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<AttendanceDto>> getCompanyAttendances(
            @PathVariable @Min(1) Long companyId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        List<AttendanceDto> attendances = attendanceService.getCompanyAttendances(companyId, pageable);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/company/{companyId}/range")
    public ResponseEntity<List<AttendanceDto>> getCompanyAttendancesByDateRange(
            @PathVariable @Min(1) Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<AttendanceDto> attendances = attendanceService.getCompanyAttendancesByDateRange(companyId, startDate, endDate);
        return ResponseEntity.ok(attendances);
    }
}
