package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/sigi";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    public static Connection conectar() {
        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("Conexión exitosa a la base de datos SIGI.");
            return conexion;
        } catch (SQLException e) {
            System.err.println("Error al conectar con MySQL: " + e.getMessage());
            return null;
        }
    }
}