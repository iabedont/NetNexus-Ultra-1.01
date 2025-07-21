package Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para manejar la conexión a la base de datos
 * Con soporte para MySQL y base de datos en memoria como fallback
 */
public class DatabaseConnectionFallback {
    
    private static final Logger logger = Logger.getLogger(DatabaseConnectionFallback.class.getName());
    
    // Configuración de la base de datos MySQL
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/netnexus_ultra?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String[] PASSWORDS = {"", "root", "admin", "password", "123456"};
    
    // Configuración de base de datos en memoria (H2) como fallback
    private static final String H2_URL = "jdbc:h2:mem:netnexus_ultra;DB_CLOSE_DELAY=-1;MODE=MySQL";
    private static final String H2_USER = "sa";
    private static final String H2_PASSWORD = "";
    
    private static Connection connection = null;
    private static boolean usingH2 = false;
    
    static {
        try {
            // Intentar cargar driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.info("Driver MySQL cargado exitosamente");
        } catch (ClassNotFoundException e) {
            logger.log(Level.WARNING, "Driver MySQL no encontrado, usando H2 como fallback");
            try {
                Class.forName("org.h2.Driver");
                logger.info("Driver H2 cargado como fallback");
            } catch (ClassNotFoundException e2) {
                logger.log(Level.SEVERE, "No se pudo cargar ningún driver de base de datos", e2);
            }
        }
    }
    
    /**
     * Obtiene una conexión a la base de datos
     * @return Connection objeto de conexión
     * @throws SQLException si no se puede establecer la conexión
     */
    public static Connection getConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }
        
        // Intentar MySQL primero
        connection = tryMySQL();
        if (connection != null) {
            usingH2 = false;
            return connection;
        }
        
        // Si MySQL falla, usar H2
        connection = tryH2();
        if (connection != null) {
            usingH2 = true;
            initializeH2Database();
            return connection;
        }
        
        throw new SQLException("No se pudo conectar a ninguna base de datos");
    }
    
    private static Connection tryMySQL() {
        for (String password : PASSWORDS) {
            try {
                Connection conn = DriverManager.getConnection(MYSQL_URL, USER, password);
                logger.info("Conexión MySQL establecida con contraseña: " + 
                           (password.isEmpty() ? "[sin contraseña]" : "[con contraseña]"));
                return conn;
            } catch (SQLException e) {
                // Continuar con la siguiente contraseña
            }
        }
        
        // Intentar crear la base de datos
        try {
            String createDbUrl = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            for (String password : PASSWORDS) {
                try (Connection tempConn = DriverManager.getConnection(createDbUrl, USER, password);
                     Statement stmt = tempConn.createStatement()) {
                    
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS netnexus_ultra");
                    
                    Connection conn = DriverManager.getConnection(MYSQL_URL, USER, password);
                    logger.info("Base de datos MySQL creada y conexión establecida");
                    return conn;
                } catch (SQLException e) {
                    // Continuar con la siguiente contraseña
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "No se pudo conectar a MySQL", e);
        }
        
        return null;
    }
    
    private static Connection tryH2() {
        try {
            Connection conn = DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWORD);
            logger.info("Conexión H2 (en memoria) establecida como fallback");
            return conn;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "No se pudo conectar a H2", e);
            return null;
        }
    }
    
    private static void initializeH2Database() {
        if (!usingH2 || connection == null) return;
        
        try (Statement stmt = connection.createStatement()) {
            // Crear tablas básicas para funcionalidad mínima
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS cliente (
                    idCliente INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    apellido VARCHAR(100) NOT NULL,
                    telefono VARCHAR(20),
                    email VARCHAR(150) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    activo BOOLEAN DEFAULT TRUE
                )
            """);
            
            // Insertar datos de prueba
            stmt.executeUpdate("""
                INSERT INTO cliente (nombre, apellido, telefono, email, password) VALUES
                ('Juan', 'Pérez', '0987654321', 'juan.perez@email.com', 'password123'),
                ('María', 'González', '0987654322', 'maria.gonzalez@email.com', 'password456'),
                ('Carlos', 'López', '0987654323', 'carlos.lopez@email.com', 'password789')
                ON DUPLICATE KEY UPDATE nombre=nombre
            """);
            
            logger.info("Base de datos H2 inicializada con datos de prueba");
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error inicializando base de datos H2", e);
        }
    }
    
    /**
     * Cierra la conexión a la base de datos
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Conexión cerrada exitosamente");
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Error al cerrar la conexión", e);
            }
        }
    }
    
    /**
     * Verifica si la conexión está activa
     * @return true si la conexión está activa
     */
    public static boolean isConnectionActive() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error verificando conexión", e);
            return false;
        }
    }
    
    /**
     * Obtiene información de la conexión actual
     * @return String con información de la conexión
     */
    public static String getConnectionInfo() {
        try {
            if (connection != null && !connection.isClosed()) {
                String dbType = usingH2 ? "H2 (En Memoria)" : "MySQL";
                return dbType + " - Conectado a: " + connection.getMetaData().getURL() + 
                       " | Usuario: " + connection.getMetaData().getUserName();
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error obteniendo información de conexión", e);
        }
        return "Sin conexión activa";
    }
    
    /**
     * Indica si se está usando H2 como fallback
     * @return true si se está usando H2
     */
    public static boolean isUsingH2() {
        return usingH2;
    }
}
