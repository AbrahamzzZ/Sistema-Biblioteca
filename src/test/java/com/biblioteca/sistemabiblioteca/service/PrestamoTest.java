/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.biblioteca.sistemabiblioteca.service;

import dao.PrestamoDao;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import model.Prestamo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import services.PrestamoService;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


/**
 * Pruebas unitarias para PrestamoService
 */
@ExtendWith(MockitoExtension.class)
public class PrestamoTest {
    
    @Mock
    private PrestamoDao prestamoDao;  // Simulamos el DAO
    
    @InjectMocks
    private PrestamoService prestamoService;  // Inyectamos el mock
    
    private Prestamo prestamoValido;
    private List<Integer> librosIdsValidos;
    private List<Integer> librosIdsVacios;
    
    @BeforeEach
    void setUp() {
        // Configurar préstamo válido para pruebas
        prestamoValido = new Prestamo();
        prestamoValido.setId(1);
        prestamoValido.setIdCliente(1);
        prestamoValido.setNombreCliente("Juan Pérez");
        prestamoValido.setFechaPrestamo(new Date());
        prestamoValido.setFechaDevolucionEsperada(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L));
        prestamoValido.setEstado("ACTIVO");
        
        // Lista de IDs de libros válidos
        librosIdsValidos = Arrays.asList(1, 2, 3);
        
        // Lista vacía
        librosIdsVacios = Arrays.asList();
    }
    
    // ==================== PRUEBAS PARA listarPrestamos ====================
    
    @Test
    @DisplayName("Listar préstamos - Debe retornar lista de préstamos")
    void testListarPrestamos() {
        // Arrange
        List<Prestamo> prestamosEsperados = Arrays.asList(prestamoValido);
        when(prestamoDao.listarTodos()).thenReturn(prestamosEsperados);
        
        // Act
        List<Prestamo> resultado = prestamoService.listarPrestamos();
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getNombreCliente());
        verify(prestamoDao, times(1)).listarTodos();
    }
    
    @Test
    @DisplayName("Listar préstamos - Cuando no hay préstamos retorna lista vacía")
    void testListarPrestamosVacio() {
        // Arrange
        when(prestamoDao.listarTodos()).thenReturn(Arrays.asList());
        
        // Act
        List<Prestamo> resultado = prestamoService.listarPrestamos();
        
        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(prestamoDao, times(1)).listarTodos();
    }
    
    // ==================== PRUEBAS PARA obtenerPrestamo ====================
    
    @Test
    @DisplayName("Obtener préstamo por ID - ID válido debe retornar préstamo")
    void testObtenerPrestamoPorIdValido() {
        // Arrange
        when(prestamoDao.obtenerPorId(1)).thenReturn(prestamoValido);
        
        // Act
        Prestamo resultado = prestamoService.obtenerPrestamo(1);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("ACTIVO", resultado.getEstado());
        verify(prestamoDao, times(1)).obtenerPorId(1);
    }
    
    @Test
    @DisplayName("Obtener préstamo por ID - ID inválido debe lanzar excepción")
    void testObtenerPrestamoPorIdInvalido() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.obtenerPrestamo(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.obtenerPrestamo(-5);
        });
        
        verify(prestamoDao, never()).obtenerPorId(anyInt());
    }
    
    @Test
    @DisplayName("Obtener préstamo por ID - ID no existente retorna null")
    void testObtenerPrestamoPorIdNoExistente() {
        // Arrange
        when(prestamoDao.obtenerPorId(999)).thenReturn(null);
        
        // Act
        Prestamo resultado = prestamoService.obtenerPrestamo(999);
        
        // Assert
        assertNull(resultado);
        verify(prestamoDao, times(1)).obtenerPorId(999);
    }
    
    // ==================== PRUEBAS PARA registrarPrestamo ====================
    
    @Test
    @DisplayName("Registrar préstamo - Datos válidos debe registrar exitosamente")
    void testRegistrarPrestamoValido() {
        // Arrange
        when(prestamoDao.registrar(anyInt(), any(Date.class), anyList())).thenReturn(1);
        
        // Act
        int id = prestamoService.registrarPrestamo(1, 7, librosIdsValidos);
        
        // Assert
        assertEquals(1, id);
        verify(prestamoDao, times(1)).registrar(eq(1), any(Date.class), eq(librosIdsValidos));
    }
    
    @Test
    @DisplayName("Registrar préstamo - ID de cliente inválido debe lanzar excepción")
    void testRegistrarPrestamoClienteInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.registrarPrestamo(0, 7, librosIdsValidos);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.registrarPrestamo(-5, 7, librosIdsValidos);
        });
        
        verify(prestamoDao, never()).registrar(anyInt(), any(Date.class), anyList());
    }
    
    @Test
    @DisplayName("Registrar préstamo - Lista de libros vacía debe lanzar excepción")
    void testRegistrarPrestamoListaLibrosVacia() {
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.registrarPrestamo(1, 7, librosIdsVacios);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.registrarPrestamo(1, 7, null);
        });
        
        verify(prestamoDao, never()).registrar(anyInt(), any(Date.class), anyList());
    }
    
    @Test
    @DisplayName("Registrar préstamo - Días de préstamo inválidos debe lanzar excepción")
    void testRegistrarPrestamoDiasInvalidos() {
        // Días <= 0
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.registrarPrestamo(1, 0, librosIdsValidos);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.registrarPrestamo(1, -5, librosIdsValidos);
        });
        
        // Días > 30
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.registrarPrestamo(1, 31, librosIdsValidos);
        });
        
        verify(prestamoDao, never()).registrar(anyInt(), any(Date.class), anyList());
    }
    
    @Test
    @DisplayName("Registrar préstamo - Error al registrar retorna -1")
    void testRegistrarPrestamoError() {
        // Arrange
        when(prestamoDao.registrar(anyInt(), any(Date.class), anyList())).thenReturn(-1);
        
        // Act
        int id = prestamoService.registrarPrestamo(1, 7, librosIdsValidos);
        
        // Assert
        assertEquals(-1, id);
        verify(prestamoDao, times(1)).registrar(anyInt(), any(Date.class), anyList());
    }
    
    // ==================== PRUEBAS PARA registrarDevolucion ====================
    
    @Test
    @DisplayName("Registrar devolución - Datos válidos debe devolver true")
    void testRegistrarDevolucionValido() {
        // Arrange
        when(prestamoDao.registrarDevolucion(1, librosIdsValidos)).thenReturn(true);
        
        // Act
        boolean resultado = prestamoService.registrarDevolucion(1, librosIdsValidos);
        
        // Assert
        assertTrue(resultado);
        verify(prestamoDao, times(1)).registrarDevolucion(1, librosIdsValidos);
    }
    
    @Test
    @DisplayName("Registrar devolución - ID de préstamo inválido debe lanzar excepción")
    void testRegistrarDevolucionIdInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.registrarDevolucion(0, librosIdsValidos);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.registrarDevolucion(-5, librosIdsValidos);
        });
        
        verify(prestamoDao, never()).registrarDevolucion(anyInt(), anyList());
    }
    
    @Test
    @DisplayName("Registrar devolución - Lista de libros vacía debe lanzar excepción")
    void testRegistrarDevolucionListaLibrosVacia() {
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.registrarDevolucion(1, librosIdsVacios);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.registrarDevolucion(1, null);
        });
        
        verify(prestamoDao, never()).registrarDevolucion(anyInt(), anyList());
    }
    
    @Test
    @DisplayName("Registrar devolución - Fallo en devolución retorna false")
    void testRegistrarDevolucionFallo() {
        // Arrange
        when(prestamoDao.registrarDevolucion(1, librosIdsValidos)).thenReturn(false);
        
        // Act
        boolean resultado = prestamoService.registrarDevolucion(1, librosIdsValidos);
        
        // Assert
        assertFalse(resultado);
        verify(prestamoDao, times(1)).registrarDevolucion(1, librosIdsValidos);
    }
    
    // ==================== PRUEBAS PARA actualizarFechaDevolucion ====================
    
    @Test
    @DisplayName("Actualizar fecha devolución - Fecha válida debe actualizar exitosamente")
    void testActualizarFechaDevolucionValido() {
        // Arrange
        Date nuevaFecha = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L);
        when(prestamoDao.actualizarFechaDevolucion(1, nuevaFecha)).thenReturn(true);
        
        // Act
        boolean resultado = prestamoService.actualizarFechaDevolucion(1, nuevaFecha);
        
        // Assert
        assertTrue(resultado);
        verify(prestamoDao, times(1)).actualizarFechaDevolucion(1, nuevaFecha);
    }
    
    @Test
    @DisplayName("Actualizar fecha devolución - ID inválido debe lanzar excepción")
    void testActualizarFechaDevolucionIdInvalido() {
        Date nuevaFecha = new Date();
        
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.actualizarFechaDevolucion(0, nuevaFecha);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.actualizarFechaDevolucion(-5, nuevaFecha);
        });
        
        verify(prestamoDao, never()).actualizarFechaDevolucion(anyInt(), any(Date.class));
    }
    
    @Test
    @DisplayName("Actualizar fecha devolución - Fecha nula debe lanzar excepción")
    void testActualizarFechaDevolucionFechaNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.actualizarFechaDevolucion(1, null);
        });
        
        verify(prestamoDao, never()).actualizarFechaDevolucion(anyInt(), any(Date.class));
    }
    
    @Test
    @DisplayName("Actualizar fecha devolución - Fecha anterior a hoy debe lanzar excepción")
    void testActualizarFechaDevolucionFechaAnterior() {
        // Fecha de ayer
        Date fechaAyer = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L);
        
        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.actualizarFechaDevolucion(1, fechaAyer);
        });
        
        verify(prestamoDao, never()).actualizarFechaDevolucion(anyInt(), any(Date.class));
    }
    
    @Test
    @DisplayName("Actualizar fecha devolución - Fallo en actualización retorna false")
    void testActualizarFechaDevolucionFallo() {
        // Arrange
        Date nuevaFecha = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L);
        when(prestamoDao.actualizarFechaDevolucion(1, nuevaFecha)).thenReturn(false);
        
        // Act
        boolean resultado = prestamoService.actualizarFechaDevolucion(1, nuevaFecha);
        
        // Assert
        assertFalse(resultado);
        verify(prestamoDao, times(1)).actualizarFechaDevolucion(1, nuevaFecha);
    }
}
