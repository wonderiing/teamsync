package com.tecmilenio.carlos.ms.attendance.ms_attendancce.services;

import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.AttendanceDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.CheckInDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.CheckOutDto;
import org.springframework.data.domain.Pageable;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    // Métodos originales (mantener para compatibilidad)
    AttendanceDto checkIn(CheckInDto checkInDto);
    AttendanceDto checkOut(CheckOutDto checkOutDto);
    List<AttendanceDto> getEmployeeAttendances(Long employeeId, Pageable pageable);
    List<AttendanceDto> getEmployeeAttendancesByDateRange(Long employeeId, LocalDate startDate, LocalDate endDate);
    List<AttendanceDto> getCompanyAttendances(Long companyId, Pageable pageable);
    List<AttendanceDto> getCompanyAttendancesByDateRange(Long companyId, LocalDate startDate, LocalDate endDate);
    AttendanceDto getAttendanceById(Long attendanceId);

    // Nuevos métodos que extraen información del token
    AttendanceDto checkIn(CheckInDto checkInDto, HttpServletRequest request);
    AttendanceDto checkOut(CheckOutDto checkOutDto, HttpServletRequest request);
    List<AttendanceDto> getEmployeeAttendances(HttpServletRequest request, Pageable pageable);
    List<AttendanceDto> getEmployeeAttendancesByDateRange(HttpServletRequest request, LocalDate startDate, LocalDate endDate);
    List<AttendanceDto> getCompanyAttendances(HttpServletRequest request, Pageable pageable);
    List<AttendanceDto> getCompanyAttendancesByDateRange(HttpServletRequest request, LocalDate startDate, LocalDate endDate);
}
