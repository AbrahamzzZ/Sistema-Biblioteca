/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Usuario;
import java.sql.*;

/**
 *
 * @author herma
 */
public class UsuarioDao {
    // VALIDAR credenciales para login
    public Usuario validarLogin(String correoElectronico, String clave) {
        String sql = "{call PA_INICIAR_SESION(?, ?)}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, correoElectronico);
            stmt.setString(2, clave);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("ID"));
                usuario.setNombreCompleto(rs.getString("NOMBRE_COMPLETO"));
                usuario.setClave(rs.getString("CLAVE"));
                usuario.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                usuario.setFechaCreacion(rs.getTimestamp("FECHA_CREACION"));
                return usuario;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar login: " + e.getMessage());
        }
        return null;
    }
}
