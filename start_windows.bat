@echo off
title Smart Academic Infrastructure - Offline Startup
echo =================================================
echo  Starting Smart Academic Infrastructure (Offline)
echo =================================================
echo.

:: Start Result Management System (Port 7512)
echo [1/4] Starting Result Management System (Port 7512)...
start "Result Management System - Port 7512" cmd /c "java -Xmx256m -XX:+UseSerialGC -jar Result-Management-System\build\libs\rms-0.0.1-SNAPSHOT.jar"

:: Start Academic Member Management (Port 7511)
echo [2/4] Starting Academic Member Management (Port 7511)...
start "Academic Members Portal - Port 7511" cmd /c "java -Xmx256m -XX:+UseSerialGC -jar Skylink-custom-backend\build\libs\AttendanceSystem-0.0.1-SNAPSHOT.jar"

:: Start Student Watch System (Port 7513)
echo [3/4] Starting Student Watch System (Port 7513)...
start "Student Watch - Port 7513" cmd /c "java -Xmx256m -XX:+UseSerialGC -jar watch-employee\build\libs\watch-employee-0.0.1-SNAPSHOT.jar"

:: Start Vite Frontend Portal (Port 7510)
echo [4/4] Starting Vite Frontend Portal (Port 7510)...
start "Vue Executive Portal - Port 7510" /D smart-academic-portal cmd /c npm run dev

echo.
echo =================================================
echo  All services are launching offline!
echo  
echo  Access the Executive Portal at: http://localhost:7510
echo.
echo  To stop all services:
echo  - Simply close the opened Command Prompt windows, OR
echo  - Run 'stop_windows.bat'
echo =================================================
pause
