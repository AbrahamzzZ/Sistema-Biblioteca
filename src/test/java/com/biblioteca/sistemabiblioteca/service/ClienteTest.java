/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.biblioteca.sistemabiblioteca.service;

import dao.ClienteDao;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import services.ClienteService;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


/**
 * Pruebas unitarias para ClienteService
 */
@ExtendWith(MockitoExtension.class)
public class ClienteTest {
    
    @Mock
    private ClienteDao clienteDao;  // Simulamos el DAO
    
    @InjectMocks
    private ClienteService clienteService;  // Inyectamos el mock
    
    private Cliente clienteValido;
    private Cliente clienteInvalido;
    
    @BeforeEach
    void setUp() {
        // Configurar cliente válido para pruebas
        clienteValido = new Cliente();
        clienteValido.setId(1);
        clienteValido.setNombreCompleto("Juan Pérez");
        clienteValido.setCedula("12345678");
        clienteValido.setTelefono("987654321");
        clienteValido.setCorreoElectronico("juan@example.com");
        clienteValido.setDireccion("Av. Principal 123");
        clienteValido.setEstado(true);
        clienteValido.setFechaRegistro(new Date());
        
        // Configurar cliente inválido
        clienteInvalido = new Cliente();
        clienteInvalido.setNombreCompleto("");
        clienteInvalido.setCedula("");
    }
    
    // ==================== PRUEBAS PARA listarClientes ====================
    
    @Test
    @DisplayName("Listar clientes - Debe retornar lista de clientes")
    void testListarClientes() {
        // Arrange
        List<Cliente> clientesEsperados = Arrays.asList(clienteValido);
        when(clienteDao.listarTodos()).thenReturn(clientesEsperados);
        
        // Act
        List<Cliente> resultado = clienteService.listarClientes();
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getNombreCompleto());
        verify(clienteDao, times(1)).listarTodos();
    }
    
    // ==================== PRUEBAS PARA obtenerCliente ====================
    
    @Test
    @DisplayName("Obtener cliente por ID - ID válido debe retornar cliente")
    void testObtenerClientePorIdValido() {
        // Arrange
        when(clienteDao.obtenerPorId(1)).thenReturn(clienteValido);
        
        // Act
        Cliente resultado = clienteService.obtenerCliente(1);
        
        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombreCompleto());
        verify(clienteDao, times(1)).obtenerPorId(1);
    }
    
    @Test
    @DisplayName("Obtener cliente por ID - ID inválido debe lanzar excepción")
    void testObtenerClientePorIdInvalido() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.obtenerCliente(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.obtenerCliente(-5);
        });
        
        // Verificar que NO se llamó al DAO
        verify(clienteDao, never()).obtenerPorId(anyInt());
    }
    
    // ==================== PRUEBAS PARA obtenerClientePorCedula ====================
    
    @Test
    @DisplayName("Obtener cliente por cédula - Cédula válida debe retornar cliente")
    void testObtenerClientePorCedulaValida() {
        // Arrange
        when(clienteDao.obtenerPorCedula("12345678")).thenReturn(clienteValido);
        
        // Act
        Cliente resultado = clienteService.obtenerClientePorCedula("12345678");
        
        // Assert
        assertNotNull(resultado);
        assertEquals("12345678", resultado.getCedula());
        verify(clienteDao, times(1)).obtenerPorCedula("12345678");
    }
    
    @Test
    @DisplayName("Obtener cliente por cédula - Cédula nula debe lanzar excepción")
    void testObtenerClientePorCedulaNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.obtenerClientePorCedula(null);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.obtenerClientePorCedula("");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.obtenerClientePorCedula("   ");
        });
    }
    
    // ==================== PRUEBAS PARA registrarCliente ====================
    
    @Test
    @DisplayName("Registrar cliente - Cliente válido debe registrar exitosamente")
    void testRegistrarClienteValido() {
        // Arrange
        when(clienteDao.obtenerPorCedula("12345678")).thenReturn(null);
        when(clienteDao.registrar(any(Cliente.class))).thenReturn(1);
        
        // Act
        int id = clienteService.registrarCliente(clienteValido);
        
        // Assert
        assertEquals(1, id);
        verify(clienteDao, times(1)).obtenerPorCedula("12345678");
        verify(clienteDao, times(1)).registrar(clienteValido);
    }
    
    @Test
    @DisplayName("Registrar cliente - Cliente con nombre vacío debe lanzar excepción")
    void testRegistrarClienteNombreVacio() {
        clienteInvalido.setCedula("12345678");
        
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.registrarCliente(clienteInvalido);
        });
        
        verify(clienteDao, never()).registrar(any());
    }
    
    @Test
    @DisplayName("Registrar cliente - Cliente con cédula vacía debe lanzar excepción")
    void testRegistrarClienteCedulaVacia() {
        clienteInvalido.setNombreCompleto("Juan Pérez");
        
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.registrarCliente(clienteInvalido);
        });
    }
    
    @Test
    @DisplayName("Registrar cliente - Cédula con longitud incorrecta debe lanzar excepción")
    void testRegistrarClienteCedulaLongitudIncorrecta() {
        clienteValido.setCedula("123");  // Muy corta
        
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.registrarCliente(clienteValido);
        });
        
        clienteValido.setCedula("12345678901");  // Muy larga
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.registrarCliente(clienteValido);
        });
    }
    
    @Test
    @DisplayName("Registrar cliente - Email inválido debe lanzar excepción")
    void testRegistrarClienteEmailInvalido() {
        clienteValido.setCorreoElectronico("correo-invalido");
        
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.registrarCliente(clienteValido);
        });
    }
    
    @Test
    @DisplayName("Registrar cliente - Cédula duplicada debe lanzar excepción")
    void testRegistrarClienteCedulaDuplicada() {
        // Arrange
        when(clienteDao.obtenerPorCedula("12345678")).thenReturn(clienteValido);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.registrarCliente(clienteValido);
        });
        
        verify(clienteDao, never()).registrar(any());
    }
    
    // ==================== PRUEBAS PARA editarCliente ====================
    
    @Test
    @DisplayName("Editar cliente - Cliente válido debe editar exitosamente")
    void testEditarClienteValido() {
        // Arrange
        when(clienteDao.editar(clienteValido)).thenReturn(true);
        
        // Act
        boolean resultado = clienteService.editarCliente(clienteValido);
        
        // Assert
        assertTrue(resultado);
        verify(clienteDao, times(1)).editar(clienteValido);
    }
    
    @Test
    @DisplayName("Editar cliente - ID inválido debe lanzar excepción")
    void testEditarClienteIdInvalido() {
        clienteValido.setId(0);
        
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.editarCliente(clienteValido);
        });
        
        clienteValido.setId(-1);
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.editarCliente(clienteValido);
        });
    }
    
    // ==================== PRUEBAS PARA eliminarCliente ====================
    
    @Test
    @DisplayName("Eliminar cliente - ID válido debe eliminar exitosamente")
    void testEliminarClienteValido() {
        // Arrange
        when(clienteDao.eliminar(1, false)).thenReturn(true);
        
        // Act
        boolean resultado = clienteService.eliminarCliente(1);
        
        // Assert
        assertTrue(resultado);
        verify(clienteDao, times(1)).eliminar(1, false);
    }
    
    @Test
    @DisplayName("Eliminar cliente - ID inválido debe lanzar excepción")
    void testEliminarClienteIdInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.eliminarCliente(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.eliminarCliente(-5);
        });
    }
    
    // ==================== PRUEBAS PARA activarCliente ====================
    
    @Test
    @DisplayName("Activar cliente - ID válido debe activar exitosamente")
    void testActivarClienteValido() {
        // Arrange
        when(clienteDao.eliminar(1, true)).thenReturn(true);
        
        // Act
        boolean resultado = clienteService.activarCliente(1);
        
        // Assert
        assertTrue(resultado);
        verify(clienteDao, times(1)).eliminar(1, true);
    }
}
