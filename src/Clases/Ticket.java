/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;
import java.sql.Date;

public class Ticket {
    private int idTicket;
    private int serviciosIdServicios;
    private Date fechaCreacion;
    private String descripcion;
    private String prioridad;
    private String estado;

    public Ticket(int idTicket, int serviciosIdServicios, Date fechaCreacion, String descripcion, String prioridad, String estado) {
        this.idTicket = idTicket;
        this.serviciosIdServicios = serviciosIdServicios;
        this.fechaCreacion = fechaCreacion;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
    }

    public int getIdTicket() { return idTicket; }
    public void setIdTicket(int idTicket) { this.idTicket = idTicket; }
    public int getServiciosIdServicios() { return serviciosIdServicios; }
    public void setServiciosIdServicios(int serviciosIdServicios) { this.serviciosIdServicios = serviciosIdServicios; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}