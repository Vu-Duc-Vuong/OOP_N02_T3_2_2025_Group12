@echo off
echo ================================================
echo 🚀 SPRING BOOT GUI - QUAN LY HANG HOA
echo ================================================
echo.

REM Kiểm tra Java
echo 📋 Kiem tra Java version...
java -version
if %errorlevel% neq 0 (
    echo ❌ Java khong duoc cai dat hoac khong trong PATH
    echo 💡 Vui long cai dat Java 11 tro len
    pause
    exit /b 1
)

echo.
echo 📁 Chuyen den thu muc project...
cd gs-serving-web-content-main\complete

if not exist "%cd%" (
    echo ❌ Khong tim thay thu muc project
    pause
    exit /b 1
)

echo ✅ Da vao thu muc: %cd%
echo.

echo 🔨 Dang build va khoi dong ung dung...
echo ⏳ Vui long doi...
echo.

REM Chạy ứng dụng
mvnw.cmd spring-boot:run

echo.
echo 👋 Ung dung da dung!
pause
