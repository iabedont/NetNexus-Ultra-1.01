-- Script de Base de Datos NetNexus Ultra v1.5.1
-- Incluye todas las funcionalidades implementadas por el equipo
-- Compatible con gestión de ubicaciones y funcionalidades avanzadas

-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS netnexus_ultra;
USE netnexus_ultra;

-- =====================================================
-- TABLA CLIENTE
-- =====================================================
CREATE TABLE IF NOT EXISTS cliente (
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
CREATE TABLE IF NOT EXISTS vehiculos (
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
CREATE TABLE IF NOT EXISTS tecnicos (
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
CREATE TABLE IF NOT EXISTS tiposervicio (
    idTipoServicio INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio_base DECIMAL(10,2),
    categoria ENUM('internet', 'telefonia', 'television', 'paquete') NOT NULL
);

-- =====================================================
-- TABLA CONTRATO
-- =====================================================
CREATE TABLE IF NOT EXISTS contrato (
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
CREATE TABLE IF NOT EXISTS servicios (
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
CREATE TABLE IF NOT EXISTS ubicacion (
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
    FOREIGN KEY (Servicios_idServicios) REFERENCES servicios(idServicios) ON DELETE CASCADE,
    INDEX idx_ubicacion_servicio (Servicios_idServicios),
    INDEX idx_ubicacion_favorita (es_favorita)
);

-- =====================================================
-- TABLA EQUIPOS
-- =====================================================
CREATE TABLE IF NOT EXISTS equipos (
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
CREATE TABLE IF NOT EXISTS factura (
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
CREATE TABLE IF NOT EXISTS ticket (
    idTicket INT AUTO_INCREMENT PRIMARY KEY,
    Servicios_idServicios INT NOT NULL,
    fecha_creacion DATE NOT NULL,
    descripcion TEXT,
    prioridad ENUM('baja', 'media', 'alta', 'critica') DEFAULT 'media',
    estado ENUM('abierto', 'en_proceso', 'resuelto', 'cerrado', 'pendiente_pago') DEFAULT 'abierto',
    fecha_resolucion DATE,
    tiempo_estimado INT, -- minutos estimados
    tecnico_asignado INT,
    FOREIGN KEY (Servicios_idServicios) REFERENCES servicios(idServicios) ON DELETE CASCADE,
    FOREIGN KEY (tecnico_asignado) REFERENCES tecnicos(idTecnicos)
);

-- =====================================================
-- TABLA TARJETAS_USUARIO (NUEVA FUNCIONALIDAD)
-- =====================================================
CREATE TABLE IF NOT EXISTS tarjetas_usuario (
    idTarjeta INT AUTO_INCREMENT PRIMARY KEY,
    Cliente_idCliente INT NOT NULL,
    tipo_tarjeta ENUM('credito', 'debito') NOT NULL,
    ultimos_cuatro_digitos VARCHAR(4) NOT NULL,
    fecha_vencimiento VARCHAR(7), -- MM/YYYY
    nombre_titular_visible VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activa BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (Cliente_idCliente) REFERENCES cliente(idCliente) ON DELETE CASCADE,
    INDEX idx_tarjeta_cliente (Cliente_idCliente),
    INDEX idx_tarjeta_activa (activa)
);

-- =====================================================
-- VISTA PARA FACTURAS CON INFORMACIÓN DE TARJETA
-- =====================================================
DROP VIEW IF EXISTS vista_facturas_con_tarjeta;
CREATE VIEW vista_facturas_con_tarjeta AS
SELECT 
    f.idFactura,
    f.Cliente_idCliente,
    c.nombre,
    c.apellido,
    f.fecha_emision,
    f.monto_total,
    f.estado_pago,
    f.metodo_pago,
    CASE 
        WHEN f.metodo_pago IN ('Tarjeta de Credito', 'Tarjeta de Debito') 
        THEN CONCAT(f.metodo_pago, ' - ', COALESCE(f.numero_tarjeta_oculto, '****'))
        ELSE f.metodo_pago
    END as metodo_pago_detallado,
    f.subtotal,
    f.descuento_aplicado,
    f.impuesto
FROM factura f
INNER JOIN cliente c ON f.Cliente_idCliente = c.idCliente;

-- =====================================================
-- VISTA PARA UBICACIONES FAVORITAS
-- =====================================================
DROP VIEW IF EXISTS vista_ubicaciones_favoritas;
CREATE VIEW vista_ubicaciones_favoritas AS
SELECT DISTINCT
    u.idUbicacion,
    u.direccion,
    u.ciudad,
    u.provincia,
    u.codigo_postal,
    c.idCliente,
    c.nombre,
    c.apellido,
    u.fecha_registro
FROM ubicacion u
INNER JOIN servicios s ON u.Servicios_idServicios = s.idServicios
INNER JOIN contrato ct ON s.Contrato_idContrato = ct.idContrato
INNER JOIN cliente c ON ct.Cliente_idCliente = c.idCliente
WHERE u.es_favorita = TRUE OR u.Servicios_idServicios < 0
ORDER BY u.fecha_registro DESC;

-- =====================================================
-- INSERCIONES DE DATOS DE PRUEBA
-- =====================================================

-- Insertar clientes de prueba
INSERT IGNORE INTO cliente (idCliente, nombre, apellido, telefono, email, password) VALUES
(1, 'Juan', 'Pérez', '0987654321', 'juan.perez@email.com', 'password123'),
(2, 'María', 'González', '0987654322', 'maria.gonzalez@email.com', 'password456'),
(3, 'Carlos', 'López', '0987654323', 'carlos.lopez@email.com', 'password789');

-- Insertar vehículos
INSERT IGNORE INTO vehiculos (idVehiculos, placa, modelo, marca, año, estado) VALUES
(1, 'ABC-123', 'Hilux', 'Toyota', 2020, 'disponible'),
(2, 'XYZ-789', 'Ranger', 'Ford', 2019, 'disponible'),
(3, 'DEF-456', 'NP300', 'Nissan', 2021, 'disponible');

-- Insertar técnicos
INSERT IGNORE INTO tecnicos (idTecnicos, Vehiculos_idVehiculos, nombre, apellido, telefono, email, especialidad) VALUES
(1, 1, 'Pedro', 'Martínez', '0998765432', 'pedro.martinez@netnexus.com', 'Instalación Internet'),
(2, 2, 'Ana', 'Rodríguez', '0998765433', 'ana.rodriguez@netnexus.com', 'Telefonía'),
(3, 3, 'Luis', 'Fernández', '0998765434', 'luis.fernandez@netnexus.com', 'Televisión');

-- Insertar tipos de servicio
INSERT IGNORE INTO tiposervicio (idTipoServicio, nombre, descripcion, precio_base, categoria) VALUES
(1, 'Internet Básico', 'Conexión a internet de 50 Mbps', 25.99, 'internet'),
(2, 'Internet Premium', 'Conexión a internet de 200 Mbps', 45.99, 'internet'),
(3, 'Telefonía Fija', 'Línea telefónica fija con llamadas ilimitadas', 15.99, 'telefonia'),
(4, 'TV Básica', 'Paquete básico de televisión', 20.99, 'television'),
(5, 'Paquete Completo', 'Internet + Telefonía + TV', 75.99, 'paquete');

-- =====================================================
-- PROCEDIMIENTOS ALMACENADOS (NUEVAS FUNCIONALIDADES)
-- =====================================================

-- Eliminar procedimientos si existen
DROP PROCEDURE IF EXISTS GuardarUbicacionFavorita;
DROP PROCEDURE IF EXISTS ObtenerUbicacionesCliente;

-- Procedimiento para guardar ubicación favorita
DELIMITER //
CREATE PROCEDURE GuardarUbicacionFavorita(
    IN p_cliente_id INT,
    IN p_direccion VARCHAR(255),
    IN p_ciudad VARCHAR(100),
    IN p_provincia VARCHAR(100),
    IN p_codigo_postal VARCHAR(20)
)
BEGIN
    DECLARE v_ubicacion_id INT;
    
    -- Generar ID único para ubicación favorita
    SET v_ubicacion_id = 900000 + FLOOR(RAND() * 99999);
    
    -- Insertar ubicación favorita (usando ID negativo del cliente como servicio temporal)
    INSERT INTO ubicacion (
        idUbicacion, 
        Servicios_idServicios, 
        direccion, 
        ciudad, 
        provincia, 
        codigo_postal,
        es_favorita
    ) VALUES (
        v_ubicacion_id,
        -p_cliente_id,
        p_direccion,
        p_ciudad,
        p_provincia,
        p_codigo_postal,
        TRUE
    );
    
    SELECT v_ubicacion_id as ubicacion_id, 'Ubicación guardada exitosamente' as mensaje;
END //

-- Procedimiento para obtener ubicaciones del cliente
CREATE PROCEDURE ObtenerUbicacionesCliente(
    IN p_cliente_id INT
)
BEGIN
    SELECT DISTINCT 
        u.idUbicacion,
        u.direccion,
        u.ciudad,
        u.provincia,
        u.codigo_postal,
        u.es_favorita,
        u.fecha_registro
    FROM ubicacion u
    INNER JOIN servicios s ON u.Servicios_idServicios = s.idServicios
    INNER JOIN contrato c ON s.Contrato_idContrato = c.idContrato
    WHERE c.Cliente_idCliente = p_cliente_id
    
    UNION
    
    SELECT 
        u.idUbicacion,
        u.direccion,
        u.ciudad,
        u.provincia,
        u.codigo_postal,
        u.es_favorita,
        u.fecha_registro
    FROM ubicacion u
    WHERE u.Servicios_idServicios = -p_cliente_id
    
    ORDER BY fecha_registro DESC;
END //

DELIMITER ;

-- =====================================================
-- ÍNDICES BÁSICOS (Sintaxis Simple y Compatible)
-- =====================================================

-- Crear índices básicos sin verificaciones complejas
CREATE INDEX idx_cliente_email ON cliente(email);
CREATE INDEX idx_contrato_cliente ON contrato(Cliente_idCliente);
CREATE INDEX idx_contrato_fecha ON contrato(fecha_inicio);
CREATE INDEX idx_factura_cliente ON factura(Cliente_idCliente);
CREATE INDEX idx_factura_fecha ON factura(fecha_emision);
CREATE INDEX idx_servicios_contrato ON servicios(Contrato_idContrato);
CREATE INDEX idx_ticket_servicio ON ticket(Servicios_idServicios);

-- =====================================================
-- DATOS DE PRUEBA ADICIONALES
-- =====================================================

-- Insertar contratos de prueba
INSERT IGNORE INTO contrato (idContrato, Cliente_idCliente, fecha_inicio, monto_total, tiposervicio, estado) VALUES
(1, 1, '2024-01-01', 25.99, 'Internet Básico', 'activo'),
(2, 2, '2024-02-01', 45.99, 'Internet Premium', 'activo'),
(3, 3, '2024-03-01', 75.99, 'Paquete Completo', 'activo');

-- Insertar servicios de prueba
INSERT IGNORE INTO servicios (idServicios, Contrato_idContrato, Tecnicos_idTecnicos, TipoServicio_idTipoServicio, fecha_servicio, descripcion, estado) VALUES
(1, 1, 1, 1, '2024-01-15', 'Instalación Internet Básico', 'completado'),
(2, 2, 2, 2, '2024-02-15', 'Instalación Internet Premium', 'completado'),
(3, 3, 3, 5, '2024-03-15', 'Instalación Paquete Completo', 'completado');

-- Insertar ubicaciones de prueba
INSERT IGNORE INTO ubicacion (idUbicacion, Servicios_idServicios, direccion, ciudad, provincia, codigo_postal, es_favorita) VALUES
(1, 1, 'Av. Amazonas 123', 'Quito', 'Pichincha', '170101', FALSE),
(2, 2, 'Calle 10 de Agosto 456', 'Quito', 'Pichincha', '170102', TRUE),
(3, 3, 'Av. Simón Bolívar 789', 'Guayaquil', 'Guayas', '090101', FALSE);

-- Insertar facturas de prueba
INSERT IGNORE INTO factura (idFactura, Cliente_idCliente, fecha_emision, monto_total, estado_pago, metodo_pago, subtotal, impuesto) VALUES
(1, 1, '2024-01-31', 29.89, 'pagado', 'Tarjeta de Credito', 25.99, 15.00),
(2, 2, '2024-02-28', 52.89, 'pendiente', 'Agencia', 45.99, 15.00),
(3, 3, '2024-03-31', 87.39, 'pagado', 'Tarjeta de Debito', 75.99, 15.00);

-- Insertar tarjetas de prueba
INSERT IGNORE INTO tarjetas_usuario (idTarjeta, Cliente_idCliente, tipo_tarjeta, ultimos_cuatro_digitos, fecha_vencimiento, nombre_titular_visible, activa) VALUES
(1, 1, 'credito', '1234', '12/2025', 'JUAN PEREZ', TRUE),
(2, 3, 'debito', '5678', '06/2026', 'CARLOS LOPEZ', TRUE);

-- =====================================================
-- VERIFICACIÓN FINAL
-- =====================================================
SELECT 'Base de datos NetNexus Ultra v1.5.1 creada exitosamente' as resultado;
SELECT COUNT(*) as total_tablas FROM information_schema.tables WHERE table_schema = 'netnexus_ultra';
SELECT COUNT(*) as total_clientes FROM cliente;
SELECT COUNT(*) as total_tecnicos FROM tecnicos;
SELECT COUNT(*) as total_servicios FROM tiposervicio;
