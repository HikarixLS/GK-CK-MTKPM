@echo off
chcp 65001 >nul
echo ======================================
echo    SETUP PROJECT LẦN ĐẦU TIÊN
echo ======================================

cd /d "%~dp0\.."

echo [1/4] Kiểm tra MySQL...
tasklist /FI "IMAGENAME eq mysqld.exe" 2>NUL | find /I /N "mysqld.exe">NUL
if "%ERRORLEVEL%"=="1" (
    echo ❌ MySQL không chạy! Vui lòng khởi động XAMPP trước.
    pause
    exit /b 1
)
echo ✅ MySQL đang chạy

echo [2/4] Tạo thư mục bin...
if not exist "bin" mkdir bin
echo ✅ Thư mục bin sẵn sàng

echo [3/4] Compile Java files...
javac -encoding UTF-8 -cp "lib\mysql-connector-java.jar" src\*.java -d bin
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Compile thất bại!
    pause
    exit /b 1
)
echo ✅ Compile thành công

echo [4/4] Test kết nối database...
java -cp "bin;lib\mysql-connector-java.jar" DatabaseConnection
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Test kết nối thất bại!
    pause
    exit /b 1
)

echo.
echo 🎉 SETUP HOÀN TẤT!
echo Bây giờ bạn có thể chạy: run_main.bat
echo.
pause
