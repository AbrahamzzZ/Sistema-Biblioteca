/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import dao.PrestamoDao;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import model.Prestamo;

/**
 *
 * @author herma
 */
public class PrestamoService {
    private PrestamoDao prestamoDao;
    
    public PrestamoService() {
        this.prestamoDao = new PrestamoDao();
    }
    
    /**
     * Listar todos los préstamos
     */
    public List<Prestamo> listarPrestamos() {
        return prestamoDao.listarTodos();
    }
    
    /**
     * Obtener un préstamo completo por su ID
     */
    public Prestamo obtenerPrestamo(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de préstamo inválido");
        }
        return prestamoDao.obtenerPorId(id);
    }
    
    /**
     * Registrar un nuevo préstamo
     * @param idCliente ID del cliente
     * @param diasPrestamo Días para la devolución (máximo 30)
     * @param librosIds Lista de IDs de libros a prestar
     * @return ID del préstamo registrado, -1 si hay error
     */
    public int registrarPrestamo(int idCliente, int diasPrestamo, List<Integer> librosIds) {
        // Validaciones
        if (idCliente <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un cliente");
        }
        if (librosIds == null || librosIds.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un libro");
        }
        if (diasPrestamo <= 0) {
            throw new IllegalArgumentException("Los días de préstamo deben ser mayores a 0");
        }
        if (diasPrestamo > 30) {
            throw new IllegalArgumentException("El préstamo no puede exceder los 30 días");
        }
        
        // Calcular fecha de devolución esperada
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, diasPrestamo);
        Date fechaDevolucion = cal.getTime();
        
        return prestamoDao.registrar(idCliente, fechaDevolucion, librosIds);
    }
    
    /**
     * Registrar devolución de uno o varios libros
     * @param idPrestamo ID del préstamo
     * @param librosIds Lista de IDs de libros a devolver
     */
    public boolean registrarDevolucion(int idPrestamo, List<Integer> librosIds) {
        if (idPrestamo <= 0) {
            throw new IllegalArgumentException("ID de préstamo inválido");
        }
        if (librosIds == null || librosIds.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un libro para devolver");
        }
        return prestamoDao.registrarDevolucion(idPrestamo, librosIds);
    }
    
    /**
     * Actualizar la fecha de devolución esperada
     */
    public boolean actualizarFechaDevolucion(int idPrestamo, Date nuevaFecha) {
        if (idPrestamo <= 0) {
            throw new IllegalArgumentException("ID de préstamo inválido");
        }
        if (nuevaFecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        if (nuevaFecha.before(new Date())) {
            throw new IllegalArgumentException("La nueva fecha debe ser posterior a hoy");
        }
        return prestamoDao.actualizarFechaDevolucion(idPrestamo, nuevaFecha);
    }
}
