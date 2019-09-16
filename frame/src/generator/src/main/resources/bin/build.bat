@echo off
@title Disco
@color a
@rem by disco
@if "%OS%"=="Windows_NT" SETLOCAL
:init
@if "%JAVA_HOME%"=="" goto nojava
@goto main
@goto eof

:main
"%JAVA_HOME%\bin\java.exe" -cp "../commons/build/ant.jar;../commons/build/ant-nodeps.jar;../commons/build/ant-junit.jar;../commons/build/junit-3.8.1.jar;%JAVA_HOME%/lib/tools.jar" org.apache.tools.ant.Main -f ../build.xml %1
@goto eof

:nojava
@echo 在您的操作系统上没有安装JAVA运行环境，请先设置JAVA_HOME环境变量或安装JDK
@goto eof

:eof
@pause

@if "%OS%"=="Windows_NT" ENDLOCAL
