package Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para manejar la conexión a la base de datos MySQL
 * Configuración mejorada para NetNexus Ultra v1.5.1
 */
public class DatabaseConnection {
    
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());
    
    // Configuración de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/mydb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    
    // Configuración de contraseña principal (actualizada)
    private static final String PASSWORD = "6cgR3VFNGGBhDFPEjlTI";
    
    // Configuraciones de contraseña alternativas como respaldo
    private static final String[] PASSWORDS_FALLBACK = {"", "root", "admin", "password", "123456"};
    
    private static Connection connection = null;
    
    static {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.info("Driver MySQL cargado exitosamente");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error al cargar el driver MySQL", e);
        }
    }
    
    /**
     * Obtiene una conexión a la base de datos
     * @return Connection objeto de conexión
     * @throws SQLException si no se puede establecer la conexión
     */
    public static Connection getConnection() throws SQLException {
        // Intentar con la contraseña principal primero
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("Conexión establecida exitosamente");
            return connection;
        } catch (SQLException e) {
            logger.log(Level.FINE, "Falló intento con contraseña principal");
        }
        
        // Si no funciona con la contraseña principal, intentar alternativas
        for (String fallbackPassword : PASSWORDS_FALLBACK) {
            try {
                connection = DriverManager.getConnection(URL, USER, fallbackPassword);
                logger.info("Conexión establecida con contraseña alternativa");
                return connection;
            } catch (SQLException e) {
                logger.log(Level.FINE, "Falló intento con contraseña alternativa");
            }
        }
        
        // Si no funciona, intentar crear la base de datos
        try {
            String createDbUrl = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            
            // Intentar crear con contraseña principal
            try (Connection tempConn = DriverManager.getConnection(createDbUrl, USER, PASSWORD)) {
                try (var stmt = tempConn.createStatement()) {
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS mydb");
                }
                
                // Conectar a la base de datos creada
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                logger.info("Base de datos creada y conexión establecida");
                return connection;
            } catch (SQLException e2) {
                // Intentar con contraseñas alternativas
                for (String fallbackPassword : PASSWORDS_FALLBACK) {
                    try (Connection tempConn = DriverManager.getConnection(createDbUrl, USER, fallbackPassword)) {
                        try (var stmt = tempConn.createStatement()) {
                            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS mydb");
                        }
                        
                        connection = DriverManager.getConnection(URL, USER, fallbackPassword);
                        logger.info("Base de datos creada y conexión establecida con contraseña alternativa");
                        return connection;
                    } catch (SQLException e3) {
                        // Continuar con la siguiente contraseña
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "No se pudo crear la base de datos automáticamente", e);
        }
        
        // Si todo falla, lanzar excepción con mensaje útil
        throw new SQLException("""
                No se pudo conectar a MySQL. Verifica:
                1. MySQL está ejecutándose en localhost:3306
                2. Usuario 'root' existe
                3. Contraseña correcta
                4. Base de datos 'mydb' existe o se puede crear
                """);
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
                return "Conectado a: " + connection.getMetaData().getURL() + 
                       " | Usuario: " + connection.getMetaData().getUserName();
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error obteniendo información de conexión", e);
        }
        return "Sin conexión activa";
    }
}
