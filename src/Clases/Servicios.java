/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;
import java.sql.Date;

public class Servicios {
    private int idServicios;
    private int contratoIdContrato;
    private int tecnicosIdTecnicos;
    private int tipoServicioIdTipoServicio;
    private Date fechaServicio;
    private String descripcion;
    private String estado;

    public Servicios(int idServicios, int contratoIdContrato, int tecnicosIdTecnicos, int tipoServicioIdTipoServicio, Date fechaServicio, String descripcion, String estado) {
        this.idServicios = idServicios;
        this.contratoIdContrato = contratoIdContrato;
        this.tecnicosIdTecnicos = tecnicosIdTecnicos;
        this.tipoServicioIdTipoServicio = tipoServicioIdTipoServicio;
        this.fechaServicio = fechaServicio;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public int getIdServicios() { return idServicios; }
    public void setIdServicios(int idServicios) { this.idServicios = idServicios; }
    public int getContratoIdContrato() { return contratoIdContrato; }
    public void setContratoIdContrato(int contratoIdContrato) { this.contratoIdContrato = contratoIdContrato; }
    public int getTecnicosIdTecnicos() { return tecnicosIdTecnicos; }
    public void setTecnicosIdTecnicos(int tecnicosIdTecnicos) { this.tecnicosIdTecnicos = tecnicosIdTecnicos; }
    public int getTipoServicioIdTipoServicio() { return tipoServicioIdTipoServicio; }
    public void setTipoServicioIdTipoServicio(int tipoServicioIdTipoServicio) { this.tipoServicioIdTipoServicio = tipoServicioIdTipoServicio; }
    public Date getFechaServicio() { return fechaServicio; }
    public void setFechaServicio(Date fechaServicio) { this.fechaServicio = fechaServicio; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
