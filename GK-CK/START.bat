@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul 2>&1
title 🏬 QUẢN LÝ KHO HÀNG HÓA - Tích Hợp Đầy Đủ
color 0A

REM ========================================
REM        CẤU HÌNH JAVA TỰ ĐỘNG
REM ========================================
set JAVA_EXE=java
set JAVAC_EXE=javac

REM Đảm bảo làm việc trong đúng thư mục
cd /d "%~dp0"

REM Kiểm tra Java trong PATH trước
where java >nul 2>&1
if !ERRORLEVEL! EQU 0 (
    REM Java đã có trong PATH, sử dụng luôn
    set JAVA_EXE=java
    set JAVAC_EXE=javac
    echo    ✅ Sử dụng Java từ PATH: java và javac
) else (
    REM Nếu không có trong PATH, thử đường dẫn đầy đủ trên ổ H:
    if exist "H:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\java.exe" (
        set "JAVA_EXE=H:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\java.exe"
        set "JAVAC_EXE=H:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\javac.exe"
        echo    ✅ Sử dụng Java từ ổ H: Eclipse Adoptium
    ) else if exist "H:\Java\jdk-21\bin\java.exe" (
        set "JAVA_EXE=H:\Java\jdk-21\bin\java.exe"
        set "JAVAC_EXE=H:\Java\jdk-21\bin\javac.exe"
        echo    ✅ Sử dụng Java từ ổ H: H:\Java\jdk-21
    ) else if exist "H:\jdk\bin\java.exe" (
        set "JAVA_EXE=H:\jdk\bin\java.exe"
        set "JAVAC_EXE=H:\jdk\bin\javac.exe"
        echo    ✅ Sử dụng Java từ ổ H: H:\jdk
    ) else (
        REM Fallback to C: if not found on H:
        if exist "C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\java.exe" (
            set "JAVA_EXE=C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\java.exe"
            set "JAVAC_EXE=C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\javac.exe"
            echo    ✅ Sử dụng Java từ ổ C: Eclipse Adoptium
        ) else (
            echo    ❌ Không tìm thấy Java! Hãy cài đặt Java JDK
        )
    )
)

:MAIN_MENU
cls
echo.
echo    🏬 =============================================== 🏬
echo           CHƯƠNG TRÌNH QUẢN LÝ KHO HÀNG HÓA
echo    🏬 =============================================== 🏬
echo.
echo    📋 MENU CHÍNH:
echo    ================
echo    [1] 🎨 GUI Mode      - Giao diện đồ họa
echo    [2] 💻 Console Mode  - Giao diện dòng lệnh
echo    [3] 🔨 Compile       - Biên dịch source code
echo    [4] ❌ Thoát
echo.
echo    🏬 =============================================== 🏬
echo.
set /p choice="    ➤ Nhập lựa chọn (1-4): "

if "%choice%"=="1" goto GUI_MODE
if "%choice%"=="2" goto CONSOLE_MODE
if "%choice%"=="3" goto COMPILE
if "%choice%"=="4" goto EXIT

echo    ❌ Lựa chọn không hợp lệ!
timeout /t 2 >nul
goto MAIN_MENU

REM ========================================
REM               GUI MODE
REM ========================================
:GUI_MODE
cls
echo.
echo    🎨 ===================================== 🎨
echo              KHỞI ĐỘNG GUI MODE
echo    🎨 ===================================== 🎨
echo.

REM Kiểm tra Java
echo    📋 Kiểm tra Java Runtime...
call %JAVA_EXE% -version >nul 2>&1
if !ERRORLEVEL! NEQ 0 (
    echo    ❌ Java chưa được cài đặt hoặc không tìm thấy!
    echo    💡 Vui lòng cài đặt Java JDK từ: https://adoptium.net/
    echo    💡 Hoặc thêm Java vào PATH của Windows.
    echo.
    echo    🔄 Quay lại menu chính...
    timeout /t 5 >nul
    goto MAIN_MENU
)

echo    ✅ Java OK! Đang khởi động GUI...
echo    🔍 Kiểm tra XAMPP và MySQL...
echo.

REM Kiểm tra file compiled
if not exist "bin\view\*.class" (
    echo    ⚠️  Chưa có file .class! Đang tự động biên dịch...
    echo.
    goto COMPILE
)

call "%JAVA_EXE%" -cp "%~dp0bin;%~dp0lib\mysql-connector-java.jar" view.Main gui

if !ERRORLEVEL! NEQ 0 (
    echo.
    echo    ❌ Lỗi chạy ứng dụng MVC.
    echo    💡 Hãy kiểm tra XAMPP đã bật MySQL chưa.
    echo    💡 Hoặc thử biên dịch lại bằng chọn [3] Compile
    pause
) else (
    echo.
    echo    ✅ GUI đã đóng thành công.
)
timeout /t 3 >nul
goto MAIN_MENU

REM ========================================
REM             CONSOLE MODE
REM ========================================
:CONSOLE_MODE
cls
echo.
echo    💻 ===================================== 💻
echo            KHỞI ĐỘNG CONSOLE MODE
echo    💻 ===================================== 💻
echo.

REM Kiểm tra Java
echo    📋 Kiểm tra Java Runtime...
call %JAVA_EXE% -version >nul 2>&1
if !ERRORLEVEL! NEQ 0 (
    echo    ❌ Java chưa được cài đặt hoặc không tìm thấy!
    echo    💡 Vui lòng cài đặt Java JDK từ: https://adoptium.net/
    echo    💡 Hoặc thêm Java vào PATH của Windows.
    echo.
    echo    🔄 Quay lại menu chính...
    timeout /t 5 >nul
    goto MAIN_MENU
)

echo    ✅ Java OK! Đang khởi động Console...
echo    🔍 Kiểm tra XAMPP và MySQL...
echo.

REM Kiểm tra file compiled
if not exist "bin\view\*.class" (
    echo    ⚠️  Chưa có file .class! Đang tự động biên dịch...
    echo.
    goto COMPILE
)

call "%JAVA_EXE%" -cp "%~dp0bin;%~dp0lib\mysql-connector-java.jar" view.Main console

if !ERRORLEVEL! NEQ 0 (
    echo.
    echo    ❌ Lỗi chạy console mode.
    echo    💡 Hãy kiểm tra XAMPP đã bật MySQL chưa.
    echo    💡 Hoặc thử biên dịch lại bằng chọn [3] Compile
    pause
) else (
    echo.
    echo    ✅ Console mode đã đóng thành công.
)
timeout /t 3 >nul
goto MAIN_MENU

REM ========================================
REM               COMPILE CODE
REM ========================================
:COMPILE
cls
echo.
echo    🔨 ===================================== 🔨
echo           BIÊN DỊCH SOURCE CODE JAVA
echo    🔨 ===================================== 🔨
echo.

REM Kiểm tra Java Compiler
echo    📋 KIỂM TRA JAVA COMPILER:
echo    ===========================
call %JAVAC_EXE% -version >nul 2>&1
if !ERRORLEVEL! NEQ 0 (
    echo    ❌ Java Compiler không tìm thấy!
    echo    💡 Hãy cài đặt Java JDK từ: https://adoptium.net/
    echo.
    pause
    goto MAIN_MENU
)
echo    ✅ Java Compiler: OK
echo.

REM Tạo thư mục bin nếu chưa có
echo    📋 CHUẨN BỊ THƯ MỤC:
echo    =====================
if not exist "bin" (
    mkdir bin
    echo    ✅ Đã tạo thư mục bin/
) else (
    echo    ✅ Thư mục bin/ đã tồn tại
)
echo.

REM Kiểm tra MySQL driver
echo    📋 KIỂM TRA DEPENDENCIES:
echo    ==========================
if exist "lib\mysql-connector-java.jar" (
    echo    ✅ MySQL Driver: CÓ
) else (
    echo    ❌ MySQL Driver: THIẾU (lib\mysql-connector-java.jar)
    echo    💡 Tải từ: https://dev.mysql.com/downloads/connector/j/
    echo    � Hoặc từ: https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar
    echo.
    pause
    goto MAIN_MENU
)
echo.

REM Biên dịch source code
echo    📋 BIÊN DỊCH MVC PATTERN:
echo    ==========================
echo    🔄 Đang biên dịch cấu trúc MVC...

REM Biên dịch theo thứ tự dependency: Model -> Controller -> View
echo    📦 Biên dịch Model layer...
call "%JAVAC_EXE%" -d bin -cp "lib\mysql-connector-java.jar" -encoding UTF-8 src\model\*.java

if !ERRORLEVEL! NEQ 0 (
    echo    ❌ Lỗi biên dịch Model layer!
    pause
    goto MAIN_MENU
)

echo    🎮 Biên dịch Controller layer...
call "%JAVAC_EXE%" -d bin -cp "lib\mysql-connector-java.jar;bin" -encoding UTF-8 src\controller\*.java

if !ERRORLEVEL! NEQ 0 (
    echo    ❌ Lỗi biên dịch Controller layer!
    pause
    goto MAIN_MENU
)

echo    🖼️ Biên dịch View layer...
call "%JAVAC_EXE%" -d bin -cp "lib\mysql-connector-java.jar;bin" -encoding UTF-8 src\view\*.java

if !ERRORLEVEL! EQU 0 (
    echo    ✅ Biên dịch MVC Pattern thành công!
    echo.
    echo    📄 Cấu trúc MVC đã được tạo trong thư mục bin/
    echo    📦 Model: model\*.class
    echo    🎮 Controller: controller\*.class  
    echo    🖼️ View: view\*.class
    echo    🚀 Bạn có thể chạy chương trình bằng GUI hoặc Console mode
) else (
    echo    ❌ Có lỗi trong quá trình biên dịch View layer!
    echo    💡 Hãy kiểm tra lại source code trong thư mục src/
    echo.
    pause
    goto MAIN_MENU
)

echo.
echo    🎉 HOÀN THÀNH BIÊN DỊCH!
echo.
timeout /t 3 >nul
goto MAIN_MENU

REM ========================================
REM                 EXIT
REM ========================================
:EXIT
cls
echo.
echo    🏬 ===================================== 🏬
echo              TỰ ĐỘNG DỌN DẸP VÀ THOÁT
echo    🏬 ===================================== 🏬
echo.

echo    🧹 Đang dọn dẹp project trước khi thoát...
echo    ==========================================
echo.

REM Dọn dẹp các file đã biên dịch
echo    🔄 Dọn dẹp MVC compiled files...
if exist "bin\model\*.class" (
    del /Q "bin\model\*.class" 2>nul
    echo    ✅ Đã xóa Model classes
)
if exist "bin\controller\*.class" (
    del /Q "bin\controller\*.class" 2>nul
    echo    ✅ Đã xóa Controller classes
)
if exist "bin\view\*.class" (
    del /Q "bin\view\*.class" 2>nul
    echo    ✅ Đã xóa View classes
)
if exist "bin\*.class" (
    del /Q "bin\*.class" 2>nul
    echo    ✅ Đã xóa các file .class khác
)

REM Dọn dẹp các file tạm thời
echo    🔄 Dọn dẹp file tạm thời...
if exist "*.tmp" del /Q "*.tmp" 2>nul
if exist "*.log" del /Q "*.log" 2>nul
if exist "src\*.class" del /Q "src\*.class" 2>nul
if exist "database\*.class" del /Q "database\*.class" 2>nul
echo    ✅ Đã xóa các file tạm thời

echo.
echo    🎉 Dọn dẹp hoàn tất!
echo.
echo    🏬 ===================================== 🏬
echo              CẢM ƠN BẠN ĐÃ SỬ DỤNG!
echo    🏬 ===================================== 🏬
echo.
echo    💝 Cảm ơn bạn đã sử dụng chương trình!
echo    🚀 Chúc bạn làm việc hiệu quả!
echo    👋 Hẹn gặp lại...
echo.
echo    🏬 ===================================== 🏬
echo.
timeout /t 3 >nul
exit
