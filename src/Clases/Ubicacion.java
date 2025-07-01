/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;
public class Ubicacion {
    private int idUbicacion;
    private int serviciosIdServicios;
    private String direccion;
    private String ciudad;
    private String provincia;
    private String codigoPostal;

    public Ubicacion(int idUbicacion, int serviciosIdServicios, String direccion, String ciudad, String provincia, String codigoPostal) {
        this.idUbicacion = idUbicacion;
        this.serviciosIdServicios = serviciosIdServicios;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
    }

    public int getIdUbicacion() { return idUbicacion; }
    public void setIdUbicacion(int idUbicacion) { this.idUbicacion = idUbicacion; }
    public int getServiciosIdServicios() { return serviciosIdServicios; }
    public void setServiciosIdServicios(int serviciosIdServicios) { this.serviciosIdServicios = serviciosIdServicios; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
}
