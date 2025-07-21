-- Script de Base de Datos NetNexus Ultra v1.5.1 - VERSIÓN COMPATIBLE
-- Sintaxis 100% compatible con todas las versiones de MySQL
-- Sin características avanzadas que puedan causar errores de compatibilidad

-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS netnexus_ultra;
USE netnexus_ultra;

-- =====================================================
-- ELIMINAR TABLAS SI EXISTEN (ORDEN CORRECTO POR DEPENDENCIAS)
-- =====================================================
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS tarjetas_usuario;
DROP TABLE IF EXISTS ticket;
DROP TABLE IF EXISTS factura;
DROP TABLE IF EXISTS equipos;
DROP TABLE IF EXISTS ubicacion;
DROP TABLE IF EXISTS servicios;
DROP TABLE IF EXISTS contrato;
DROP TABLE IF EXISTS tiposervicio;
DROP TABLE IF EXISTS tecnicos;
DROP TABLE IF EXISTS vehiculos;
DROP TABLE IF EXISTS cliente;
SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- TABLA CLIENTE
-- =====================================================
CREATE TABLE cliente (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    tipo_cliente ENUM('residencial', 'empresarial') DEFAULT 'residencial'
);

-- =====================================================
-- TABLA VEHICULOS
-- =====================================================
CREATE TABLE vehiculos (
    idVehiculos INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(20) UNIQUE NOT NULL,
    modelo VARCHAR(100),
    marca VARCHAR(50),
    año INT,
    estado ENUM('disponible', 'en_uso', 'mantenimiento') DEFAULT 'disponible',
    tipo_vehiculo ENUM('camioneta', 'motocicleta', 'camion') DEFAULT 'camioneta'
);

-- =====================================================
-- TABLA TECNICOS
-- =====================================================
CREATE TABLE tecnicos (
    idTecnicos INT AUTO_INCREMENT PRIMARY KEY,
    Vehiculos_idVehiculos INT,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(150),
    especialidad VARCHAR(100),
    estado ENUM('disponible', 'ocupado', 'fuera_servicio') DEFAULT 'disponible',
    fecha_contratacion DATE,
    FOREIGN KEY (Vehiculos_idVehiculos) REFERENCES vehiculos(idVehiculos)
);

-- =====================================================
-- TABLA TIPO_SERVICIO
-- =====================================================
CREATE TABLE tiposervicio (
    idTipoServicio INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio_base DECIMAL(10,2),
    categoria ENUM('internet', 'telefonia', 'television', 'paquete') NOT NULL
);

-- =====================================================
-- TABLA CONTRATO
-- =====================================================
CREATE TABLE contrato (
    idContrato INT AUTO_INCREMENT PRIMARY KEY,
    Cliente_idCliente INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    monto_total DECIMAL(10,2) NOT NULL,
    tiposervicio VARCHAR(100),
    estado ENUM('activo', 'suspendido', 'cancelado', 'vencido') DEFAULT 'activo',
    FOREIGN KEY (Cliente_idCliente) REFERENCES cliente(idCliente) ON DELETE CASCADE
);

-- =====================================================
-- TABLA SERVICIOS
-- =====================================================
CREATE TABLE servicios (
    idServicios INT AUTO_INCREMENT PRIMARY KEY,
    Contrato_idContrato INT NOT NULL,
    Tecnicos_idTecnicos INT,
    TipoServicio_idTipoServicio INT,
    fecha_servicio DATE,
    descripcion TEXT,
    estado ENUM('pendiente', 'en_proceso', 'completado', 'cancelado') DEFAULT 'pendiente',
    notas_tecnico TEXT,
    FOREIGN KEY (Contrato_idContrato) REFERENCES contrato(idContrato) ON DELETE CASCADE,
    FOREIGN KEY (Tecnicos_idTecnicos) REFERENCES tecnicos(idTecnicos),
    FOREIGN KEY (TipoServicio_idTipoServicio) REFERENCES tiposervicio(idTipoServicio)
);

-- =====================================================
-- TABLA UBICACION (MEJORADA CON FUNCIONALIDADES AVANZADAS)
-- =====================================================
CREATE TABLE ubicacion (
    idUbicacion INT AUTO_INCREMENT PRIMARY KEY,
    Servicios_idServicios INT NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    provincia VARCHAR(100) NOT NULL,
    codigo_postal VARCHAR(20),
    latitud DECIMAL(10, 8),
    longitud DECIMAL(11, 8),
    referencia VARCHAR(255),
    tipo_ubicacion ENUM('residencial', 'comercial', 'industrial') DEFAULT 'residencial',
    es_favorita BOOLEAN DEFAULT FALSE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (Servicios_idServicios) REFERENCES servicios(idServicios) ON DELETE CASCADE
);

-- =====================================================
-- TABLA EQUIPOS
-- =====================================================
CREATE TABLE equipos (
    idEquipos INT AUTO_INCREMENT PRIMARY KEY,
    Servicios_idServicios INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(50),
    estado ENUM('disponible', 'instalado', 'mantenimiento', 'defectuoso') DEFAULT 'disponible',
    numero_serie VARCHAR(100) UNIQUE,
    modelo VARCHAR(100),
    fecha_instalacion DATE,
    FOREIGN KEY (Servicios_idServicios) REFERENCES servicios(idServicios) ON DELETE CASCADE
);

-- =====================================================
-- TABLA FACTURA (MEJORADA)
-- =====================================================
CREATE TABLE factura (
    idFactura INT AUTO_INCREMENT PRIMARY KEY,
    Cliente_idCliente INT NOT NULL,
    fecha_emision DATE NOT NULL,
    monto_total DECIMAL(10,2) NOT NULL,
    estado_pago ENUM('pendiente', 'pagado', 'vencido', 'cancelado') DEFAULT 'pendiente',
    metodo_pago ENUM('Agencia', 'Tarjeta de Credito', 'Tarjeta de Debito', 'Transferencia') DEFAULT 'Agencia',
    numero_tarjeta_oculto VARCHAR(20),
    fecha_vencimiento DATE,
    descuento_aplicado DECIMAL(5,2) DEFAULT 0.00,
    impuesto DECIMAL(5,2) DEFAULT 15.00,
    subtotal DECIMAL(10,2),
    FOREIGN KEY (Cliente_idCliente) REFERENCES cliente(idCliente) ON DELETE CASCADE
);

-- =====================================================
-- TABLA TICKET
-- =====================================================
CREATE TABLE ticket (
    idTicket INT AUTO_INCREMENT PRIMARY KEY,
    Servicios_idServicios INT NOT NULL,
    fecha_creacion DATE NOT NULL,
    descripcion TEXT,
    prioridad ENUM('baja', 'media', 'alta', 'critica') DEFAULT 'media',
    estado ENUM('abierto', 'en_proceso', 'resuelto', 'cerrado', 'pendiente_pago') DEFAULT 'abierto',
    fecha_resolucion DATE,
    tiempo_estimado INT,
    tecnico_asignado INT,
    FOREIGN KEY (Servicios_idServicios) REFERENCES servicios(idServicios) ON DELETE CASCADE,
    FOREIGN KEY (tecnico_asignado) REFERENCES tecnicos(idTecnicos)
);

-- =====================================================
-- TABLA TARJETAS_USUARIO (NUEVA FUNCIONALIDAD)
-- =====================================================
CREATE TABLE tarjetas_usuario (
    idTarjeta INT AUTO_INCREMENT PRIMARY KEY,
    Cliente_idCliente INT NOT NULL,
    tipo_tarjeta ENUM('credito', 'debito') NOT NULL,
    ultimos_cuatro_digitos VARCHAR(4) NOT NULL,
    fecha_vencimiento VARCHAR(7),
    nombre_titular_visible VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activa BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (Cliente_idCliente) REFERENCES cliente(idCliente) ON DELETE CASCADE
);

-- =====================================================
-- INSERCIONES DE DATOS DE PRUEBA
-- =====================================================

-- Insertar clientes de prueba
INSERT INTO cliente (idCliente, nombre, apellido, telefono, email, password) VALUES
(1, 'Juan', 'Pérez', '0987654321', 'juan.perez@email.com', 'password123'),
(2, 'María', 'González', '0987654322', 'maria.gonzalez@email.com', 'password456'),
(3, 'Carlos', 'López', '0987654323', 'carlos.lopez@email.com', 'password789');

-- Insertar vehículos
INSERT INTO vehiculos (idVehiculos, placa, modelo, marca, año, estado) VALUES
(1, 'ABC-123', 'Hilux', 'Toyota', 2020, 'disponible'),
(2, 'XYZ-789', 'Ranger', 'Ford', 2019, 'disponible'),
(3, 'DEF-456', 'NP300', 'Nissan', 2021, 'disponible');

-- Insertar técnicos
INSERT INTO tecnicos (idTecnicos, Vehiculos_idVehiculos, nombre, apellido, telefono, email, especialidad) VALUES
(1, 1, 'Pedro', 'Martínez', '0998765432', 'pedro.martinez@netnexus.com', 'Instalación Internet'),
(2, 2, 'Ana', 'Rodríguez', '0998765433', 'ana.rodriguez@netnexus.com', 'Telefonía'),
(3, 3, 'Luis', 'Fernández', '0998765434', 'luis.fernandez@netnexus.com', 'Televisión');

-- Insertar tipos de servicio
INSERT INTO tiposervicio (idTipoServicio, nombre, descripcion, precio_base, categoria) VALUES
(1, 'Internet Básico', 'Conexión a internet de 50 Mbps', 25.99, 'internet'),
(2, 'Internet Premium', 'Conexión a internet de 200 Mbps', 45.99, 'internet'),
(3, 'Telefonía Fija', 'Línea telefónica fija con llamadas ilimitadas', 15.99, 'telefonia'),
(4, 'TV Básica', 'Paquete básico de televisión', 20.99, 'television'),
(5, 'Paquete Completo', 'Internet + Telefonía + TV', 75.99, 'paquete');

-- Insertar contratos de prueba
INSERT INTO contrato (idContrato, Cliente_idCliente, fecha_inicio, monto_total, tiposervicio, estado) VALUES
(1, 1, '2024-01-01', 25.99, 'Internet Básico', 'activo'),
(2, 2, '2024-02-01', 45.99, 'Internet Premium', 'activo'),
(3, 3, '2024-03-01', 75.99, 'Paquete Completo', 'activo');

-- Insertar servicios de prueba
INSERT INTO servicios (idServicios, Contrato_idContrato, Tecnicos_idTecnicos, TipoServicio_idTipoServicio, fecha_servicio, descripcion, estado) VALUES
(1, 1, 1, 1, '2024-01-15', 'Instalación Internet Básico', 'completado'),
(2, 2, 2, 2, '2024-02-15', 'Instalación Internet Premium', 'completado'),
(3, 3, 3, 5, '2024-03-15', 'Instalación Paquete Completo', 'completado');

-- Insertar ubicaciones de prueba
INSERT INTO ubicacion (idUbicacion, Servicios_idServicios, direccion, ciudad, provincia, codigo_postal, es_favorita) VALUES
(1, 1, 'Av. Amazonas 123', 'Quito', 'Pichincha', '170101', FALSE),
(2, 2, 'Calle 10 de Agosto 456', 'Quito', 'Pichincha', '170102', TRUE),
(3, 3, 'Av. Simón Bolívar 789', 'Guayaquil', 'Guayas', '090101', FALSE);

-- Insertar facturas de prueba
INSERT INTO factura (idFactura, Cliente_idCliente, fecha_emision, monto_total, estado_pago, metodo_pago, subtotal, impuesto) VALUES
(1, 1, '2024-01-31', 29.89, 'pagado', 'Tarjeta de Credito', 25.99, 15.00),
(2, 2, '2024-02-28', 52.89, 'pendiente', 'Agencia', 45.99, 15.00),
(3, 3, '2024-03-31', 87.39, 'pagado', 'Tarjeta de Debito', 75.99, 15.00);

-- Insertar tarjetas de prueba
INSERT INTO tarjetas_usuario (idTarjeta, Cliente_idCliente, tipo_tarjeta, ultimos_cuatro_digitos, fecha_vencimiento, nombre_titular_visible, activa) VALUES
(1, 1, 'credito', '1234', '12/2025', 'JUAN PEREZ', TRUE),
(2, 3, 'debito', '5678', '06/2026', 'CARLOS LOPEZ', TRUE);

-- =====================================================
-- ÍNDICES BÁSICOS (Solo los esenciales)
-- =====================================================
CREATE INDEX idx_cliente_email ON cliente(email);
CREATE INDEX idx_contrato_cliente ON contrato(Cliente_idCliente);
CREATE INDEX idx_factura_cliente ON factura(Cliente_idCliente);
CREATE INDEX idx_servicios_contrato ON servicios(Contrato_idContrato);
CREATE INDEX idx_ubicacion_servicio ON ubicacion(Servicios_idServicios);
CREATE INDEX idx_tarjeta_cliente ON tarjetas_usuario(Cliente_idCliente);

-- =====================================================
-- VERIFICACIÓN FINAL
-- =====================================================
SELECT 'Base de datos NetNexus Ultra v1.5.1 (Compatible) creada exitosamente' as resultado;
SELECT COUNT(*) as total_tablas FROM information_schema.tables WHERE table_schema = 'netnexus_ultra';
SELECT COUNT(*) as total_clientes FROM cliente;
SELECT COUNT(*) as total_tecnicos FROM tecnicos;
SELECT COUNT(*) as total_servicios FROM tiposervicio;
SELECT COUNT(*) as total_contratos FROM contrato;
SELECT COUNT(*) as total_facturas FROM factura;

-- Verificar datos de ubicaciones favoritas
SELECT 'Verificación ubicaciones favoritas:' as verificacion;
SELECT u.idUbicacion, u.direccion, u.ciudad, u.es_favorita, c.nombre, c.apellido
FROM ubicacion u
JOIN servicios s ON u.Servicios_idServicios = s.idServicios
JOIN contrato ct ON s.Contrato_idContrato = ct.idContrato
JOIN cliente c ON ct.Cliente_idCliente = c.idCliente
WHERE u.es_favorita = TRUE;
