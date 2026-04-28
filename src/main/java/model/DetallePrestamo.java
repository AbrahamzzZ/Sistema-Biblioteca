/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author herma
 */
public class DetallePrestamo {
    private int idLibro;
    private String tituloLibro;
    private String estadoLibro;
    private Date fechaRealDevolucionLibro;
    
    public int getIdLibro() { return idLibro; }
    public void setIdLibro(int idLibro) { this.idLibro = idLibro; }
    
    public String getTituloLibro() { return tituloLibro; }
    public void setTituloLibro(String tituloLibro) { this.tituloLibro = tituloLibro; }
    
    public String getEstadoLibro() { return estadoLibro; }
    public void setEstadoLibro(String estadoLibro) { this.estadoLibro = estadoLibro; }
    
    public Date getFechaRealDevolucionLibro() { return fechaRealDevolucionLibro; }
    public void setFechaRealDevolucionLibro(Date fechaRealDevolucionLibro) { this.fechaRealDevolucionLibro = fechaRealDevolucionLibro; } 
}
