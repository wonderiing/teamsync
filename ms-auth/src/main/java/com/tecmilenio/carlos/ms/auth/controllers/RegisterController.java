package com.tecmilenio.carlos.ms.auth.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tecmilenio.carlos.ms.auth.dto.RegisterRequest;
import com.tecmilenio.carlos.ms.auth.dto.RegisterResponse;
import com.tecmilenio.carlos.ms.auth.entities.Role;
import com.tecmilenio.carlos.ms.auth.entities.User;
import com.tecmilenio.carlos.ms.auth.repositories.UserRepository;
import com.tecmilenio.carlos.ms.auth.services.EmployeeFeignClient;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class RegisterController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmployeeFeignClient employeeFeignClient;
    
    /**
     * Registro de empleado - busca automáticamente por email
     * POST /api/v1/auth/register/employee
     */
    @PostMapping("/register/employee")
    public ResponseEntity<?> registerEmployee(@RequestBody RegisterRequest request) {
        try {
            // Buscar empleado por email en ms-employees
            Map<String, Object> employee = employeeFeignClient.findEmployeeByEmail(request.getEmail());
            
            if (employee != null) {
                Long employeeId = Long.valueOf(employee.get("id").toString());
                Long companyId = Long.valueOf(employee.get("companyId").toString());
                
                // Verificar si el usuario ya existe
                if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                    return ResponseEntity.badRequest().body(Map.of(
                        "error", "Ya existe un usuario con este email"
                    ));
                }
                
                // Crear usuario
                User user = new User();
                user.setUsername(request.getUsername());
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setRole(Role.EMPLOYEE);
                user.setEmployeeId(employeeId);
                user.setCompanyId(companyId);
                
                userRepository.save(user);
                
                return ResponseEntity.ok(new RegisterResponse(
                    "Usuario empleado registrado exitosamente",
                    request.getUsername(),
                    Role.EMPLOYEE.getValue(),
                    employeeId,
                    companyId
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "No se encontró un empleado con el email: " + request.getEmail()
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al registrar empleado: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Registro de HR
     * POST /api/v1/auth/register/hr
     */
    @PostMapping("/register/hr")
    public ResponseEntity<?> registerHr(@RequestBody RegisterRequest request) {
        try {
            // Verificar si el usuario ya existe
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Ya existe un usuario con este email"
                ));
            }
            
            // Crear usuario HR
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.HR);
            user.setEmployeeId(null);
            user.setCompanyId(request.getCompanyId()); // HR debe especificar companyId
            
            userRepository.save(user);
            
            return ResponseEntity.ok(new RegisterResponse(
                "Usuario HR registrado exitosamente",
                request.getUsername(),
                Role.HR.getValue(),
                null,
                request.getCompanyId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al registrar HR: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Registro de Admin
     * POST /api/v1/auth/register/admin
     */
    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request) {
        try {
            // Validar que todos los campos requeridos estén presentes
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Username es requerido"
                ));
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Email es requerido"
                ));
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Password es requerido"
                ));
            }
            if (request.getCompanyId() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "CompanyId es requerido para Admin"
                ));
            }
            
            // Verificar si el usuario ya existe
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Ya existe un usuario con este email"
                ));
            }
            
            // Crear usuario Admin
            User user = new User();
            user.setUsername(request.getUsername().trim());
            user.setEmail(request.getEmail().trim());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.ADMIN);
            user.setEmployeeId(null);
            user.setCompanyId(request.getCompanyId());
            
            userRepository.save(user);
            
            return ResponseEntity.ok(new RegisterResponse(
                "Usuario Admin registrado exitosamente",
                request.getUsername(),
                Role.ADMIN.getValue(),
                null,
                request.getCompanyId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al registrar Admin: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Listar todos los usuarios (solo para Admin)
     * GET /api/v1/auth/users
     */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(Map.of(
                "users", users,
                "total", users.size(),
                "message", "Lista de usuarios obtenida exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al obtener usuarios: " + e.getMessage()
            ));
        }
    }
}