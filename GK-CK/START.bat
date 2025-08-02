@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul 2>&1
title 🏬 QUAN LY KHO HANG HOA
color 0A

REM Chuyen den thu muc hien tai
cd /d "%~dp0"

REM Thiet lap Java
set "JAVA_EXE=java"
set "JAVAC_EXE=javac"

REM Kiem tra Java
java -version >nul 2>&1
if !ERRORLEVEL! EQU 0 (
    echo ✅ Java da san sang
) else (
    REM Tim Java tu cac duong dan pho bien
    if exist "C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\java.exe" (
        set "JAVA_EXE=C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\java.exe"
        set "JAVAC_EXE=C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\javac.exe"
        echo ✅ Su dung Java tu Eclipse Adoptium
    ) else if exist "H:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\java.exe" (
        set "JAVA_EXE=H:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\java.exe"
        set "JAVAC_EXE=H:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\javac.exe"
        echo ✅ Su dung Java tu o H
    ) else (
        echo ❌ Khong tim thay Java!
        echo 💡 Cai dat Java tu: https://adoptium.net/
        pause
        exit /b 1
    )
)

:MAIN_MENU
cls
echo.
echo 🏬 =============================================== 🏬
echo        CHUONG TRINH QUAN LY KHO HANG HOA
echo 🏬 =============================================== 🏬
echo.
echo 📋 MENU:
echo [1] 🎨 GUI Mode
echo [2] 💻 Console Mode  
echo [3] 🔨 Compile Code
echo [4] 🧹 Clean Build
echo [5] ❌ Exit
echo.
set /p "choice=➤ Chon (1-5): "

if "%choice%"=="1" (
    call :RUN_GUI
    goto MAIN_MENU
)
if "%choice%"=="2" (
    call :RUN_CONSOLE
    goto MAIN_MENU
)
if "%choice%"=="3" (
    call :COMPILE_CODE
    goto MAIN_MENU
)
if "%choice%"=="4" (
    call :CLEAN_BUILD
    goto MAIN_MENU
)
if "%choice%"=="5" (
    goto EXIT_PROGRAM
)

echo ❌ Lua chon khong hop le!
timeout /t 2 >nul
goto MAIN_MENU

:RUN_GUI
cls
echo 🎨 Khoi dong GUI...
if not exist "bin\view\Main.class" (
    echo ⚠️ Chua bien dich! Dang bien dich...
    call :COMPILE_CODE
    if !ERRORLEVEL! NEQ 0 (
        echo ❌ Bien dich that bai!
        pause
        exit /b 1
    )
)
echo Chay ung dung GUI...
"!JAVA_EXE!" -cp "bin;lib\mysql-connector-java.jar" view.Main gui
if !ERRORLEVEL! NEQ 0 (
    echo ❌ Loi chay GUI
    pause
)
exit /b 0

:RUN_CONSOLE
cls
echo 💻 Khoi dong Console...
if not exist "bin\view\Main.class" (
    echo ⚠️ Chua bien dich! Dang bien dich...
    call :COMPILE_CODE
    if !ERRORLEVEL! NEQ 0 (
        echo ❌ Bien dich that bai!
        pause
        exit /b 1
    )
)
echo Chay ung dung Console...
"!JAVA_EXE!" -cp "bin;lib\mysql-connector-java.jar" view.Main console
if !ERRORLEVEL! NEQ 0 (
    echo ❌ Loi chay Console
    pause
)
exit /b 0

:COMPILE_CODE
cls
echo 🔨 Bien dich Java...

REM Kiem tra javac
"!JAVAC_EXE!" -version >nul 2>&1
if !ERRORLEVEL! NEQ 0 (
    echo ❌ Khong tim thay javac!
    pause
    exit /b 1
)

REM Tao thu muc
if not exist "bin" mkdir "bin"
if not exist "bin\model" mkdir "bin\model"
if not exist "bin\controller" mkdir "bin\controller"
if not exist "bin\view" mkdir "bin\view"

REM Kiem tra MySQL JAR
if not exist "lib\mysql-connector-java.jar" (
    echo ❌ Thieu MySQL JAR trong thu muc lib\
    pause
    exit /b 1
)

echo 📦 Bien dich cac file...

REM Bien dich model layer
echo - Model layer...
if exist "src\model\*.java" (
    "!JAVAC_EXE!" -d bin -cp "lib\mysql-connector-java.jar" -encoding UTF-8 src\model\*.java
    if !ERRORLEVEL! NEQ 0 (
        echo ❌ Loi bien dich model layer
        pause
        exit /b 1
    )
)

REM Bien dich root files
echo - Root files...
for %%f in (src\*.java) do (
    if exist "%%f" (
        "!JAVAC_EXE!" -d bin -cp "lib\mysql-connector-java.jar" -encoding UTF-8 "%%f"
        if !ERRORLEVEL! NEQ 0 (
            echo ❌ Loi bien dich file %%f
            pause
            exit /b 1
        )
    )
)

REM Bien dich controller layer
echo - Controller layer...
if exist "src\controller\*.java" (
    "!JAVAC_EXE!" -d bin -cp "lib\mysql-connector-java.jar;bin" -encoding UTF-8 src\controller\*.java
    if !ERRORLEVEL! NEQ 0 (
        echo ❌ Loi bien dich controller layer
        pause
        exit /b 1
    )
)

REM Bien dich view layer
echo - View layer...
if exist "src\view\*.java" (
    "!JAVAC_EXE!" -d bin -cp "lib\mysql-connector-java.jar;bin" -encoding UTF-8 src\view\*.java
    if !ERRORLEVEL! NEQ 0 (
        echo ❌ Loi bien dich view layer
        pause
        exit /b 1
    )
)

if exist "bin\view\Main.class" (
    echo ✅ Bien dich thanh cong!
) else (
    echo ❌ Bien dich that bai!
    exit /b 1
)
pause
exit /b 0

:CLEAN_BUILD
cls
echo 🧹 Don dep build...
if exist "bin" (
    rmdir /s /q "bin" 2>nul
    echo ✅ Da xoa thu muc bin
) else (
    echo ℹ️ Thu muc bin khong ton tai
)

REM Xoa cac file .class du thua neu co
for /r "src" %%f in (*.class) do (
    if exist "%%f" (
        del "%%f" 2>nul
        echo ✅ Da xoa file class du thua: %%f
    )
)

echo ✅ Don dep hoan tat!
pause
exit /b 0

:EXIT_PROGRAM
cls
echo 👋 Tam biet!
echo 🧹 Don dep...
if exist "bin" (
    rmdir /s /q "bin" 2>nul
    echo ✅ Da don dep
)
timeout /t 2 >nul
exit /b 0
