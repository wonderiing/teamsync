# 📋 Endpoints Actualizados con Seguridad por Roles

## 🔐 **Autenticación**

### Login
```bash
POST /api/v1/auth/login/email
Content-Type: application/json

{
    "email": "usuario@email.com",
    "password": "password123"
}
```

**Respuesta:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "usuario",
    "role": "EMPLOYEE",
    "employeeId": 123,
    "companyId": 456
}
```

---

## 👤 **ENDPOINTS PARA EMPLEADOS** (Role: EMPLOYEE)

### 📊 **ms-attendance (Asistencia)**

#### Registrar entrada/salida
```bash
POST /api/v1/attendances/check-in
Authorization: Bearer <token>
Content-Type: application/json

{
    "attendanceDate": "2024-01-15",
    "checkInTime": "09:00:00",
    "location": "Oficina Central"
}
```

```bash
POST /api/v1/attendances/check-out
Authorization: Bearer <token>
Content-Type: application/json

{
    "attendanceDate": "2024-01-15",
    "checkOutTime": "18:00:00"
}
```

#### Ver mis asistencias
```bash
GET /api/v1/attendances/my-attendances?page=0&size=10
Authorization: Bearer <token>
```

```bash
GET /api/v1/attendances/my-attendances/range?startDate=2024-01-01&endDate=2024-01-31
Authorization: Bearer <token>
```

#### Ver registro específico
```bash
GET /api/v1/attendances/{id}
Authorization: Bearer <token>
```

---

### 📝 **ms-requests (Solicitudes)**

#### Crear solicitud
```bash
POST /api/v1/requests
Authorization: Bearer <token>
Content-Type: application/json

{
    "requestType": "VACATION",
    "description": "Vacaciones de fin de año",
    "startDate": "2024-12-20",
    "endDate": "2024-12-30"
}
```

#### Ver mis solicitudes
```bash
GET /api/v1/requests/my-requests?page=0&size=10&type=VACATION&status=PENDING
Authorization: Bearer <token>
```

#### Ver solicitud específica
```bash
GET /api/v1/requests/{id}
Authorization: Bearer <token>
```

---

### 🎓 **ms-training (Capacitación)**

#### Ver todos los tutoriales
```bash
GET /api/v1/tutorials?page=0&size=10
Authorization: Bearer <token>
```

#### Ver tutoriales de mi empresa
```bash
GET /api/v1/tutorials/my-company?page=0&size=10
Authorization: Bearer <token>
```

#### Ver tutorial específico
```bash
GET /api/v1/tutorials/{id}
Authorization: Bearer <token>
```

#### Buscar por categoría
```bash
GET /api/v1/tutorials/category/{category}?page=0&size=10
Authorization: Bearer <token>
```

#### Buscar tutoriales
```bash
GET /api/v1/tutorials/search?query=java&page=0&size=10
Authorization: Bearer <token>
```

---

### 👥 **ms-employees (Empleados)**

#### Ver mi perfil
```bash
GET /api/v1/employees/my-profile
Authorization: Bearer <token>
```

---

## 👔 **ENDPOINTS PARA RECURSOS HUMANOS** (Role: HR)

### 📊 **ms-attendance (Asistencia)**

#### Ver asistencias de la empresa
```bash
GET /api/v1/attendances/company-attendances?page=0&size=10
Authorization: Bearer <token>
```

```bash
GET /api/v1/attendances/company-attendances/range?startDate=2024-01-01&endDate=2024-01-31
Authorization: Bearer <token>
```

---

### 📝 **ms-requests (Solicitudes)**

#### Aprobar/Rechazar solicitud
```bash
PUT /api/v1/requests/{id}/status
Authorization: Bearer <token>
Content-Type: application/json

{
    "status": "APPROVED",
    "reason": "Solicitud aprobada"
}
```

#### Ver solicitudes de la empresa
```bash
GET /api/v1/requests/company-requests?page=0&size=10&type=VACATION&status=PENDING
Authorization: Bearer <token>
```

---

### 👥 **ms-employees (Empleados)**

#### Ver todos los empleados
```bash
GET /api/v1/employees?page=0&size=10
Authorization: Bearer <token>
```

#### Ver empleados de la empresa
```bash
GET /api/v1/employees/company-employees?page=0&size=10
Authorization: Bearer <token>
```

#### Crear empleado
```bash
POST /api/v1/employees
Authorization: Bearer <token>
Content-Type: application/json

{
    "fullName": "Juan Pérez",
    "email": "juan@empresa.com",
    "telephone": "555-0123",
    "position": "Desarrollador",
    "companyId": 1,
    "departmentId": 2
}
```

#### Actualizar empleado
```bash
PUT /api/v1/employees/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
    "fullName": "Juan Pérez Actualizado",
    "email": "juan.actualizado@empresa.com",
    "telephone": "555-0124",
    "position": "Senior Developer",
    "companyId": 1,
    "departmentId": 2
}
```

#### Eliminar empleado (soft delete)
```bash
DELETE /api/v1/employees/{id}
Authorization: Bearer <token>
```

---

### 🎓 **ms-training (Capacitación)**

#### Crear tutorial
```bash
POST /api/v1/tutorials
Authorization: Bearer <token>
Content-Type: application/json

{
    "title": "Curso de Java",
    "description": "Curso completo de programación en Java",
    "category": "Programación",
    "idCompany": 1
}
```

#### Actualizar tutorial
```bash
PUT /api/v1/tutorials/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
    "title": "Curso de Java Actualizado",
    "description": "Curso completo actualizado de programación en Java",
    "category": "Programación",
    "idCompany": 1
}
```

#### Eliminar tutorial
```bash
DELETE /api/v1/tutorials/{id}
Authorization: Bearer <token>
```

---

## 👑 **ENDPOINTS PARA ADMINISTRADORES** (Role: ADMIN)

### 🏢 **ms-companies (Empresas)**

#### Ver todas las empresas
```bash
GET /api/v1/companies?page=0&size=10
Authorization: Bearer <token>
```

#### Ver empresa específica
```bash
GET /api/v1/companies/{id}
Authorization: Bearer <token>
```

#### Crear empresa
```bash
POST /api/v1/companies
Authorization: Bearer <token>
Content-Type: application/json

{
    "name": "Nueva Empresa",
    "address": "Calle 123, Ciudad",
    "phone": "555-0000",
    "email": "contacto@nuevaempresa.com"
}
```

#### Actualizar empresa
```bash
PUT /api/v1/companies/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
    "name": "Empresa Actualizada",
    "address": "Nueva Dirección 456",
    "phone": "555-0001",
    "email": "nuevo@empresa.com"
}
```

#### Eliminar empresa (soft delete)
```bash
DELETE /api/v1/companies/{id}
Authorization: Bearer <token>
```

---

### 🏬 **ms-companies/departments (Departamentos)**

#### Ver todos los departamentos
```bash
GET /api/v1/departments?page=0&size=10
Authorization: Bearer <token>
```

#### Ver departamentos de una empresa
```bash
GET /api/v1/departments/company/{companyId}?page=0&size=10
Authorization: Bearer <token>
```

#### Ver departamento específico
```bash
GET /api/v1/departments/{id}
Authorization: Bearer <token>
```

#### Crear departamento
```bash
POST /api/v1/departments
Authorization: Bearer <token>
Content-Type: application/json

{
    "name": "Nuevo Departamento",
    "description": "Descripción del departamento",
    "companyId": 1
}
```

#### Eliminar departamento
```bash
DELETE /api/v1/departments/{id}
Authorization: Bearer <token>
```

---

## 🔒 **Matriz de Permisos**

| Endpoint | EMPLOYEE | HR | ADMIN |
|----------|----------|----|----|
| `/api/v1/auth/**` | ✅ | ✅ | ✅ |
| `/api/v1/attendances/check-in` | ✅ | ✅ | ✅ |
| `/api/v1/attendances/check-out` | ✅ | ✅ | ✅ |
| `/api/v1/attendances/my-attendances` | ✅ | ✅ | ✅ |
| `/api/v1/attendances/company-attendances` | ❌ | ✅ | ✅ |
| `/api/v1/requests` (POST) | ✅ | ✅ | ✅ |
| `/api/v1/requests/my-requests` | ✅ | ✅ | ✅ |
| `/api/v1/requests/{id}/status` (PUT) | ❌ | ✅ | ✅ |
| `/api/v1/requests/company-requests` | ❌ | ✅ | ✅ |
| `/api/v1/employees/my-profile` | ✅ | ✅ | ✅ |
| `/api/v1/employees` (GET) | ❌ | ✅ | ✅ |
| `/api/v1/employees` (POST/PUT/DELETE) | ❌ | ✅ | ✅ |
| `/api/v1/tutorials` (GET) | ✅ | ✅ | ✅ |
| `/api/v1/tutorials` (POST/PUT/DELETE) | ❌ | ✅ | ✅ |
| `/api/v1/companies` (GET) | ❌ | ❌ | ✅ |
| `/api/v1/companies` (POST/PUT/DELETE) | ❌ | ❌ | ✅ |
| `/api/v1/departments` (GET) | ❌ | ❌ | ✅ |
| `/api/v1/departments` (POST/DELETE) | ❌ | ❌ | ✅ |

---

## 📝 **Notas Importantes**

1. **Token JWT**: Todos los endpoints (excepto auth) requieren el header `Authorization: Bearer <token>`
2. **Headers automáticos**: El gateway agrega automáticamente:
   - `X-User-Id`: Username del usuario
   - `X-User-Role`: Rol del usuario
   - `X-Employee-Id`: ID del empleado (si aplica)
   - `X-Company-Id`: ID de la empresa (si aplica)
3. **Paginación**: La mayoría de endpoints GET soportan `?page=0&size=10`
4. **Filtros**: Los endpoints de requests y attendances soportan filtros por tipo y estado
5. **Soft Delete**: Los endpoints DELETE realizan eliminación lógica (marcan como inactivo)
