package com.tecmilenio.carlos.ms.auth.repositories;

import com.tecmilenio.carlos.ms.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByEmployeeId(Long employeeId);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
