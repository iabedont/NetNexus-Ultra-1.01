/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;
import java.sql.Date;

public class Contrato {
    private int idContrato;
    private int clienteIdCliente;
    private Date fechaInicio;
    private Date fechaFin;
    private double montoTotal;

    public Contrato(int idContrato, int clienteIdCliente, Date fechaInicio, Date fechaFin, double montoTotal) {
        this.idContrato = idContrato;
        this.clienteIdCliente = clienteIdCliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.montoTotal = montoTotal;
    }

    public int getIdContrato() { return idContrato; }
    public void setIdContrato(int idContrato) { this.idContrato = idContrato; }
    public int getClienteIdCliente() { return clienteIdCliente; }
    public void setClienteIdCliente(int clienteIdCliente) { this.clienteIdCliente = clienteIdCliente; }
    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }
    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }
    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }
}
