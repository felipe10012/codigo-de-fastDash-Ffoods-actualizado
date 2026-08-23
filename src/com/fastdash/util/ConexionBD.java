package com.fastdash.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // =================================================================
    // Configuracion de la base de datos MySQL local.
    // Ajustar segun la instalacion: host, puerto, base de datos,
    // usuario y contrasena.
    // =================================================================
    private static final String HOST = "localhost";
    private static final String PUERTO = "3306";
    private static final String BASE_DE_DATOS = "fastdash_foods";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "Felipe21";

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DE_DATOS
                    + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
                    + "&characterEncoding=UTF-8";

    private ConexionBD() {
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}
