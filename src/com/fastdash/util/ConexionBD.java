package com.fastdash.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String HOST = "localhost";
    private static final String PUERTO = "3306";
    private static final String BASE_DE_DATOS = "fastdash_foods";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "Felipe213550";

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DE_DATOS
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}
