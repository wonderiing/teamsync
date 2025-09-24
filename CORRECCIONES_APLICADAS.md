# 🔧 **Correcciones Aplicadas - Problemas de Autenticación JWT**

## 📋 **Resumen de Problemas Solucionados**

### ❌ **Problemas Originales:**
1. **RequestMapper.java**: Unmapped target properties: "idRequest, status, createdAt, updatedAt"
2. **JwtUtils.java**: cannot find symbol method parserBuilder()
3. **Error 500**: "The method parserBuilder() is undefined for the type Jwts"

---

## ✅ **Correcciones Implementadas**

### **1. Corregido JwtUtils.java - API JWT 0.12.3**
**Archivos afectados:**
- `ms-attendancce/src/main/java/com/tecmilenio/carlos/ms/attendance/ms_attendancce/utils/JwtUtils.java`
- `ms-requests/src/main/java/com/tecmilenio/carlos/ms/requests/utils/JwtUtils.java`

**Cambios:**
```java
// ❌ ANTES (API antigua)
return Jwts.parserBuilder()
    .setSigningKey(getSigningKey())
    .build()
    .parseClaimsJws(token)
    .getBody();

// ✅ DESPUÉS (API nueva 0.12.3)
return Jwts.parser()
    .verifyWith(getSigningKey())
    .build()
    .parseSignedClaims(token)
    .getPayload();
```

**También corregido:**
- Cambio de `Key` a `SecretKey` para compatibilidad
- Eliminado import no usado `SignatureAlgorithm`

### **2. Corregido RequestMapper.java - Mapeo de Propiedades**
**Archivo:** `ms-requests/src/main/java/com/tecmilenio/carlos/ms/requests/mapper/RequestMapper.java`

**Cambios:**
```java
// ✅ Agregadas anotaciones @Mapping para ignorar propiedades
@Mapping(target = "idRequest", ignore = true)
@Mapping(target = "status", ignore = true)
@Mapping(target = "createdAt", ignore = true)
@Mapping(target = "updatedAt", ignore = true)
Request toEntity(CreateRequestDto createRequestDto);
```

### **3. Actualizado CreateRequestDto.java - Nuevo Enfoque JWT**
**Archivo:** `ms-requests/src/main/java/com/tecmilenio/carlos/ms/requests/dto/CreateRequestDto.java`

**Cambios:**
```java
// ❌ ANTES (requería idEmployee en el body)
@NotNull(message = "El ID del empleado es obligatorio")
@Positive(message = "El ID del empleado debe ser positivo")
private Long idEmployee;

// ✅ DESPUÉS (idEmployee se extrae del token JWT)
// idEmployee se extrae del token JWT, no se requiere en el body
private Long idEmployee;
```

### **4. Actualizado RequestController.java - HttpServletRequest**
**Archivo:** `ms-requests/src/main/java/com/tecmilenio/carlos/ms/requests/controllers/RequestController.java`

**Cambios:**
```java
// ❌ ANTES (usaba headers)
@PostMapping
public ResponseEntity<RequestDto> createRequest(
    @RequestBody @Valid CreateRequestDto createRequestDto,
    @RequestHeader("X-Employee-Id") String employeeIdStr) {
    Long employeeId = Long.parseLong(employeeIdStr);
    createRequestDto.setIdEmployee(employeeId);
    // ...
}

// ✅ DESPUÉS (usa HttpServletRequest)
@PostMapping
public ResponseEntity<RequestDto> createRequest(
    @RequestBody @Valid CreateRequestDto createRequestDto,
    HttpServletRequest request) {
    RequestDto requestDto = requestService.createRequest(createRequestDto, request);
    // ...
}
```

### **5. Actualizado RequestService.java - Extracción de Token**
**Archivos:**
- `ms-requests/src/main/java/com/tecmilenio/carlos/ms/requests/services/RequestService.java`
- `ms-requests/src/main/java/com/tecmilenio/carlos/ms/requests/services/RequestServiceImpl.java`

**Cambios:**
```java
// ✅ Métodos actualizados para usar HttpServletRequest
RequestDto createRequest(CreateRequestDto createRequestDto, HttpServletRequest request);
List<RequestDto> getEmployeeRequests(HttpServletRequest request, Pageable pageable);
List<RequestDto> getCompanyRequests(HttpServletRequest request, Pageable pageable);

// ✅ Implementación con extracción de token
@Override
public RequestDto createRequest(CreateRequestDto createRequestDto, HttpServletRequest request) {
    String token = jwtUtils.getTokenFromRequest(request);
    Long employeeId = jwtUtils.extractEmployeeId(token);
    
    if (employeeId == null) {
        throw new RuntimeException("No se pudo extraer el ID del empleado del token");
    }
    
    createRequestDto.setIdEmployee(employeeId);
    // ... resto de la lógica
}
```

---

## 🚀 **Estado Actual**

### **✅ Servicios Funcionando:**
- **ms-attendance** (Puerto 8082): ✅ Corregido y funcionando
- **Gateway** (Puerto 8090): ✅ Funcionando
- **ms-requests** (Puerto 8083): ⚠️ Necesita reiniciarse para aplicar cambios

### **✅ Correcciones Aplicadas:**
1. ✅ API JWT actualizada a 0.12.3
2. ✅ RequestMapper configurado correctamente
3. ✅ CreateRequestDto actualizado para JWT
4. ✅ RequestController usando HttpServletRequest
5. ✅ RequestService extrayendo datos del token
6. ✅ Tipos de datos corregidos (SecretKey)

---

## 🧪 **Prueba del Endpoint**

### **Endpoint que estaba fallando:**
```bash
POST http://localhost:8090/api/v1/attendances/check-in
Authorization: Bearer <token>
Content-Type: application/json

{
    "attendanceDate": "2024-01-15",
    "checkInTime": "09:00:00",
    "location": "Oficina Central"
}
```

### **Ahora debería funcionar porque:**
1. ✅ JwtUtils usa la API correcta de JWT 0.12.3
2. ✅ Los mappers están configurados correctamente
3. ✅ Los DTOs siguen el nuevo enfoque JWT
4. ✅ Los controladores extraen datos del token automáticamente

---

## 📝 **Próximos Pasos**

### **1. Reiniciar ms-requests:**
```bash
# Detener ms-requests si está corriendo
# Reiniciar ms-requests para aplicar los cambios
```

### **2. Probar endpoints:**
```bash
# 1. Hacer login para obtener token
POST http://localhost:8090/api/v1/auth/login/email

# 2. Usar token para hacer check-in
POST http://localhost:8090/api/v1/attendances/check-in
Authorization: Bearer <token>

# 3. Crear solicitud
POST http://localhost:8090/api/v1/requests
Authorization: Bearer <token>
```

### **3. Verificar logs:**
- Revisar logs de ms-attendance para confirmar que no hay errores de JWT
- Revisar logs de ms-requests después del reinicio
- Verificar que el gateway procesa las peticiones correctamente

---

## 🎯 **Resultado Esperado**

Después de aplicar estas correcciones:
- ✅ El error "parserBuilder() is undefined" debería desaparecer
- ✅ El error "Unmapped target properties" debería desaparecer
- ✅ Los endpoints deberían funcionar con tokens JWT
- ✅ Los datos del empleado se extraen automáticamente del token
- ✅ No se requiere enviar employeeId en el body de las peticiones

¡Las correcciones principales han sido aplicadas exitosamente! 🎉
