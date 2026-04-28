/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author herma
 */
public class Prestamo {
    private int id;
    private int idCliente;
    private String nombreCliente;
    private Date fechaPrestamo;
    private Date fechaDevolucionEsperada;
    private Date fechaRealDevolucion;
    private String estado;
    private List<DetallePrestamo> detalles;
      
    // Constructores
    public Prestamo() {}
    
    public Prestamo(int id, int idCliente, Date fechaPrestamo, Date fechaDevolucionEsperada, Date fechaRealDevolucion, String estado) {
        this.id = id;
        this.idCliente = idCliente;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
        this.fechaRealDevolucion = fechaRealDevolucion;
        this.estado = estado;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    
    public Date getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(Date fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }
    
    public Date getFechaDevolucionEsperada() { return fechaDevolucionEsperada; }
    public void setFechaDevolucionEsperada(Date fechaDevolucionEsperada) { this.fechaDevolucionEsperada = fechaDevolucionEsperada; }
    
    public Date getFechaRealDevolucion() { return fechaRealDevolucion; }
    public void setFechaRealDevolucion(Date fechaRealDevolucion) { this.fechaRealDevolucion = fechaRealDevolucion; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public List<DetallePrestamo> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePrestamo> detalles) { this.detalles = detalles; }
}
