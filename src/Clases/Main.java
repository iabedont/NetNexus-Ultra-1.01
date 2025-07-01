
package Clases;
import javax.swing.*;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        // Probar la conexión a la base de datos al iniciar
        try {
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("Conexión a la base de datos exitosa!");
            conn.close();
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "No se pudo conectar a la base de datos: " + e.getMessage(), 
                "Error de Conexión", 
                JOptionPane.ERROR_MESSAGE);
            return; // Si no se puede conectar, termina el programa
        }

        // Iniciar la interfaz gráfica en el hilo de despacho de eventos
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}