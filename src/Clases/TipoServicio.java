/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;
public class TipoServicio {
    private int idTipoServicio;
    private String nombre;
    private String descripcion;

    public TipoServicio(int idTipoServicio, String nombre, String descripcion) {
        this.idTipoServicio = idTipoServicio;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdTipoServicio() { return idTipoServicio; }
    public void setIdTipoServicio(int idTipoServicio) { this.idTipoServicio = idTipoServicio; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
