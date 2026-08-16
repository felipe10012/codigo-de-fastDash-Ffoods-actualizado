package com.fastdash.dao.impl;

import com.fastdash.dao.RestauranteDao;
import com.fastdash.model.Restaurante;
import com.fastdash.util.ConexionSupabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class RestauranteDaoImpl implements RestauranteDao {

    private static final String TABLA = "restaurante";
    private final Gson gson = new Gson();

    @Override
    public void insertar(Restaurante restaurante) {
        try {
            JsonObject cuerpo = new JsonObject();
            cuerpo.addProperty("nombre", restaurante.getNombre());
            if (restaurante.getDireccion() != null) {
                cuerpo.addProperty("direccion", restaurante.getDireccion());
            }
            if (restaurante.getTelefono() != null) {
                cuerpo.addProperty("telefono", restaurante.getTelefono());
            }
            String respuesta = ConexionSupabase.insertar(TABLA, cuerpo.toString(), true);
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null && filas.size() > 0) {
                restaurante.setIdRestaurante(filas.get(0).getAsJsonObject().get("id_restaurante").getAsInt());
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al insertar el restaurante: " + excepcion.getMessage());
        }
    }

    @Override
    public Restaurante consultarPorId(int idRestaurante) {
        try {
            String respuesta = ConexionSupabase.get(
                    TABLA, "select=*&id_restaurante=eq." + ConexionSupabase.codificar(String.valueOf(idRestaurante)));
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null && filas.size() > 0) {
                return mapearRestaurante(filas.get(0).getAsJsonObject());
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al consultar el restaurante por id: " + excepcion.getMessage());
        }
        return null;
    }

    @Override
    public List<Restaurante> listarTodos() {
        List<Restaurante> restaurantes = new ArrayList<>();
        try {
            String respuesta = ConexionSupabase.get(TABLA, "select=*&order=id_restaurante");
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null) {
                for (int i = 0; i < filas.size(); i++) {
                    restaurantes.add(mapearRestaurante(filas.get(i).getAsJsonObject()));
                }
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al listar los restaurantes: " + excepcion.getMessage());
        }
        return restaurantes;
    }

    @Override
    public void actualizar(Restaurante restaurante) {
        try {
            JsonObject cuerpo = new JsonObject();
            cuerpo.addProperty("nombre", restaurante.getNombre());
            if (restaurante.getDireccion() != null) {
                cuerpo.addProperty("direccion", restaurante.getDireccion());
            }
            if (restaurante.getTelefono() != null) {
                cuerpo.addProperty("telefono", restaurante.getTelefono());
            }
            String filtro = "id_restaurante=eq." + ConexionSupabase.codificar(String.valueOf(restaurante.getIdRestaurante()));
            ConexionSupabase.actualizar(TABLA, filtro, cuerpo.toString());
        } catch (RuntimeException excepcion) {
            System.err.println("Error al actualizar el restaurante: " + excepcion.getMessage());
        }
    }

    @Override
    public void eliminar(int idRestaurante) {
        try {
            String filtro = "id_restaurante=eq." + ConexionSupabase.codificar(String.valueOf(idRestaurante));
            ConexionSupabase.eliminar(TABLA, filtro);
        } catch (RuntimeException excepcion) {
            System.err.println("Error al eliminar el restaurante: " + excepcion.getMessage());
        }
    }

    private Restaurante mapearRestaurante(JsonObject fila) {
        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(fila.get("id_restaurante").getAsInt());
        restaurante.setNombre(texto(fila, "nombre"));
        restaurante.setDireccion(texto(fila, "direccion"));
        restaurante.setTelefono(texto(fila, "telefono"));
        return restaurante;
    }

    private String texto(JsonObject fila, String columna) {
        return fila.has(columna) && !fila.get(columna).isJsonNull() ? fila.get(columna).getAsString() : null;
    }
}
