package com.fastdash.dao.impl;

import com.fastdash.dao.RepartidorDao;
import com.fastdash.model.Repartidor;
import com.fastdash.util.ConexionSupabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class RepartidorDaoImpl implements RepartidorDao {

    private static final String TABLA = "repartidor";
    private final Gson gson = new Gson();

    @Override
    public void insertar(Repartidor repartidor) {
        try {
            JsonObject cuerpo = new JsonObject();
            cuerpo.addProperty("id_restaurante", repartidor.getIdRestaurante());
            cuerpo.addProperty("nombre", repartidor.getNombre());
            if (repartidor.getTelefono() != null) {
                cuerpo.addProperty("telefono", repartidor.getTelefono());
            }
            if (repartidor.getVehiculo() != null) {
                cuerpo.addProperty("vehiculo", repartidor.getVehiculo());
            }
            String respuesta = ConexionSupabase.insertar(TABLA, cuerpo.toString(), true);
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null && filas.size() > 0) {
                repartidor.setIdRepartidor(filas.get(0).getAsJsonObject().get("id_repartidor").getAsInt());
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al insertar el repartidor: " + excepcion.getMessage());
        }
    }

    @Override
    public Repartidor consultarPorId(int idRepartidor) {
        try {
            String respuesta = ConexionSupabase.get(
                    TABLA, "select=*&id_repartidor=eq." + ConexionSupabase.codificar(String.valueOf(idRepartidor)));
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null && filas.size() > 0) {
                return mapearRepartidor(filas.get(0).getAsJsonObject());
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al consultar el repartidor por id: " + excepcion.getMessage());
        }
        return null;
    }

    @Override
    public List<Repartidor> listarTodos() {
        List<Repartidor> repartidores = new ArrayList<>();
        try {
            String respuesta = ConexionSupabase.get(TABLA, "select=*&order=id_repartidor");
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null) {
                for (int i = 0; i < filas.size(); i++) {
                    repartidores.add(mapearRepartidor(filas.get(i).getAsJsonObject()));
                }
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al listar los repartidores: " + excepcion.getMessage());
        }
        return repartidores;
    }

    @Override
    public List<Repartidor> listarPorRestaurante(int idRestaurante) {
        List<Repartidor> repartidores = new ArrayList<>();
        try {
            String respuesta = ConexionSupabase.get(TABLA,
                    "select=*&id_restaurante=eq." + ConexionSupabase.codificar(String.valueOf(idRestaurante))
                            + "&order=id_repartidor");
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null) {
                for (int i = 0; i < filas.size(); i++) {
                    repartidores.add(mapearRepartidor(filas.get(i).getAsJsonObject()));
                }
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al listar los repartidores del restaurante: " + excepcion.getMessage());
        }
        return repartidores;
    }

    @Override
    public void actualizar(Repartidor repartidor) {
        try {
            JsonObject cuerpo = new JsonObject();
            cuerpo.addProperty("id_restaurante", repartidor.getIdRestaurante());
            cuerpo.addProperty("nombre", repartidor.getNombre());
            if (repartidor.getTelefono() != null) {
                cuerpo.addProperty("telefono", repartidor.getTelefono());
            }
            if (repartidor.getVehiculo() != null) {
                cuerpo.addProperty("vehiculo", repartidor.getVehiculo());
            }
            String filtro = "id_repartidor=eq." + ConexionSupabase.codificar(String.valueOf(repartidor.getIdRepartidor()));
            ConexionSupabase.actualizar(TABLA, filtro, cuerpo.toString());
        } catch (RuntimeException excepcion) {
            System.err.println("Error al actualizar el repartidor: " + excepcion.getMessage());
        }
    }

    @Override
    public void eliminar(int idRepartidor) {
        try {
            String filtro = "id_repartidor=eq." + ConexionSupabase.codificar(String.valueOf(idRepartidor));
            ConexionSupabase.eliminar(TABLA, filtro);
        } catch (RuntimeException excepcion) {
            System.err.println("Error al eliminar el repartidor: " + excepcion.getMessage());
        }
    }

    private Repartidor mapearRepartidor(JsonObject fila) {
        Repartidor repartidor = new Repartidor();
        repartidor.setIdRepartidor(fila.get("id_repartidor").getAsInt());
        repartidor.setIdRestaurante(fila.get("id_restaurante").getAsInt());
        repartidor.setNombre(texto(fila, "nombre"));
        repartidor.setTelefono(texto(fila, "telefono"));
        repartidor.setVehiculo(texto(fila, "vehiculo"));
        return repartidor;
    }

    private String texto(JsonObject fila, String columna) {
        return fila.has(columna) && !fila.get(columna).isJsonNull() ? fila.get(columna).getAsString() : null;
    }
}
