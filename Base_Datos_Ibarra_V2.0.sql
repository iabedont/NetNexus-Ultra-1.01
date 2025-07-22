-- ================================================================
-- SCRIPT DE MIGRACIÓN A DATOS REALES - IBARRA, IMBABURA, ECUADOR
-- NetNexus Ultra - Sistema de Telecomunicaciones
-- Fecha: 22 de julio de 2025
-- ================================================================

USE mydb;

-- ================================================================
-- LIMPIEZA DE DATOS DE PRUEBA
-- ================================================================

-- Desactivar verificaciones de claves foráneas temporalmente
SET FOREIGN_KEY_CHECKS = 0;

-- Limpiar tablas relacionadas primero
DELETE FROM respuestas_empresa;
DELETE FROM utilidad_calificaciones;
DELETE FROM calificaciones_servicios;
DELETE FROM vehicle_problems;
DELETE FROM vehicle_requests;
DELETE FROM vehiculos_flota;
DELETE FROM ticket;
DELETE FROM ubicacion;
DELETE FROM servicios;
DELETE FROM factura;
DELETE FROM contrato;
DELETE FROM tarjetas_usuario;
DELETE FROM equipos;
DELETE FROM tecnicos;
DELETE FROM administradores;
DELETE FROM usuarios;
DELETE FROM cliente;

-- Reactivar verificaciones de claves foráneas
SET FOREIGN_KEY_CHECKS = 1;

-- ================================================================
-- INSERCIÓN DE DATOS REALES - IBARRA, IMBABURA, ECUADOR
-- ================================================================

-- ================================================================
-- CLIENTES (15 TOTAL: 3 ADMIN + 5 TÉCNICOS + 7 USUARIOS)
-- ================================================================

-- ADMINISTRADORES (3)
INSERT INTO cliente (idCliente, nombre, apellido, telefono, email, contrasena, tipo) VALUES 
(1001, 'María Elena', 'Yépez Rueda', '0991847362', 'maria.yepez@netnexus.ec', 'admin2025', 'Administrador'),
(1002, 'Carlos Alberto', 'Morales Intriago', '0992536174', 'carlos.morales@netnexus.ec', 'admin2025', 'Administrador'),
(1003, 'Ana Lucía', 'Santacruz Flores', '0993428517', 'ana.santacruz@netnexus.ec', 'admin2025', 'Administrador');

-- TÉCNICOS (5)
INSERT INTO cliente (idCliente, nombre, apellido, telefono, email, contrasena, tipo) VALUES 
(2001, 'Jorge Luis', 'Valenzuela Pozo', '0994827361', 'jorge.valenzuela@netnexus.ec', 'tech2025', 'Técnico'),
(2002, 'Patricia Elizabeth', 'Cadena Guerrero', '0995739482', 'patricia.cadena@netnexus.ec', 'tech2025', 'Técnico'),
(2003, 'Miguel Andrés', 'Rosero Benítez', '0996851473', 'miguel.rosero@netnexus.ec', 'tech2025', 'Técnico'),
(2004, 'Silvia Maribel', 'Ruiz Moncayo', '0997264851', 'silvia.ruiz@netnexus.ec', 'tech2025', 'Técnico'),
(2005, 'Edison Ramiro', 'Terán Villarreal', '0998573649', 'edison.teran@netnexus.ec', 'tech2025', 'Técnico');

-- USUARIOS REGULARES (7)
INSERT INTO cliente (idCliente, nombre, apellido, telefono, email, contrasena, tipo) VALUES 
(3001, 'Rosa María', 'Chávez Montenegro', '0991364827', 'rosa.chavez@gmail.com', 'user123', 'Usuario'),
(3002, 'Fabián Oswaldo', 'Imbaquingo Yar', '0992475816', 'fabian.imbaquingo@hotmail.com', 'user123', 'Usuario'),
(3003, 'Carmen Dolores', 'Andrade Paredes', '0993586294', 'carmen.andrade@yahoo.com', 'user123', 'Usuario'),
(3004, 'Roberto Carlos', 'Maldonado Enríquez', '0994697385', 'roberto.maldonado@outlook.com', 'user123', 'Usuario'),
(3005, 'Luz Marina', 'Bolaños Cevallos', '0995748172', 'luz.bolanos@gmail.com', 'user123', 'Usuario'),
(3006, 'Hernán Patricio', 'Vásquez Calderón', '0996859263', 'hernan.vasquez@hotmail.com', 'user123', 'Usuario'),
(3007, 'Gloria Esperanza', 'Cuasapaz Malte', '0997961384', 'gloria.cuasapaz@gmail.com', 'user123', 'Usuario');

-- ================================================================
-- ADMINISTRADORES
-- ================================================================
INSERT INTO administradores (idadministradores, nombre, apellido, telefono, email, contrasena) VALUES 
(1001, 'María Elena', 'Yépez Rueda', '0991847362', 'maria.yepez@netnexus.ec', 'admin2025'),
(1002, 'Carlos Alberto', 'Morales Intriago', '0992536174', 'carlos.morales@netnexus.ec', 'admin2025'),
(1003, 'Ana Lucía', 'Santacruz Flores', '0993428517', 'ana.santacruz@netnexus.ec', 'admin2025');

-- ================================================================
-- TÉCNICOS
-- ================================================================
INSERT INTO tecnicos (idTecnicos, Vehiculos_idVehiculos, nombre, apellido, telefono, email, especialidad) VALUES 
(2001, 1, 'Jorge Luis', 'Valenzuela Pozo', '0994827361', 'jorge.valenzuela@netnexus.ec', 'Redes y Fibra Óptica'),
(2002, 2, 'Patricia Elizabeth', 'Cadena Guerrero', '0995739482', 'patricia.cadena@netnexus.ec', 'Instalaciones WiFi'),
(2003, 3, 'Miguel Andrés', 'Rosero Benítez', '0996851473', 'miguel.rosero@netnexus.ec', 'Telecomunicaciones'),
(2004, 4, 'Silvia Maribel', 'Ruiz Moncayo', '0997264851', 'silvia.ruiz@netnexus.ec', 'Soporte Técnico'),
(2005, 5, 'Edison Ramiro', 'Terán Villarreal', '0998573649', 'edison.teran@netnexus.ec', 'Mantenimiento de Equipos');

-- ================================================================
-- CONTRATOS DE SERVICIOS
-- ================================================================
INSERT INTO contrato (idContrato, Cliente_idCliente, fechaInicio, fechaFin, precioBase, tipoServicio) VALUES 
-- Contratos de usuarios regulares
(10001, 3001, '2024-03-15', '2025-03-15', 35.00, 'Plan Estándar'),
(10002, 3002, '2024-05-20', '2025-05-20', 60.00, 'Plan Ultra'),
(10003, 3003, '2024-01-10', '2025-01-10', 20.00, 'Plan Básico'),
(10004, 3004, '2024-07-05', '2025-07-05', 35.00, 'Plan Estándar'),
(10005, 3005, '2024-09-12', '2025-09-12', 60.00, 'Plan Ultra'),
(10006, 3006, '2024-11-08', '2025-11-08', 20.00, 'Plan Básico'),
(10007, 3007, '2024-02-18', '2025-02-18', 35.00, 'Plan Estándar');

-- ================================================================
-- SERVICIOS
-- ================================================================
INSERT INTO servicios (idServicios, Contrato_idContrato, TipoServicio_idTipoServicio, Tecnicos_idTecnicos, fecha, descripcion, estado) VALUES 
(10001, 10001, 2, 2001, '2024-03-15', 'Instalación Plan Estándar - Rosa Chávez - Sector La Victoria', 'completado'),
(10002, 10002, 3, 2002, '2024-05-20', 'Instalación Plan Ultra - Fabián Imbaquingo - Sector El Ejido', 'completado'),
(10003, 10003, 1, 2003, '2024-01-10', 'Instalación Plan Básico - Carmen Andrade - Sector Azaya', 'completado'),
(10004, 10004, 2, 2004, '2024-07-05', 'Instalación Plan Estándar - Roberto Maldonado - Sector Alpachaca', 'completado'),
(10005, 10005, 3, 2005, '2024-09-12', 'Instalación Plan Ultra - Luz Bolaños - Sector Caranqui', 'completado'),
(10006, 10006, 1, 2001, '2024-11-08', 'Instalación Plan Básico - Hernán Vásquez - Sector La Esperanza', 'completado'),
(10007, 10007, 2, 2002, '2024-02-18', 'Instalación Plan Estándar - Gloria Cuasapaz - Sector Yahuarcocha', 'completado');

-- ================================================================
-- UBICACIONES (Sectores reales de Ibarra)
-- ================================================================
INSERT INTO ubicacion (idUbicacion, Servicios_idServicios, direccion, ciudad, provincia, codigoPostal) VALUES 
(10001, 10001, 'Av. Mariano Acosta 8-45 y Cristóbal de Troya', 'Ibarra', 'Imbabura', '100150'),
(10002, 10002, 'Calle Sucre 7-89 y García Moreno', 'Ibarra', 'Imbabura', '100150'),
(10003, 10003, 'Av. Jaime Rivadeneira 12-34 y Olmedo', 'Ibarra', 'Imbabura', '100150'),
(10004, 10004, 'Calle Bolívar 5-67 y Flores', 'Ibarra', 'Imbabura', '100150'),
(10005, 10005, 'Av. El Retorno 15-23 y Panamericana Norte', 'Ibarra', 'Imbabura', '100150'),
(10006, 10006, 'Calle José Miguel Leoro 9-45 y Maldonado', 'Ibarra', 'Imbabura', '100150'),
(10007, 10007, 'Av. Atahualpa 18-67 y Teodoro Gómez', 'Ibarra', 'Imbabura', '100150');

-- ================================================================
-- FACTURAS
-- ================================================================
INSERT INTO factura (idFactura, Cliente_idCliente, fecha, monto, estado, metodoPago, numeroTarjeta) VALUES 
(20001, 3001, '2024-12-15', 35.00, 'pagado', 'Tarjeta de Credito', '**** **** **** 4892'),
(20002, 3002, '2024-12-20', 60.00, 'pagado', 'Agencia', NULL),
(20003, 3003, '2024-12-10', 20.00, 'pagado', 'Tarjeta de Debito', '**** **** **** 7351'),
(20004, 3004, '2024-12-05', 35.00, 'pendiente', 'Tarjeta de Credito', '**** **** **** 2648'),
(20005, 3005, '2024-12-12', 60.00, 'pagado', 'Agencia', NULL),
(20006, 3006, '2024-12-08', 20.00, 'pagado', 'Tarjeta de Debito', '**** **** **** 9517'),
(20007, 3007, '2024-12-18', 35.00, 'pagado', 'Tarjeta de Credito', '**** **** **** 8263');

-- ================================================================
-- TARJETAS DE USUARIO (Datos realistas enmascarados)
-- ================================================================
INSERT INTO tarjetas_usuario (idTarjeta, Cliente_idCliente, tipo, numeroTarjeta, fechaExpiracion, nombreTitular, fechaRegistro, activa) VALUES 
(1, 3001, 'credito', '4892', '08/2027', 'Rosa María Chávez Montenegro', '2024-03-15 10:30:00', 1),
(2, 3003, 'debito', '7351', '12/2026', 'Carmen Dolores Andrade Paredes', '2024-01-10 14:20:00', 1),
(3, 3004, 'credito', '2648', '05/2028', 'Roberto Carlos Maldonado Enríquez', '2024-07-05 09:15:00', 1),
(4, 3006, 'debito', '9517', '03/2027', 'Hernán Patricio Vásquez Calderón', '2024-11-08 16:45:00', 1),
(5, 3007, 'credito', '8263', '10/2026', 'Gloria Esperanza Cuasapaz Malte', '2024-02-18 11:30:00', 1);

-- ================================================================
-- EQUIPOS ASIGNADOS
-- ================================================================
INSERT INTO equipos (idEquipos, Servicios_idServicios, nombre, tipo, estado) VALUES 
(1, 10001, 'Router WiFi TP-Link AX3000', 'Router', 'en uso'),
(2, 10002, 'Router Fibra Óptica Huawei AX6600', 'Router', 'en uso'),
(3, 10003, 'Router Básico D-Link AC1200', 'Router', 'en uso'),
(4, 10004, 'Medidor de Fibra OTDR', 'Medicion', 'disponible'),
(5, 10005, 'Switch Gigabit 8 puertos', 'Switch', 'disponible'),
(6, 10006, 'Antena WiFi direccional', 'Antena', 'en uso'),
(7, 10007, 'Analizador de espectro RF', 'Medicion', 'disponible');

-- ================================================================
-- TICKETS DE SOPORTE
-- ================================================================
INSERT INTO ticket (idTicket, Servicios_idServicios, fecha, descripcion, prioridad, estado) VALUES 
(30001, 10001, '2024-12-01', 'Instalación completada - Sector La Victoria, Ibarra\nCliente: Rosa Chávez\nPlan Estándar 500 Mbps\nDirección: Av. Mariano Acosta 8-45', 'media', 'cerrado'),
(30002, 10002, '2024-12-02', 'Instalación Plan Ultra - Sector El Ejido\nCliente: Fabián Imbaquingo\nFibra óptica 1 Gbps\nDirección: Calle Sucre 7-89', 'alta', 'cerrado'),
(30003, 10003, '2024-12-03', 'Soporte técnico - Problema de conectividad\nCliente: Carmen Andrade\nSector Azaya - Plan Básico', 'alta', 'en progreso'),
(30004, 10004, '2024-12-04', 'Instalación Plan Estándar - Sector Alpachaca\nCliente: Roberto Maldonado\nDirección: Calle Bolívar 5-67', 'media', 'abierto'),
(30005, 10005, '2024-12-05', 'Configuración avanzada Plan Ultra\nCliente: Luz Bolaños\nSector Caranqui - Ibarra', 'baja', 'cerrado');

-- ================================================================
-- CALIFICACIONES DE SERVICIOS
-- ================================================================
INSERT INTO calificaciones_servicios (idCalificacion, Cliente_idCliente, Contrato_idContrato, Servicios_idServicios, 
    calificacion_velocidad, calificacion_atencion, calificacion_precio, calificacion_soporte, calificacion_general, 
    comentarios, recomendaria, fecha_calificacion, reportado) VALUES 
(1, 3001, 10001, 10001, 5, 5, 4, 5, 4.75, 'Excelente servicio en La Victoria, muy buena velocidad y atención técnica profesional.', 1, '2024-12-16 10:30:00', 0),
(2, 3002, 10002, 10002, 5, 4, 4, 5, 4.50, 'Plan Ultra funciona perfecto en El Ejido, muy contento con la velocidad de 1 Gbps.', 1, '2024-12-21 15:45:00', 0),
(3, 3003, 10003, 10003, 4, 5, 5, 4, 4.50, 'Plan Básico ideal para mis necesidades en Azaya, buen precio y servicio estable.', 1, '2024-12-11 09:20:00', 0);

-- ================================================================
-- ACTUALIZAR CONTADORES AUTO_INCREMENT
-- ================================================================
ALTER TABLE cliente AUTO_INCREMENT = 4000;
ALTER TABLE contrato AUTO_INCREMENT = 20000;
ALTER TABLE servicios AUTO_INCREMENT = 20000;
ALTER TABLE ubicacion AUTO_INCREMENT = 20000;
ALTER TABLE factura AUTO_INCREMENT = 30000;
ALTER TABLE tarjetas_usuario AUTO_INCREMENT = 10;
ALTER TABLE equipos AUTO_INCREMENT = 10;
ALTER TABLE ticket AUTO_INCREMENT = 40000;
ALTER TABLE calificaciones_servicios AUTO_INCREMENT = 10;

-- ================================================================
-- VERIFICACIÓN DE DATOS
-- ================================================================

-- Verificar clientes por tipo
SELECT 
    tipo, 
    COUNT(*) as cantidad,
    GROUP_CONCAT(CONCAT(nombre, ' ', apellido) SEPARATOR ', ') as nombres
FROM cliente 
GROUP BY tipo;

-- Verificar servicios activos por sector
SELECT 
    u.direccion,
    c.nombre,
    c.apellido,
    s.descripcion,
    s.estado
FROM ubicacion u
JOIN servicios s ON u.Servicios_idServicios = s.idServicios
JOIN contrato co ON s.Contrato_idContrato = co.idContrato
JOIN cliente c ON co.Cliente_idCliente = c.idCliente
ORDER BY u.direccion;

COMMIT;

-- ================================================================
-- RESUMEN DE MIGRACIÓN
-- ================================================================
/*
DATOS MIGRADOS EXITOSAMENTE:
✓ 3 Administradores (Ibarra, Imbabura)
✓ 5 Técnicos especializados (Ibarra, Imbabura)  
✓ 7 Usuarios regulares (Ibarra, Imbabura)
✓ 7 Contratos de servicios activos
✓ 7 Servicios instalados en sectores de Ibarra
✓ 7 Ubicaciones en sectores reales de Ibarra
✓ 7 Facturas con diferentes métodos de pago
✓ 5 Tarjetas de usuario registradas
✓ 7 Equipos asignados a servicios
✓ 5 Tickets de soporte técnico
✓ 3 Calificaciones de servicios

SECTORES DE IBARRA INCLUIDOS:
- La Victoria
- El Ejido  
- Azaya
- Alpachaca
- Caranqui
- La Esperanza
- Yahuarcocha

TOTAL: 15 personas con datos realistas de Ibarra, Imbabura, Ecuador
*/
