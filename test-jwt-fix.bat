@echo off
echo Testing JWT token extraction approach...
echo.

echo Step 1: Getting JWT token...
curl -X POST http://localhost:8090/api/v1/auth/login/email ^
  -H "Content-Type: application/json" ^
  -d "{\"email\": \"ca22aw@gmail.com\", \"password\": \"Alonso1106#\"}" ^
  -w "\nHTTP Status: %%{http_code}\n" ^
  -s > temp_token.json

echo.
echo Step 2: Extracting token from response...
for /f "tokens=2 delims=:" %%a in ('findstr "token" temp_token.json') do set TOKEN=%%a
set TOKEN=%TOKEN:"=%
set TOKEN=%TOKEN:,=%
set TOKEN=%TOKEN: =%

echo Token extracted: %TOKEN:~0,50%...

echo.
echo Step 3: Testing attendance check-in (should extract employeeId from token)...
curl -X POST http://localhost:8090/api/v1/attendances/check-in ^
  -H "Authorization: Bearer %TOKEN%" ^
  -H "Content-Type: application/json" ^
  -d "{\"notes\": \"Check-in desde casa\"}" ^
  -w "\nHTTP Status: %%{http_code}\n" ^
  -v

echo.
echo Step 4: Testing my attendances endpoint...
curl -X GET "http://localhost:8090/api/v1/attendances/my-attendances?page=0&size=5" ^
  -H "Authorization: Bearer %TOKEN%" ^
  -w "\nHTTP Status: %%{http_code}\n" ^
  -v

echo.
echo Step 5: Testing employee profile endpoint...
curl -X GET http://localhost:8090/api/v1/employees/my-profile ^
  -H "Authorization: Bearer %TOKEN%" ^
  -w "\nHTTP Status: %%{http_code}\n" ^
  -v

echo.
echo Cleaning up temporary files...
del temp_token.json

echo.
echo Test completed. If all endpoints return 200, the JWT approach is working!
pause
