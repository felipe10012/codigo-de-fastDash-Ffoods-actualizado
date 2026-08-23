@echo off
title FastDash Ffoods - Compilador
cd /d "%~dp0"
echo Compilando el proyecto FastDash Ffoods...
javac -encoding UTF-8 -cp "lib\mysql-connector-j-8.0.33.jar;lib\gson-2.11.0.jar" -d out src\com\fastdash\Main.java src\com\fastdash\dao\*.java src\com\fastdash\dao\impl\*.java src\com\fastdash\dao\impl\jdbc\*.java src\com\fastdash\model\*.java src\com\fastdash\service\*.java src\com\fastdash\util\*.java
if %errorlevel% equ 0 (
    echo.
    echo Compilacion exitosa. Ejecuta ejecutar.bat para iniciar la aplicacion.
) else (
    echo.
    echo Ocurrieron errores de compilacion. Revisa los mensajes anteriores.
)
pause
