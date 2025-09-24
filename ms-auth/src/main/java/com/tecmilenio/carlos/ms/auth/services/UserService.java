package com.tecmilenio.carlos.ms.auth.services;

import com.tecmilenio.carlos.ms.auth.dto.LoginRequest;
import com.tecmilenio.carlos.ms.auth.dto.LoginResponse;
import com.tecmilenio.carlos.ms.auth.dto.UserInfo;
import com.tecmilenio.carlos.ms.auth.entities.Role;
import com.tecmilenio.carlos.ms.auth.entities.User;
import com.tecmilenio.carlos.ms.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public LoginResponse authenticate(LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }
        
        User user = userOpt.get();
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        String token = jwtService.generateToken(
            user.getUsername(),
            user.getRole().getValue(),
            user.getEmployeeId(),
            user.getCompanyId()
        );
        
        return new LoginResponse(
            token,
            user.getUsername(),
            user.getRole().getValue(),
            user.getEmployeeId(),
            user.getCompanyId()
        );
    }
    
    public LoginResponse authenticateByEmail(LoginRequest loginRequest) {
        // Usar email si está disponible, sino usar username como email
        String email = loginRequest.getEmail() != null ? loginRequest.getEmail() : loginRequest.getUsername();
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }
        
        User user = userOpt.get();
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        String token = jwtService.generateToken(
            user.getUsername(),
            user.getRole().getValue(),
            user.getEmployeeId(),
            user.getCompanyId()
        );
        
        return new LoginResponse(
            token,
            user.getUsername(),
            user.getRole().getValue(),
            user.getEmployeeId(),
            user.getCompanyId()
        );
    }
    
    public UserInfo getUserInfoFromToken(String token) {
        String username = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);
        Long employeeId = jwtService.extractEmployeeId(token);
        Long companyId = jwtService.extractCompanyId(token);
        
        return new UserInfo(username, role, employeeId, companyId);
    }
    
    public User createUser(String username, String email, String password, Role role, Long employeeId, Long companyId) {
        User user = new User(username, email, passwordEncoder.encode(password), role, employeeId, companyId);
        return userRepository.save(user);
    }
    
    public boolean validateToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            return jwtService.validateToken(token, username);
        } catch (Exception e) {
            return false;
        }
    }
}
