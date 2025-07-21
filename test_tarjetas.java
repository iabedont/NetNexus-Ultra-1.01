import java.sql.*;

public class test_tarjetas {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/netnexus_ultra_v1_9?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "root", "");
            
            System.out.println("Conexión exitosa a la base de datos V1.9");
            
            // Verificar si la nueva estructura existe
            String query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'netnexus_ultra_v1_9' AND TABLE_NAME = 'tarjetas_usuario'";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Tabla tarjetas_usuario encontrada");
                
                // Verificar campos nuevos
                String columnQuery = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'netnexus_ultra_v1_9' AND TABLE_NAME = 'tarjetas_usuario'";
                PreparedStatement colStmt = conn.prepareStatement(columnQuery);
                ResultSet colRs = colStmt.executeQuery();
                
                System.out.println("Columnas en tarjetas_usuario:");
                while (colRs.next()) {
                    System.out.println("- " + colRs.getString("COLUMN_NAME"));
                }
                
                colRs.close();
                colStmt.close();
            } else {
                System.out.println("Tabla tarjetas_usuario NO encontrada. Por favor ejecute el script BD_Tarjetas_Usuario_V1.9.sql");
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
