/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;
public class Tecnicos {
    private int idTecnicos;
    private int vehiculosIdVehiculos;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String especialidad;

    public Tecnicos(int idTecnicos, int vehiculosIdVehiculos, String nombre, String apellido, String telefono, String email, String especialidad) {
        this.idTecnicos = idTecnicos;
        this.vehiculosIdVehiculos = vehiculosIdVehiculos;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.especialidad = especialidad;
    }

    public int getIdTecnicos() { return idTecnicos; }
    public void setIdTecnicos(int idTecnicos) { this.idTecnicos = idTecnicos; }
    public int getVehiculosIdVehiculos() { return vehiculosIdVehiculos; }
    public void setVehiculosIdVehiculos(int vehiculosIdVehiculos) { this.vehiculosIdVehiculos = vehiculosIdVehiculos; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}