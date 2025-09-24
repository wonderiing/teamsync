package com.tecmilenio.carlos.ms.attendance.ms_attendancce.services;

import com.tecmilenio.carlos.ms.attendance.ms_attendancce.clients.EmployeeFeignClient;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.AttendanceDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.CheckInDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.CheckOutDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.EmployeeDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.entities.Attendance;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.exceptions.*;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.mapper.AttendanceMapper;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.repository.AttendanceRepository;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;
    private final EmployeeFeignClient employeeFeignClient;

    @Autowired
    private JwtUtils jwtUtils;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                               AttendanceMapper attendanceMapper,
                               EmployeeFeignClient employeeFeignClient) {
        this.attendanceRepository = attendanceRepository;
        this.attendanceMapper = attendanceMapper;
        this.employeeFeignClient = employeeFeignClient;
    }

    @Override
    public AttendanceDto checkIn(CheckInDto checkInDto) {
        try {
            EmployeeDto employee = employeeFeignClient.getEmployeeById(checkInDto.getIdEmployee());
            if (!"active".equals(employee.getStatus())) {
                throw new EmployeeNotFoundException("El empleado no está activo");
            }
        } catch (Exception e) {
            throw new EmployeeNotFoundException("Empleado no encontrado con ID: " + checkInDto.getIdEmployee());
        }

        LocalDate today = LocalDate.now();
        
        // Verificar si ya registró entrada hoy
        Optional<Attendance> existingAttendance = attendanceRepository
                .findByIdEmployeeAndAttendanceDate(checkInDto.getIdEmployee(), today);
        
        if (existingAttendance.isPresent() && existingAttendance.get().getCheckInTime() != null) {
            throw new AlreadyCheckedInException("El empleado ya registró entrada el día de hoy");
        }

        Attendance attendance;
        if (existingAttendance.isPresent()) {
            // Actualizar registro existente
            attendance = existingAttendance.get();
        } else {
            // Crear nuevo registro
            attendance = new Attendance();
            attendance.setIdEmployee(checkInDto.getIdEmployee());
            attendance.setAttendanceDate(today);
        }

        attendance.setCheckInTime(LocalTime.now());
        attendance.setNotes(checkInDto.getNotes());

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return attendanceMapper.toDto(savedAttendance);
    }

    @Override
    public AttendanceDto checkOut(CheckOutDto checkOutDto) {
        // Validar que el empleado existe
        try {
            employeeFeignClient.getEmployeeById(checkOutDto.getIdEmployee());
        } catch (Exception e) {
            throw new EmployeeNotFoundException("Empleado no encontrado con ID: " + checkOutDto.getIdEmployee());
        }

        LocalDate today = LocalDate.now();
        
        // Buscar registro de entrada del día
        Attendance attendance = attendanceRepository
                .findByIdEmployeeAndAttendanceDate(checkOutDto.getIdEmployee(), today)
                .orElseThrow(() -> new NotCheckedInException("El empleado no ha registrado entrada el día de hoy"));

        if (attendance.getCheckInTime() == null) {
            throw new NotCheckedInException("El empleado no ha registrado entrada el día de hoy");
        }

        if (attendance.getCheckOutTime() != null) {
            throw new AlreadyCheckedInException("El empleado ya registró salida el día de hoy");
        }

        LocalTime checkOutTime = LocalTime.now();
        attendance.setCheckOutTime(checkOutTime);
        
        // Calcular horas trabajadas
        Duration duration = Duration.between(attendance.getCheckInTime(), checkOutTime);
        BigDecimal workedHours = BigDecimal.valueOf(duration.toMinutes()).divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_HALF_UP);
        attendance.setWorkedHours(workedHours);

        // Actualizar notas si se proporcionan
        if (checkOutDto.getNotes() != null && !checkOutDto.getNotes().trim().isEmpty()) {
            String existingNotes = attendance.getNotes() != null ? attendance.getNotes() : "";
            attendance.setNotes(existingNotes + (existingNotes.isEmpty() ? "" : " | ") + checkOutDto.getNotes());
        }

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return attendanceMapper.toDto(savedAttendance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceDto> getEmployeeAttendances(Long employeeId, Pageable pageable) {
        // Validar que el empleado existe
        try {
            employeeFeignClient.getEmployeeById(employeeId);
        } catch (Exception e) {
            throw new EmployeeNotFoundException("Empleado no encontrado con ID: " + employeeId);
        }

        Page<Attendance> attendances = attendanceRepository.findByIdEmployeeOrderByAttendanceDateDesc(employeeId, pageable);
        return attendanceMapper.toDtoList(attendances.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceDto> getEmployeeAttendancesByDateRange(Long employeeId, LocalDate startDate, LocalDate endDate) {
        // Validar que el empleado existe
        try {
            employeeFeignClient.getEmployeeById(employeeId);
        } catch (Exception e) {
            throw new EmployeeNotFoundException("Empleado no encontrado con ID: " + employeeId);
        }

        List<Attendance> attendances = attendanceRepository
                .findByIdEmployeeAndAttendanceDateBetweenOrderByAttendanceDateDesc(employeeId, startDate, endDate);
        return attendanceMapper.toDtoList(attendances);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceDto> getCompanyAttendances(Long companyId, Pageable pageable) {
        // Obtener todos los empleados de la empresa
        List<EmployeeDto> employees = employeeFeignClient.getEmployeesByCompanyId(companyId);
        List<Long> employeeIds = employees.stream().map(EmployeeDto::getId).toList();
        
        if (employeeIds.isEmpty()) {
            return List.of();
        }
        
        Page<Attendance> attendances = attendanceRepository.findByIdEmployeeInOrderByAttendanceDateDesc(employeeIds, pageable);
        return attendanceMapper.toDtoList(attendances.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceDto> getCompanyAttendancesByDateRange(Long companyId, LocalDate startDate, LocalDate endDate) {
        // Obtener todos los empleados de la empresa
        List<EmployeeDto> employees = employeeFeignClient.getEmployeesByCompanyId(companyId);
        List<Long> employeeIds = employees.stream().map(EmployeeDto::getId).toList();
        
        if (employeeIds.isEmpty()) {
            return List.of();
        }
        
        List<Attendance> attendances = attendanceRepository
                .findByIdEmployeeInAndAttendanceDateBetweenOrderByAttendanceDateDesc(employeeIds, startDate, endDate);
        return attendanceMapper.toDtoList(attendances);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceDto getAttendanceById(Long attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new AttendanceNotFoundException("Asistencia no encontrada con ID: " + attendanceId));
        return attendanceMapper.toDto(attendance);
    }

    // Nuevos métodos que extraen información del token JWT

    @Override
    public AttendanceDto checkIn(CheckInDto checkInDto, HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long employeeId = jwtUtils.extractEmployeeId(token);
        
        if (employeeId == null) {
            throw new RuntimeException("No se pudo extraer el ID del empleado del token");
        }
        
        checkInDto.setIdEmployee(employeeId);
        return checkIn(checkInDto);
    }

    @Override
    public AttendanceDto checkOut(CheckOutDto checkOutDto, HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long employeeId = jwtUtils.extractEmployeeId(token);
        
        if (employeeId == null) {
            throw new RuntimeException("No se pudo extraer el ID del empleado del token");
        }
        
        checkOutDto.setIdEmployee(employeeId);
        return checkOut(checkOutDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceDto> getEmployeeAttendances(HttpServletRequest request, Pageable pageable) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long employeeId = jwtUtils.extractEmployeeId(token);
        
        if (employeeId == null) {
            throw new RuntimeException("No se pudo extraer el ID del empleado del token");
        }
        
        return getEmployeeAttendances(employeeId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceDto> getEmployeeAttendancesByDateRange(HttpServletRequest request, LocalDate startDate, LocalDate endDate) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long employeeId = jwtUtils.extractEmployeeId(token);
        
        if (employeeId == null) {
            throw new RuntimeException("No se pudo extraer el ID del empleado del token");
        }
        
        return getEmployeeAttendancesByDateRange(employeeId, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceDto> getCompanyAttendances(HttpServletRequest request, Pageable pageable) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long companyId = jwtUtils.extractCompanyId(token);
        
        if (companyId == null) {
            throw new RuntimeException("No se pudo extraer el ID de la empresa del token");
        }
        
        return getCompanyAttendances(companyId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceDto> getCompanyAttendancesByDateRange(HttpServletRequest request, LocalDate startDate, LocalDate endDate) {
        String token = jwtUtils.getTokenFromRequest(request);
        Long companyId = jwtUtils.extractCompanyId(token);
        
        if (companyId == null) {
            throw new RuntimeException("No se pudo extraer el ID de la empresa del token");
        }
        
        return getCompanyAttendancesByDateRange(companyId, startDate, endDate);
    }
}
