package com.tecmilenio.carlos.ms.auth.config;

import com.tecmilenio.carlos.ms.auth.entities.Role;
import com.tecmilenio.carlos.ms.auth.entities.User;
import com.tecmilenio.carlos.ms.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existen usuarios
        if (userRepository.count() == 0) {
            createTestUsers();
        }
    }
    
    private void createTestUsers() {
        // Usuario empleado 1
        User empleado1 = new User();
        empleado1.setUsername("empleado1");
        empleado1.setEmail("empleado1@test.com");
        empleado1.setPassword(passwordEncoder.encode("password"));
        empleado1.setRole(Role.EMPLOYEE);
        empleado1.setEmployeeId(1L);
        empleado1.setCompanyId(1L);
        userRepository.save(empleado1);
        
        // Usuario HR 1
        User hr1 = new User();
        hr1.setUsername("hr1");
        hr1.setEmail("hr1@test.com");
        hr1.setPassword(passwordEncoder.encode("password"));
        hr1.setRole(Role.HR);
        hr1.setEmployeeId(null);
        hr1.setCompanyId(1L);
        userRepository.save(hr1);
        
        // Usuario empleado 2
        User empleado2 = new User();
        empleado2.setUsername("empleado2");
        empleado2.setEmail("empleado2@test.com");
        empleado2.setPassword(passwordEncoder.encode("password"));
        empleado2.setRole(Role.EMPLOYEE);
        empleado2.setEmployeeId(2L);
        empleado2.setCompanyId(1L);
        userRepository.save(empleado2);
        
        // Usuario HR 2
        User hr2 = new User();
        hr2.setUsername("hr2");
        hr2.setEmail("hr2@test.com");
        hr2.setPassword(passwordEncoder.encode("password"));
        hr2.setRole(Role.HR);
        hr2.setEmployeeId(null);
        hr2.setCompanyId(2L);
        userRepository.save(hr2);
        
        System.out.println("✅ Usuarios de prueba creados exitosamente");
    }
}
