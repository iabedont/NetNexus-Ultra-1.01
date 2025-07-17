/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;
import java.sql.Date;

public class Factura {
    private int idFactura;
    private Date fechaFactura;
    private double monto;
    private String estadoPago;
    private String metodoPago;

    public Factura(int idFactura, Date fechaFactura, double monto, String estadoPago, String metodoPago) {
        this.idFactura = idFactura;
        this.fechaFactura = fechaFactura;
        this.monto = monto;
        this.estadoPago = estadoPago;
        this.metodoPago = metodoPago;
    }

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }
    public Date getFechaFactura() { return fechaFactura; }
    public void setFechaFactura(Date fechaFactura) { this.fechaFactura = fechaFactura; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}