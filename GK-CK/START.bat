@echo off
chcp 65001 >nul
title Quản Lý Kho Hàng Hóa
color 0A

:MAIN_MENU
cls
echo.
echo    🏪 ========================================== 🏪
echo        CHƯƠNG TRÌNH QUẢN LÝ KHO HÀNG HÓA
echo    🏪 ========================================== 🏪
echo.
echo    📋 Chọn chế độ chạy:
echo.
echo    [1] 🖼️  GUI Mode    - Giao diện đồ họa (khuyến nghị)
echo    [2] 🖥️  Console Mode - Giao diện dòng lệnh
echo    [3] 🔧  Compile      - Biên dịch lại source code
echo    [4] 🚪  Thoát
echo.
echo    ==========================================
echo.
set /p choice="    👉 Nhập lựa chọn (1-4): "

if "%choice%"=="1" goto GUI_MODE
if "%choice%"=="2" goto CONSOLE_MODE
if "%choice%"=="3" goto COMPILE
if "%choice%"=="4" goto EXIT

echo    ❌ Lựa chọn không hợp lệ!
timeout /t 2 >nul
goto MAIN_MENU

:GUI_MODE
echo.
echo    🖼️ Khởi động GUI Mode...
echo    🏪 Chạy giao diện GUI cho Quản Lý Kho
echo    =====================================
echo.
echo    Kiểm tra XAMPP và MySQL...
echo.

java -cp "bin;lib\mysql-connector-java.jar" KhoGUI

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo    ❌ Lỗi chạy GUI. Thử console mode...
    echo.
    java -cp "bin;lib\mysql-connector-java.jar" Main
    pause
) else (
    echo.
    echo    ✅ GUI đã đóng thành công.
)
timeout /t 3 >nul
goto MAIN_MENU

:CONSOLE_MODE
echo.
echo    🖥️ Khởi động Console Mode...
echo    🖥️ Chạy Console Mode cho Quản Lý Kho
echo    ====================================
echo.
echo    Kiểm tra XAMPP và MySQL...
echo.

java -cp "bin;lib\mysql-connector-java.jar" Main

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo    ❌ Lỗi chạy console mode.
    echo    💡 Hãy kiểm tra XAMPP đã bật MySQL chưa.
    pause
) else (
    echo.
    echo    ✅ Console mode đã đóng thành công.
)
timeout /t 3 >nul
goto MAIN_MENU

:COMPILE
echo.
echo    🔧 Đang biên dịch source code...
echo    ==============================
echo.

REM Tạo thư mục bin nếu chưa có
if not exist "bin" mkdir bin

REM Biên dịch tất cả file Java
echo    📝 Compiling Java files...
javac -d bin -cp "lib\mysql-connector-java.jar" src\*.java

if %ERRORLEVEL% EQU 0 (
    echo    ✅ Biên dịch thành công!
) else (
    echo    ❌ Có lỗi trong quá trình biên dịch!
    pause
    goto MAIN_MENU
)

echo    ✅ Biên dịch hoàn tất!
timeout /t 3 >nul
goto MAIN_MENU

:EXIT
echo.
echo    👋 Cảm ơn bạn đã sử dụng chương trình!
echo    💾 Hẹn gặp lại...
timeout /t 2 >nul
exit
