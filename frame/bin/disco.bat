@echo off
title Disco
color a
rem by lengyu 2012-12-05 11:54
if "%OS%"=="Windows_NT" SETLOCAL
:init
if "%JAVA_HOME%"=="" goto nojava
goto main
goto eof

:main
if not exist ../src/core/src/main/resources/cn/disco/web/resources/disco-web.xml (
    @set ROOT=0
    @call setenv.bat
) else (
    @set ROOT=1
)

if "%1"=="war" goto war
if "%1"=="run" goto run
if "%1"=="crud" goto crud
if "%1"=="project" goto project

if "%1"=="" goto usage
goto eof


:usage
@echo ---------------------------------------------------------------------------------------------------------------------------
@echo 【在生成项目的/bin目录下执行】                                                                                                              
@echo ---------------------------------------------------------------------------------------------------------------------------
@echo disco war                               ——打包当前工程成一个war包
@echo disco run                               ——用Maven直接运行本工程
@echo disco crud cn.disco.domain.Example      ——根据一个类来生成它的相关应用
@echo disco crud cn.disco.domain.Example ../src/main/java/cn/disco/domain/Example.java  ——根据一个JAVA源文件来生成它的相关应用
@echo 以上两个crud只是crud的几种灵活使用方法，它的宗旨就是，给一个JAVA类，它会根据JAVA类生成相应的其它文件。
@echo.
@echo ---------------------------------------------------------------------------------------------------------------------------
@echo 【当目录为Disco框架的/bin目录时】
@echo ---------------------------------------------------------------------------------------------------------------------------
@echo disco project d:\myapp           ——在d:\下生成一个名为myapp的简单MVC应用
@echo disco project d:\myapp -djs      ——在d:\下生成一个名为myapp的应用且项目为(disco + JPA + Spring)的结构
@echo disco project d:\myapp -djs -maven            ——在d:\下生成一个名为myapp的托管于Maven的应用
@echo disco project d:\myapp -djs -extjs            ——生成基于DJS(Disco+JPA+Spring)构架及界面基于ExtJS的应用项目
@echo disco project d:\myapp -ssh -extjs            ——生成基于SSH1(Struts1.X+Hibernate+Spring)构架及界面基于ExtJS的应用项目
@echo disco project d:\myapp -ssh2 -extjs           ——生成基于SSH2(Struts2.X+Hibernate+Spring)构架及界面基于ExtJS的应用项目
@echo disco project d:\myapp -djs -extjs -maven -platform  ——生成Disco企业级快速开发平台
@echo ---------------------------------------------------------------------------------------------------------------------------
goto eof

:crud
@if "1"=="%ROOT%" @goto isDISCOPROJECT
if "%2"=="" goto usage
@echo start crud %2
title 生成%2 CRUD
if not "%3"=="" @javac %3 -d ../src/main/webapp/WEB-INF/classes/ -encoding UTF-8
java cn.disco.generator.Generator %2
goto eof

:project
if "%2"=="" goto usage
if "0"=="%ROOT%" goto notDISCOPROJECT

if "%6"=="-platform" goto PROJECT_DJS_PLATFORM
if "%6"=="platform" goto PROJECT_DJS_PLATFORM

if "%4"=="-maven" goto projectDJSMAVEN
if "%4"=="maven" goto projectDJSMAVEN
if "%4"=="-mvn" goto projectDJSMAVEN
if "%4"=="mvn" goto projectDJSMAVEN

if "%4"=="-extjs" goto projectDJSEXTJS
if "%4"=="extjs" goto projectDJSEXTJS


if "%3"=="-extjs" goto projectEXTJS
if "%3"=="extjs" goto projectEXTJS


if "%3"=="djs" goto projectDJS
if "%3"=="-djs" goto projectDJS

if "%3"=="ssh" goto projectSSHEXTJS
if "%3"=="-ssh" goto projectSSHEXTJS

if "%3"=="ssh2" goto projectSSH2EXTJS
if "%3"=="-ssh2" goto projectSSH2EXTJS

@echo 开始生成MINI项目%2
title 生成%2 MINI Project
if exist "%2" @echo %2已存在了，为了安全起见，请先删除本目录或指定一个不存在的目录！
if exist "%2" goto eof
if not exist "%2" md "%2"
@xcopy "templates\mini" "%2" /E /C /F /I /Q /R /K /Y >nul 2>nul
@rem copy the disco jars to target dir
@copy "..\lib\disco-core-*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul
@copy "..\lib\required\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul
@copy "lib\build\*.jar" "%2\bin\lib\build\" >nul 2>nul
if not exist "%2\bin" md "%2\bin"
@copy "*.bat" "%2\bin\" >nul 2>nul
@echo 成功完成，正在打开目标目录
@explorer "%2"
goto eof

:projectDJS
if "%3"=="" goto usage
@echo 开始生成Disco JPA Spring Project项目 %2
title 生成%2 Disco JPA Spring Project
if exist "%2" @echo %2已存在了，为了安全起见，请先删除本目录或指定一个不存在的目录！
if exist "%2" goto eof
if not exist "%2" md "%2"
@xcopy "templates\djs" "%2" /E /C /F /I /Q /R /K /Y >nul 2>nul
@rem copy the disco jars to target dir
@copy "..\lib\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul
@copy "..\lib\required\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul

@copy "..\lib\jpa\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul
@copy "..\lib\spring\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul
@copy "..\lib\other\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul


@copy "lib\build\*.jar" "%2\bin\lib\build\" >nul 2>nul
rem @del "%2\src\main\webapp\WEB-INF\lib\servlet-api-2.5-6.1.4.jar" /q >nul 2>nul
rem @del "%2\pom.xml" /q
@rd "%2\4maven" /S /Q
@del "%2\djs.launch" /q
if not exist "%2\bin" md "%2\bin"
@copy "*.bat" "%2\bin\" >nul 2>nul
@echo 成功完成，正在打开目标目录
@explorer "%2"
goto eof

:projectDJSMAVEN
if "%3"=="" goto usage
if "%4"=="" goto usage
@echo 开始生成Disco JPA Spring Project项目，(使用MAVEN管理项目) %2
title 生成%2 Disco JPA Spring Project
if exist "%2" @echo %2已存在了，为了安全起见，请先删除本目录或指定一个不存在的目录！
if exist "%2" @explorer "%2"
if exist "%2" goto eof
if not exist "%2" md "%2"
@xcopy "templates\djs" "%2" /E /C /F /I /Q /R /K /Y >nul 2>nul
if not exist "%2\bin" md "%2\bin"
@copy "*.bat" "%2\bin\" >nul 2>nul
cd /d "%2"
@echo 正在生成eclipse maven工程
call mvn eclipse:eclipse install
@echo 成功生成项目，正在打开目标目录
@explorer "%2"
goto eof

:PROJECT_DJS_PLATFORM
if "%3"=="" goto usage
if "%4"=="" goto usage
if "%5"=="" goto usage
if "%6"=="" goto usage
@echo 开始生成Disco快速开发平台，(使用MAVEN管理项目) %2
@echo 请耐心等待，正在复制平台所需要的所有文件。
title 生成%2 Disco快速开发平台
if exist "%2" @echo %2已存在了，为了安全起见，请先删除本目录或指定一个不存在的目录！
if exist "%2" @explorer "%2"
if exist "%2" goto eof
if not exist "%2" md "%2"
@xcopy "templates\djs" "%2" /E /C /F /I /Q /R /K /Y >nul 2>nul
@xcopy "templates\platform" "%2" /E /C /F /I /Q /R /K /Y >nul 2>nul
if not exist "%2\bin" md "%2\bin"
@copy "*.bat" "%2\bin\" >nul 2>nul
cd /d "%2"
@echo 正在生成eclipse maven工程
call mvn eclipse:eclipse install
@echo 成功生成项目，正在打开目标目录
@explorer "%2"
goto eof



:projectDJSEXTJS
if "%3"=="" goto usage
if "%3"=="-ssh2" goto projectSSH2EXTJS
if "%3"=="ssh2" goto projectSSH2EXTJS
if "%3"=="-ssh" goto projectSSHEXTJS
if "%3"=="ssh" goto projectSSHEXTJS
@echo 开始生成Disco JPA Spring Project项目 %2
title 生成%2 Disco JPA Spring Project
if exist "%2" @echo %2已存在了，为了安全起见，请先删除本目录或指定一个不存在的目录！
if exist "%2" goto eof
if not exist "%2" md "%2"
@xcopy "templates\djs" "%2" /E /C /F /I /Q /R /K /Y >nul 2>nul
@xcopy "..\lib\extjs\ext-3.2" "%2\src\main\webapp\plugins\extjs\ext-3.2" /E /C /F /I /Q /R /K /Y >nul 2>nul
@copy "templates\extjs\*.*" "%2\templates\" >nul 2>nul
@xcopy "templates\extjs\disco\*.*" "%2\src\main\webapp\plugins\extjs\disco\" /E /C /F /I /Q /R /K /Y>nul 2>nul
if not exist "%2\bin" md "%2\bin"
@copy "*.bat" "%2\bin\" >nul 2>nul

@echo 正在生成eclipse maven工程
cd /d "%2"
call mvn eclipse:eclipse install

@echo 成功完成，正在打开目标目录
@explorer "%2"
goto eof


:projectSSH2EXTJS
if "%3"=="" goto usage
@echo 开始生成Disco JPA Spring Project项目 %2
title 生成%2 Disco JPA Spring Project
if exist "%2" @echo %2已存在了，为了安全起见，请先删除本目录或指定一个不存在的目录！
if exist "%2" goto eof
if not exist "%2" md "%2"
@xcopy "templates\djs" "%2" /E /C /F /I /Q /R /K /Y >nul 2>nul
@rem copy the disco jars to target dir
@copy "..\lib\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul
@copy "..\lib\required\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul

@copy "..\lib\jpa\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul
@copy "..\lib\spring\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul
@copy "..\lib\other\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul
@copy "..\lib\struts2\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul

@xcopy "..\lib\extjs\ext-3.2" "%2\src\main\webapp\plugins\extjs\ext-3.2" /E /C /F /I /Q /R /K /Y >nul 2>nul
@copy "lib\build\*.jar" "%2\bin\lib\build\" >nul 2>nul
@copy "templates\extjs\*.*" "%2\templates\" >nul 2>nul
@xcopy "templates\extjs\disco\*.*" "%2\src\main\webapp\plugins\extjs\disco\" /E /C /F /I /Q /R /K /Y >nul 2>nul

@del "%2\src\main\webapp\WEB-INF\disco-web.xml" >nul 2>nul
@del "%2\src\main\webapp\WEB-INF\mvc.xml" >nul 2>nul

@xcopy "templates\struts2\*.*" "%2\" /E /C /F /I /Q /R /K /Y>nul 2>nul
rem @del "%2\src\main\webapp\WEB-INF\lib\servlet-api-2.5-6.1.4.jar" /q >nul 2>nul
rem @del "%2\pom.xml" /q
if not exist "%2\bin" md "%2\bin"
@copy "*.bat" "%2\bin\" >nul 2>nul
@echo 成功完成，正在打开目标目录
@explorer "%2"
goto eof

:projectSSHEXTJS
if "%3"=="" goto usage
@echo 开始生成Disco JPA Spring Project项目 %2
title 生成%2 Disco JPA Spring Project
if exist "%2" @echo %2已存在了，为了安全起见，请先删除本目录或指定一个不存在的目录！
if exist "%2" goto eof
if not exist "%2" md "%2"
@xcopy "templates\djs" "%2" /E /C /F /I /Q /R /K /Y >nul 2>nul
@rem copy the disco jars to target dir
@copy "..\lib\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul
@copy "..\lib\required\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul

@copy "..\lib\jpa\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul
@copy "..\lib\spring\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul
@copy "..\lib\other\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul
@copy "..\lib\struts1\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul

@xcopy "..\lib\extjs\ext-3.2" "%2\src\main\webapp\plugins\extjs\ext-3.2" /E /C /F /I /Q /R /K /Y >nul 2>nul
@copy "lib\build\*.jar" "%2\bin\lib\build\" >nul 2>nul
@copy "templates\extjs\*.*" "%2\templates\" >nul 2>nul
@xcopy "templates\extjs\disco\*.*" "%2\src\main\webapp\plugins\extjs\disco\" /E /C /F /I /Q /R /K /Y>nul 2>nul

@del "%2\src\main\webapp\WEB-INF\disco-web.xml" >nul 2>nul
@del "%2\src\main\webapp\WEB-INF\mvc.xml" >nul 2>nul

@xcopy "templates\struts1\*.*" "%2\" /E /C /F /I /Q /R /K /Y>nul 2>nul
rem @del "%2\src\main\webapp\WEB-INF\lib\servlet-api-2.5-6.1.4.jar" /q >nul 2>nul
rem @del "%2\pom.xml" /q
if not exist "%2\bin" md "%2\bin"
@copy "*.bat" "%2\bin\" >nul 2>nul
@echo 成功完成，正在打开目标目录
@explorer "%2"
goto eof


:projectEXTJS
@echo 开始生成EXTJS项目%2
title 生成%2 MINI ProjectEXTJS
if exist "%2" @echo %2已存在了，为了安全起见，请先删除本目录或指定一个不存在的目录！
if exist "%2" goto eof
if not exist "%2" md "%2"
@xcopy "templates\mini" "%2" /E /C /F /I /Q /R /K /Y >nul 2>nul
@rem copy the disco jars to target dir
@copy "..\lib\disco-core-*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul
@copy "..\lib\required\*.jar" "%2\src\main\webapp\WEB-INF\lib\" >nul 2>nul
@xcopy "..\lib\extjs\ext-3.2" "%2\src\main\webapp\plugins\extjs\ext-3.2" /E /C /F /I /Q /R /K /Y >nul 2>nul
@copy "lib\build\*.jar" "%2\bin\lib\build\" >nul 2>nul
if not exist "%2\bin" md "%2\bin"
@copy "*.bat" "%2\bin\" >nul 2>nul
@echo 成功完成，正在打开目标目录
@explorer "%2"
goto eof




:war
@if "1"=="%ROOT%" @goto isDISCOPROJECT
@call build war
goto eof

:run
@if "1"=="%ROOT%" @goto isDISCOPROJECT
@echo 开始运行本项目
cd ..
@mvn jetty:run
goto eof


:nojava
@echo 在您的操作系统上没有安装JAVA运行环境，请先设置JAVA_HOME环境变量或安装JDK
goto eof

:isDISCOPROJECT
@echo 本项目是DISCO项目，不能在此执行当前命令！
@echo.
@echo 此处可以执行的命令有：
@echo disco project d:\myapp              ——在d:\下生成一个名为myapp的简单MVC应用
@echo disco project d:\myapp -djs         ——在d:\下生成一个名为myapp的应用
@echo 且项目为（Disco + JPA + Spring）的结构
@echo disco project d:\myapp -djs -maven          ——在d:\下生成一个名为myapp的托管于Maven的应用
goto eof

:notDISCOPROJECT
@echo 本项目不是DISCO项目，请在Disco项目下执行本操作！
@echo.
@echo 此处可以执行的命令有：
@echo disco war                         ——打包当前工程成一个war包
@echo disco run                         ——用Maven直接运行本工程
@echo disco crud cn.disco.domain.Example                ——根据一个类来生成它的相关应用
@echo disco crud  cn.disco.domain.Example ../src/main/java/org/disco/domain/Example.java   ——根据一个JAVA源文件来生成它的相关应用
@echo 以上两个crud只是crud的几种灵活使用方法，它的宗旨就是，给一个JAVA类，它会根据JAVA类生成相应的其它文件。

:eof
@rem pause

if "%OS%"=="Windows_NT" ENDLOCAL
