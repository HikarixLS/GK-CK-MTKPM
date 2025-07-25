@echo off
chcp 65001 >nul
echo ===================================
echo        CLEAN PROJECT FILES
echo ===================================

cd /d "%~dp0\.."

echo [INFO] Cleaning compiled files...
if exist "bin\*.class" (
    del /Q "bin\*.class"
    echo ✅ Deleted all .class files
) else (
    echo ℹ️  No .class files to delete  
)

echo [INFO] Cleaning temporary files...
if exist "*.tmp" del /Q "*.tmp" 2>nul
if exist "*.log" del /Q "*.log" 2>nul
if exist "src\*.class" del /Q "src\*.class" 2>nul
if exist "database\*.class" del /Q "database\*.class" 2>nul

echo [INFO] ✅ Project cleaned successfully!
echo.
pause
