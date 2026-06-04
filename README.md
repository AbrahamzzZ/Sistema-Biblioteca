# Sistema de Biblioteca

Sistema de gestión bibliotecaria desarrollado en **Java** con **Maven**, que permite administrar libros, clientes y préstamos de manera eficiente. Cuenta con una interfaz gráfica amigable desarrollada con **Swing** y conexión a **SQL Server**.

---

## Características principales

- **CRUD completo** de libros, clientes y préstamos
- **Sistema de autenticación** de usuarios (login)
- **Préstamos múltiples** (un cliente puede pedir varios libros)
- **Control de stock** (actualización automática de cantidad disponible)
- **Devoluciones parciales** (posibilidad de devolver libros por separado)
- **Renovación de préstamos** (actualización de fecha de devolución)
- **Dashboard interactivo** con préstamos activos
- **Pruebas unitarias** (53 pruebas con JUnit 5 y Mockito)

## Instalación y ejecución

### Requisitos previos

- Java JDK 21 o superior
- Maven 3.x
- SQL Server (Express)
- Git 

### Pasos de instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/AbrahamzzZ/Sistema-Biblioteca.git
   cd Sistema-Biblioteca

2. **Configurar la base de datos**
    - Ejecutar el script en db/ 
    - Modificar la conexión en ConexionDB.java

3. **Credenciales de acceso**
    - Usuario: admin@gmail.com    
    - Clave: admin123