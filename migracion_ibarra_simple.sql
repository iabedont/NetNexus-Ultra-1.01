-- ================================================================
-- SCRIPT SIMPLE - MIGRACIÓN A DATOS REALES IBARRA
-- NetNexus Ultra - Compatible con todas las versiones de MySQL
-- ================================================================

-- Limpiar datos existentes
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE respuestas_empresa;
TRUNCATE TABLE utilidad_calificaciones;  
TRUNCATE TABLE calificaciones_servicios;
TRUNCATE TABLE vehicle_problems;
TRUNCATE TABLE vehicle_requests;
TRUNCATE TABLE vehiculos_flota;
TRUNCATE TABLE ticket;
TRUNCATE TABLE ubicacion;
TRUNCATE TABLE equipos;
TRUNCATE TABLE servicios;
TRUNCATE TABLE factura;
TRUNCATE TABLE contrato;
TRUNCATE TABLE tarjetas_usuario;
TRUNCATE TABLE tecnicos;
TRUNCATE TABLE administradores;
TRUNCATE TABLE usuarios;
TRUNCATE TABLE cliente;

SET FOREIGN_KEY_CHECKS = 1;

-- ================================================================
-- DATOS REALES DE IBARRA, IMBABURA, ECUADOR
-- ================================================================

-- CLIENTES (15 personas)
INSERT INTO cliente VALUES 
(1001, 'María Elena', 'Yépez Rueda', '0991847362', 'maria.yepez@netnexus.ec', 'Marielena2024!', 'Administrador'),
(1002, 'Carlos Alberto', 'Morales Intriago', '0992536174', 'carlos.morales@netnexus.ec', 'Carlos#Admin99', 'Administrador'),
(1003, 'Ana Lucía', 'Santacruz Flores', '0993428517', 'ana.santacruz@netnexus.ec', 'AnaLucia$2025', 'Administrador'),
(2001, 'Jorge Luis', 'Valenzuela Pozo', '0994827361', 'jorge.valenzuela@netnexus.ec', 'JorgeTech@47', 'Técnico'),
(2002, 'Patricia Elizabeth', 'Cadena Guerrero', '0995739482', 'patricia.cadena@netnexus.ec', 'PatriWifi123*', 'Técnico'),
(2003, 'Miguel Andrés', 'Rosero Benítez', '0996851473', 'miguel.rosero@netnexus.ec', 'MiguelRed&85', 'Técnico'),
(2004, 'Silvia Maribel', 'Ruiz Moncayo', '0997264851', 'silvia.ruiz@netnexus.ec', 'SilviaSupport7!', 'Técnico'),
(2005, 'Edison Ramiro', 'Terán Villarreal', '0998573649', 'edison.teran@netnexus.ec', 'EdisonFibra92$', 'Técnico'),
(3001, 'Rosa María', 'Chávez Montenegro', '0991364827', 'rosa.chavez@gmail.com', 'Rosa_Victoria2024', 'Usuario'),
(3002, 'Fabián Oswaldo', 'Imbaquingo Yar', '0992475816', 'fabian.imbaquingo@hotmail.com', 'FabianEjido89*', 'Usuario'),
(3003, 'Carmen Dolores', 'Andrade Paredes', '0993586294', 'carmen.andrade@yahoo.com', 'Carmen#Azaya56', 'Usuario'),
(3004, 'Roberto Carlos', 'Maldonado Enríquez', '0994697385', 'roberto.maldonado@outlook.com', 'Roberto!Alpachaca', 'Usuario'),
(3005, 'Luz Marina', 'Bolaños Cevallos', '0995748172', 'luz.bolanos@gmail.com', 'LuzCaranqui2025&', 'Usuario'),
(3006, 'Hernán Patricio', 'Vásquez Calderón', '0996859263', 'hernan.vasquez@hotmail.com', 'HernanEsperanza74', 'Usuario'),
(3007, 'Gloria Esperanza', 'Cuasapaz Malte', '0997961384', 'gloria.cuasapaz@gmail.com', 'Gloria@Yahuarcocha', 'Usuario');

-- ADMINISTRADORES
INSERT INTO administradores VALUES 
(1001, 'María Elena', 'Yépez Rueda', '0991847362', 'maria.yepez@netnexus.ec', 'Marielena2024!'),
(1002, 'Carlos Alberto', 'Morales Intriago', '0992536174', 'carlos.morales@netnexus.ec', 'Carlos#Admin99'),
(1003, 'Ana Lucía', 'Santacruz Flores', '0993428517', 'ana.santacruz@netnexus.ec', 'AnaLucia$2025');

-- TÉCNICOS  
INSERT INTO tecnicos VALUES 
(2001, 1, 'Jorge Luis', 'Valenzuela Pozo', '0994827361', 'jorge.valenzuela@netnexus.ec', 'Redes y Fibra Óptica'),
(2002, 2, 'Patricia Elizabeth', 'Cadena Guerrero', '0995739482', 'patricia.cadena@netnexus.ec', 'Instalaciones WiFi'),
(2003, 3, 'Miguel Andrés', 'Rosero Benítez', '0996851473', 'miguel.rosero@netnexus.ec', 'Telecomunicaciones'),
(2004, 4, 'Silvia Maribel', 'Ruiz Moncayo', '0997264851', 'silvia.ruiz@netnexus.ec', 'Soporte Técnico'),
(2005, 5, 'Edison Ramiro', 'Terán Villarreal', '0998573649', 'edison.teran@netnexus.ec', 'Mantenimiento de Equipos');

-- CONTRATOS (Múltiples servicios por usuario)
INSERT INTO contrato VALUES 
-- Rosa María Chávez - 2 servicios
(10001, 3001, '2024-03-15', '2025-03-15', 35.00, 'Plan Estándar'),
(10011, 3001, '2024-06-20', '2025-06-20', 15.00, 'Plan TV Digital'),
-- Fabián Imbaquingo - 3 servicios  
(10002, 3002, '2024-05-20', '2025-05-20', 60.00, 'Plan Ultra'),
(10012, 3002, '2024-07-15', '2025-07-15', 25.00, 'Plan Telefonía'),
(10022, 3002, '2024-09-10', '2025-09-10', 10.00, 'Servicio Adicional WiFi'),
-- Carmen Andrade - 1 servicio
(10003, 3003, '2024-01-10', '2025-01-10', 20.00, 'Plan Básico'),
-- Roberto Maldonado - 2 servicios
(10004, 3004, '2024-07-05', '2025-07-05', 35.00, 'Plan Estándar'),
(10014, 3004, '2024-11-12', '2025-11-12', 20.00, 'Plan Streaming'),
-- Luz Bolaños - 3 servicios
(10005, 3005, '2024-09-12', '2025-09-12', 60.00, 'Plan Ultra'),
(10015, 3005, '2024-10-08', '2025-10-08', 15.00, 'Plan TV Digital'),
(10025, 3005, '2024-12-01', '2025-12-01', 8.00, 'Antivirus Premium'),
-- Hernán Vásquez - 2 servicios
(10006, 3006, '2024-11-08', '2025-11-08', 20.00, 'Plan Básico'),
(10016, 3006, '2024-12-15', '2025-12-15', 12.00, 'Backup en la Nube'),
-- Gloria Cuasapaz - 1 servicio
(10007, 3007, '2024-02-18', '2025-02-18', 35.00, 'Plan Estándar');

-- SERVICIOS (Múltiples servicios por usuario)
INSERT INTO servicios VALUES 
-- Servicios de Rosa María Chávez
(10001, 10001, 2, 2001, '2024-03-15', 'Instalación Plan Estándar - Rosa Chávez - Sector La Victoria', 'completado'),
(10011, 10011, 1, 2003, '2024-06-20', 'Instalación TV Digital - Rosa Chávez - Sector La Victoria', 'completado'),
-- Servicios de Fabián Imbaquingo
(10002, 10002, 3, 2002, '2024-05-20', 'Instalación Plan Ultra - Fabián Imbaquingo - Sector El Ejido', 'completado'),
(10012, 10012, 1, 2004, '2024-07-15', 'Instalación Telefonía - Fabián Imbaquingo - Sector El Ejido', 'completado'),
(10022, 10022, 1, 2005, '2024-09-10', 'Servicio WiFi Adicional - Fabián Imbaquingo - Sector El Ejido', 'completado'),
-- Servicios de Carmen Andrade
(10003, 10003, 1, 2003, '2024-01-10', 'Instalación Plan Básico - Carmen Andrade - Sector Azaya', 'completado'),
-- Servicios de Roberto Maldonado
(10004, 10004, 2, 2004, '2024-07-05', 'Instalación Plan Estándar - Roberto Maldonado - Sector Alpachaca', 'completado'),
(10014, 10014, 1, 2001, '2024-11-12', 'Instalación Plan Streaming - Roberto Maldonado - Sector Alpachaca', 'completado'),
-- Servicios de Luz Bolaños
(10005, 10005, 3, 2005, '2024-09-12', 'Instalación Plan Ultra - Luz Bolaños - Sector Caranqui', 'completado'),
(10015, 10015, 1, 2002, '2024-10-08', 'Instalación TV Digital - Luz Bolaños - Sector Caranqui', 'completado'),
(10025, 10025, 1, 2003, '2024-12-01', 'Configuración Antivirus Premium - Luz Bolaños - Sector Caranqui', 'completado'),
-- Servicios de Hernán Vásquez
(10006, 10006, 1, 2001, '2024-11-08', 'Instalación Plan Básico - Hernán Vásquez - Sector La Esperanza', 'completado'),
(10016, 10016, 1, 2004, '2024-12-15', 'Configuración Backup en Nube - Hernán Vásquez - Sector La Esperanza', 'completado'),
-- Servicios de Gloria Cuasapaz
(10007, 10007, 2, 2002, '2024-02-18', 'Instalación Plan Estándar - Gloria Cuasapaz - Sector Yahuarcocha', 'completado');

-- UBICACIONES (Sectores reales de Ibarra) - Servicios múltiples
INSERT INTO ubicacion VALUES 
-- Ubicaciones principales
(10001, 10001, 'Av. Mariano Acosta 8-45 y Cristóbal de Troya', 'Ibarra', 'Imbabura', '100150'),
(10002, 10002, 'Calle Sucre 7-89 y García Moreno', 'Ibarra', 'Imbabura', '100150'),
(10003, 10003, 'Av. Jaime Rivadeneira 12-34 y Olmedo', 'Ibarra', 'Imbabura', '100150'),
(10004, 10004, 'Calle Bolívar 5-67 y Flores', 'Ibarra', 'Imbabura', '100150'),
(10005, 10005, 'Av. El Retorno 15-23 y Panamericana Norte', 'Ibarra', 'Imbabura', '100150'),
(10006, 10006, 'Calle José Miguel Leoro 9-45 y Maldonado', 'Ibarra', 'Imbabura', '100150'),
(10007, 10007, 'Av. Atahualpa 18-67 y Teodoro Gómez', 'Ibarra', 'Imbabura', '100150'),
-- Ubicaciones para servicios adicionales (mismas direcciones, servicios diferentes)
(10011, 10011, 'Av. Mariano Acosta 8-45 y Cristóbal de Troya', 'Ibarra', 'Imbabura', '100150'),
(10012, 10012, 'Calle Sucre 7-89 y García Moreno', 'Ibarra', 'Imbabura', '100150'),
(10014, 10014, 'Calle Bolívar 5-67 y Flores', 'Ibarra', 'Imbabura', '100150'),
(10015, 10015, 'Av. El Retorno 15-23 y Panamericana Norte', 'Ibarra', 'Imbabura', '100150'),
(10016, 10016, 'Calle José Miguel Leoro 9-45 y Maldonado', 'Ibarra', 'Imbabura', '100150'),
(10022, 10022, 'Calle Sucre 7-89 y García Moreno', 'Ibarra', 'Imbabura', '100150'),
(10025, 10025, 'Av. El Retorno 15-23 y Panamericana Norte', 'Ibarra', 'Imbabura', '100150');

-- FACTURAS (Múltiples servicios por usuario)
INSERT INTO factura VALUES 
-- Facturas de Rosa María Chávez (2 servicios)
(20001, 3001, '2024-12-15', 35.00, 'pagado', 'Tarjeta de Credito', '**** **** **** 4892'),
(20011, 3001, '2024-12-15', 15.00, 'pagado', 'Tarjeta de Credito', '**** **** **** 4892'),
-- Facturas de Fabián Imbaquingo (3 servicios)
(20002, 3002, '2024-12-20', 60.00, 'pagado', 'Agencia', NULL),
(20012, 3002, '2024-12-20', 25.00, 'pagado', 'Agencia', NULL),
(20022, 3002, '2024-12-20', 10.00, 'pagado', 'Agencia', NULL),
-- Facturas de Carmen Andrade (1 servicio)
(20003, 3003, '2024-12-10', 20.00, 'pagado', 'Tarjeta de Debito', '**** **** **** 7351'),
-- Facturas de Roberto Maldonado (2 servicios)
(20004, 3004, '2024-12-05', 35.00, 'pendiente', 'Tarjeta de Credito', '**** **** **** 2648'),
(20014, 3004, '2024-12-05', 20.00, 'pendiente', 'Tarjeta de Credito', '**** **** **** 2648'),
-- Facturas de Luz Bolaños (3 servicios)
(20005, 3005, '2024-12-12', 60.00, 'pagado', 'Agencia', NULL),
(20015, 3005, '2024-12-12', 15.00, 'pagado', 'Agencia', NULL),
(20025, 3005, '2024-12-12', 8.00, 'pagado', 'Agencia', NULL),
-- Facturas de Hernán Vásquez (2 servicios)
(20006, 3006, '2024-12-08', 20.00, 'pagado', 'Tarjeta de Debito', '**** **** **** 9517'),
(20016, 3006, '2024-12-08', 12.00, 'pagado', 'Tarjeta de Debito', '**** **** **** 9517'),
-- Facturas de Gloria Cuasapaz (1 servicio)
(20007, 3007, '2024-12-18', 35.00, 'pagado', 'Tarjeta de Credito', '**** **** **** 8263');

-- TARJETAS DE USUARIO
INSERT INTO tarjetas_usuario VALUES 
(1, 3001, 'credito', '4892', '08/2027', 'Rosa María Chávez Montenegro', '2024-03-15 10:30:00', 1),
(2, 3003, 'debito', '7351', '12/2026', 'Carmen Dolores Andrade Paredes', '2024-01-10 14:20:00', 1),
(3, 3004, 'credito', '2648', '05/2028', 'Roberto Carlos Maldonado Enríquez', '2024-07-05 09:15:00', 1),
(4, 3006, 'debito', '9517', '03/2027', 'Hernán Patricio Vásquez Calderón', '2024-11-08 16:45:00', 1),
(5, 3007, 'credito', '8263', '10/2026', 'Gloria Esperanza Cuasapaz Malte', '2024-02-18 11:30:00', 1);

-- EQUIPOS ASIGNADOS (Múltiples servicios)
INSERT INTO equipos VALUES 
-- Equipos principales
(1, 10001, 'Router WiFi TP-Link AX3000', 'Router', 'en uso'),
(2, 10002, 'Router Fibra Óptica Huawei AX6600', 'Router', 'en uso'),
(3, 10003, 'Router Básico D-Link AC1200', 'Router', 'en uso'),
(4, 10004, 'Router WiFi TP-Link AX1800', 'Router', 'en uso'),
(5, 10005, 'Router Fibra Óptica Huawei AX9000', 'Router', 'en uso'),
(6, 10006, 'Router Básico D-Link AC750', 'Router', 'en uso'),
(7, 10007, 'Router WiFi TP-Link AX3000', 'Router', 'en uso'),
-- Equipos adicionales para servicios múltiples
(8, 10011, 'Decodificador TV Digital HD', 'Decodificador', 'en uso'),
(9, 10012, 'Teléfono IP Cisco', 'Telefono', 'en uso'),
(10, 10014, 'Streaming Box 4K', 'Streaming', 'en uso'),
(11, 10015, 'Decodificador TV Digital 4K', 'Decodificador', 'en uso'),
(12, 10016, 'Backup Storage 1TB', 'Almacenamiento', 'en uso'),
(13, 10022, 'Amplificador WiFi Mesh', 'Amplificador', 'en uso'),
(14, 10025, 'Dispositivo Antivirus Hardware', 'Seguridad', 'en uso');

-- TICKETS DE SOPORTE (Servicios múltiples)
INSERT INTO ticket VALUES 
(30001, 10001, '2024-12-01', 'Instalación completada - Plan Estándar\nCliente: Rosa Chávez\nDirección: Av. Mariano Acosta 8-45\nVelocidad: 500 Mbps', 'media', 'cerrado'),
(30002, 10002, '2024-12-02', 'Instalación Plan Ultra - Fibra Óptica\nCliente: Fabián Imbaquingo\nDirección: Calle Sucre 7-89\nVelocidad: 1 Gbps', 'alta', 'cerrado'),
(30003, 10003, '2024-12-03', 'Soporte técnico - Conectividad\nCliente: Carmen Andrade\nSector Azaya - Plan Básico\nProblema resuelto', 'alta', 'cerrado'),
(30004, 10004, '2024-12-04', 'Instalación Plan Estándar\nCliente: Roberto Maldonado\nDirección: Calle Bolívar 5-67\nEquipo: Router TP-Link AX1800', 'media', 'cerrado'),
(30005, 10005, '2024-12-05', 'Configuración Plan Ultra\nCliente: Luz Bolaños\nSector Caranqui - Fibra Óptica\nVelocidad: 1 Gbps', 'baja', 'cerrado'),
(30006, 10011, '2024-12-06', 'Instalación TV Digital\nCliente: Rosa Chávez\nServicio adicional configurado\nCanales HD activos', 'baja', 'cerrado'),
(30007, 10012, '2024-12-07', 'Configuración Telefonía IP\nCliente: Fabián Imbaquingo\nTeléfono Cisco configurado\nLlamadas funcionando', 'media', 'cerrado'),
(30008, 10022, '2024-12-08', 'Instalación WiFi Mesh\nCliente: Fabián Imbaquingo\nAmplificador instalado\nCobertura mejorada', 'baja', 'cerrado');

-- CALIFICACIONES DE SERVICIOS
INSERT INTO calificaciones_servicios VALUES 
(1, 3001, 10001, 10001, 5, 5, 4, 5, 4.75, 'Excelente servicio en La Victoria, muy buena velocidad y atención técnica profesional.', 1, '2024-12-16 10:30:00', 0),
(2, 3002, 10002, 10002, 5, 4, 4, 5, 4.50, 'Plan Ultra funciona perfecto en El Ejido, muy contento con la velocidad de 1 Gbps.', 1, '2024-12-21 15:45:00', 0),
(3, 3003, 10003, 10003, 4, 5, 5, 4, 4.50, 'Plan Básico ideal para mis necesidades en Azaya, buen precio y servicio estable.', 1, '2024-12-11 09:20:00', 0);

-- VERIFICACIÓN Y RESUMEN
SELECT 'MIGRACIÓN COMPLETADA CON ÉXITO' as Resultado, COUNT(*) as Total_Clientes FROM cliente;
SELECT tipo, COUNT(*) as Cantidad FROM cliente GROUP BY tipo;
SELECT 'CONTRATOS MÚLTIPLES' as Info, COUNT(*) as Total_Contratos FROM contrato;
SELECT 'SERVICIOS ACTIVOS' as Info, COUNT(*) as Total_Servicios FROM servicios;
SELECT 'FACTURAS GENERADAS' as Info, COUNT(*) as Total_Facturas FROM factura;
SELECT 'EQUIPOS ASIGNADOS' as Info, COUNT(*) as Total_Equipos FROM equipos;
