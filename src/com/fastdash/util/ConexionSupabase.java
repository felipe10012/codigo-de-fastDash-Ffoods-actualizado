package com.fastdash.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ConexionSupabase {

    // =================================================================
    // IMPORTANTE: reemplazar con los datos de tu proyecto de Supabase.
    // Dashboard -> Settings (engranaje) -> API
    //   Project URL:   https://XXXX.supabase.co
    //   anon public:   la clave anon/pública
    // =================================================================
    private static final String URL_PROYECTO = "https://TU-PROYECTO.supabase.co";
    private static final String ANON_KEY = "TU-ANON-KEY";

    private static final int TIEMPO_CONEXION_MS = 10000;
    private static final int TIEMPO_LECTURA_MS = 15000;

    private ConexionSupabase() {
    }

    public static String get(String tabla, String query) {
        return peticion("GET", tabla, query, null, false);
    }

    public static String insertar(String tabla, String json, boolean devolverFila) {
        return peticion("POST", tabla, null, json, devolverFila);
    }

    public static String actualizar(String tabla, String filtro, String json) {
        return peticion("PATCH", tabla, filtro, json, false);
    }

    public static String eliminar(String tabla, String filtro) {
        return peticion("DELETE", tabla, filtro, null, false);
    }

    private static String peticion(String metodo, String tabla, String query, String json, boolean devolverFila) {
        if (!URL_PROYECTO.startsWith("https://") || ANON_KEY.startsWith("TU-ANON")) {
            throw new IllegalStateException(
                    "Configura URL_PROYECTO y ANON_KEY en src/com/fastdash/util/ConexionSupabase.java");
        }
        HttpURLConnection conexion = null;
        try {
            String base = URL_PROYECTO + "/rest/v1/" + tabla;
            String urlFinal = base
                    + (query != null && !query.isEmpty() ? "?" + query : "");
            conexion = (HttpURLConnection) new URL(urlFinal).openConnection();
            conexion.setRequestMethod(metodo);
            conexion.setConnectTimeout(TIEMPO_CONEXION_MS);
            conexion.setReadTimeout(TIEMPO_LECTURA_MS);
            conexion.setRequestProperty("apikey", ANON_KEY);
            conexion.setRequestProperty("Authorization", "Bearer " + ANON_KEY);
            conexion.setRequestProperty("Accept", "application/json");
            if (json != null) {
                conexion.setDoOutput(true);
                conexion.setRequestProperty("Content-Type", "application/json");
                conexion.setRequestProperty("Prefer",
                        devolverFila ? "return=representation" : "return=minimal");
                try (OutputStream salida = conexion.getOutputStream()) {
                    salida.write(json.getBytes(StandardCharsets.UTF_8));
                }
            }
            int codigo = conexion.getResponseCode();
            String cuerpo = leerCuerpo(codigo >= 400 ? conexion.getErrorStream() : conexion.getInputStream());
            if (codigo >= 400) {
                throw new RuntimeException(
                        "Supabase [" + metodo + " " + tabla + "] HTTP " + codigo + ": " + cuerpo);
            }
            return cuerpo;
        } catch (IOException excepcion) {
            throw new RuntimeException(
                    "Error de red con Supabase [" + metodo + " " + tabla + "]: " + excepcion.getMessage(),
                    excepcion);
        } finally {
            if (conexion != null) {
                conexion.disconnect();
            }
        }
    }

    private static String leerCuerpo(InputStream flujo) throws IOException {
        if (flujo == null) {
            return "";
        }
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(flujo, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                contenido.append(linea);
            }
        }
        return contenido.toString();
    }

    public static String codificar(String valor) {
        try {
            return URLEncoder.encode(valor, StandardCharsets.UTF_8.name()).replace("+", "%20");
        } catch (Exception excepcion) {
            return valor;
        }
    }
}
