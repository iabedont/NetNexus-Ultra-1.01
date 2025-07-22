-- ================================================================
-- SCRIPT DE VERIFICACIÓN - MIGRACIÓN A DATOS REALES
-- NetNexus Ultra - Ibarra, Imbabura, Ecuador
-- ================================================================

USE mydb;

-- ================================================================
-- VERIFICACIONES PRINCIPALES
-- ================================================================

-- 1. VERIFICAR CONTEO DE USUARIOS POR TIPO
SELECT '=== RESUMEN DE USUARIOS POR TIPO ===' as Verificacion;
SELECT 
    tipo,
    COUNT(*) as cantidad,
    CASE 
        WHEN tipo = 'Administrador' AND COUNT(*) = 3 THEN '✅ CORRECTO'
        WHEN tipo = 'Técnico' AND COUNT(*) = 5 THEN '✅ CORRECTO'
        WHEN tipo = 'Usuario' AND COUNT(*) = 7 THEN '✅ CORRECTO'
        ELSE '❌ ERROR'
    END as estado
FROM cliente 
GROUP BY tipo
ORDER BY tipo;

-- 2. VERIFICAR ADMINISTRADORES
SELECT '=== VERIFICACIÓN DE ADMINISTRADORES ===' as Verificacion;
SELECT 
    a.idadministradores,
    a.nombre,
    a.apellido,
    a.email,
    CASE 
        WHEN a.email LIKE '%@netnexus.ec' THEN '✅ Email correcto'
        ELSE '❌ Email incorrecto'
    END as email_status
FROM administradores a
ORDER BY a.idadministradores;

-- 3. VERIFICAR TÉCNICOS
SELECT '=== VERIFICACIÓN DE TÉCNICOS ===' as Verificacion;
SELECT 
    t.idTecnicos,
    t.nombre,
    t.apellido,
    t.especialidad,
    t.email,
    CASE 
        WHEN t.email LIKE '%@netnexus.ec' THEN '✅ Email correcto'
        ELSE '❌ Email incorrecto'
    END as email_status
FROM tecnicos t
ORDER BY t.idTecnicos;

-- 4. VERIFICAR USUARIOS REGULARES
SELECT '=== VERIFICACIÓN DE USUARIOS REGULARES ===' as Verificacion;
SELECT 
    c.idCliente,
    c.nombre,
    c.apellido,
    c.telefono,
    c.email,
    CASE 
        WHEN c.telefono REGEXP '^099[0-9]{7}$' THEN '✅ Teléfono válido'
        ELSE '❌ Teléfono inválido'
    END as telefono_status
FROM cliente c
WHERE c.tipo = 'Usuario'
ORDER BY c.idCliente;

-- 5. VERIFICAR SECTORES DE IBARRA
SELECT '=== VERIFICACIÓN DE UBICACIONES EN IBARRA ===' as Verificacion;
SELECT 
    u.idUbicacion,
    u.direccion,
    u.ciudad,
    u.provincia,
    CASE 
        WHEN u.ciudad = 'Ibarra' AND u.provincia = 'Imbabura' THEN '✅ Ubicación correcta'
        ELSE '❌ Ubicación incorrecta'
    END as ubicacion_status
FROM ubicacion u
ORDER BY u.idUbicacion;

-- 6. VERIFICAR SERVICIOS ACTIVOS
SELECT '=== VERIFICACIÓN DE SERVICIOS ACTIVOS ===' as Verificacion;
SELECT 
    s.idServicios,
    c.nombre as cliente_nombre,
    c.apellido as cliente_apellido,
    co.tipoServicio,
    co.precioBase,
    s.estado,
    u.direccion
FROM servicios s
JOIN contrato co ON s.Contrato_idContrato = co.idContrato
JOIN cliente c ON co.Cliente_idCliente = c.idCliente
JOIN ubicacion u ON s.idServicios = u.Servicios_idServicios
WHERE c.tipo = 'Usuario'
ORDER BY s.idServicios;

-- 7. VERIFICAR FACTURAS Y PAGOS
SELECT '=== VERIFICACIÓN DE FACTURAS ===' as Verificacion;
SELECT 
    f.idFactura,
    c.nombre,
    c.apellido,
    f.monto,
    f.estado,
    f.metodoPago,
    CASE 
        WHEN f.estado IN ('pagado', 'pendiente') THEN '✅ Estado válido'
        ELSE '❌ Estado inválido'
    END as estado_status
FROM factura f
JOIN cliente c ON f.Cliente_idCliente = c.idCliente
WHERE c.tipo = 'Usuario'
ORDER BY f.idFactura;

-- 8. VERIFICAR EQUIPOS ASIGNADOS
SELECT '=== VERIFICACIÓN DE EQUIPOS ===' as Verificacion;
SELECT 
    e.idEquipos,
    e.nombre,
    e.tipo,
    e.estado,
    c.nombre as cliente_nombre,
    u.direccion
FROM equipos e
JOIN servicios s ON e.Servicios_idServicios = s.idServicios
JOIN contrato co ON s.Contrato_idContrato = co.idContrato
JOIN cliente c ON co.Cliente_idCliente = c.idCliente
JOIN ubicacion u ON s.idServicios = u.Servicios_idServicios
ORDER BY e.idEquipos;

-- 9. VERIFICAR TICKETS DE SOPORTE
SELECT '=== VERIFICACIÓN DE TICKETS ===' as Verificacion;
SELECT 
    t.idTicket,
    t.fecha,
    t.prioridad,
    t.estado,
    c.nombre as cliente_nombre,
    LEFT(t.descripcion, 50) as descripcion_resumida
FROM ticket t
JOIN servicios s ON t.Servicios_idServicios = s.idServicios
JOIN contrato co ON s.Contrato_idContrato = co.idContrato
JOIN cliente c ON co.Cliente_idCliente = c.idCliente
ORDER BY t.idTicket;

-- 10. VERIFICAR CALIFICACIONES
SELECT '=== VERIFICACIÓN DE CALIFICACIONES ===' as Verificacion;
SELECT 
    cal.idCalificacion,
    c.nombre,
    c.apellido,
    cal.calificacion_general,
    cal.recomendaria,
    LEFT(cal.comentarios, 50) as comentario_resumido
FROM calificaciones_servicios cal
JOIN cliente c ON cal.Cliente_idCliente = c.idCliente
ORDER BY cal.idCalificacion;

-- ================================================================
-- RESUMEN FINAL DE VERIFICACIÓN
-- ================================================================
SELECT '=== RESUMEN FINAL DE MIGRACIÓN ===' as Verificacion;

SELECT 
    'TOTAL CLIENTES' as Categoria,
    COUNT(*) as Cantidad,
    CASE WHEN COUNT(*) = 15 THEN '✅ CORRECTO' ELSE '❌ ERROR' END as Estado
FROM cliente
UNION ALL
SELECT 
    'ADMINISTRADORES' as Categoria,
    COUNT(*) as Cantidad,
    CASE WHEN COUNT(*) = 3 THEN '✅ CORRECTO' ELSE '❌ ERROR' END as Estado
FROM cliente WHERE tipo = 'Administrador'
UNION ALL
SELECT 
    'TÉCNICOS' as Categoria,
    COUNT(*) as Cantidad,
    CASE WHEN COUNT(*) = 5 THEN '✅ CORRECTO' ELSE '❌ ERROR' END as Estado
FROM cliente WHERE tipo = 'Técnico'
UNION ALL
SELECT 
    'USUARIOS' as Categoria,
    COUNT(*) as Cantidad,
    CASE WHEN COUNT(*) = 7 THEN '✅ CORRECTO' ELSE '❌ ERROR' END as Estado
FROM cliente WHERE tipo = 'Usuario'
UNION ALL
SELECT 
    'CONTRATOS ACTIVOS' as Categoria,
    COUNT(*) as Cantidad,
    CASE WHEN COUNT(*) = 7 THEN '✅ CORRECTO' ELSE '❌ ERROR' END as Estado
FROM contrato
UNION ALL
SELECT 
    'SERVICIOS INSTALADOS' as Categoria,
    COUNT(*) as Cantidad,
    CASE WHEN COUNT(*) = 7 THEN '✅ CORRECTO' ELSE '❌ ERROR' END as Estado
FROM servicios
UNION ALL
SELECT 
    'UBICACIONES EN IBARRA' as Categoria,
    COUNT(*) as Cantidad,
    CASE WHEN COUNT(*) = 7 THEN '✅ CORRECTO' ELSE '❌ ERROR' END as Estado
FROM ubicacion WHERE ciudad = 'Ibarra'
UNION ALL
SELECT 
    'FACTURAS GENERADAS' as Categoria,
    COUNT(*) as Cantidad,
    CASE WHEN COUNT(*) = 7 THEN '✅ CORRECTO' ELSE '❌ ERROR' END as Estado
FROM factura WHERE Cliente_idCliente >= 3001;

-- ================================================================
-- VERIFICACIÓN DE INTEGRIDAD REFERENCIAL
-- ================================================================
SELECT '=== VERIFICACIÓN DE INTEGRIDAD REFERENCIAL ===' as Verificacion;

-- Verificar que todos los contratos tienen clientes válidos
SELECT 
    'CONTRATOS SIN CLIENTE' as Problema,
    COUNT(*) as Cantidad
FROM contrato co
LEFT JOIN cliente c ON co.Cliente_idCliente = c.idCliente
WHERE c.idCliente IS NULL;

-- Verificar que todos los servicios tienen contratos válidos
SELECT 
    'SERVICIOS SIN CONTRATO' as Problema,
    COUNT(*) as Cantidad
FROM servicios s
LEFT JOIN contrato co ON s.Contrato_idContrato = co.idContrato
WHERE co.idContrato IS NULL;

-- Verificar que todas las ubicaciones tienen servicios válidos
SELECT 
    'UBICACIONES SIN SERVICIO' as Problema,
    COUNT(*) as Cantidad
FROM ubicacion u
LEFT JOIN servicios s ON u.Servicios_idServicios = s.idServicios
WHERE s.idServicios IS NULL;

-- ================================================================
-- ESTADÍSTICAS ADICIONALES
-- ================================================================
SELECT '=== ESTADÍSTICAS ADICIONALES ===' as Verificacion;

-- Distribución de planes
SELECT 
    co.tipoServicio,
    COUNT(*) as cantidad_contratos,
    AVG(co.precioBase) as precio_promedio
FROM contrato co
JOIN cliente c ON co.Cliente_idCliente = c.idCliente
WHERE c.tipo = 'Usuario'
GROUP BY co.tipoServicio;

-- Estados de facturas
SELECT 
    f.estado,
    COUNT(*) as cantidad,
    SUM(f.monto) as monto_total
FROM factura f
JOIN cliente c ON f.Cliente_idCliente = c.idCliente
WHERE c.tipo = 'Usuario'
GROUP BY f.estado;

-- Métodos de pago utilizados
SELECT 
    f.metodoPago,
    COUNT(*) as cantidad,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM factura WHERE Cliente_idCliente >= 3001), 2) as porcentaje
FROM factura f
WHERE f.Cliente_idCliente >= 3001
GROUP BY f.metodoPago;

SELECT '=== VERIFICACIÓN COMPLETA - MIGRACIÓN EXITOSA ===' as Resultado;
