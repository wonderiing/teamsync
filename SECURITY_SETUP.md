# 🔐 Configuración de Seguridad con JWT

## 📋 Resumen
Sistema de autenticación y autorización implementado con JWT para microservicios Spring Boot.

## 🏗️ Arquitectura

### Microservicios:
- **ms-auth** (Puerto 8086): Autenticación y generación de JWT
- **ms-attendance** (Puerto 8082): Gestión de asistencias
- **ms-companies** (Puerto 8085): Gestión de empresas
- **ms-employees** (Puerto 8081): Gestión de empleados
- **ms-requests** (Puerto 8083): Gestión de solicitudes
- **ms-training** (Puerto 8084): Gestión de capacitaciones
- **Gateway** (Puerto 8090): Enrutamiento y filtros de seguridad

## 🚀 Configuración Inicial

### 1. Bases de Datos
```sql
-- Crear base de datos para autenticación
CREATE DATABASE auth;

-- Las otras bases de datos ya existen:
-- companies, employees, ms_attendance, ms_requests, ms_training
```

### 2. Docker Compose
```bash
cd docker
docker-compose up -d
```

### 3. Orden de Inicio de Servicios
```bash
# 1. Eureka Server (Puerto 8761)
# 2. ms-auth (Puerto 8086)
# 3. Otros microservicios
# 4. Gateway (Puerto 8090)
```

## 🔑 Autenticación

### Endpoints de Autenticación
- `POST /api/v1/auth/login` - Iniciar sesión
- `POST /api/v1/auth/validate` - Validar token
- `GET /api/v1/test/users` - Listar usuarios (solo para testing)

### Usuarios de Prueba
| Usuario | Contraseña | Rol | Employee ID | Company ID |
|---------|------------|-----|-------------|------------|
| empleado1 | password | EMPLOYEE | 1 | 1 |
| hr1 | password | HR | null | 1 |
| empleado2 | password | EMPLOYEE | 2 | 1 |
| hr2 | password | HR | null | 2 |

## 🛡️ Endpoints Seguros

### Para Empleados (EMPLOYEE)
```
POST /api/v1/secure/attendances/check-in
POST /api/v1/secure/attendances/check-out
GET  /api/v1/secure/attendances/my-attendances
GET  /api/v1/secure/attendances/my-attendances/range
```

### Para Recursos Humanos (HR)
```
GET /api/v1/secure/attendances/company-attendances
GET /api/v1/secure/attendances/company-attendances/range
```

## 🧪 Pruebas

### 1. Login
```bash
curl -X POST http://localhost:8090/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"empleado1","password":"password"}'
```

### 2. Usar Token
```bash
curl -X GET http://localhost:8090/api/v1/secure/attendances/my-attendances \
  -H "Authorization: Bearer <token>"
```

### 3. Check-in
```bash
curl -X POST http://localhost:8090/api/v1/secure/attendances/check-in \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"notes":"Entrada desde casa"}'
```

## 🔧 Configuración JWT

### Propiedades
```properties
jwt.secret=mySecretKey123456789012345678901234567890
jwt.expiration=86400000  # 24 horas
```

### Estructura del Token
```json
{
  "sub": "username",
  "role": "EMPLOYEE|HR",
  "employeeId": 1,
  "companyId": 1,
  "iat": 1234567890,
  "exp": 1234654290
}
```

## 🚨 Solución de Problemas

### Error: "cannot find symbol class EnableEurekaClient"
- **Solución**: La anotación `@EnableEurekaClient` ya no existe en Spring Cloud 2025.0.0
- **Corrección**: Eliminar la anotación, Spring Boot auto-configura Eureka

### Error: "cannot find symbol method setEmployeeId"
- **Solución**: Los DTOs usan `setIdEmployee()` no `setEmployeeId()`
- **Corrección**: Cambiar a `checkInDto.setIdEmployee(employeeId)`

### Error: "package io.jsonwebtoken does not exist"
- **Solución**: Actualizar a JWT 0.12.3 y usar nueva API
- **Corrección**: Usar `Jwts.parser().verifyWith().build().parseSignedClaims()`

## 📝 Notas Importantes

1. **Seguridad**: El Gateway valida todos los tokens automáticamente
2. **Roles**: EMPLOYEE solo ve sus datos, HR ve datos de toda la empresa
3. **Tokens**: Se extraen automáticamente del header Authorization
4. **Base de Datos**: Los usuarios se crean automáticamente al iniciar ms-auth

## 🔄 Flujo de Autenticación

1. Cliente envía credenciales a `/api/v1/auth/login`
2. ms-auth valida credenciales y genera JWT
3. Cliente incluye JWT en header Authorization
4. Gateway valida JWT con ms-auth
5. Microservicio extrae información del token
6. Se ejecuta la lógica de negocio con autorización

## 📊 Monitoreo

- **Eureka Dashboard**: http://localhost:8761
- **Gateway**: http://localhost:8090
- **Logs**: Revisar logs de Gateway para errores de validación
