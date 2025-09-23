package com.tecmilenio.carlos.ms.training.repository;

import com.tecmilenio.carlos.ms.training.entities.Course;
import com.tecmilenio.carlos.ms.training.entities.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByIdCompanyOrderByCreatedAtDesc(Long idCompany, Pageable pageable);

    Page<Course> findByIdCompanyAndStatusOrderByCreatedAtDesc(Long idCompany, CourseStatus status, Pageable pageable);

    Page<Course> findByStatusOrderByCreatedAtDesc(CourseStatus status, Pageable pageable);

    Page<Course> findByCategoryAndStatusOrderByCreatedAtDesc(String category, CourseStatus status, Pageable pageable);

    Page<Course> findByTitleContainingIgnoreCaseAndStatusOrderByCreatedAtDesc(String title, CourseStatus status, Pageable pageable);

    long countByIdCompany(Long idCompany);

    long countByIdCompanyAndStatus(Long idCompany, CourseStatus status);

    long countByStatus(CourseStatus status);

    long countByCategoryAndStatus(String category, CourseStatus status);
}
