@echo off
echo Compilando clases de NetNexus Ultra...

REM Crear directorio de salida si no existe
if not exist "build\classes" mkdir "build\classes"

@echo off
echo Compilando clases de NetNexus Ultra...

REM Crear directorio de salida si no existe
if not exist "build\classes" mkdir "build\classes"

REM PASO 1: Limpiar archivos .class potencialmente corruptos
echo Limpiando archivos .class previos...
del /Q "build\classes\GUI_CHIDO\*.class" 2>nul

REM PASO 2: Compilar dependencias base
echo Compilando clases base...
for %%f in (src\Clases\*.java) do (
    javac -cp "libraries\mysql-connector-j-9.3.0.jar" -d "build\classes" "%%f" 2>nul
)

REM PASO 3: Compilar Facturación.java (CRÍTICO - debe compilarse desde el archivo correcto)
echo Compilando Facturación.java...
javac -cp "libraries\mysql-connector-j-9.3.0.jar;build\classes" -d "build\classes" "src\GUI_CHIDO\Facturación.java"

if errorlevel 1 (
    echo Error compilando Facturación.java - CRÍTICO
    pause
    exit /b 1
)

REM PASO 4: Verificar que setParentFrame está disponible
echo Verificando método setParentFrame en Facturación...
javap -cp "build\classes" -public GUI_CHIDO.Facturación | findstr "setParentFrame" >nul
if errorlevel 1 (
    echo ERROR: método setParentFrame no encontrado en Facturación.class
    pause
    exit /b 1
) else (
    echo ✅ método setParentFrame verificado
)

REM PASO 5: Compilar clases de planes
echo Compilando PUltra.java...
javac -cp "libraries\mysql-connector-j-9.3.0.jar;build\classes" -d "build\classes" "src\GUI_CHIDO\PUltra.java"

echo Compilando PEstandar.java...
javac -cp "libraries\mysql-connector-j-9.3.0.jar;build\classes" -d "build\classes" "src\GUI_CHIDO\PEstandar.java"

echo Compilando PBasic.java...
javac -cp "libraries\mysql-connector-j-9.3.0.jar;build\classes" -d "build\classes" "src\GUI_CHIDO\PBasic.java"

if errorlevel 1 (
    echo Error compilando PUltra.java
    pause
    exit /b 1
)

echo Compilación completada exitosamente!
echo.
echo Archivos compilados:
dir "build\classes\GUI_CHIDO\Facturación*.class" "build\classes\GUI_CHIDO\PUltra.class" 2>nul

pause
