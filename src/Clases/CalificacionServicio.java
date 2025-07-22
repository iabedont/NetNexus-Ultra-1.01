package Clases;

import java.sql.Timestamp;

/**
 * Clase para representar las calificaciones de servicios en NetNexus Ultra
 * Permite a los clientes calificar diferentes aspectos de los servicios contratados
 * 
 * @author ASUS
 * @version 1.0
 */
public class CalificacionServicio {
    
    private int idCalificacion;
    private int clienteIdCliente;
    private int servicioIdServicio;
    private int tipoServicioIdTipoServicio;
    private int calificacionTecnico;
    private int calificacionServicio;
    private int calificacionTiempoRespuesta;
    private int calificacionPrecio;
    private double calificacionGeneral;
    private String comentario;
    private boolean recomendaria;
    private Timestamp fechaCalificacion;
    private int utilParaOtros;
    
    // Constructor completo
    public CalificacionServicio(int idCalificacion, int clienteIdCliente, int servicioIdServicio, 
                               int tipoServicioIdTipoServicio, int calificacionTecnico, 
                               int calificacionServicio, int calificacionTiempoRespuesta, 
                               int calificacionPrecio, double calificacionGeneral, 
                               String comentario, boolean recomendaria, 
                               Timestamp fechaCalificacion, int utilParaOtros) {
        this.idCalificacion = idCalificacion;
        this.clienteIdCliente = clienteIdCliente;
        this.servicioIdServicio = servicioIdServicio;
        this.tipoServicioIdTipoServicio = tipoServicioIdTipoServicio;
        this.calificacionTecnico = calificacionTecnico;
        this.calificacionServicio = calificacionServicio;
        this.calificacionTiempoRespuesta = calificacionTiempoRespuesta;
        this.calificacionPrecio = calificacionPrecio;
        this.calificacionGeneral = calificacionGeneral;
        this.comentario = comentario;
        this.recomendaria = recomendaria;
        this.fechaCalificacion = fechaCalificacion;
        this.utilParaOtros = utilParaOtros;
    }
    
    // Constructor para nueva calificación (sin ID ni fecha)
    public CalificacionServicio(int clienteIdCliente, int servicioIdServicio, 
                               int tipoServicioIdTipoServicio, int calificacionTecnico, 
                               int calificacionServicio, int calificacionTiempoRespuesta, 
                               int calificacionPrecio, String comentario, boolean recomendaria) {
        this.clienteIdCliente = clienteIdCliente;
        this.servicioIdServicio = servicioIdServicio;
        this.tipoServicioIdTipoServicio = tipoServicioIdTipoServicio;
        this.calificacionTecnico = calificacionTecnico;
        this.calificacionServicio = calificacionServicio;
        this.calificacionTiempoRespuesta = calificacionTiempoRespuesta;
        this.calificacionPrecio = calificacionPrecio;
        this.comentario = comentario;
        this.recomendaria = recomendaria;
        this.utilParaOtros = 0;
        
        // Calcular calificación general automáticamente
        this.calificacionGeneral = calcularCalificacionGeneral();
    }
    
    /**
     * Calcula la calificación general basada en las calificaciones individuales
     * @return La calificación general promedio
     */
    private double calcularCalificacionGeneral() {
        return (double)(calificacionTecnico + calificacionServicio + 
                       calificacionTiempoRespuesta + calificacionPrecio) / 4.0;
    }
    
    /**
     * Valida que todas las calificaciones estén en el rango válido (1-5)
     * @return true si todas las calificaciones son válidas
     */
    public boolean validarCalificaciones() {
        return validarRango(calificacionTecnico) && 
               validarRango(calificacionServicio) && 
               validarRango(calificacionTiempoRespuesta) && 
               validarRango(calificacionPrecio);
    }
    
    /**
     * Valida que una calificación esté en el rango 1-5
     * @param calificacion La calificación a validar
     * @return true si está en rango válido
     */
    private boolean validarRango(int calificacion) {
        return calificacion >= 1 && calificacion <= 5;
    }
    
    /**
     * Obtiene una descripción textual de la calificación general
     * @return Descripción de la calificación
     */
    public String getDescripcionCalificacion() {
        if (calificacionGeneral >= 4.5) return "EXCELENTE";
        else if (calificacionGeneral >= 4.0) return "MUY BUENO";
        else if (calificacionGeneral >= 3.5) return "BUENO";
        else if (calificacionGeneral >= 3.0) return "REGULAR";
        else return "NECESITA MEJORAS";
    }
    
    /**
     * Genera estrellas para representar visualmente la calificación
     * @param calificacion La calificación numérica
     * @return String con estrellas (★☆)
     */
    public static String generarEstrellas(double calificacion) {
        StringBuilder estrellas = new StringBuilder();
        int estrellasCompletas = (int) calificacion;
        boolean mediaEstrella = (calificacion - estrellasCompletas) >= 0.5;
        
        for (int i = 0; i < estrellasCompletas; i++) {
            estrellas.append("★");
        }
        
        if (mediaEstrella && estrellasCompletas < 5) {
            estrellas.append("½");
        }
        
        while (estrellas.length() < 5) {
            estrellas.append("☆");
        }
        
        return estrellas.toString();
    }
    
    // Getters y Setters
    public int getIdCalificacion() { return idCalificacion; }
    public void setIdCalificacion(int idCalificacion) { this.idCalificacion = idCalificacion; }
    
    public int getClienteIdCliente() { return clienteIdCliente; }
    public void setClienteIdCliente(int clienteIdCliente) { this.clienteIdCliente = clienteIdCliente; }
    
    public int getServicioIdServicio() { return servicioIdServicio; }
    public void setServicioIdServicio(int servicioIdServicio) { this.servicioIdServicio = servicioIdServicio; }
    
    public int getTipoServicioIdTipoServicio() { return tipoServicioIdTipoServicio; }
    public void setTipoServicioIdTipoServicio(int tipoServicioIdTipoServicio) { 
        this.tipoServicioIdTipoServicio = tipoServicioIdTipoServicio; 
    }
    
    public int getCalificacionTecnico() { return calificacionTecnico; }
    public void setCalificacionTecnico(int calificacionTecnico) { 
        this.calificacionTecnico = calificacionTecnico;
        this.calificacionGeneral = calcularCalificacionGeneral();
    }
    
    public int getCalificacionServicio() { return calificacionServicio; }
    public void setCalificacionServicio(int calificacionServicio) { 
        this.calificacionServicio = calificacionServicio;
        this.calificacionGeneral = calcularCalificacionGeneral();
    }
    
    public int getCalificacionTiempoRespuesta() { return calificacionTiempoRespuesta; }
    public void setCalificacionTiempoRespuesta(int calificacionTiempoRespuesta) { 
        this.calificacionTiempoRespuesta = calificacionTiempoRespuesta;
        this.calificacionGeneral = calcularCalificacionGeneral();
    }
    
    public int getCalificacionPrecio() { return calificacionPrecio; }
    public void setCalificacionPrecio(int calificacionPrecio) { 
        this.calificacionPrecio = calificacionPrecio;
        this.calificacionGeneral = calcularCalificacionGeneral();
    }
    
    public double getCalificacionGeneral() { return calificacionGeneral; }
    public void setCalificacionGeneral(double calificacionGeneral) { 
        this.calificacionGeneral = calificacionGeneral; 
    }
    
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    
    public boolean isRecomendaria() { return recomendaria; }
    public void setRecomendaria(boolean recomendaria) { this.recomendaria = recomendaria; }
    
    public Timestamp getFechaCalificacion() { return fechaCalificacion; }
    public void setFechaCalificacion(Timestamp fechaCalificacion) { 
        this.fechaCalificacion = fechaCalificacion; 
    }
    
    public int getUtilParaOtros() { return utilParaOtros; }
    public void setUtilParaOtros(int utilParaOtros) { this.utilParaOtros = utilParaOtros; }
    
    @Override
    public String toString() {
        return String.format("Calificación: %.1f %s | %s | %s", 
                           calificacionGeneral, 
                           generarEstrellas(calificacionGeneral),
                           getDescripcionCalificacion(),
                           recomendaria ? "RECOMENDADO" : "NO RECOMENDADO");
    }
}
