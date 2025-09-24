package com.tecmilenio.carlos.ms.training.controllers;

import com.tecmilenio.carlos.ms.training.dto.*;
import com.tecmilenio.carlos.ms.training.services.TrainingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tutorials")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@Validated
public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping()
    public ResponseEntity<CourseDto> createTutorial(@RequestBody @Valid CreateCourseDto createTutorialDto) {
        CourseDto tutorial = trainingService.createTutorial(createTutorialDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tutorial.getIdCourse())
                .toUri();
        return ResponseEntity.created(location).body(tutorial);
    }

    @GetMapping()
    public ResponseEntity<List<CourseDto>> getAllTutorials(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        List<CourseDto> tutorials = trainingService.getAllTutorials(pageable);
        return ResponseEntity.ok(tutorials);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CourseDto>> getCompanyTutorials(
            @PathVariable @Min(1) Long companyId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        List<CourseDto> tutorials = trainingService.getCompanyTutorials(companyId, pageable);
        return ResponseEntity.ok(tutorials);
    }

    @GetMapping("/my-company")
    public ResponseEntity<List<CourseDto>> getMyCompanyTutorials(
            @RequestHeader("X-Company-Id") String companyIdStr,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        
        Long companyId = Long.parseLong(companyIdStr);
        Pageable pageable = PageRequest.of(page, size);
        List<CourseDto> tutorials = trainingService.getCompanyTutorials(companyId, pageable);
        return ResponseEntity.ok(tutorials);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<CourseDto>> getTutorialsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        List<CourseDto> tutorials = trainingService.getTutorialsByCategory(category, pageable);
        return ResponseEntity.ok(tutorials);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getTutorialById(@PathVariable @Min(1) Long id) {
        CourseDto tutorial = trainingService.getTutorialById(id);
        return ResponseEntity.ok(tutorial);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateTutorial(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid CreateCourseDto updateTutorialDto) {
        CourseDto tutorial = trainingService.updateTutorial(id, updateTutorialDto);
        return ResponseEntity.ok(tutorial);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTutorial(@PathVariable @Min(1) Long id) {
        trainingService.deleteTutorial(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseDto>> searchTutorials(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        List<CourseDto> tutorials = trainingService.searchTutorials(query, pageable);
        return ResponseEntity.ok(tutorials);
    }
}
