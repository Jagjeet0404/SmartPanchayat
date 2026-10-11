@echo off
cd /d "%~dp0backend"
if exist "%~dp0apikey.txt" set /p ANTHROPIC_API_KEY=<"%~dp0apikey.txt"
set H2_CONSOLE=true
echo Starting Smart Panchayat on http://localhost:8080
start "" http://localhost:8080/login.html
call mvnw.cmd spring-boot:run
pause
