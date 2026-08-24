package com.fastdash.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {

    private static final String ARCHIVO_PROPIEDADES = "db.properties";

    private static final String HOST;
    private static final String PUERTO;
    private static final String BASE_DE_DATOS;
    private static final String USUARIO;
    private static final String CONTRASENA;

    static {
        Properties propiedades = cargarPropiedades();
        HOST = propiedades.getProperty("db.host", "localhost");
        PUERTO = propiedades.getProperty("db.puerto", "3306");
        BASE_DE_DATOS = propiedades.getProperty("db.nombre", "fastdash_foods");
        USUARIO = propiedades.getProperty("db.usuario", "root");
        CONTRASENA = propiedades.getProperty("db.contrasena", "");
    }

    private ConexionBD() {
    }

    public static Connection obtenerConexion() throws SQLException {
        String url = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DE_DATOS
                + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
                + "&characterEncoding=UTF-8";
        return DriverManager.getConnection(url, USUARIO, CONTRASENA);
    }

    private static Properties cargarPropiedades() {
        Properties propiedades = new Properties();
        try (InputStream entrada = new FileInputStream(ARCHIVO_PROPIEDADES)) {
            propiedades.load(entrada);
        } catch (IOException excepcion) {
            System.err.println("No se encontro el archivo " + ARCHIVO_PROPIEDADES
                    + ". Copia db.properties.example como db.properties y coloca tus datos de MySQL.");
        }
        return propiedades;
    }
}
