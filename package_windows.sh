#!/bin/bash
echo "================================================="
echo " Packaging Smart Academic Infrastructure for Windows"
echo "================================================="

# 1. Compile Spring Boot Backends (if not already compiled/UP-TO-DATE)
echo "Building Skylink..."
cd Skylink-custom-backend && ./gradlew bootJar && cd ..

echo "Building RMS..."
cd Result-Management-System && ./gradlew bootJar && cd ..

echo "Building Student Watch..."
cd watch-employee && ./gradlew bootJar && cd ..

# 2. Create Staging Directory
STAGING_DIR="smart-academic-windows"
echo "Creating staging folder '$STAGING_DIR'..."
rm -rf $STAGING_DIR
mkdir -p $STAGING_DIR

# 3. Copy files to Staging
echo "Copying portal frontend (including node_modules)..."
mkdir -p $STAGING_DIR/smart-academic-portal
cp -R smart-academic-portal/src $STAGING_DIR/smart-academic-portal/
cp -R smart-academic-portal/public $STAGING_DIR/smart-academic-portal/
cp -R smart-academic-portal/node_modules $STAGING_DIR/smart-academic-portal/
cp smart-academic-portal/package.json $STAGING_DIR/smart-academic-portal/
cp smart-academic-portal/vite.config.js $STAGING_DIR/smart-academic-portal/
cp smart-academic-portal/index.html $STAGING_DIR/smart-academic-portal/

echo "Copying pre-compiled Java JARs..."
mkdir -p $STAGING_DIR/Result-Management-System/build/libs/
cp Result-Management-System/build/libs/rms-0.0.1-SNAPSHOT.jar $STAGING_DIR/Result-Management-System/build/libs/

mkdir -p $STAGING_DIR/Skylink-custom-backend/build/libs/
cp Skylink-custom-backend/build/libs/AttendanceSystem-0.0.1-SNAPSHOT.jar $STAGING_DIR/Skylink-custom-backend/build/libs/

mkdir -p $STAGING_DIR/watch-employee/build/libs/
cp watch-employee/build/libs/watch-employee-0.0.1-SNAPSHOT.jar $STAGING_DIR/watch-employee/build/libs/

echo "Copying Windows Batch Scripts..."
cp setup_windows.bat $STAGING_DIR/
cp start_windows.bat $STAGING_DIR/
cp stop_windows.bat $STAGING_DIR/

# 4. Create ZIP File
echo "Creating zip archive..."
rm -f smart-academic-windows.zip
zip -r smart-academic-windows.zip $STAGING_DIR > /dev/null

# 5. Clean up Staging Directory
echo "Cleaning up staging directory..."
rm -rf $STAGING_DIR

echo "================================================="
echo " SUCCESS: Packaged smart-academic-windows.zip"
echo " Copy this zip file to Windows and extract it."
echo "================================================="
