CREATE DATABASE Sistema_Biblioteca;

USE Sistema_Biblioteca;
GO
-- TABLA USUARIO 
CREATE TABLE Usuario (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    NOMBRE_COMPLETO NVARCHAR(50) NOT NULL UNIQUE,
    CLAVE NVARCHAR(100) NOT NULL,
    CORREO_ELECTRONICO NVARCHAR(100) NOT NULL,
    FECHA_CREACION DATETIME DEFAULT GETDATE()
);

INSERT INTO Usuario (NOMBRE_COMPLETO, CLAVE, CORREO_ELECTRONICO) VALUES ('admin', 'admin123', 'admin@gmail.com');
GO

CREATE PROCEDURE PA_INICIAR_SESION(
@Correo_Electronico varchar(50),
@Clave nvarchar(300)
)
AS 
BEGIN
	SELECT ID,  NOMBRE_COMPLETO, CLAVE, CORREO_ELECTRONICO, FECHA_CREACION FROM USUARIO 
	WHERE CORREO_ELECTRONICO = @Correo_Electronico and CLAVE = @Clave;
END;
GO

-- TABLA LIBRO
CREATE TABLE Libro (
    ID INT IDENTITY PRIMARY KEY,
    TITULO NVARCHAR(200) NOT NULL,
    AUTOR NVARCHAR(100) NOT NULL,
    AÑO_PUBLICACION INT,
    EDITORIAL NVARCHAR(100),
    CANTIDAD INT, 
    ESTADO BIT,
	FECHA_REGISTRO DATETIME DEFAULT GETDATE()
);
GO

CREATE PROCEDURE PA_LISTAR_LIBROS
AS
BEGIN
	SELECT * FROM LIBRO
END;
GO

CREATE PROCEDURE PA_OBTENER_LIBRO(
@id INT
)
AS 
BEGIN
	SELECT * FROM LIBRO WHERE ID = @id;
END;
GO

CREATE PROCEDURE PA_REGISTRAR_LIBRO(
@titulo NVARCHAR(200),
@autor NVARCHAR(100),
@año_publicacion INT,
@editorial NVARCHAR(100),
@cantidad INT, 
@estado BIT 
)
AS
BEGIN
	INSERT INTO LIBRO (TITULO, AUTOR, AÑO_PUBLICACION, EDITORIAL, CANTIDAD, ESTADO) 
	VALUES (@titulo, @autor, @año_publicacion, @editorial, @cantidad, @estado);
END;
GO

CREATE PROCEDURE PA_EDITAR_LIBRO(
@id INT,
@titulo NVARCHAR(200),
@autor NVARCHAR(100),
@año_publicacion INT,
@editorial NVARCHAR(100),
@cantidad INT, 
@estado BIT  
)
AS
BEGIN
	UPDATE LIBRO SET TITULO = @titulo, AUTOR = @autor, AÑO_PUBLICACION = @año_publicacion, EDITORIAL = @editorial, CANTIDAD = @cantidad, ESTADO = @estado WHERE ID = @id;
END;
GO

CREATE PROCEDURE PA_ELIMINAR_LIBRO(
@id INT,
@estado BIT 
)
AS
BEGIN
	UPDATE LIBRO SET ESTADO = @estado WHERE ID = @id;
END;
GO

-- TABLA CLIENTE
CREATE TABLE Cliente (
    ID INT IDENTITY PRIMARY KEY,
    NOMBRE_COMPLETO NVARCHAR(100),
    CEDULA NVARCHAR(10) UNIQUE,
    TELEFONO NVARCHAR(10),
    CORREO_ELECTRONICO NVARCHAR(100),
    DIRECCION NVARCHAR(200),
    ESTADO BIT,
	FECHA_REGISTRO DATETIME DEFAULT GETDATE()
);
GO

CREATE PROCEDURE PA_LISTAR_CLIENTES
AS
BEGIN
    SELECT * FROM Cliente;
END;
GO

CREATE PROCEDURE PA_OBTENER_CLIENTE
    @id INT
AS 
BEGIN
    SELECT * FROM Cliente WHERE ID = @id;
END;
GO

CREATE PROCEDURE PA_REGISTRAR_CLIENTE
    @nombre_completo NVARCHAR(100),
    @cedula NVARCHAR(10),
    @telefono NVARCHAR(10),
    @correo_electronico NVARCHAR(100),
    @direccion NVARCHAR(200),
    @estado BIT
AS
BEGIN
    INSERT INTO Cliente (NOMBRE_COMPLETO, CEDULA, TELEFONO, CORREO_ELECTRONICO, DIRECCION, ESTADO) 
    VALUES (@nombre_completo, @cedula, @telefono, @correo_electronico, @direccion, @estado);
END;
GO

CREATE PROCEDURE PA_EDITAR_CLIENTE
    @id INT,
    @nombre_completo NVARCHAR(100),
    @cedula NVARCHAR(10),
    @telefono NVARCHAR(10),
    @correo_electronico NVARCHAR(100),
    @direccion NVARCHAR(200),
    @estado BIT
AS
BEGIN
    UPDATE Cliente 
    SET NOMBRE_COMPLETO = @nombre_completo, 
        CEDULA = @cedula, 
        TELEFONO = @telefono, 
        CORREO_ELECTRONICO = @correo_electronico, 
        DIRECCION = @direccion, 
        ESTADO = @estado
    WHERE ID = @id;
END;
GO

CREATE PROCEDURE PA_ELIMINAR_CLIENTE
@id INT,
@estado BIT
AS
BEGIN
    UPDATE Cliente SET ESTADO = @estado WHERE ID = @id;
END;

GO

--   TABLA PRESTAMO
CREATE TABLE Prestamo (
    ID INT IDENTITY PRIMARY KEY,
    ID_CLIENTE INT REFERENCES CLIENTE(ID),
    FECHA_PRESTAMO DATETIME DEFAULT GETDATE(),
    FECHA_DEVOLUCION_ESPERADA DATE,
    FECHA_REAL_DEVOLUCION DATE,
    ESTADO NVARCHAR(20) DEFAULT 'ACTIVO'
);
GO

-- TABLA DETALLE PRESTAMO 

CREATE TABLE Detalle_Prestamo (
    ID INT IDENTITY PRIMARY KEY,
    ID_PRESTAMO INT REFERENCES PRESTAMO (ID),
    ID_LIBRO INT REFERENCES LIBRO (ID),
    FECHA_REAL_DEVOLUCION_LIBRO DATE, 
    ESTADO_LIBRO NVARCHAR(20) DEFAULT 'PRESTADO'
);
GO

CREATE PROCEDURE PA_LISTAR_PRESTAMOS
AS
BEGIN
    SELECT 
        p.ID AS ID_PRESTAMO,
        c.NOMBRE_COMPLETO AS CLIENTE,
        c.CEDULA,
        p.FECHA_PRESTAMO,
        p.FECHA_DEVOLUCION_ESPERADA,
        p.FECHA_REAL_DEVOLUCION,
        p.ESTADO,
        COUNT(dp.ID_LIBRO) AS TOTAL_LIBROS,
        SUM(CASE WHEN dp.ESTADO_LIBRO = 'DEVUELTO' THEN 1 ELSE 0 END) AS LIBROS_DEVUELTOS
    FROM Prestamo p
    INNER JOIN Cliente c ON p.ID_CLIENTE = c.ID
    INNER JOIN Detalle_Prestamo dp ON p.ID = dp.ID_PRESTAMO
    GROUP BY p.ID, c.NOMBRE_COMPLETO, c.CEDULA, p.FECHA_PRESTAMO, p.FECHA_DEVOLUCION_ESPERADA, 
             p.FECHA_REAL_DEVOLUCION, p.ESTADO
    ORDER BY p.ID DESC;
END;
GO

CREATE PROCEDURE PA_OBTENER_PRESTAMO(
@id_prestamo INT
)
AS
BEGIN
    -- Cabecera del préstamo
    SELECT 
        p.ID,
        p.ID_CLIENTE,
        c.NOMBRE_COMPLETO AS CLIENTE,
        p.FECHA_PRESTAMO,
        p.FECHA_DEVOLUCION_ESPERADA,
        p.FECHA_REAL_DEVOLUCION,
        p.ESTADO
    FROM Prestamo p
    INNER JOIN Cliente c ON p.ID_CLIENTE = c.ID
    WHERE p.ID = @id_prestamo;
    
    -- Detalle de libros prestados
    SELECT 
        dp.ID_LIBRO,
        l.TITULO,
        l.AUTOR,
        dp.ESTADO_LIBRO,
        dp.FECHA_REAL_DEVOLUCION_LIBRO
    FROM Detalle_Prestamo dp
    INNER JOIN Libro l ON dp.ID_LIBRO = l.ID
    WHERE dp.ID_PRESTAMO = @id_prestamo;
END;
GO

CREATE PROCEDURE PA_REGISTRAR_PRESTAMO
    @id_cliente INT,
    @fecha_devolucion_esperada DATE,
    @libros_ids NVARCHAR(MAX)
AS
BEGIN
    DECLARE @id_prestamo INT;
    
    BEGIN TRANSACTION;
    
    -- 1. Insertar cabecera del préstamo
    INSERT INTO Prestamo (ID_CLIENTE, FECHA_DEVOLUCION_ESPERADA, ESTADO)
    VALUES (@id_cliente, @fecha_devolucion_esperada, 'ACTIVO');
    
    SET @id_prestamo = SCOPE_IDENTITY();
    
    -- 2. Insertar cada libro en el detalle
    DECLARE @libro_id INT;
    
    DECLARE cursor_libros CURSOR FOR
        SELECT value FROM STRING_SPLIT(@libros_ids, ',');
    
    OPEN cursor_libros;
    FETCH NEXT FROM cursor_libros INTO @libro_id;
    
    WHILE @@FETCH_STATUS = 0
    BEGIN
        -- Verificar disponibilidad
        DECLARE @disponibles INT;
        SELECT @disponibles = CANTIDAD FROM Libro WHERE ID = @libro_id;
        
        IF @disponibles <= 0
        BEGIN
            ROLLBACK TRANSACTION;
            RETURN;
        END
        
        -- Insertar detalle
        INSERT INTO Detalle_Prestamo (ID_PRESTAMO, ID_LIBRO, ESTADO_LIBRO)
        VALUES (@id_prestamo, @libro_id, 'PRESTADO');
        
        -- Disminuir cantidad disponible
        UPDATE Libro SET CANTIDAD = CANTIDAD - 1 WHERE ID = @libro_id;
        
        FETCH NEXT FROM cursor_libros INTO @libro_id;
    END
    
    CLOSE cursor_libros;
    DEALLOCATE cursor_libros;
    
    COMMIT TRANSACTION;
END;
GO

CREATE PROCEDURE PA_REGISTRAR_DEVOLUCION
    @id_prestamo INT,
    @libros_ids NVARCHAR(MAX) 
AS
BEGIN
    DECLARE @libro_id INT;
    
    BEGIN TRANSACTION;
    
    DECLARE cursor_devolucion CURSOR FOR
        SELECT value FROM STRING_SPLIT(@libros_ids, ',');
    
    OPEN cursor_devolucion;
    FETCH NEXT FROM cursor_devolucion INTO @libro_id;
    
    WHILE @@FETCH_STATUS = 0
    BEGIN
        -- Marcar libro como devuelto
        UPDATE Detalle_Prestamo 
        SET ESTADO_LIBRO = 'DEVUELTO',
            FECHA_REAL_DEVOLUCION_LIBRO = GETDATE()
        WHERE ID_PRESTAMO = @id_prestamo AND ID_LIBRO = @libro_id;
        
        -- Aumentar cantidad disponible del libro
        UPDATE Libro SET CANTIDAD = CANTIDAD + 1 WHERE ID = @libro_id;
        
        FETCH NEXT FROM cursor_devolucion INTO @libro_id;
    END
    
    CLOSE cursor_devolucion;
    DEALLOCATE cursor_devolucion;
    
    -- Verificar si todos los libros fueron devueltos
    DECLARE @pendientes INT;
    SELECT @pendientes = COUNT(*) FROM Detalle_Prestamo 
    WHERE ID_PRESTAMO = @id_prestamo AND ESTADO_LIBRO = 'PRESTADO';
    
    IF @pendientes = 0
    BEGIN
        UPDATE Prestamo SET 
            ESTADO = 'COMPLETADO',
            FECHA_REAL_DEVOLUCION = GETDATE()
        WHERE ID = @id_prestamo;
    END
    
    COMMIT TRANSACTION;
    
    SELECT 'DEVOLUCIÓN REGISTRADA EXITOSAMENTE' AS Mensaje;
END;
GO

CREATE PROCEDURE PA_ACTUALIZAR_FECHA_DEVOLUCION(
@id_prestamo INT,
@nueva_fecha DATE
)
AS
BEGIN
    UPDATE Prestamo SET FECHA_DEVOLUCION_ESPERADA = @nueva_fecha WHERE ID = @id_prestamo AND ESTADO = 'ACTIVO';
END;