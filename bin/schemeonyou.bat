@echo off
setlocal
set "APP_HOME=%~dp0.."
if not defined JAVA_HOME set "JAVA_HOME=%USERPROFILE%\workspace\java\openjdk-25.0.2"
set "JAVA=%JAVA_HOME%\bin\java.exe"
set "CP=%APP_HOME%\client\target\classes;%APP_HOME%\core\target\classes;%APP_HOME%\client\target\client-0.0.1-SNAPSHOT.jar;%APP_HOME%\core\target\core-0.0.1-SNAPSHOT.jar"
if exist "%APP_HOME%\client\target\lib" for %%J in ("%APP_HOME%\client\target\lib\*.jar") do call set "CP=%%CP%%;%%~fJ"
if exist "%APP_HOME%\core\target\lib" for %%J in ("%APP_HOME%\core\target\lib\*.jar") do call set "CP=%%CP%%;%%~fJ"
"%JAVA%" -cp "%CP%" see.schemeonyou.SchemeOnYouMain %*
endlocal
