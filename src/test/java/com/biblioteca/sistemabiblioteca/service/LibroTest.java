/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.biblioteca.sistemabiblioteca.service;

import dao.LibroDao;
import java.util.Arrays;
import java.util.List;
import model.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import services.LibroService;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Pruebas unitarias para LibroService
 */
@ExtendWith(MockitoExtension.class)
public class LibroTest {
    
    @Mock
    private LibroDao libroDao;  // Simulamos el DAO
    
    @InjectMocks
    private LibroService libroService;  // Inyectamos el mock
    
    private Libro libroValido;
    private Libro libroInvalido;
    
    @BeforeEach
    void setUp() {
        // Configurar libro válido para pruebas
        libroValido = new Libro();
        libroValido.setId(1);
        libroValido.setTitulo("Cien años de soledad");
        libroValido.setAutor("Gabriel García Márquez");
        libroValido.setAnioPublicacion(1967);
        libroValido.setEditorial("Sudamericana");
        libroValido.setCantidad(5);
        libroValido.setEstado(true);
        
        // Configurar libro inválido
        libroInvalido = new Libro();
        libroInvalido.setTitulo("");
        libroInvalido.setAutor("");
        libroInvalido.setCantidad(-1);
        libroInvalido.setAnioPublicacion(-5);
    }
    
    // ==================== PRUEBAS PARA listarLibros ====================
    
    @Test
    @DisplayName("Listar libros - Debe retornar lista de libros")
    void testListarLibros() {
        // Arrange
        List<Libro> librosEsperados = Arrays.asList(libroValido);
        when(libroDao.listarTodos()).thenReturn(librosEsperados);
        
        // Act
        List<Libro> resultado = libroService.listarLibros();
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Cien años de soledad", resultado.get(0).getTitulo());
        verify(libroDao, times(1)).listarTodos();
    }
    
    @Test
    @DisplayName("Listar libros - Cuando no hay libros retorna lista vacía")
    void testListarLibrosVacio() {
        // Arrange
        when(libroDao.listarTodos()).thenReturn(Arrays.asList());
        
        // Act
        List<Libro> resultado = libroService.listarLibros();
        
        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(libroDao, times(1)).listarTodos();
    }
    
    // ==================== PRUEBAS PARA obtenerLibro ====================
    
    @Test
    @DisplayName("Obtener libro por ID - ID válido debe retornar libro")
    void testObtenerLibroPorIdValido() {
        // Arrange
        when(libroDao.obtenerPorId(1)).thenReturn(libroValido);
        
        // Act
        Libro resultado = libroService.obtenerLibro(1);
        
        // Assert
        assertNotNull(resultado);
        assertEquals("Cien años de soledad", resultado.getTitulo());
        assertEquals("Gabriel García Márquez", resultado.getAutor());
        verify(libroDao, times(1)).obtenerPorId(1);
    }
    
    @Test
    @DisplayName("Obtener libro por ID - ID inválido debe lanzar excepción")
    void testObtenerLibroPorIdInvalido() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            libroService.obtenerLibro(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            libroService.obtenerLibro(-5);
        });
        
        verify(libroDao, never()).obtenerPorId(anyInt());
    }
    
    @Test
    @DisplayName("Obtener libro por ID - ID no existente retorna null")
    void testObtenerLibroPorIdNoExistente() {
        // Arrange
        when(libroDao.obtenerPorId(999)).thenReturn(null);
        
        // Act
        Libro resultado = libroService.obtenerLibro(999);
        
        // Assert
        assertNull(resultado);
        verify(libroDao, times(1)).obtenerPorId(999);
    }
    
    // ==================== PRUEBAS PARA registrarLibro ====================
    
    @Test
    @DisplayName("Registrar libro - Libro válido debe registrar exitosamente")
    void testRegistrarLibroValido() {
        // Arrange
        when(libroDao.registrar(any(Libro.class))).thenReturn(1);
        
        // Act
        int id = libroService.registrarLibro(libroValido);
        
        // Assert
        assertEquals(1, id);
        verify(libroDao, times(1)).registrar(libroValido);
    }
    
    @Test
    @DisplayName("Registrar libro - Título vacío debe lanzar excepción")
    void testRegistrarLibroTituloVacio() {
        libroInvalido.setTitulo("");
        libroInvalido.setAutor("Autor válido");
        libroInvalido.setCantidad(5);
        
        assertThrows(IllegalArgumentException.class, () -> {
            libroService.registrarLibro(libroInvalido);
        });
        
        verify(libroDao, never()).registrar(any());
    }
    
    @Test
    @DisplayName("Registrar libro - Autor vacío debe lanzar excepción")
    void testRegistrarLibroAutorVacio() {
        libroInvalido.setTitulo("Título válido");
        libroInvalido.setAutor("");
        libroInvalido.setCantidad(5);
        
        assertThrows(IllegalArgumentException.class, () -> {
            libroService.registrarLibro(libroInvalido);
        });
        
        verify(libroDao, never()).registrar(any());
    }
    
    @Test
    @DisplayName("Registrar libro - Cantidad negativa debe lanzar excepción")
    void testRegistrarLibroCantidadNegativa() {
        libroValido.setCantidad(-5);
        
        assertThrows(IllegalArgumentException.class, () -> {
            libroService.registrarLibro(libroValido);
        });
        
        verify(libroDao, never()).registrar(any());
    }
    
    @Test
    @DisplayName("Registrar libro - Año inválido (negativo) debe lanzar excepción")
    void testRegistrarLibroAnioNegativo() {
        libroValido.setAnioPublicacion(-1);
        
        assertThrows(IllegalArgumentException.class, () -> {
            libroService.registrarLibro(libroValido);
        });
        
        verify(libroDao, never()).registrar(any());
    }
    
    @Test
    @DisplayName("Registrar libro - Año inválido (mayor a 2026) debe lanzar excepción")
    void testRegistrarLibroAnioFuturo() {
        libroValido.setAnioPublicacion(2100);
        
        assertThrows(IllegalArgumentException.class, () -> {
            libroService.registrarLibro(libroValido);
        });
        
        verify(libroDao, never()).registrar(any());
    }
    
    // ==================== PRUEBAS PARA editarLibro ====================
    
    @Test
    @DisplayName("Editar libro - Libro válido debe editar exitosamente")
    void testEditarLibroValido() {
        // Arrange
        when(libroDao.editar(libroValido)).thenReturn(true);
        
        // Act
        boolean resultado = libroService.editarLibro(libroValido);
        
        // Assert
        assertTrue(resultado);
        verify(libroDao, times(1)).editar(libroValido);
    }
    
    @Test
    @DisplayName("Editar libro - ID inválido debe lanzar excepción")
    void testEditarLibroIdInvalido() {
        libroValido.setId(0);
        
        assertThrows(IllegalArgumentException.class, () -> {
            libroService.editarLibro(libroValido);
        });
        
        libroValido.setId(-5);
        assertThrows(IllegalArgumentException.class, () -> {
            libroService.editarLibro(libroValido);
        });
        
        verify(libroDao, never()).editar(any());
    }
    
    @Test
    @DisplayName("Editar libro - Fallo en edición retorna false")
    void testEditarLibroFallo() {
        // Arrange
        when(libroDao.editar(libroValido)).thenReturn(false);
        
        // Act
        boolean resultado = libroService.editarLibro(libroValido);
        
        // Assert
        assertFalse(resultado);
        verify(libroDao, times(1)).editar(libroValido);
    }
    
    // ==================== PRUEBAS PARA eliminarLibro ====================
    
    @Test
    @DisplayName("Eliminar libro - ID válido debe eliminar exitosamente")
    void testEliminarLibroValido() {
        // Arrange
        when(libroDao.eliminar(1, false)).thenReturn(true);
        
        // Act
        boolean resultado = libroService.eliminarLibro(1);
        
        // Assert
        assertTrue(resultado);
        verify(libroDao, times(1)).eliminar(1, false);
    }
    
    @Test
    @DisplayName("Eliminar libro - ID inválido debe lanzar excepción")
    void testEliminarLibroIdInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            libroService.eliminarLibro(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            libroService.eliminarLibro(-5);
        });
        
        verify(libroDao, never()).eliminar(anyInt(), anyBoolean());
    }
    
    // ==================== PRUEBAS PARA activarLibro ====================
    
    @Test
    @DisplayName("Activar libro - ID válido debe activar exitosamente")
    void testActivarLibroValido() {
        // Arrange
        when(libroDao.eliminar(1, true)).thenReturn(true);
        
        // Act
        boolean resultado = libroService.activarLibro(1);
        
        // Assert
        assertTrue(resultado);
        verify(libroDao, times(1)).eliminar(1, true);
    }
    
    @Test
    @DisplayName("Activar libro - ID inválido debe lanzar excepción")
    void testActivarLibroIdInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            libroService.activarLibro(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            libroService.activarLibro(-5);
        });
        
        verify(libroDao, never()).eliminar(anyInt(), anyBoolean());
    }
}