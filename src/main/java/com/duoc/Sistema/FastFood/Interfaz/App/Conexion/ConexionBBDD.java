package com.duoc.Sistema.FastFood.Interfaz.App.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de gestionar la conexión con la base de datos MySQL.
 */
public class ConexionBBDD {

    private static final String URL = "jdbc:mysql://localhost:3306/FastFoodBD";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "qwe123";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}