@echo off
chcp 65001 >nul
echo 🏪 Chạy giao diện GUI cho Quản Lý Kho
echo =====================================

echo Kiểm tra XAMPP và MySQL...
echo.

java -cp "bin;lib\mysql-connector-java.jar" KhoGUI

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Lỗi chạy GUI. Thử console mode...
    echo.
    java -cp "bin;lib\mysql-connector-java.jar" Main
    pause
) else (
    echo.
    echo ✅ GUI đã đóng thành công.
)
