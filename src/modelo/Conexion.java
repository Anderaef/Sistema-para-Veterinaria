package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private final String baseDatos = "vetclinic";
    private final String url = "jdbc:postgresql://localhost:5432/" + baseDatos;
    private final String usuario = "ander_dawa";
    private final String contraseña = "aaef2003"; 

    private Connection con = null;

    public Connection getConexion() {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("¡Conexión exitosa a PostgreSQL!");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de PostgreSQL.");
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
        return con;
    }
}