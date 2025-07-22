@echo off
echo ================================================================
echo INSTALACION CORREGIDA - DATOS REALES IBARRA
echo NetNexus Ultra - Sistema de Telecomunicaciones
echo ================================================================
echo.

echo Opciones disponibles:
echo [1] Ejecutar script simple (recomendado)
echo [2] Ejecutar script completo
echo [3] Solo verificar conexion
echo.
set /p opcion="Seleccione una opcion (1-3): "

if "%opcion%"=="1" goto simple
if "%opcion%"=="2" goto completo  
if "%opcion%"=="3" goto verificar
goto error

:simple
echo.
echo [OPCION 1] Ejecutando script simple...
echo.
mysql -u root -p mydb < "migracion_ibarra_simple.sql"
if %ERRORLEVEL% EQU 0 (
    echo ✅ Migracion simple completada exitosamente
    goto resumen
) else (
    echo ❌ Error en la migracion simple
    echo Verifique que la base de datos 'mydb' exista
)
goto fin

:completo
echo.
echo [OPCION 2] Ejecutando script completo...
echo.
mysql -u root -p mydb < "Base_Datos_Ibarra_CORREGIDO.sql"
if %ERRORLEVEL% EQU 0 (
    echo ✅ Migracion completa exitosa
    goto resumen
) else (
    echo ❌ Error en la migracion completa
    echo Intente con la opcion 1 (script simple)
)
goto fin

:verificar
echo.
echo [OPCION 3] Verificando conexion a MySQL...
echo.
mysql -u root -p -e "SELECT 'Conexion exitosa' as Estado; USE mydb; SELECT COUNT(*) as Tablas_Existentes FROM information_schema.tables WHERE table_schema = 'mydb';"
goto fin

:resumen
echo.
echo ================================================================
echo INSTALACION COMPLETADA EXITOSAMENTE
echo ================================================================
echo.
echo ✅ DATOS INSTALADOS:
echo   • 3 Administradores (Ibarra, Imbabura)
echo   • 5 Tecnicos especializados
echo   • 7 Usuarios regulares
echo   • 7 Sectores de Ibarra representados
echo.
echo 🔑 CREDENCIALES DE PRUEBA:
echo   Administradores: maria.yepez@netnexus.ec / admin2025
echo   Tecnicos: jorge.valenzuela@netnexus.ec / tech2025
echo   Usuarios: rosa.chavez@gmail.com / user123
echo.
echo 🏠 SECTORES INCLUIDOS:
echo   • La Victoria • El Ejido • Azaya • Alpachaca
echo   • Caranqui • La Esperanza • Yahuarcocha
goto fin

:error
echo ❌ Opcion invalida. Use 1, 2 o 3.

:fin
echo.
echo Presione cualquier tecla para continuar...
pause
