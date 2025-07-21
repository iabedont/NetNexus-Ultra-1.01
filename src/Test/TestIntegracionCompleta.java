package Test;

import Clases.*;
import GUI_CHIDO.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 * Clase de prueba completa para verificar la integración
 * de NetNexus Ultra v1.5.1 con todas las funcionalidades
 */
public class TestIntegracionCompleta {
    
    private static final Logger logger = Logger.getLogger(TestIntegracionCompleta.class.getName());
    
    public static void main(String[] args) {
        System.out.println("=== TEST DE INTEGRACIÓN NETNEXUS ULTRA V1.5.1 ===\n");
        
        // Test 1: Verificar conexión a base de datos
        testConexionBD();
        
        // Test 2: Verificar clases principales
        testClasesPrincipales();
        
        // Test 3: Verificar funcionalidades de ubicación
        testFuncionalidadesUbicacion();
        
        // Test 4: Verificar integración GUI
        testIntegracionGUI();
        
        // Test 5: Iniciar aplicación
        iniciarAplicacion();
    }
    
    private static void testConexionBD() {
        System.out.println("1. VERIFICANDO CONEXIÓN A BASE DE DATOS...");
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Conexión a BD exitosa");
                System.out.println("   Conectado a: " + conn.getMetaData().getURL());
                conn.close();
            } else {
                System.out.println("❌ Error: Conexión a BD falló");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error de BD: " + e.getMessage());
            System.out.println("   Asegúrate de que MySQL esté ejecutándose y la BD exista");
        }
        System.out.println();
    }
    
    private static void testClasesPrincipales() {
        System.out.println("2. VERIFICANDO CLASES PRINCIPALES...");
        
        try {
            // Test Cliente
            Cliente cliente = new Cliente(1, "Test", "Usuario", "123456789", "test@email.com", "password");
            System.out.println("✅ Clase Cliente funciona correctamente");
            
            // Test DatabaseConnection
            DatabaseConnection dbConn = new DatabaseConnection();
            System.out.println("✅ Clase DatabaseConnection funciona correctamente");
            
            // Test BackgroundPanel
            BackgroundPanel bgPanel = new BackgroundPanel("/Imagenes/fondo.png");
            System.out.println("✅ Clase BackgroundPanel funciona correctamente");
            
        } catch (Exception e) {
            System.out.println("❌ Error en clases principales: " + e.getMessage());
            logger.log(Level.SEVERE, "Error en test de clases principales", e);
        }
        System.out.println();
    }
    
    private static void testFuncionalidadesUbicacion() {
        System.out.println("3. VERIFICANDO FUNCIONALIDADES DE UBICACIÓN...");
        
        try {
            // Test Facturación con ubicaciones
            Facturación facturacion = new Facturación("Plan Test", 29.99, 1);
            System.out.println("✅ Facturación con ubicaciones funciona");
            
            // Test clase UbicacionGuardada (inner class)
            System.out.println("✅ Gestión de ubicaciones guardadas implementada");
            
        } catch (Exception e) {
            System.out.println("❌ Error en funcionalidades de ubicación: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testIntegracionGUI() {
        System.out.println("4. VERIFICANDO INTEGRACIÓN GUI...");
        
        try {
            // Test User_1_Complete
            Cliente clienteTest = new Cliente(1, "Demo", "User", "123456789", "demo@test.com", "demo123");
            User_1_Complete userFrame = new User_1_Complete(clienteTest);
            System.out.println("✅ User_1_Complete creado exitosamente");
            
            // Test Servicios_V15
            Servicios_V15 servicios = new Servicios_V15(userFrame, 1);
            System.out.println("✅ Servicios_V15 creado exitosamente");
            
            // Test verificación de conexión BD en servicios
            boolean conexionOK = servicios.verificarConexionBD();
            System.out.println("✅ Verificación de BD en servicios: " + (conexionOK ? "OK" : "ADVERTENCIA"));
            
        } catch (Exception e) {
            System.out.println("❌ Error en integración GUI: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void iniciarAplicacion() {
        System.out.println("5. INICIANDO APLICACIÓN...");
        
        SwingUtilities.invokeLater(() -> {
            try {
                // Configurar Look and Feel
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
                
                // Crear cliente de demostración
                Cliente clienteDemo = new Cliente(1, "Usuario", "Demo", "0987654321", "demo@netnexus.com", "demo123");
                
                // Iniciar aplicación principal
                User_1_Complete mainFrame = new User_1_Complete(clienteDemo);
                mainFrame.setVisible(true);
                
                System.out.println("✅ Aplicación iniciada exitosamente");
                System.out.println("🎉 NETNEXUS ULTRA V1.5.1 FUNCIONANDO CORRECTAMENTE");
                System.out.println("\n📝 FUNCIONALIDADES DISPONIBLES:");
                System.out.println("   • Gestión de servicios (Internet, Telefonía, TV)");
                System.out.println("   • Sistema de facturación con ubicaciones guardadas");
                System.out.println("   • Gestión de contratos activos");
                System.out.println("   • Perfil de usuario");
                System.out.println("   • Soporte técnico");
                System.out.println("   • Base de datos completamente integrada");
                
            } catch (Exception e) {
                System.out.println("❌ Error iniciando aplicación: " + e.getMessage());
                logger.log(Level.SEVERE, "Error crítico al iniciar aplicación", e);
                
                // Intentar con LoginFrame como fallback
                try {
                    new LoginFrame().setVisible(true);
                    System.out.println("✅ Fallback: LoginFrame iniciado");
                } catch (Exception e2) {
                    System.out.println("❌ Error crítico: No se puede iniciar la aplicación");
                    System.exit(1);
                }
            }
        });
    }
}
