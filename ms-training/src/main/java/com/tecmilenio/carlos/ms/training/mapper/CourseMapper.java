package com.tecmilenio.carlos.ms.training.mapper;

import com.tecmilenio.carlos.ms.training.dto.CourseDto;
import com.tecmilenio.carlos.ms.training.dto.CreateCourseDto;
import com.tecmilenio.carlos.ms.training.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {

    CourseDto toDto(Course course);

    Course toEntity(CourseDto courseDto);

    Course toEntity(CreateCourseDto createCourseDto);

    List<CourseDto> toDtoList(List<Course> courses);

    List<Course> toEntityList(List<CourseDto> courseDtos);
}
