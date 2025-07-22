@echo off
echo ================================================================
echo           APLICANDO MIGRACIÓN DATOS REALES IBARRA
echo ================================================================
echo.

echo Intentando localizar MySQL...

REM Verificar rutas comunes de MySQL
set MYSQL_PATH=""

if exist "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" (
    set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
    echo MySQL encontrado en: C:\Program Files\MySQL\MySQL Server 8.0\bin\
) else if exist "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe" (
    set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe"
    echo MySQL encontrado en: C:\Program Files\MySQL\MySQL Server 5.7\bin\
) else if exist "C:\xampp\mysql\bin\mysql.exe" (
    set MYSQL_PATH="C:\xampp\mysql\bin\mysql.exe"
    echo MySQL encontrado en: C:\xampp\mysql\bin\
) else if exist "C:\wamp64\bin\mysql\mysql8.0.31\bin\mysql.exe" (
    set MYSQL_PATH="C:\wamp64\bin\mysql\mysql8.0.31\bin\mysql.exe"
    echo MySQL encontrado en: C:\wamp64\bin\mysql\
) else (
    echo ERROR: No se pudo localizar MySQL
    echo Por favor instala MySQL o verifica la ruta
    echo.
    echo Rutas comunes donde buscar:
    echo - C:\Program Files\MySQL\MySQL Server X.X\bin\
    echo - C:\xampp\mysql\bin\
    echo - C:\wamp64\bin\mysql\
    echo.
    pause
    exit /b 1
)

echo.
echo ================================================================
echo APLICANDO SCRIPT: migracion_ibarra_simple.sql
echo ================================================================
echo.
echo IMPORTANTE: 
echo - Se reemplazarán los datos de prueba con datos reales de Ibarra
echo - Se aplicarán contraseñas realistas
echo - Cada usuario tendrá múltiples servicios
echo.
echo Presiona cualquier tecla para continuar o Ctrl+C para cancelar...
pause > nul

echo.
echo Ejecutando migración...
echo Ingresa la contraseña de MySQL cuando se solicite:

%MYSQL_PATH% -u root -p mydb < migracion_ibarra_simple.sql

if %ERRORLEVEL% == 0 (
    echo.
    echo ================================================================
    echo          MIGRACIÓN COMPLETADA CON ÉXITO
    echo ================================================================
    echo.
    echo ✓ 15 personas de Ibarra, Imbabura agregadas
    echo ✓ 3 Administradores con contraseñas realistas
    echo ✓ 5 Técnicos especializados
    echo ✓ 7 Usuarios con servicios múltiples
    echo ✓ Contratos, servicios, facturas y equipos configurados
    echo.
    echo Los datos de prueba han sido reemplazados exitosamente.
) else (
    echo.
    echo ================================================================
    echo                    ERROR EN LA MIGRACIÓN
    echo ================================================================
    echo.
    echo Revisa los mensajes de error anteriores.
    echo Posibles causas:
    echo - Contraseña incorrecta
    echo - Base de datos 'mydb' no existe
    echo - Problemas de permisos
    echo.
)

echo.
echo Presiona cualquier tecla para salir...
pause > nul
