@echo off
chcp 65001 >nul
title Kiểm Tra Hệ Thống
color 0D

echo.
echo    🔍 ===================================== 🔍
echo        KIỂM TRA TÌNH TRẠNG HỆ THỐNG
echo    🔍 ===================================== 🔍
echo.

echo    📋 KIỂM TRA JAVA:
echo    ==================
java -version 2>nul
if %ERRORLEVEL% EQU 0 (
    echo    ✅ Java Runtime Environment: OK
) else (
    echo    ❌ Java Runtime Environment: KHÔNG TÌM THẤY
)

javac -version 2>nul
if %ERRORLEVEL% EQU 0 (
    echo    ✅ Java Compiler (javac): OK
) else (
    echo    ❌ Java Compiler (javac): KHÔNG TÌM THẤY
)

echo.
echo    📋 KIỂM TRA FILE DỰ ÁN:
echo    ========================
if exist "src\*.java" (
    echo    ✅ Source code: CÓ
) else (
    echo    ❌ Source code: THIẾU
)

if exist "lib\mysql-connector-java.jar" (
    echo    ✅ MySQL Driver: CÓ
) else (
    echo    ❌ MySQL Driver: THIẾU
)

if exist "database\database.sql" (
    echo    ✅ Database script: CÓ
) else (
    echo    ❌ Database script: THIẾU
)

if exist "database.properties" (
    echo    ✅ Database config: CÓ
) else (
    echo    ❌ Database config: THIẾU
)

echo.
echo    📋 KIỂM TRA MYSQL:
echo    ===================
echo    💡 Hãy đảm bảo XAMPP đã khởi động MySQL
echo    💡 Database 'QuanLyKhoHang' đã được tạo
echo.

echo    📋 KHUYẾN NGHỊ:
echo    ================
java -version 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo    1️⃣  Cài đặt Java JDK 11 hoặc mới hơn
    echo        Tải từ: https://adoptium.net/
)

if not exist "bin\*.class" (
    echo    2️⃣  Biên dịch source code
    echo        Chạy: .\scripts\compile.bat
)

echo    3️⃣  Khởi động XAMPP và MySQL
echo    4️⃣  Import database từ database\database.sql
echo.

pause
