/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import dao.UsuarioDao;
import model.Usuario;

/**
 *
 * @author herma
 */
public class UsuarioService {
    private UsuarioDao usuarioDAO;
    
    public UsuarioService() {
        this.usuarioDAO = new UsuarioDao();
    }
    
    public Usuario login(String nombreUsuario, String contrasenia) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        
        Usuario usuario = usuarioDAO.validarLogin(nombreUsuario, contrasenia);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }
        return usuario;
    }
}
