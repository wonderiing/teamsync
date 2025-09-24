# 👥 Usuarios HR y Admin - Endpoints de Registro y Login

## 🚀 Endpoints Disponibles

### 1. **Registro de Usuarios**

#### Registro de HR (Recursos Humanos)
```http
POST /api/v1/auth/register/hr
Content-Type: application/json

{
  "username": "hr1",
  "email": "hr1@empresa.com",
  "password": "password123",
  "companyId": 1
}
```

#### Registro de Admin
```http
POST /api/v1/auth/register/admin
Content-Type: application/json

{
  "username": "admin1",
  "email": "admin1@empresa.com",
  "password": "password123",
  "companyId": 1
}
```

### 2. **Login Específico por Rol**

#### Login para HR
```http
POST /api/v1/auth/login/hr
Content-Type: application/json

{
  "email": "hr1@empresa.com",
  "password": "password123"
}
```

#### Login para Admin
```http
POST /api/v1/auth/login/admin
Content-Type: application/json

{
  "email": "admin1@empresa.com",
  "password": "password123"
}
```

### 3. **Gestión de Usuarios (Solo Admin)**

#### Listar todos los usuarios
```http
GET /api/v1/auth/users
Authorization: Bearer <admin_token>
```

## 📋 Ejemplos de Usuarios de Prueba

### Usuario HR
- **Username:** `hr1`
- **Email:** `hr1@empresa.com`
- **Password:** `password123`
- **Rol:** `HR`
- **Company ID:** `1`

### Usuario Admin
- **Username:** `admin1`
- **Email:** `admin1@empresa.com`
- **Password:** `password123`
- **Rol:** `ADMIN`
- **Company ID:** `1`

## 🔐 Respuestas de Login

### Respuesta Exitosa
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "hr1",
  "email": "hr1@empresa.com",
  "role": "HR",
  "employeeId": null,
  "companyId": 1
}
```

### Respuesta de Error
```json
{
  "error": "Credenciales inválidas"
}
```

## 🛠️ Cómo Probar

1. **Registra un usuario HR:**
   ```bash
   curl -X POST http://localhost:8090/api/v1/auth/register/hr \
     -H "Content-Type: application/json" \
     -d '{
       "username": "hr1",
       "email": "hr1@empresa.com",
       "password": "password123",
       "companyId": 1
     }'
   ```

2. **Registra un usuario Admin:**
   ```bash
   curl -X POST http://localhost:8090/api/v1/auth/register/admin \
     -H "Content-Type: application/json" \
     -d '{
       "username": "admin1",
       "email": "admin1@empresa.com",
       "password": "password123",
       "companyId": 1
     }'
   ```

3. **Login como HR:**
   ```bash
   curl -X POST http://localhost:8090/api/v1/auth/login/hr \
     -H "Content-Type: application/json" \
     -d '{
       "email": "hr1@empresa.com",
       "password": "password123"
     }'
   ```

4. **Login como Admin:**
   ```bash
   curl -X POST http://localhost:8090/api/v1/auth/login/admin \
     -H "Content-Type: application/json" \
     -d '{
       "email": "admin1@empresa.com",
       "password": "password123"
     }'
   ```

## 🔑 Diferencias entre Roles

### HR (Recursos Humanos)
- Puede gestionar empleados de su empresa
- Puede ver asistencias de su empresa
- Puede aprobar/rechazar solicitudes
- Acceso a reportes de su empresa

### ADMIN
- Acceso completo al sistema
- Puede gestionar todas las empresas
- Puede crear usuarios HR
- Acceso a reportes globales
- Puede listar todos los usuarios

## 📝 Notas Importantes

1. **Company ID:** Tanto HR como Admin necesitan especificar un `companyId` al registrarse
2. **Seguridad:** Los endpoints de login específicos verifican que el usuario tenga el rol correcto
3. **Tokens:** Los tokens JWT incluyen información del rol para autorización
4. **Validación:** Se verifica que no existan usuarios duplicados por email
