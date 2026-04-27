/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author herma
 */
public class Conexion {
    
    private static final String SERVER = "localhost";  
    private static final String INSTANCE = "SQLSEXPRESS"; 
    private static final String DATABASE = "Sistema_Biblioteca";  
    private static final String USER = "user_sistema_biblioteca";
    private static final String PASSWORD = "Afjunio282003";
    
    private static final String URL = "jdbc:sqlserver://" + SERVER + ";" + "databaseName=" + DATABASE + ";"  + "encrypt=true;" + "trustServerCertificate=true";
        
    /**
     * Obtiene una conexión a la base de datos
     * @return Connection object
     * @throws SQLException si hay error de conexión
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC no encontrado. Revisa la dependencia en pom.xml", e);
        }
    }
     /**
     * Prueba la conexión con la base de datos
     * @return true si la conexión es exitosa
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Conexión exitosa a SQL Server");
            System.out.println("   Servidor: " + SERVER);
            System.out.println("   Base de datos: " + DATABASE);
            System.out.println("   Usuario: " + USER);
            System.out.println("   JDBC URL: " + URL);
            return true;
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
