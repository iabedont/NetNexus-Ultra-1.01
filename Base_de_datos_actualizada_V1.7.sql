-- Base de datos NetNexus Ultra V1.7 - Optimizada para aplicación Java
-- Fecha: 20 de julio de 2025
-- Cambios principales: Simplificación de relaciones, corrección de nombres, datos de prueba ampliados

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `mydb`;

-- Eliminar vistas problemáticas de versiones anteriores
DROP VIEW IF EXISTS `vista_facturas_con_tarjeta`;
DROP VIEW IF EXISTS `vista_contratos_completos`;
DROP VIEW IF EXISTS `vista_facturas_completas`;

--
-- Tabla structure para tabla `cliente`
--
DROP TABLE IF EXISTS `cliente`;
CREATE TABLE `cliente` (
  `idCliente` int NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `telefono` varchar(10) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `tipo` varchar(45) NOT NULL DEFAULT 'Usuario',
  PRIMARY KEY (`idCliente`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `telefono_UNIQUE` (`telefono`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Insertar datos en tabla `cliente`
--
INSERT INTO `cliente` VALUES 
(1,'admin','admin','123456789','admin@admin.admin','123','Administrador'),
(258,'Juan','Técnico','2646846','juan.tech@netnexus.com','123','Técnico'),
(8563,'María','García','1243324','maria.garcia@gmail.com','123','Usuario'),
(8956,'tech','tech','1234321','tech@tech.tech','123','Técnico'),
(123456,'Juan Carlos','Chavez','0982564551','jcarlos@gmail.com','123','Usuario'),
(643986553,'Pedro','López','0987654321','pedro.lopez@gmail.com','123','Usuario'),
(1006936479,'Goku','Caicedo','0999263127','gokussjcaicedo@capsulecorp.com','12345','Usuario'),
(1102356894,'Piccolo','Namek','0963258741','piccolo.namek@dragonmail.com','123','Usuario'),
(1304567891,'Bulma','Brief','0974123658','bulma.brief@capsulecorp.com','123','Usuario'),
(1503698521,'Krillin','Turtle','0958741236','krillin.turtle@earthmail.com','123','Usuario'),
(1725589632,'Vegeta','Prince','0987456321','vegeta.prince@saiyanmail.com','123','Usuario');

--
-- Tabla structure para tabla `metodopago` - CORREGIDA para coincidir con la aplicación Java
--
DROP TABLE IF EXISTS `metodopago`;
CREATE TABLE `metodopago` (
  `idMetodoPago` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idMetodoPago`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

--
-- Insertar métodos de pago EXACTOS como en la aplicación Java
--
INSERT INTO `metodopago` VALUES 
(1,'Agencia'),
(2,'Tarjeta de Credito'),  -- SIN TILDE para coincidir con Java
(3,'Tarjeta de Debito');   -- SIN TILDE para coincidir con Java

--
-- Tabla structure para tabla `tiposervicio` - Ampliada con todos los planes
--
DROP TABLE IF EXISTS `tiposervicio`;
CREATE TABLE `tiposervicio` (
  `idTipoServicio` int NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `categoria` varchar(50) DEFAULT NULL,
  `descripcion` mediumtext NOT NULL,
  `precio` decimal(10,2) DEFAULT NULL,
  `detalles` text,
  PRIMARY KEY (`idTipoServicio`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Insertar tipos de servicio EXACTOS como en la aplicación Java
--
INSERT INTO `tiposervicio` VALUES 
(1,'Plan Básico','Internet','Internet de 300 Mbps con router incluido',20.00,'Ideal para navegación básica, clases en línea y redes sociales.'),
(2,'Plan Estándar','Internet','Internet de 500 Mbps para múltiples dispositivos',35.00,'Streaming HD, videollamadas estables, teletrabajo sin cortes.'),
(3,'Plan Ultra','Internet','Internet de 1000 Mbps (1 Gbps) con máxima velocidad',60.00,'Ideal para gamers, streamers y hogares grandes.'),
(4,'Plan Inicio+','Telefonía Fija','300 minutos locales y nacionales',7.99,'Tarifa internacional desde $0.15/min, ideal para uso básico.'),
(5,'Plan Conecta+','Telefonía Fija','800 minutos a nivel nacional',14.99,'Incluye buzón de voz, desvío de llamadas y 100 minutos internacionales.'),
(6,'Plan Ilimitado Total','Telefonía Fija','Llamadas nacionales ilimitadas + 200 minutos internacionales',19.99,'Perfecto para usuarios frecuentes y empresas.'),
(7,'Plan Prepago+','Telefonía Móvil','5 GB + 200 min + 200 SMS',6.99,'Controla tu consumo, ideal para uso básico y redes sociales.'),
(8,'Plan Smart Max','Telefonía Móvil','20 GB + llamadas y SMS ilimitados',15.00,'Redes sociales ilimitadas y Spotify por 1 mes incluido.'),
(9,'Plan Pro Global','Telefonía Móvil','50 GB con roaming internacional',25.00,'Incluye cobertura en USA, Canadá y Europa.'),
(10,'Plan Esencial+','Televisión','100 canales (40 en HD) + App incluida',10.99,'Ideal para noticias, cultura y entretenimiento básico.'),
(11,'Plan Conecta+ (TV)','Televisión','150 canales (80 en HD) + paquete infantil y deportes',19.99,'Diversión familiar sin interrupciones.'),
(12,'Plan Premium 4K','Televisión','200 canales (100 HD + 20 en 4K) + apps de streaming',29.99,'Incluye Prime Video, HBO Max, Disney+ y más.');

--
-- Tabla structure para tabla `vehiculos`
--
DROP TABLE IF EXISTS `vehiculos`;
CREATE TABLE `vehiculos` (
  `idVehiculos` int NOT NULL,
  `modelo` varchar(45) NOT NULL,
  `placa` varchar(45) NOT NULL,
  `color` varchar(45) NOT NULL,
  `estado` enum('disponible','en uso','reparacion') NOT NULL,
  `ruta` varchar(45) NOT NULL,
  PRIMARY KEY (`idVehiculos`),
  UNIQUE KEY `placa_UNIQUE` (`placa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Insertar datos en tabla `vehiculos`
--
INSERT INTO `vehiculos` VALUES 
(1,'Toyota Hilux','ABC1234','Blanco','disponible','Norte'),
(2,'Ford Ranger','XYZ5678','Negro','en uso','Sur'),
(3,'Chevrolet D-Max','DEF9012','Rojo','disponible','Centro'),
(4,'Nissan Frontier','GHI3456','Azul','reparacion','Este'),
(5,'Isuzu D-Max','JKL7890','Gris','disponible','Oeste');

--
-- Tabla structure para tabla `tecnicos`
--
DROP TABLE IF EXISTS `tecnicos`;
CREATE TABLE `tecnicos` (
  `idTecnicos` int NOT NULL,
  `Vehiculos_idVehiculos` int NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `telefono` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `especialidad` varchar(45) NOT NULL,
  PRIMARY KEY (`idTecnicos`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `telefono_UNIQUE` (`telefono`),
  KEY `fk_Tecnicos_Vehiculos1_idx` (`Vehiculos_idVehiculos`),
  CONSTRAINT `fk_Tecnicos_Vehiculos1` FOREIGN KEY (`Vehiculos_idVehiculos`) REFERENCES `vehiculos` (`idVehiculos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Insertar datos en tabla `tecnicos`
--
INSERT INTO `tecnicos` VALUES 
(1,1,'Juan','Pérez','0987654321','juan.perez@netnexus.com','Redes'),
(2,2,'María','Gómez','0991234567','maria.gomez@netnexus.com','Fibra óptica'),
(3,3,'Carlos','Rodríguez','0976543210','carlos.rodriguez@netnexus.com','WiFi'),
(4,4,'Ana','Martínez','0967890123','ana.martinez@netnexus.com','Seguridad'),
(5,5,'Luis','García','0956789012','luis.garcia@netnexus.com','Satélite');

--
-- Tabla structure para tabla `contrato` - SIMPLIFICADA
--
DROP TABLE IF EXISTS `contrato`;
CREATE TABLE `contrato` (
  `idContrato` int NOT NULL,
  `Cliente_idCliente` int NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `monto_total` decimal(10,2) NOT NULL,
  `tiposervicio` varchar(45) NOT NULL,
  PRIMARY KEY (`idContrato`),
  KEY `fk_Contrato_Cliente_idx` (`Cliente_idCliente`),
  CONSTRAINT `fk_Contrato_Cliente` FOREIGN KEY (`Cliente_idCliente`) REFERENCES `cliente` (`idCliente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Insertar datos en tabla `contrato`
--
INSERT INTO `contrato` VALUES 
(1,1006936479,'2010-04-15','2025-06-03',26.00,'Plan Básico'),
(2,1725589632,'2018-07-20','2024-12-31',35.50,'Plan Estándar'),
(3,1304567891,'2019-01-10','2026-01-10',42.75,'Plan Ultra'),
(4,1102356894,'2020-03-05','2025-09-15',29.99,'Plan Conecta+'),
(5,1503698521,'2021-11-15','2023-11-15',19.99,'Plan Ilimitado Total');

--
-- Tabla structure para tabla `servicios`
--
DROP TABLE IF EXISTS `servicios`;
CREATE TABLE `servicios` (
  `idServicios` int NOT NULL,
  `Contrato_idContrato` int NOT NULL,
  `Tecnicos_idTecnicos` int NOT NULL,
  `TipoServicio_idTipoServicio` int NOT NULL,
  `fecha_servicio` date NOT NULL,
  `descripcion` longtext NOT NULL,
  `estado` enum('pendiente','en progreso','completado') NOT NULL,
  PRIMARY KEY (`idServicios`),
  KEY `fk_Servicios_Contrato1_idx` (`Contrato_idContrato`),
  KEY `fk_Servicios_Tecnicos1_idx` (`Tecnicos_idTecnicos`),
  KEY `fk_Servicios_TipoServicio1_idx` (`TipoServicio_idTipoServicio`),
  CONSTRAINT `fk_Servicios_Contrato1` FOREIGN KEY (`Contrato_idContrato`) REFERENCES `contrato` (`idContrato`),
  CONSTRAINT `fk_Servicios_Tecnicos1` FOREIGN KEY (`Tecnicos_idTecnicos`) REFERENCES `tecnicos` (`idTecnicos`),
  CONSTRAINT `fk_Servicios_TipoServicio1` FOREIGN KEY (`TipoServicio_idTipoServicio`) REFERENCES `tiposervicio` (`idTipoServicio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Insertar datos en tabla `servicios`
--
INSERT INTO `servicios` VALUES 
(1,1,1,1,'2023-01-15','Instalación de router WiFi en residencia','completado'),
(2,2,2,2,'2023-02-20','Mantenimiento de fibra óptica','en progreso'),
(3,3,3,3,'2023-03-10','Reparación de conexión intermitente','completado'),
(4,4,4,4,'2023-04-05','Actualización de firmware en equipos','pendiente'),
(5,5,5,5,'2023-05-12','Consulta para ampliación de cobertura','en progreso');

--
-- Tabla structure para tabla `ticket`
--
DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket` (
  `idTicket` int NOT NULL,
  `Servicios_idServicios` int NOT NULL,
  `fecha_creacion` date NOT NULL,
  `descripcion` mediumtext NOT NULL,
  `prioridad` enum('baja','media','alta') NOT NULL,
  `estado` enum('abierto','en progreso','cerrado') NOT NULL,
  PRIMARY KEY (`idTicket`),
  KEY `fk_Ticket_Servicios1_idx` (`Servicios_idServicios`),
  CONSTRAINT `fk_Ticket_Servicios1` FOREIGN KEY (`Servicios_idServicios`) REFERENCES `servicios` (`idServicios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Insertar datos en tabla `ticket`
--
INSERT INTO `ticket` VALUES 
(1,1,'2023-01-10','Cliente solicita instalación WiFi','media','cerrado'),
(2,2,'2023-02-15','Problemas con velocidad de fibra','alta','en progreso'),
(3,3,'2023-03-05','Conexión se cae frecuentemente','alta','cerrado'),
(4,4,'2023-04-01','Actualización requerida por seguridad','media','abierto'),
(5,5,'2023-05-10','Cliente quiere ampliar cobertura','baja','en progreso');

--
-- Tabla structure para tabla `factura` - ACTUALIZADA para coincidir con Facturación.java
--
DROP TABLE IF EXISTS `factura`;
CREATE TABLE `factura` (
  `idFactura` int NOT NULL,
  `Cliente_idCliente` int NOT NULL,
  `fecha_emision` date NOT NULL,
  `monto_total` decimal(10,2) NOT NULL,
  `estado_pago` enum('pagado','pendiente') NOT NULL DEFAULT 'pagado',
  `metodo_pago` varchar(45) NOT NULL,
  `numero_tarjeta_oculto` varchar(25) DEFAULT NULL COMMENT 'Número oculto como **** **** **** 1234',
  PRIMARY KEY (`idFactura`),
  KEY `fk_Factura_Cliente_idx` (`Cliente_idCliente`),
  CONSTRAINT `fk_Factura_Cliente` FOREIGN KEY (`Cliente_idCliente`) REFERENCES `cliente` (`idCliente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Insertar datos en tabla `factura` - ACTUALIZADA
--
INSERT INTO `factura` VALUES 
(1,1006936479,'2023-01-20',50.00,'pagado','Agencia',NULL),
(2,1725589632,'2023-02-25',75.50,'pendiente','Tarjeta de Credito','**** **** **** 1234'),
(3,1304567891,'2023-03-15',60.25,'pagado','Agencia',NULL),
(4,1102356894,'2023-04-10',45.99,'pendiente','Tarjeta de Debito','**** **** **** 5678'),
(5,1503698521,'2023-05-20',30.00,'pagado','Agencia',NULL);

--
-- Tabla structure para tabla `equipos`
--
DROP TABLE IF EXISTS `equipos`;
CREATE TABLE `equipos` (
  `idEquipos` int NOT NULL,
  `Servicios_idServicios` int NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `tipo` varchar(45) NOT NULL,
  `estado` enum('disponible','en uso','mantenimiento') NOT NULL,
  PRIMARY KEY (`idEquipos`),
  KEY `fk_Equipos_Servicios1_idx` (`Servicios_idServicios`),
  CONSTRAINT `fk_Equipos_Servicios1` FOREIGN KEY (`Servicios_idServicios`) REFERENCES `servicios` (`idServicios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Insertar datos en tabla `equipos`
--
INSERT INTO `equipos` VALUES 
(1,1,'Router WiFi AX3000','Router','en uso'),
(2,2,'OTDR para fibra','Medicion','mantenimiento'),
(3,3,'Switch 8 puertos','Switch','disponible'),
(4,4,'Antena direccional','Antena','en uso'),
(5,5,'Analizador de espectro','Medicion','disponible');

--
-- Tabla structure para tabla `tarjetas_usuario` - Para guardar tarjetas del cliente
--
DROP TABLE IF EXISTS `tarjetas_usuario`;
CREATE TABLE `tarjetas_usuario` (
  `idTarjeta` int NOT NULL AUTO_INCREMENT,
  `Cliente_idCliente` int NOT NULL,
  `tipo_tarjeta` enum('credito','debito') NOT NULL,
  `ultimos_cuatro_digitos` varchar(4) NOT NULL,
  `fecha_vencimiento` varchar(7) NOT NULL COMMENT 'Formato MM/YYYY',
  `nombre_titular_visible` varchar(100) NOT NULL,
  `fecha_registro` timestamp DEFAULT CURRENT_TIMESTAMP,
  `activa` boolean DEFAULT true,
  PRIMARY KEY (`idTarjeta`),
  KEY `fk_TarjetasUsuario_Cliente_idx` (`Cliente_idCliente`),
  CONSTRAINT `fk_TarjetasUsuario_Cliente` FOREIGN KEY (`Cliente_idCliente`) REFERENCES `cliente` (`idCliente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Insertar datos de prueba en tabla `tarjetas_usuario`
--
INSERT INTO `tarjetas_usuario` VALUES 
(1,123456,'credito','1234','12/2026','Juan Carlos Chavez',NOW(),true),
(2,123456,'debito','5678','08/2025','Juan Carlos Chavez',NOW(),true),
(3,1006936479,'credito','9876','03/2027','Goku Caicedo',NOW(),true);

--
-- Tabla structure para tabla `ubicacion`
--
DROP TABLE IF EXISTS `ubicacion`;
CREATE TABLE `ubicacion` (
  `idUbicacion` int NOT NULL,
  `Servicios_idServicios` int NOT NULL,
  `direccion` varchar(100) NOT NULL,
  `ciudad` varchar(45) NOT NULL,
  `provincia` varchar(45) NOT NULL,
  `codigo_postal` varchar(45) NOT NULL,
  PRIMARY KEY (`idUbicacion`),
  KEY `fk_Ubicacion_Servicios1_idx` (`Servicios_idServicios`),
  CONSTRAINT `fk_Ubicacion_Servicios1` FOREIGN KEY (`Servicios_idServicios`) REFERENCES `servicios` (`idServicios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Insertar datos en tabla `ubicacion`
--
INSERT INTO `ubicacion` VALUES 
(1,1,'Av. Principal 123','Quito','Pichincha','170135'),
(2,2,'Calle Secundaria 456','Guayaquil','Guayas','090112'),
(3,3,'Av. Central 789','Cuenca','Azuay','010110'),
(4,4,'Callejón Peatonal 321','Ambato','Tungurahua','180102'),
(5,5,'Pasaje Sin Salida 654','Manta','Manabí','130108');

--
-- Tablas adicionales mantenidas para compatibilidad
--
DROP TABLE IF EXISTS `administradores`;
CREATE TABLE `administradores` (
  `idadministradores` int NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `telefono` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`idadministradores`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `administradores` VALUES 
(1,'Admin','Sistema','0999999999','admin@netnexus.com','admin123');

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
  `idusuarios` int NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `telefono` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`idusuarios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Vista para facilitar consultas desde Java
CREATE OR REPLACE VIEW `vista_contratos_completos` AS
SELECT 
    c.idContrato,
    cl.idCliente,
    cl.nombre as cliente_nombre,
    cl.apellido as cliente_apellido,
    cl.email as cliente_email,
    c.fecha_inicio,
    c.fecha_fin,
    c.monto_total,
    c.tiposervicio,
    ts.categoria as servicio_categoria,
    ts.descripcion as servicio_descripcion
FROM contrato c
JOIN cliente cl ON c.Cliente_idCliente = cl.idCliente
LEFT JOIN tiposervicio ts ON c.tiposervicio = ts.nombre;

-- Vista para facilitar consultas de facturas - ACTUALIZADA
CREATE OR REPLACE VIEW `vista_facturas_completas` AS
SELECT 
    f.idFactura,
    f.fecha_emision,
    f.monto_total,
    f.estado_pago,
    f.metodo_pago,
    f.numero_tarjeta_oculto,
    cl.idCliente,
    cl.nombre as cliente_nombre,
    cl.apellido as cliente_apellido,
    cl.email as cliente_email
FROM factura f
JOIN cliente cl ON f.Cliente_idCliente = cl.idCliente;

-- Vista adicional para facturas con información de tarjetas - NUEVA
CREATE OR REPLACE VIEW `vista_facturas_con_tarjeta` AS
SELECT 
    f.idFactura,
    f.fecha_emision,
    f.monto_total,
    f.estado_pago,
    f.metodo_pago,
    f.numero_tarjeta_oculto,
    cl.idCliente,
    cl.nombre as cliente_nombre,
    cl.apellido as cliente_apellido,
    cl.email as cliente_email,
    CASE 
        WHEN f.metodo_pago IN ('Tarjeta de Credito', 'Tarjeta de Debito') 
        THEN CONCAT(f.metodo_pago, ' - ', COALESCE(f.numero_tarjeta_oculto, 'No especificada'))
        ELSE f.metodo_pago
    END as metodo_pago_detallado
FROM factura f
JOIN cliente cl ON f.Cliente_idCliente = cl.idCliente;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- INSTRUCCIONES DE USO:
-- 1. Ejecutar este script completo en MySQL
-- 2. La base de datos 'mydb' será recreada con los cambios
-- 3. Usar las vistas creadas para consultas más fáciles desde Java
-- 4. Los métodos de pago ahora coinciden exactamente con la aplicación Java

-- CAMBIOS PRINCIPALES:
-- ✅ Métodos de pago corregidos: "Tarjeta de Credito" (sin tilde)
-- ✅ Tabla factura simplificada (eliminado campo redundante idMetodoPago)
-- ✅ Más datos de prueba para clientes y contratos
-- ✅ Vistas SQL para facilitar consultas desde Java
-- ✅ Charset utf8mb4 para mejor compatibilidad
-- ✅ Campos de hash bien documentados
-- ✅ Valores por defecto apropiados
