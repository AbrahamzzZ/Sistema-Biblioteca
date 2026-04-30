/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;

/**
 *
 * @author herma
 */
public class ClienteDao {
     // LISTAR todos los clientes
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "{call PA_LISTAR_CLIENTES}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("ID"));
                cliente.setNombreCompleto(rs.getString("NOMBRE_COMPLETO"));
                cliente.setCedula(rs.getString("CEDULA"));
                cliente.setTelefono(rs.getString("TELEFONO"));
                cliente.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                cliente.setDireccion(rs.getString("DIRECCION"));
                cliente.setEstado(rs.getBoolean("ESTADO"));
                cliente.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return clientes;
    }
    
    // OBTENER cliente por ID
    public Cliente obtenerPorId(int id) {
        String sql = "{call PA_OBTENER_CLIENTE(?)}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("ID"));
                cliente.setNombreCompleto(rs.getString("NOMBRE_COMPLETO"));
                cliente.setCedula(rs.getString("CEDULA"));
                cliente.setTelefono(rs.getString("TELEFONO"));
                cliente.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                cliente.setDireccion(rs.getString("DIRECCION"));
                cliente.setEstado(rs.getBoolean("ESTADO"));
                cliente.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                return cliente;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cliente: " + e.getMessage());
        }
        return null;
    }
    
    // OBTENER cliente por cédula (para validar duplicados)
    public Cliente obtenerPorCedula(String cedula) {
        String sql = "{call PA_OBTENER_CLIENTE_POR_CEDULA(?)}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("ID"));
                cliente.setNombreCompleto(rs.getString("NOMBRE_COMPLETO"));
                cliente.setCedula(rs.getString("CEDULA"));
                return cliente;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por cédula: " + e.getMessage());
        }
        return null;
    }
    
    // REGISTRAR nuevo cliente
    public int registrar(Cliente cliente) {
        String sql = "{call PA_REGISTRAR_CLIENTE(?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, cliente.getNombreCompleto());
            stmt.setString(2, cliente.getCedula());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getCorreoElectronico());
            stmt.setString(5, cliente.getDireccion());
            stmt.setBoolean(6, cliente.isEstado());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar cliente: " + e.getMessage());
        }
        return -1;
    }
    
    // EDITAR cliente
    public boolean editar(Cliente cliente) {
        String sql = "{call PA_EDITAR_CLIENTE(?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, cliente.getId());
            stmt.setString(2, cliente.getNombreCompleto());
            stmt.setString(3, cliente.getCedula());
            stmt.setString(4, cliente.getTelefono());
            stmt.setString(5, cliente.getCorreoElectronico());
            stmt.setString(6, cliente.getDireccion());
            stmt.setBoolean(7, cliente.isEstado());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al editar cliente: " + e.getMessage());
        }
        return false;
    }
    
    // ELIMINAR (cambiar estado)
    public boolean eliminar(int id, boolean estado) {
        String sql = "{call PA_ELIMINAR_CLIENTE(?, ?)}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, id);
            stmt.setBoolean(2, estado);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
        }
        return false;
    }
}
