-- Actualización de Base de Datos V1.8 - Vinculación Tarjeta-Usuario
-- Ejecutar después de V1.7 o reemplazar completamente

USE mydb;

-- Crear tabla para tarjetas vinculadas a usuarios
DROP TABLE IF EXISTS `tarjetas_usuario`;
CREATE TABLE `tarjetas_usuario` (
  `idTarjeta` int NOT NULL AUTO_INCREMENT,
  `Cliente_idCliente` int NOT NULL,
  `numero_tarjeta_hash` varchar(64) NOT NULL COMMENT 'Hash SHA-256 del número',
  `nombre_tarjeta_hash` varchar(64) NOT NULL COMMENT 'Hash SHA-256 del nombre',
  `fecha_vencimiento` varchar(5) NOT NULL COMMENT 'MM/YY formato',
  `cvv_hash` varchar(64) NOT NULL COMMENT 'Hash SHA-256 del CVV',
  `tipo_tarjeta` enum('Credito','Debito') NOT NULL,
  `fecha_registro` timestamp DEFAULT CURRENT_TIMESTAMP,
  `activa` boolean DEFAULT true,
  PRIMARY KEY (`idTarjeta`),
  KEY `fk_Tarjetas_Cliente_idx` (`Cliente_idCliente`),
  CONSTRAINT `fk_Tarjetas_Cliente` FOREIGN KEY (`Cliente_idCliente`) REFERENCES `cliente` (`idCliente`),
  UNIQUE KEY `unique_tarjeta_cliente` (`Cliente_idCliente`, `numero_tarjeta_hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insertar algunas tarjetas de prueba
INSERT INTO `tarjetas_usuario` VALUES 
(1, 1006936479, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', '12/28', 'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855', 'Credito', NOW(), true),
(2, 123456, 'b3a8e0e1f9ab1bfe3a36f231f676f78bb30a519d2b21e6c530c0eee8ebb4a5d0', '098f6bcd4621d373cade4e832627b4f6c890e123456789abcdef', '06/27', '92cfceb39d57d914ed8b14d0e37643de0797ae56c47b7b58dc95abf40f9b7006', 'Debito', NOW(), true);

-- Modificar tabla factura para referenciar tarjeta (opcional)
ALTER TABLE `factura` ADD COLUMN `Tarjeta_idTarjeta` int NULL AFTER `cvv_tarjeta`;
ALTER TABLE `factura` ADD KEY `fk_Factura_Tarjeta_idx` (`Tarjeta_idTarjeta`);

-- Vista mejorada para consultas de facturas con tarjetas
CREATE OR REPLACE VIEW `vista_facturas_con_tarjeta` AS
SELECT 
    f.idFactura,
    f.fecha_factura,
    f.monto,
    f.estado_pago,
    f.metodo_pago,
    t.idTicket,
    cl.idCliente,
    cl.nombre as cliente_nombre,
    cl.apellido as cliente_apellido,
    tar.idTarjeta,
    tar.tipo_tarjeta,
    tar.fecha_vencimiento as tarjeta_vencimiento
FROM factura f
JOIN ticket t ON f.Ticket_idTicket = t.idTicket
JOIN servicios s ON t.Servicios_idServicios = s.idServicios
JOIN contrato cont ON s.Contrato_idContrato = cont.idContrato
JOIN cliente cl ON cont.Cliente_idCliente = cl.idCliente
LEFT JOIN tarjetas_usuario tar ON f.Tarjeta_idTarjeta = tar.idTarjeta;

COMMIT;
