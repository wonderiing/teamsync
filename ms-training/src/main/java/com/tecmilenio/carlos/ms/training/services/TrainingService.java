package com.tecmilenio.carlos.ms.training.services;

import com.tecmilenio.carlos.ms.training.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TrainingService {

    // Tutorial management
    CourseDto createTutorial(CreateCourseDto createTutorialDto);

    List<CourseDto> getAllTutorials(Pageable pageable);

    List<CourseDto> getCompanyTutorials(Long companyId, Pageable pageable);

    List<CourseDto> getTutorialsByCategory(String category, Pageable pageable);

    CourseDto getTutorialById(Long tutorialId);

    CourseDto updateTutorial(Long tutorialId, CreateCourseDto updateTutorialDto);

    void deleteTutorial(Long tutorialId);

    List<CourseDto> searchTutorials(String query, Pageable pageable);
}
