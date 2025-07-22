package Test;

import java.sql.*;
import Clases.DatabaseConnection;

public class TestTicketStructure {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Conexión exitosa a la base de datos");
            
            // Obtener metadatos de la tabla ticket
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, "ticket", null);
            
            System.out.println("\nColumnas de la tabla 'ticket':");
            System.out.println("==============================");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                int columnSize = columns.getInt("COLUMN_SIZE");
                boolean isNullable = columns.getBoolean("NULLABLE");
                
                System.out.printf("%-20s %-15s %-10s %s\n", 
                    columnName, dataType, "(" + columnSize + ")", 
                    isNullable ? "NULL" : "NOT NULL");
            }
            
            // También equipos
            columns = metaData.getColumns(null, null, "equipos", null);
            
            System.out.println("\nColumnas de la tabla 'equipos':");
            System.out.println("===============================");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                int columnSize = columns.getInt("COLUMN_SIZE");
                boolean isNullable = columns.getBoolean("NULLABLE");
                
                System.out.printf("%-20s %-15s %-10s %s\n", 
                    columnName, dataType, "(" + columnSize + ")", 
                    isNullable ? "NULL" : "NOT NULL");
            }
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
