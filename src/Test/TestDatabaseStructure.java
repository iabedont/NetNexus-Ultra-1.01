package Test;

import Clases.DatabaseConnection;
import java.sql.*;

public class TestDatabaseStructure {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Conexión exitosa a la base de datos");
            
            // Obtener metadatos de la tabla contrato
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, "contrato", null);
            
            System.out.println("\nColumnas de la tabla 'contrato':");
            System.out.println("================================");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                int columnSize = columns.getInt("COLUMN_SIZE");
                boolean isNullable = columns.getBoolean("NULLABLE");
                
                System.out.printf("%-20s %-15s %-10s %s\n", 
                    columnName, dataType, "(" + columnSize + ")", 
                    isNullable ? "NULL" : "NOT NULL");
            }
            
            // También vamos a probar una consulta simple
            System.out.println("\nProbando consulta simple:");
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM contrato LIMIT 1");
                ResultSetMetaData rsmd = rs.getMetaData();
                
                System.out.println("\nColumnas disponibles en la consulta:");
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    System.out.println(i + ". " + rsmd.getColumnName(i) + " (" + rsmd.getColumnTypeName(i) + ")");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
