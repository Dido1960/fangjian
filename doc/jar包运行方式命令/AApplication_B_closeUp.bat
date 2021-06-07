@echo off
for /f "tokens=5" %%i in ('netstat -aon ^| findstr ":9002"') do (
set n=%%i
)
if defined n (taskkill /f /pid %n%)


::@echo off
::杀死javaw进程
::taskkill -f -t -im javaw.exe
