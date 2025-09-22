package com.tecmilenio.carlos.ms.attendance.ms_attendancce.mapper;

import com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto.AttendanceDto;
import com.tecmilenio.carlos.ms.attendance.ms_attendancce.entities.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttendanceMapper {

    AttendanceDto toDto(Attendance attendance);

    Attendance toEntity(AttendanceDto attendanceDto);

    List<AttendanceDto> toDtoList(List<Attendance> attendances);

    List<Attendance> toEntityList(List<AttendanceDto> attendanceDtos);
}
