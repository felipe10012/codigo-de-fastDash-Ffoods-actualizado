@echo off
title FastDash Ffoods
cd /d "%~dp0"
java -cp "out;lib\mysql-connector-j-8.0.33.jar" com.fastdash.Main
pause
