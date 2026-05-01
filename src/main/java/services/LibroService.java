/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import model.Libro;
import dao.LibroDao;
import java.util.List;

/**
 *
 * @author herma
 */
public class LibroService {
    private LibroDao libroDao;
    
    public LibroService() {
        this.libroDao = new LibroDao();
    }
    
    /**
     * Listar todos los libros activos
     */
    public List<Libro> listarLibros() {
        return libroDao.listarTodos();
    }
    
    /**
     * Obtener un libro por su ID
     */
    public Libro obtenerLibro(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de libro inválido");
        }
        return libroDao.obtenerPorId(id);
    }
    
    /**
     * Registrar un nuevo libro
     */
    public int registrarLibro(Libro libro) {
        validarLibro(libro);
        return libroDao.registrar(libro);
    }
    
    /**
     * Editar un libro existente
     */
    public boolean editarLibro(Libro libro) {
        if (libro.getId() <= 0) {
            throw new IllegalArgumentException("ID de libro inválido");
        }
        validarLibro(libro);
        return libroDao.editar(libro);
    }
    
    /**
     * Eliminar (cambiar estado a inactivo)
     */
    public boolean eliminarLibro(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de libro inválido");
        }
        return libroDao.eliminar(id, false);
    }
    
    /**
     * Activar un libro
     */
    public boolean activarLibro(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de libro inválido");
        }
        return libroDao.eliminar(id, true);
    }
    
    /**
     * Validar los datos de un libro
     */
    private void validarLibro(Libro libro) {
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        if (libro.getAutor() == null || libro.getAutor().trim().isEmpty()) {
            throw new IllegalArgumentException("El autor es obligatorio");
        }
        if (libro.getCantidad() < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        if (libro.getAnioPublicacion() < 0 || libro.getAnioPublicacion() > 2026) {
            throw new IllegalArgumentException("El año de publicación no es válido");
        }
    }
}
