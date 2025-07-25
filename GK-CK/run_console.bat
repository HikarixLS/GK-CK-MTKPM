@echo off
chcp 65001 >nul
echo 🖥️ Chạy Console Mode cho Quản Lý Kho
echo ====================================

echo Kiểm tra XAMPP và MySQL...
echo.

java -cp "bin;lib\mysql-connector-java.jar" Main

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Lỗi chạy console mode.
    echo 💡 Hãy kiểm tra XAMPP đã bật MySQL chưa.
    pause
) else (
    echo.
    echo ✅ Console mode đã đóng thành công.
)
