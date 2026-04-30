/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Libro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author herma
 */
public class LibroDao {
    public List<Libro> listarTodos() {
        List<Libro> libros = new ArrayList<>();
        String sql = "{call PA_LISTAR_LIBROS}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Libro libro = new Libro();
                libro.setId(rs.getInt("ID"));
                libro.setTitulo(rs.getString("TITULO"));
                libro.setAutor(rs.getString("AUTOR"));
                libro.setAnioPublicacion(rs.getInt("AÑO_PUBLICACION"));
                libro.setEditorial(rs.getString("EDITORIAL"));
                libro.setCantidad(rs.getInt("CANTIDAD"));
                libro.setEstado(rs.getBoolean("ESTADO"));
                libros.add(libro);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar libros: " + e.getMessage());
        }
        return libros;
    }
    
    // OBTENER libro por ID
    public Libro obtenerPorId(int id) {
        String sql = "{call PA_OBTENER_LIBRO(?)}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Libro libro = new Libro();
                libro.setId(rs.getInt("ID"));
                libro.setTitulo(rs.getString("TITULO"));
                libro.setAutor(rs.getString("AUTOR"));
                libro.setAnioPublicacion(rs.getInt("AÑO_PUBLICACION"));
                libro.setEditorial(rs.getString("EDITORIAL"));
                libro.setCantidad(rs.getInt("CANTIDAD"));
                libro.setEstado(rs.getBoolean("ESTADO"));
                return libro;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener libro: " + e.getMessage());
        }
        return null;
    }
    
    // REGISTRAR nuevo libro
    public int registrar(Libro libro) {
        String sql = "{call PA_REGISTRAR_LIBRO(?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setInt(3, libro.getAnioPublicacion());
            stmt.setString(4, libro.getEditorial());
            stmt.setInt(5, libro.getCantidad());
            stmt.setBoolean(6, libro.isEstado());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar libro: " + e.getMessage());
        }
        return -1;
    }
    
    // EDITAR libro
    public boolean editar(Libro libro) {
        String sql = "{call PA_EDITAR_LIBRO(?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, libro.getId());
            stmt.setString(2, libro.getTitulo());
            stmt.setString(3, libro.getAutor());
            stmt.setInt(4, libro.getAnioPublicacion());
            stmt.setString(5, libro.getEditorial());
            stmt.setInt(6, libro.getCantidad());
            stmt.setBoolean(7, libro.isEstado());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al editar libro: " + e.getMessage());
        }
        return false;
    }
    
    // ELIMINAR (cambiar estado)
    public boolean eliminar(int id, boolean estado) {
        String sql = "{call PA_ELIMINAR_LIBRO(?, ?)}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, id);
            stmt.setBoolean(2, estado);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
        }
        return false;
    }
}
