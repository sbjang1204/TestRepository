@echo off
setlocal

set "ROOT=%~dp0"
set "OUT=%ROOT%out"

if not exist "%JAVA_HOME%\bin\javac.exe" (
    echo JDK가 필요합니다. JAVA_HOME이 JDK 폴더를 가리키는지 확인하세요.
    exit /b 1
)

if exist "%OUT%" rmdir /s /q "%OUT%"
mkdir "%OUT%"

echo Compiling...
"%JAVA_HOME%\bin\javac.exe" -encoding UTF-8 -d "%OUT%" "%ROOT%src\main\java\org\example\Main.java"
if errorlevel 1 exit /b 1

echo Copying resources...
xcopy "%ROOT%src\main\resources" "%OUT%" /E /I /Y >nul
if errorlevel 1 exit /b 1

echo Starting server at http://localhost:8080
"%JAVA_HOME%\bin\java.exe" -cp "%OUT%" org.example.Main
