# 🔐 **Enfoque JWT Token - Extracción Directa de Claims**

## 📋 **Resumen de Cambios**

Se ha implementado un enfoque más limpio y eficiente donde **cada microservicio extrae directamente la información del token JWT** en lugar de usar headers automáticos del gateway.

---

## 🔧 **Cambios Implementados**

### **1. Utilidad JWT Común**
Cada microservicio tiene su propia utilidad `JwtUtils.java` que:
- Extrae el token del header `Authorization: Bearer <token>`
- Parsea los claims del JWT
- Obtiene `employeeId`, `companyId`, `username`, `role`

### **2. DTOs Simplificados**
Los DTOs ya **NO requieren** `employeeId` en el body:
```java
// ❌ ANTES (requería employeeId en el body)
{
    "idEmployee": 123,
    "notes": "Check-in notes"
}

// ✅ AHORA (employeeId se extrae del token)
{
    "notes": "Check-in notes"
}
```

### **3. Controladores Simplificados**
Los controladores ahora reciben `HttpServletRequest` en lugar de headers:
```java
// ❌ ANTES
@PostMapping("/check-in")
public ResponseEntity<AttendanceDto> checkIn(
    @RequestBody CheckInDto dto,
    @RequestHeader("X-Employee-Id") String employeeIdStr) {
    // ...
}

// ✅ AHORA
@PostMapping("/check-in")
public ResponseEntity<AttendanceDto> checkIn(
    @RequestBody CheckInDto dto,
    HttpServletRequest request) {
    // employeeId se extrae automáticamente del token
}
```

### **4. Servicios con Extracción de Token**
Los servicios extraen la información del token:
```java
@Override
public AttendanceDto checkIn(CheckInDto checkInDto, HttpServletRequest request) {
    String token = jwtUtils.getTokenFromRequest(request);
    Long employeeId = jwtUtils.extractEmployeeId(token);
    
    if (employeeId == null) {
        throw new RuntimeException("No se pudo extraer el ID del empleado del token");
    }
    
    checkInDto.setIdEmployee(employeeId);
    return checkIn(checkInDto);
}
```

### **5. Gateway Simplificado**
El gateway **NO agrega headers automáticos**, solo:
- Valida el token
- Verifica roles y permisos
- Permite o deniega el acceso

---

## 📁 **Archivos Modificados**

### **ms-attendance**
- ✅ `JwtUtils.java` - Utilidad para extraer claims del token
- ✅ `CheckInDto.java` - Removidas validaciones de employeeId
- ✅ `CheckOutDto.java` - Removidas validaciones de employeeId
- ✅ `AttendanceController.java` - Usa HttpServletRequest
- ✅ `AttendanceService.java` - Métodos con extracción de token
- ✅ `AttendanceServiceImpl.java` - Implementación con JwtUtils

### **ms-requests** (Pendiente)
- ✅ `JwtUtils.java` - Creado
- ⏳ `CreateRequestDto.java` - Pendiente modificar
- ⏳ `RequestController.java` - Pendiente modificar
- ⏳ `RequestService.java` - Pendiente modificar

### **ms-employees** (Pendiente)
- ⏳ `JwtUtils.java` - Pendiente crear
- ⏳ `EmployeeController.java` - Pendiente modificar

### **ms-training** (Pendiente)
- ⏳ `JwtUtils.java` - Pendiente crear
- ⏳ `TrainingController.java` - Pendiente modificar

### **gateway**
- ✅ `RoleBasedAuthFilter.java` - Removidos headers automáticos

---

## 🚀 **Ventajas del Nuevo Enfoque**

### **1. Más Limpio**
- No hay headers automáticos
- DTOs más simples
- Menos código en controladores

### **2. Más Seguro**
- Cada microservicio valida su propio token
- No hay dependencia de headers del gateway
- Validación directa de claims

### **3. Más Eficiente**
- Menos procesamiento en el gateway
- Extracción directa de información
- Menos transferencia de datos

### **4. Más Mantenible**
- Código más claro y directo
- Menos dependencias entre servicios
- Más fácil de debuggear

---

## 📝 **Ejemplo de Uso**

### **Petición del Cliente**
```bash
POST /api/v1/attendances/check-in
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
    "notes": "Check-in desde casa"
}
```

### **Procesamiento en el Microservicio**
```java
@Override
public AttendanceDto checkIn(CheckInDto checkInDto, HttpServletRequest request) {
    // 1. Extraer token del header Authorization
    String token = jwtUtils.getTokenFromRequest(request);
    
    // 2. Extraer employeeId del token
    Long employeeId = jwtUtils.extractEmployeeId(token);
    
    // 3. Establecer employeeId en el DTO
    checkInDto.setIdEmployee(employeeId);
    
    // 4. Procesar normalmente
    return checkIn(checkInDto);
}
```

---

## ⚠️ **Consideraciones Importantes**

### **1. Configuración JWT**
Cada microservicio debe tener la misma clave secreta:
```properties
jwt.secret=mySecretKey123456789012345678901234567890
```

### **2. Dependencias JWT**
Cada microservicio necesita las dependencias JWT:
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
```

### **3. Validación de Token**
El gateway sigue validando el token antes de permitir el acceso, pero cada microservicio también puede validar si es necesario.

---

## 🔄 **Próximos Pasos**

1. **Completar ms-requests** - Aplicar el mismo patrón
2. **Completar ms-employees** - Aplicar el mismo patrón
3. **Completar ms-training** - Aplicar el mismo patrón
4. **Probar endpoints** - Verificar que todo funciona correctamente
5. **Actualizar documentación** - Reflejar los cambios en los endpoints

---

## ✅ **Estado Actual**

- ✅ **ms-attendance** - Completado
- ⏳ **ms-requests** - En progreso
- ⏳ **ms-employees** - Pendiente
- ⏳ **ms-training** - Pendiente
- ✅ **gateway** - Simplificado

¡El nuevo enfoque es mucho más limpio y eficiente! 🎉
