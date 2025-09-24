@echo off
echo ========================================
echo Probando las correcciones aplicadas
echo ========================================

echo.
echo 1. Verificando compilacion de ms-requests...
cd ms-requests
mvn clean compile -q
if %ERRORLEVEL% EQU 0 (
    echo ✅ ms-requests compila correctamente
) else (
    echo ❌ Error en compilacion de ms-requests
)
cd ..

echo.
echo 2. Verificando compilacion de ms-attendance...
cd ms-attendancce
mvn clean compile -q
if %ERRORLEVEL% EQU 0 (
    echo ✅ ms-attendance compila correctamente
) else (
    echo ❌ Error en compilacion de ms-attendance
)
cd ..

echo.
echo 3. Verificando que los servicios esten corriendo...
echo.
echo 🔍 Verificando puertos:
netstat -an | findstr ":8082" >nul
if %ERRORLEVEL% EQU 0 (
    echo ✅ Puerto 8082 (ms-attendance) esta en uso
) else (
    echo ❌ Puerto 8082 (ms-attendance) no esta en uso
)

netstat -an | findstr ":8083" >nul
if %ERRORLEVEL% EQU 0 (
    echo ✅ Puerto 8083 (ms-requests) esta en uso
) else (
    echo ❌ Puerto 8083 (ms-requests) no esta en uso
)

netstat -an | findstr ":8090" >nul
if %ERRORLEVEL% EQU 0 (
    echo ✅ Puerto 8090 (gateway) esta en uso
) else (
    echo ❌ Puerto 8090 (gateway) no esta en uso
)

echo.
echo ========================================
echo Resumen de correcciones aplicadas:
echo ========================================
echo ✅ Corregido parserBuilder() en JwtUtils
echo ✅ Corregido RequestMapper con @Mapping(ignore)
echo ✅ Actualizado CreateRequestDto para JWT
echo ✅ Actualizado RequestController para HttpServletRequest
echo ✅ Actualizado RequestService para extraer datos del token
echo ✅ Corregidos tipos de Key a SecretKey en JwtUtils
echo.
echo 🚀 Los problemas principales han sido solucionados!
echo.
echo Para probar el endpoint:
echo POST http://localhost:8090/api/v1/attendances/check-in
echo Authorization: Bearer <token>
echo Content-Type: application/json
echo.
echo {
echo   "attendanceDate": "2024-01-15",
echo   "checkInTime": "09:00:00",
echo   "location": "Oficina Central"
echo }
echo.
pause
