@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul 2>&1
title Quan Ly Kho Hang Hoa
color 0A

REM Dinh nghia duong dan Java (tu dong detect)
set JAVA_EXE=java
set JAVAC_EXE=javac

REM Kiem tra Java trong PATH truoc
java -version 2>nul
if %ERRORLEVEL% NEQ 0 (
    REM Neu khong co trong PATH, thu duong dan day du
    if exist "C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\java.exe" (
        set JAVA_EXE=C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\java.exe
        set JAVAC_EXE=C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\javac.exe
    )
)

:MAIN_MENU
cls
echo.
echo    CHUONG TRINH QUAN LY KHO HANG HOA
echo    ==================================
echo.
echo    [1] GUI Mode    - Giao dien do hoa
echo    [2] Console Mode - Giao dien dong lenh
echo    [3] Compile      - Bien dich source code
echo    [4] Check System - Kiem tra he thong
echo    [5] Clean        - Don dep project
echo    [6] Thoat
echo.
echo    ==================================
echo.
set /p choice="    Nhap lua chon (1-6): "

if "%choice%"=="1" goto GUI_MODE
if "%choice%"=="2" goto CONSOLE_MODE
if "%choice%"=="3" goto COMPILE
if "%choice%"=="4" goto CHECK_SYSTEM
if "%choice%"=="5" goto CLEAN
if "%choice%"=="6" goto EXIT

echo    Lua chon khong hop le!
timeout /t 2 >nul
goto MAIN_MENU

:CHECK_SYSTEM
call scripts\check-system.bat
goto MAIN_MENU

:CLEAN
call scripts\clean.bat
goto MAIN_MENU

:GUI_MODE
echo.
echo    Khoi dong GUI Mode...
echo    ====================
echo.

REM Kiem tra Java
echo    Kiem tra Java...
"%JAVA_EXE%" -version 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo    Java chua duoc cai dat hoac khong tim thay!
    echo    Vui long cai dat Java JDK tu: https://adoptium.net/
    echo    Hoac them Java vao PATH cua Windows.
    echo.
    echo    Quay lai menu chinh...
    timeout /t 5 >nul
    goto MAIN_MENU
)

echo    Java OK! Dang khoi dong GUI...
echo    Kiem tra XAMPP va MySQL...
echo.

"%JAVA_EXE%" -cp "bin;lib\mysql-connector-java.jar" KhoGUI

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo    Loi chay GUI. Thu console mode...
    echo.
    "%JAVA_EXE%" -cp "bin;lib\mysql-connector-java.jar" Main
    pause
) else (
    echo.
    echo    GUI da dong thanh cong.
)
timeout /t 3 >nul
goto MAIN_MENU

:CONSOLE_MODE
echo.
echo    Khoi dong Console Mode...
echo    =========================
echo.

REM Kiem tra Java
echo    Kiem tra Java...
"%JAVA_EXE%" -version 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo    Java chua duoc cai dat hoac khong tim thay!
    echo    Vui long cai dat Java JDK tu: https://adoptium.net/
    echo    Hoac them Java vao PATH cua Windows.
    echo.
    echo    Quay lai menu chinh...
    timeout /t 5 >nul
    goto MAIN_MENU
)

echo    Java OK! Dang khoi dong Console...
echo    Kiem tra XAMPP va MySQL...
echo.

"%JAVA_EXE%" -cp "bin;lib\mysql-connector-java.jar" Main

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo    Loi chay console mode.
    echo    Hay kiem tra XAMPP da bat MySQL chua.
    pause
) else (
    echo.
    echo    Console mode da dong thanh cong.
)
timeout /t 3 >nul
goto MAIN_MENU

:COMPILE
echo.
echo    Dang bien dich source code...
echo    =============================
echo.

REM Tao thu muc bin neu chua co
if not exist "bin" mkdir bin

REM Bien dich tat ca file Java
echo    Compiling Java files...
"%JAVAC_EXE%" -d bin -cp "lib\mysql-connector-java.jar" src\*.java

if %ERRORLEVEL% EQU 0 (
    echo    Bien dich thanh cong!
) else (
    echo    Co loi trong qua trinh bien dich!
    pause
    goto MAIN_MENU
)

echo    Bien dich hoan tat!
timeout /t 3 >nul
goto MAIN_MENU

:EXIT
echo.
echo    Cam on ban da su dung chuong trinh!
echo    Hen gap lai...
timeout /t 2 >nul
exit
