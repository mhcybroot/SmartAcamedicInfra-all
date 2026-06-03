@echo off
title Smart Academic Infrastructure - Shutdown
echo =================================================
echo  Stopping Smart Academic Infrastructure Services
echo =================================================
echo.

echo Stopping Java backends...
taskkill /f /im java.exe >nul 2>&1

echo Stopping Node/Vite frontends...
taskkill /f /im node.exe >nul 2>&1

echo [OK] All services stopped.
echo.
pause
