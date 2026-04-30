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
public class Usuario {
    private int id;
    private String nombreCompleto;
    private String clave;
    private String correoElectronico;
    private Date fechaCreacion;
    
    //Contructores
    public Usuario() {}
    
    public Usuario (int id, String nombreCompleto, String clave, String correoElectronico, Date fechaCreacion){
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.clave = clave;
        this.correoElectronico = correoElectronico;
        this.fechaCreacion = fechaCreacion;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreUsuario) { this.nombreCompleto = nombreCompleto; }
    
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String email) { this.correoElectronico = email; }
    
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
