/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;
public class Equipos {
    private int idEquipos;
    private int serviciosIdServicios;
    private String nombre;
    private String tipo;
    private String estado;

    public Equipos(int idEquipos, int serviciosIdServicios, String nombre, String tipo, String estado) {
        this.idEquipos = idEquipos;
        this.serviciosIdServicios = serviciosIdServicios;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
    }

    public int getIdEquipos() { return idEquipos; }
    public void setIdEquipos(int idEquipos) { this.idEquipos = idEquipos; }
    public int getServiciosIdServicios() { return serviciosIdServicios; }
    public void setServiciosIdServicios(int serviciosIdServicios) { this.serviciosIdServicios = serviciosIdServicios; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}