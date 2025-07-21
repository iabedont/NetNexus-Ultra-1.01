@echo off
cd /d "c:\Users\ASUS\OneDrive - Universidad Tecnica del Norte\Documentos\Uni 3.0\Programación Avanzada\Proyecto\NetNexus Ultra Versiones\NetNexus Ultra V 1.1.0\NetNexus Ultra\NetNexus Ultra 1.01"

echo ====================================
echo Verificando estructura del proyecto...
echo ====================================

if not exist "src\Clases\Main.java" (
    echo ERROR: No se encuentra src\Clases\Main.java
    pause
    exit /b 1
)

if not exist "libraries\mysql-connector-j-9.3.0.jar" (
    echo ERROR: No se encuentra libraries\mysql-connector-j-9.3.0.jar
    pause
    exit /b 1
)

echo Archivos necesarios encontrados.

echo ====================================
echo Compilando desde cero...
echo ====================================

REM Crear directorio de compilación si no existe
if not exist "build\classes\Clases" mkdir "build\classes\Clases"
if not exist "build\classes\GUI_CHIDO" mkdir "build\classes\GUI_CHIDO"

REM Limpiar archivos de compilación previos
del /Q "build\classes\Clases\*.class" 2>nul
del /Q "build\classes\GUI_CHIDO\*.class" 2>nul

echo Compilando clases principales...
javac -cp "libraries\mysql-connector-j-9.3.0.jar" -d "build\classes" src\Clases\*.java

if %ERRORLEVEL% neq 0 (
    echo ERROR: Falló la compilación de las clases principales
    pause
    exit /b 1
)

echo Compilando interfaz gráfica...
javac -cp "libraries\mysql-connector-j-9.3.0.jar;build\classes" -d "build\classes" src\GUI_CHIDO\*.java

if %ERRORLEVEL% neq 0 (
    echo ERROR: Falló la compilación de la interfaz gráfica
    pause
    exit /b 1
)

echo ====================================
echo Verificando Main.class...
echo ====================================

if not exist "build\classes\Clases\Main.class" (
    echo ERROR: No se generó Main.class después de la compilación
    pause
    exit /b 1
)

echo Main.class encontrado exitosamente.

echo ====================================
echo Ejecutando aplicación...
echo ====================================

java -cp "build\classes;libraries\mysql-connector-j-9.3.0.jar" Clases.Main

if %ERRORLEVEL% neq 0 (
    echo ERROR: Falló la ejecución de la aplicación
    pause
    exit /b 1
)

echo ====================================
echo Aplicación ejecutada exitosamente!
echo ====================================
pause
