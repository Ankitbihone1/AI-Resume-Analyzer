<# : batch portion
@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM ----------------------------------------------------------------------------

@IF "%DEBUG%" == "" @ECHO OFF
@REM set %HOME% to equivalent of $HOME
if "%HOME%" == "" (set "HOME=%HOMEDRIVE%%HOMEPATH%")

set ERROR_CODE=0

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

set "EXEC_DIR=%CD%"
set "WDIR=%~dp0"
set "DIRNAME=%~dp0%"
if "%DIRNAME%" == "" set "DIRNAME=."
set "APP_BASE_NAME=%~n0"
set "APP_HOME=%DIRNAME%"

@REM Resolve Java
if not "%JAVA_HOME%" == "" goto OkJHome
for %%i in (java.exe) do set "JAVACMD=%%~$PATH:i"
if not "%JAVACMD%" == "" goto checkJavaVersion
echo.
echo Error: JAVA_HOME is not defined correctly.
echo We cannot execute %JAVACMD%
goto error

:OkJHome
set "JAVACMD=%JAVA_HOME%\bin\java.exe"
if exist "%JAVACMD%" goto checkJavaVersion
echo.
echo Error: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = "%JAVA_HOME%"
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.
goto error

:checkJavaVersion
set "WRAPPER_JAR=%APP_HOME%\.mvn\wrapper\maven-wrapper.jar"
set "WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain"

"%JAVACMD%" -version >nul 2>&1
if ERRORLEVEL 1 goto error

@REM Execute Maven through wrapper if jar exists, else fallback to standard mvn
if exist "%WRAPPER_JAR%" (
    "%JAVACMD%" -jar "%WRAPPER_JAR%" %*
    goto end
)

@REM Fallback to PATH mvn if wrapper jar has not downloaded yet
where mvn >nul 2>&1
if %ERRORLEVEL% equ 0 (
    mvn %*
    goto end
)

echo Notice: Using system Maven or downloading wrapper...
powershell -Command "& { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar', '%WRAPPER_JAR%') }"
if exist "%WRAPPER_JAR%" (
    "%JAVACMD%" -jar "%WRAPPER_JAR%" %*
    goto end
)

:error
set ERROR_CODE=1

:end
@REM End local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endLocal
@REM (for backward compatibility)
set ERROR_CODE=%ERROR_CODE%

:endLocal
exit /B %ERROR_CODE%
