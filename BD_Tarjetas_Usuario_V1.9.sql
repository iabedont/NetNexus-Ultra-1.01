-- Actualización de Base de Datos V1.9 - Mejoras para Autocompletado de Tarjetas
-- Ejecutar después de V1.8

USE mydb;

-- Agregar campo para últimos 4 dígitos (no es información sensible)
ALTER TABLE `tarjetas_usuario` 
ADD COLUMN `ultimos_cuatro_digitos` varchar(4) NOT NULL DEFAULT '0000' COMMENT 'Últimos 4 dígitos para identificación visual',
ADD COLUMN `nombre_titular_visible` varchar(50) NOT NULL DEFAULT 'Tarjeta' COMMENT 'Nombre del titular para mostrar (no hash)';

-- Actualizar tarjetas existentes con datos de ejemplo
UPDATE `tarjetas_usuario` SET 
    `ultimos_cuatro_digitos` = '1234',
    `nombre_titular_visible` = 'JUAN PEREZ'
WHERE `idTarjeta` = 1;

UPDATE `tarjetas_usuario` SET 
    `ultimos_cuatro_digitos` = '5678',
    `nombre_titular_visible` = 'MARIA GARCIA'
WHERE `idTarjeta` = 2;

-- Agregar más tarjetas de ejemplo para diferentes tipos
INSERT INTO `tarjetas_usuario` 
(`Cliente_idCliente`, `numero_tarjeta_hash`, `nombre_tarjeta_hash`, `fecha_vencimiento`, `cvv_hash`, `tipo_tarjeta`, `ultimos_cuatro_digitos`, `nombre_titular_visible`) 
VALUES 
(1006936479, SHA2('4532123456789012', 256), SHA2('CARLOS RODRIGUEZ', 256), '03/27', SHA2('123', 256), 'Debito', '9012', 'CARLOS RODRIGUEZ'),
(1006936479, SHA2('5555444433332222', 256), SHA2('ANA MARTINEZ', 256), '08/26', SHA2('456', 256), 'Credito', '2222', 'ANA MARTINEZ');

-- Vista mejorada para consultas de tarjetas
CREATE OR REPLACE VIEW `vista_tarjetas_usuario` AS
SELECT 
    t.idTarjeta,
    t.Cliente_idCliente,
    t.tipo_tarjeta,
    t.fecha_vencimiento,
    t.ultimos_cuatro_digitos,
    t.nombre_titular_visible,
    t.fecha_registro,
    t.activa,
    c.nombre as cliente_nombre,
    c.apellido as cliente_apellido
FROM tarjetas_usuario t
JOIN cliente c ON t.Cliente_idCliente = c.idCliente
WHERE t.activa = true
ORDER BY t.fecha_registro DESC;

COMMIT;
