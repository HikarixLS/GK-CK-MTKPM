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
call run_gui_optimized.bat
timeout /t 3 >nul
goto MAIN_MENU

:CONSOLE_MODE
echo.
echo    🖥️ Khởi động Console Mode...
call run_console.bat
timeout /t 3 >nul
goto MAIN_MENU

:COMPILE
echo.
echo    🔧 Đang biên dịch source code...
call scripts\compile.bat
echo    ✅ Biên dịch hoàn tất!
timeout /t 3 >nul
goto MAIN_MENU

:EXIT
echo.
echo    👋 Cảm ơn bạn đã sử dụng chương trình!
echo    💾 Hẹn gặp lại...
timeout /t 2 >nul
exit
