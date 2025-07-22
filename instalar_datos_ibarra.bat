@echo off
echo ================================================================
echo INSTALACION DE BASE DE DATOS - DATOS REALES IBARRA
echo NetNexus Ultra - Sistema de Telecomunicaciones
echo ================================================================
echo.

echo [1/3] Conectando a MySQL...
echo Ejecutando script de migracion de datos...
echo.

mysql -u root -p mydb < "Base_Datos_Ibarra_V2.0.sql"

if %ERRORLEVEL% EQU 0 (
    echo ✅ Migracion de datos completada exitosamente
    echo.
    echo [2/3] Verificando integridad de datos...
    
    mysql -u root -p mydb < "verificar_migracion_ibarra.sql"
    
    if %ERRORLEVEL% EQU 0 (
        echo ✅ Verificacion completada
        echo.
        echo [3/3] Resumen de la instalacion:
        echo.
        echo ✅ 3 Administradores configurados
        echo ✅ 5 Tecnicos especializados  
        echo ✅ 7 Usuarios con servicios activos
        echo ✅ 7 Sectores de Ibarra representados
        echo ✅ Datos realistas de Imbabura, Ecuador
        echo.
        echo ================================================================
        echo INSTALACION COMPLETADA EXITOSAMENTE
        echo ================================================================
        echo.
        echo Credenciales de prueba:
        echo Administradores: maria.yepez@netnexus.ec / admin2025
        echo Tecnicos: jorge.valenzuela@netnexus.ec / tech2025  
        echo Usuarios: rosa.chavez@gmail.com / user123
        echo.
        echo Presione cualquier tecla para continuar...
    ) else (
        echo ❌ Error en la verificacion de datos
        echo Revise el archivo verificar_migracion_ibarra.sql
    )
) else (
    echo ❌ Error en la migracion de datos
    echo Verifique la conexion a MySQL y los permisos
)

pause
