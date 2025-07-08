/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;
public class Vehiculos {
    private int idVehiculos;
    private String modelo;
    private String placa;
    private String color;
    private String estado;

    public Vehiculos(int idVehiculos, String modelo, String placa, String color, String estado) {
        this.idVehiculos = idVehiculos;
        this.modelo = modelo;
        this.placa = placa;
        this.color = color;
        this.estado = estado;
    }

    public int getIdVehiculos() { return idVehiculos; }
    public void setIdVehiculos(int idVehiculos) { this.idVehiculos = idVehiculos; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}