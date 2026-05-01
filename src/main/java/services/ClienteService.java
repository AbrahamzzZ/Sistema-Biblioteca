/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import dao.ClienteDao;
import java.util.List;
import model.Cliente;

/**
 *
 * @author herma
 */
public class ClienteService {
   private ClienteDao clienteDao;
    
    public ClienteService() {
        this.clienteDao = new ClienteDao();
    }
    
    /**
     * Listar todos los clientes
     */
    public List<Cliente> listarClientes() {
        return clienteDao.listarTodos();
    }
    
    /**
     * Obtener un cliente por su ID
     */
    public Cliente obtenerCliente(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de cliente inválido");
        }
        return clienteDao.obtenerPorId(id);
    }
    
    /**
     * Obtener un cliente por su cédula
     */
    public Cliente obtenerClientePorCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula es obligatoria");
        }
        return clienteDao.obtenerPorCedula(cedula);
    }
    
    /**
     * Registrar un nuevo cliente
     */
    public int registrarCliente(Cliente cliente) {
        validarCliente(cliente);
        validarCedulaUnica(cliente.getCedula());
        return clienteDao.registrar(cliente);
    }
    
    /**
     * Editar un cliente existente
     */
    public boolean editarCliente(Cliente cliente) {
        if (cliente.getId() <= 0) {
            throw new IllegalArgumentException("ID de cliente inválido");
        }
        validarCliente(cliente);
        return clienteDao.editar(cliente);
    }
    
    /**
     * Eliminar (cambiar estado a inactivo)
     */
    public boolean eliminarCliente(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de cliente inválido");
        }
        return clienteDao.eliminar(id, false);
    }
    
    /**
     * Activar un cliente
     */
    public boolean activarCliente(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de cliente inválido");
        }
        return clienteDao.eliminar(id, true);
    }
    
    /**
     * Validar los datos de un cliente
     */
    private void validarCliente(Cliente cliente) {
        if (cliente.getNombreCompleto() == null || cliente.getNombreCompleto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre completo es obligatorio");
        }
        if (cliente.getCedula() == null || cliente.getCedula().trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula es obligatoria");
        }
        if (cliente.getCedula().length() < 7 || cliente.getCedula().length() > 10) {
            throw new IllegalArgumentException("La cédula debe tener entre 7 y 10 dígitos");
        }
        if (cliente.getCorreoElectronico() != null && !cliente.getCorreoElectronico().isEmpty()) {
            if (!cliente.getCorreoElectronico().contains("@")) {
                throw new IllegalArgumentException("El correo electrónico no es válido");
            }
        }
    }
    
    /**
     * Validar que la cédula no esté registrada en otro cliente
     */
    private void validarCedulaUnica(String cedula) {
        Cliente existente = clienteDao.obtenerPorCedula(cedula);
        if (existente != null) {
            throw new IllegalArgumentException("Ya existe un cliente con la cédula: " + cedula);
        }
    }  
}
