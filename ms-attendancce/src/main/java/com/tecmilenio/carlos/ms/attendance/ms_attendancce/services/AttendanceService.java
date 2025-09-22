package com.tecmilenio.carlos.ms.attendance.ms_attendancce.services;

import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.AttendanceDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.CheckInDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.CheckOutDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    AttendanceDto checkIn(CheckInDto checkInDto);

    AttendanceDto checkOut(CheckOutDto checkOutDto);

    List<AttendanceDto> getEmployeeAttendances(Long employeeId, Pageable pageable);

    List<AttendanceDto> getEmployeeAttendancesByDateRange(Long employeeId, LocalDate startDate, LocalDate endDate);

    List<AttendanceDto> getCompanyAttendances(Long companyId, Pageable pageable);

    List<AttendanceDto> getCompanyAttendancesByDateRange(Long companyId, LocalDate startDate, LocalDate endDate);

    AttendanceDto getAttendanceById(Long attendanceId);
}
