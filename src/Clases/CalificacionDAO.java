package Clases;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase DAO para manejar operaciones de calificaciones en la base de datos
 * Proporciona métodos para CRUD de calificaciones y estadísticas
 * 
 * @author ASUS
 * @version 1.0
 */
public class CalificacionDAO {
    
    private static final Logger logger = Logger.getLogger(CalificacionDAO.class.getName());
    
    /**
     * Guarda una nueva calificación en la base de datos
     * @param calificacion La calificación a guardar
     * @return true si se guardó exitosamente
     */
    public static boolean guardarCalificacion(CalificacionServicio calificacion) {
        String sql = "INSERT INTO calificaciones_servicios " +
                    "(Cliente_idCliente, Servicio_idServicio, TipoServicio_idTipoServicio, " +
                    "calificacion_tecnico, calificacion_servicio, calificacion_tiempo_respuesta, " +
                    "calificacion_precio, calificacion_general, comentario, recomendaria) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, calificacion.getClienteIdCliente());
            pstmt.setInt(2, calificacion.getServicioIdServicio());
            pstmt.setInt(3, calificacion.getTipoServicioIdTipoServicio());
            pstmt.setInt(4, calificacion.getCalificacionTecnico());
            pstmt.setInt(5, calificacion.getCalificacionServicio());
            pstmt.setInt(6, calificacion.getCalificacionTiempoRespuesta());
            pstmt.setInt(7, calificacion.getCalificacionPrecio());
            pstmt.setDouble(8, calificacion.getCalificacionGeneral());
            pstmt.setString(9, calificacion.getComentario());
            pstmt.setBoolean(10, calificacion.isRecomendaria());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al guardar calificación", e);
            return false;
        }
    }
    
    /**
     * Verifica si un cliente ya calificó un servicio específico
     * @param clienteId ID del cliente
     * @param servicioId ID del servicio
     * @return true si ya existe una calificación
     */
    public static boolean yaCalificoServicio(int clienteId, int servicioId) {
        String sql = "SELECT COUNT(*) FROM calificaciones_servicios " +
                    "WHERE Cliente_idCliente = ? AND Servicio_idServicio = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, clienteId);
            pstmt.setInt(2, servicioId);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al verificar calificación existente", e);
        }
        
        return false;
    }
    
    /**
     * Obtiene todas las calificaciones de un cliente específico
     * @param clienteId ID del cliente
     * @return Lista de calificaciones del cliente
     */
    public static List<CalificacionServicio> obtenerCalificacionesCliente(int clienteId) {
        List<CalificacionServicio> calificaciones = new ArrayList<>();
        String sql = "SELECT * FROM calificaciones_servicios WHERE Cliente_idCliente = ? " +
                    "ORDER BY fecha_calificacion DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, clienteId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                CalificacionServicio calificacion = crearCalificacionDesdeResultSet(rs);
                calificaciones.add(calificacion);
            }
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener calificaciones del cliente", e);
        }
        
        return calificaciones;
    }
    
    /**
     * Obtiene servicios disponibles para calificar por un cliente
     * (Servicios completados que aún no han sido calificados)
     * @param clienteId ID del cliente
     * @return Lista de servicios disponibles para calificar
     */
    public static List<String[]> obtenerServiciosParaCalificar(int clienteId) {
        List<String[]> servicios = new ArrayList<>();
        // Por ahora retornamos una lista vacía hasta que tengamos la estructura correcta de servicios
        return servicios;
    }
    
    /**
     * Obtiene estadísticas de servicios ordenadas por calificación
     * @return Lista de estadísticas de servicios
     */
    public static List<String[]> obtenerEstadisticasServicios() {
        List<String[]> estadisticas = new ArrayList<>();
        // Por ahora retornamos datos de ejemplo hasta que tengamos la estructura correcta
        estadisticas.add(new String[]{"Plan Básico", "Internet", "15.99", "25", "4.2", "4.1", "4.3", "4.0", "4.4", "85"});
        estadisticas.add(new String[]{"Plan Estándar", "Internet", "29.99", "45", "4.5", "4.4", "4.6", "4.3", "4.7", "92"});
        estadisticas.add(new String[]{"Plan Premium", "Internet", "49.99", "38", "4.7", "4.8", "4.6", "4.7", "4.8", "95"});
        return estadisticas;
    }
    
    /**
     * Obtiene los mejores servicios (mejor calificados)
     * @param limite Número máximo de servicios a retornar
     * @return Lista de mejores servicios
     */
    public static List<String[]> obtenerMejoresServicios(int limite) {
        List<String[]> mejores = new ArrayList<>();
        String sql = "SELECT * FROM mejores_servicios WHERE total_calificaciones >= 1 " +
                    "ORDER BY promedio_calificacion DESC, total_calificaciones DESC LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limite);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String[] servicio = {
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getString("categoria"),
                    String.valueOf(rs.getDouble("precio_base")),
                    String.valueOf(rs.getInt("total_calificaciones")),
                    String.valueOf(rs.getDouble("promedio_calificacion")),
                    String.valueOf(rs.getDouble("porcentaje_recomendacion")),
                    rs.getString("clasificacion")
                };
                mejores.add(servicio);
            }
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener mejores servicios", e);
        }
        
        return mejores;
    }
    
    /**
     * Obtiene calificaciones detalladas para mostrar al público
     * @param tipoServicioId ID del tipo de servicio (opcional, -1 para todos)
     * @param limite Número máximo de calificaciones a retornar
     * @return Lista de calificaciones detalladas
     */
    public static List<String[]> obtenerCalificacionesDetalladas(int tipoServicioId, int limite) {
        List<String[]> calificaciones = new ArrayList<>();
        String sql = "SELECT cliente_nombre, tipo_servicio, categoria, calificacion_general, " +
                    "comentario, recomendaria, fecha_calificacion, util_para_otros " +
                    "FROM calificaciones_detalladas ";
        
        if (tipoServicioId > 0) {
            sql += "WHERE TipoServicio_idTipoServicio = ? ";
        }
        
        sql += "ORDER BY fecha_calificacion DESC LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            int paramIndex = 1;
            if (tipoServicioId > 0) {
                pstmt.setInt(paramIndex++, tipoServicioId);
            }
            pstmt.setInt(paramIndex, limite);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String[] calificacion = {
                    rs.getString("cliente_nombre"),
                    rs.getString("tipo_servicio"),
                    rs.getString("categoria"),
                    String.valueOf(rs.getDouble("calificacion_general")),
                    rs.getString("comentario"),
                    rs.getBoolean("recomendaria") ? "SÍ" : "NO",
                    rs.getTimestamp("fecha_calificacion").toString(),
                    String.valueOf(rs.getInt("util_para_otros"))
                };
                calificaciones.add(calificacion);
            }
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener calificaciones detalladas", e);
        }
        
        return calificaciones;
    }
    
    /**
     * Marca una calificación como útil
     * @param calificacionId ID de la calificación
     * @param clienteId ID del cliente que marca como útil
     * @return true si se marcó exitosamente
     */
    public static boolean marcarComoUtil(int calificacionId, int clienteId) {
        String sqlInsert = "INSERT INTO utilidad_calificaciones " +
                          "(Calificacion_idCalificacion, Cliente_idCliente, es_util) " +
                          "VALUES (?, ?, TRUE) " +
                          "ON DUPLICATE KEY UPDATE es_util = TRUE";
        
        String sqlUpdate = "UPDATE calificaciones_servicios " +
                          "SET util_para_otros = util_para_otros + 1 " +
                          "WHERE idCalificacion = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Insertar o actualizar registro de utilidad
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlInsert)) {
                pstmt1.setInt(1, calificacionId);
                pstmt1.setInt(2, clienteId);
                pstmt1.executeUpdate();
            }
            
            // Actualizar contador
            try (PreparedStatement pstmt2 = conn.prepareStatement(sqlUpdate)) {
                pstmt2.setInt(1, calificacionId);
                pstmt2.executeUpdate();
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al marcar calificación como útil", e);
            return false;
        }
    }
    
    /**
     * Crea una instancia de CalificacionServicio desde un ResultSet
     * @param rs ResultSet con datos de la calificación
     * @return Instancia de CalificacionServicio
     * @throws SQLException Si hay error al leer el ResultSet
     */
    private static CalificacionServicio crearCalificacionDesdeResultSet(ResultSet rs) throws SQLException {
        return new CalificacionServicio(
            rs.getInt("idCalificacion"),
            rs.getInt("Cliente_idCliente"),
            rs.getInt("Servicio_idServicio"),
            rs.getInt("TipoServicio_idTipoServicio"),
            rs.getInt("calificacion_tecnico"),
            rs.getInt("calificacion_servicio"),
            rs.getInt("calificacion_tiempo_respuesta"),
            rs.getInt("calificacion_precio"),
            rs.getDouble("calificacion_general"),
            rs.getString("comentario"),
            rs.getBoolean("recomendaria"),
            rs.getTimestamp("fecha_calificacion"),
            rs.getInt("util_para_otros")
        );
    }
}
