#!/bin/bash

echo "================================================="
echo " Starting Smart Academic Infrastructure"
echo "================================================="

# Create a logs directory
mkdir -p logs

echo "[1/4] Starting Result Management System (Port 7512)..."
cd Result-Management-System
./gradlew bootRun > ../logs/result_management.log 2>&1 &
PID1=$!
cd ..

echo "[2/4] Starting Academic Member Management (Port 7511)..."
cd Skylink-custom-backend
./gradlew bootRun > ../logs/skylink.log 2>&1 &
PID2=$!
cd ..

echo "[3/4] Starting Student Watch System (Port 7513)..."
cd watch-employee
./gradlew bootRun > ../logs/watch_employee.log 2>&1 &
PID3=$!
cd ..

echo "[4/4] Starting Smart Academic Portal (Vue.js frontend)..."
cd smart-academic-portal
npm run dev > ../logs/portal.log 2>&1 &
PID4=$!
cd ..

echo "================================================="
echo " All services are starting up in the background!"
echo " "
echo " You can access the unified portal at: http://localhost:7510"
echo " "
echo " To view logs, check the 'logs' directory."
echo " Press Ctrl+C to stop all services and exit."
echo "================================================="

# Trap SIGINT (Ctrl+C) and kill all child processes
trap "echo 'Stopping all services...'; kill $PID1 $PID2 $PID3 $PID4; exit" INT

# Wait indefinitely until interrupted
wait
