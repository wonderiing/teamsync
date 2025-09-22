package com.tecmilenio.carlos.ms.attendance.ms_attendancce.repository;

import com.tecmilenio.carlos.ms.attendance.ms_attendancce.entities.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByIdEmployeeAndAttendanceDate(Long idEmployee, LocalDate attendanceDate);

    Page<Attendance> findByIdEmployeeOrderByAttendanceDateDesc(Long idEmployee, Pageable pageable);

    List<Attendance> findByIdEmployeeAndAttendanceDateBetweenOrderByAttendanceDateDesc(
            Long idEmployee, LocalDate startDate, LocalDate endDate);

    // Estos métodos necesitarán ser implementados en el servicio usando el cliente Feign
    // para obtener los empleados de una empresa primero
    List<Attendance> findByIdEmployeeInAndAttendanceDateBetweenOrderByAttendanceDateDesc(
            List<Long> employeeIds, LocalDate startDate, LocalDate endDate);

    Page<Attendance> findByIdEmployeeInOrderByAttendanceDateDesc(List<Long> employeeIds, Pageable pageable);
}
