/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.DetallePrestamo;
import model.Prestamo;
import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author herma
 */
public class PrestamoDao {
     // LISTAR todos los préstamos (cabecera)
    public List<Prestamo> listarTodos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "{call PA_LISTAR_PRESTAMOS}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Prestamo prestamo = new Prestamo();
                prestamo.setId(rs.getInt("ID_PRESTAMO"));
                prestamo.setNombreCliente(rs.getString("CLIENTE"));
                prestamo.setFechaPrestamo(rs.getTimestamp("FECHA_PRESTAMO"));
                prestamo.setFechaDevolucionEsperada(rs.getDate("FECHA_DEVOLUCION_ESPERADA"));
                prestamo.setFechaRealDevolucion(rs.getDate("FECHA_REAL_DEVOLUCION"));
                prestamo.setEstado(rs.getString("ESTADO"));
                prestamos.add(prestamo);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar préstamos: " + e.getMessage());
        }
        return prestamos;
    }
    
    // OBTENER préstamo completo (cabecera + detalles)
    public Prestamo obtenerPorId(int idPrestamo) {
        Prestamo prestamo = null;
        String sql = "{call PA_OBTENER_PRESTAMO(?)}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, idPrestamo);
            boolean results = stmt.execute();
            
            // Primer resultado: cabecera
            if (results) {
                ResultSet rs = stmt.getResultSet();
                if (rs.next()) {
                    prestamo = new Prestamo();
                    prestamo.setId(rs.getInt("ID"));
                    prestamo.setIdCliente(rs.getInt("ID_CLIENTE"));
                    prestamo.setNombreCliente(rs.getString("CLIENTE"));
                    prestamo.setFechaPrestamo(rs.getTimestamp("FECHA_PRESTAMO"));
                    prestamo.setFechaDevolucionEsperada(rs.getDate("FECHA_DEVOLUCION_ESPERADA"));
                    prestamo.setFechaRealDevolucion(rs.getDate("FECHA_REAL_DEVOLUCION"));
                    prestamo.setEstado(rs.getString("ESTADO"));
                }
                rs.close();
                
                // Segundo resultado: detalles (libros)
                if (stmt.getMoreResults()) {
                    ResultSet rsDetalle = stmt.getResultSet();
                    List<DetallePrestamo> detalles = new ArrayList<>();
                    while (rsDetalle.next()) {
                        DetallePrestamo detalle = new DetallePrestamo();
                        detalle.setIdLibro(rsDetalle.getInt("ID_LIBRO"));
                        detalle.setTituloLibro(rsDetalle.getString("TITULO"));
                        detalle.setEstadoLibro(rsDetalle.getString("ESTADO_LIBRO"));
                        detalle.setFechaRealDevolucionLibro(rsDetalle.getDate("FECHA_REAL_DEVOLUCION_LIBRO"));
                        detalles.add(detalle);
                    }
                    if (prestamo != null) {
                        prestamo.setDetalles(detalles);
                    }
                    rsDetalle.close();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener préstamo: " + e.getMessage());
        }
        return prestamo;
    }
    
    // REGISTRAR nuevo préstamo (con múltiples libros)
    public int registrar(int idCliente, Date fechaDevolucionEsperada, List<Integer> librosIds) {
        // Convertir lista a string separado por comas: "1,2,3"
        String librosStr = librosIds.toString().replace("[", "").replace("]", "").replace(" ", "");
        String sql = "{call PA_REGISTRAR_PRESTAMO(?, ?, ?)}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, idCliente);
            stmt.setDate(2, new java.sql.Date(fechaDevolucionEsperada.getTime()));
            stmt.setString(3, librosStr);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String mensaje = rs.getString("Mensaje");
                if (mensaje.contains("ERROR")) {
                    System.err.println(mensaje);
                    return -1;
                }
                return rs.getInt("ID_Prestamo");
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar préstamo: " + e.getMessage());
        }
        return -1;
    }
    
    // REGISTRAR devolución de libros
    public boolean registrarDevolucion(int idPrestamo, List<Integer> librosIds) {
        String librosStr = librosIds.toString().replace("[", "").replace("]", "").replace(" ", "");
        String sql = "{call PA_REGISTRAR_DEVOLUCION(?, ?)}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, idPrestamo);
            stmt.setString(2, librosStr);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar devolución: " + e.getMessage());
        }
        return false;
    }
    
    // ACTUALIZAR fecha de devolución esperada
    public boolean actualizarFechaDevolucion(int idPrestamo, Date nuevaFecha) {
        String sql = "{call PA_ACTUALIZAR_FECHA_DEVOLUCION(?, ?)}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, idPrestamo);
            stmt.setDate(2, new java.sql.Date(nuevaFecha.getTime()));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar fecha: " + e.getMessage());
        }
        return false;
    }
}
