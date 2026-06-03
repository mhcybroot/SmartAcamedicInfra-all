@echo off
title Smart Academic Infrastructure - Offline Check
echo =================================================
echo  Smart Academic Infrastructure Setup (Windows Offline)
echo =================================================
echo.

:: Check Node.js
echo Checking Node.js...
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Node.js is not installed or not in PATH.
    pause
    exit /b 1
)
echo [OK] Node.js is available.

:: Check Java
echo Checking Java JDK...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java is not installed or not in PATH.
    pause
    exit /b 1
)
echo [OK] Java JDK is available.

:: Check pre-copied node_modules
echo Checking smart-academic-portal node_modules...
if not exist "smart-academic-portal\node_modules\" (
    echo [ERROR] 'node_modules' is missing inside 'smart-academic-portal'.
    pause
    exit /b 1
)
echo [OK] node_modules folder is present.

:: Check pre-compiled JARs
echo Checking JAR files...
set "MISSING_JAR="
if not exist "Result-Management-System\build\libs\rms-0.0.1-SNAPSHOT.jar" set "MISSING_JAR=RMS"
if not exist "Skylink-custom-backend\build\libs\AttendanceSystem-0.0.1-SNAPSHOT.jar" set "MISSING_JAR=Skylink"
if not exist "watch-employee\build\libs\watch-employee-0.0.1-SNAPSHOT.jar" set "MISSING_JAR=Student Watch"

if defined MISSING_JAR (
    echo [ERROR] Pre-compiled JAR for %MISSING_JAR% is missing.
    pause
    exit /b 1
)
echo [OK] All pre-compiled JARs are present.

:: Database Setup
echo.
echo =================================================
echo  PostgreSQL Database Configuration
echo =================================================
set /p PG_PASS="Enter PostgreSQL password (default is 'root'): "
if "%PG_PASS%"=="" set PG_PASS=root
set /p PG_USER="Enter PostgreSQL username (default is 'postgres'): "
if "%PG_USER%"=="" set PG_USER=postgres

echo Attempting to create required databases...
set "PGPASSWORD=%PG_PASS%"

:: Try to run psql commands to create databases
psql -U %PG_USER% -h localhost -c "CREATE DATABASE school_exam_db;" >nul 2>&1
psql -U %PG_USER% -h localhost -c "CREATE DATABASE skylink_database;" >nul 2>&1
psql -U %PG_USER% -h localhost -c "CREATE DATABASE employee_watch;" >nul 2>&1

if %errorlevel% neq 0 (
    echo.
    echo [WARNING] Could not automatically create databases using 'psql'.
    echo Please make sure the following databases exist in your PostgreSQL server:
    echo  - school_exam_db
    echo  - skylink_database
    echo  - employee_watch
    echo.
) else (
    echo [OK] Databases verified / created successfully.
)

echo.
echo =================================================
echo  System ready to run offline!
echo  You can now run 'start_windows.bat' to start all services.
echo =================================================
pause
