package com.tecmilenio.carlos.ms.training.services;

import com.tecmilenio.carlos.ms.training.clients.CompanyFeignClient;
import com.tecmilenio.carlos.ms.training.dto.*;
import com.tecmilenio.carlos.ms.training.entities.Course;
import com.tecmilenio.carlos.ms.training.entities.CourseStatus;
import com.tecmilenio.carlos.ms.training.exceptions.*;
import com.tecmilenio.carlos.ms.training.mapper.CourseMapper;
import com.tecmilenio.carlos.ms.training.repository.CourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TrainingServiceImpl implements TrainingService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CompanyFeignClient companyFeignClient;

    public TrainingServiceImpl(CourseRepository courseRepository,
                             CourseMapper courseMapper,
                             CompanyFeignClient companyFeignClient) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.companyFeignClient = companyFeignClient;
    }

    @Override
    public CourseDto createTutorial(CreateCourseDto createTutorialDto) {
        // Validar que la empresa existe
        validateCompanyExists(createTutorialDto.getIdCompany());
        
        Course tutorial = courseMapper.toEntity(createTutorialDto);
        Course savedTutorial = courseRepository.save(tutorial);
        return courseMapper.toDto(savedTutorial);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getAllTutorials(Pageable pageable) {
        Page<Course> tutorials = courseRepository.findByStatusOrderByCreatedAtDesc(
                CourseStatus.ACTIVE, pageable);
        return courseMapper.toDtoList(tutorials.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getCompanyTutorials(Long companyId, Pageable pageable) {
        // Validar que la empresa existe
        validateCompanyExists(companyId);
        
        Page<Course> tutorials = courseRepository.findByIdCompanyAndStatusOrderByCreatedAtDesc(
                companyId, CourseStatus.ACTIVE, pageable);
        return courseMapper.toDtoList(tutorials.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getTutorialsByCategory(String category, Pageable pageable) {
        Page<Course> tutorials = courseRepository.findByCategoryAndStatusOrderByCreatedAtDesc(
                category, CourseStatus.ACTIVE, pageable);
        return courseMapper.toDtoList(tutorials.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDto getTutorialById(Long tutorialId) {
        Course tutorial = courseRepository.findById(tutorialId)
                .orElseThrow(() -> new CourseNotFoundException("Tutorial no encontrado con ID: " + tutorialId));
        return courseMapper.toDto(tutorial);
    }

    @Override
    public CourseDto updateTutorial(Long tutorialId, CreateCourseDto updateTutorialDto) {
        // Validar que la empresa existe
        validateCompanyExists(updateTutorialDto.getIdCompany());
        
        Course tutorial = courseRepository.findById(tutorialId)
                .orElseThrow(() -> new CourseNotFoundException("Tutorial no encontrado con ID: " + tutorialId));

        tutorial.setTitle(updateTutorialDto.getTitle());
        tutorial.setDescription(updateTutorialDto.getDescription());
        tutorial.setDurationMinutes(updateTutorialDto.getDurationMinutes());
        tutorial.setTutorialUrl(updateTutorialDto.getTutorialUrl());
        tutorial.setCategory(updateTutorialDto.getCategory());

        Course savedTutorial = courseRepository.save(tutorial);
        return courseMapper.toDto(savedTutorial);
    }

    @Override
    public void deleteTutorial(Long tutorialId) {
        Course tutorial = courseRepository.findById(tutorialId)
                .orElseThrow(() -> new CourseNotFoundException("Tutorial no encontrado con ID: " + tutorialId));

        // Soft delete - cambiar estado a inactivo
        tutorial.setStatus(CourseStatus.INACTIVE);
        courseRepository.save(tutorial);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> searchTutorials(String query, Pageable pageable) {
        Page<Course> tutorials = courseRepository.findByTitleContainingIgnoreCaseAndStatusOrderByCreatedAtDesc(
                query, CourseStatus.ACTIVE, pageable);
        return courseMapper.toDtoList(tutorials.getContent());
    }

    private void validateCompanyExists(Long companyId) {
        try {
            CompanyDto company = companyFeignClient.getCompanyById(companyId);
            if (company == null || !"active".equals(company.getStatus())) {
                throw new CompanyNotFoundException("Empresa no encontrada o inactiva con ID: " + companyId);
            }
        } catch (Exception e) {
            throw new CompanyNotFoundException("Empresa no encontrada con ID: " + companyId, e);
        }
    }
}
