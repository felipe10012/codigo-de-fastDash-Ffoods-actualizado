package com.fastdash.dao.impl;

import com.fastdash.dao.PedidoDao;
import com.fastdash.model.Pedido;
import com.fastdash.util.ConexionSupabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDaoImpl implements PedidoDao {

    private static final String TABLA = "pedido";
    private final Gson gson = new Gson();

    @Override
    public void insertar(Pedido pedido) {
        try {
            JsonObject cuerpo = new JsonObject();
            cuerpo.addProperty("id_usuario", pedido.getIdUsuario());
            if (pedido.getIdRepartidor() > 0) {
                cuerpo.addProperty("id_repartidor", pedido.getIdRepartidor());
            }
            if (pedido.getFechaPedido() != null) {
                cuerpo.addProperty("fecha_pedido", pedido.getFechaPedido().toInstant().toString());
            }
            cuerpo.addProperty("total", pedido.getTotal());
            if (pedido.getEstado() != null) {
                cuerpo.addProperty("estado", pedido.getEstado());
            }
            String respuesta = ConexionSupabase.insertar(TABLA, cuerpo.toString(), true);
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null && filas.size() > 0) {
                pedido.setIdPedido(filas.get(0).getAsJsonObject().get("id_pedido").getAsInt());
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al insertar el pedido: " + excepcion.getMessage());
        }
    }

    @Override
    public Pedido consultarPorId(int idPedido) {
        try {
            String respuesta = ConexionSupabase.get(
                    TABLA, "select=*&id_pedido=eq." + ConexionSupabase.codificar(String.valueOf(idPedido)));
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null && filas.size() > 0) {
                return mapearPedido(filas.get(0).getAsJsonObject());
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al consultar el pedido por id: " + excepcion.getMessage());
        }
        return null;
    }

    @Override
    public List<Pedido> listarTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        try {
            String respuesta = ConexionSupabase.get(TABLA, "select=*&order=id_pedido");
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null) {
                for (int i = 0; i < filas.size(); i++) {
                    pedidos.add(mapearPedido(filas.get(i).getAsJsonObject()));
                }
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al listar los pedidos: " + excepcion.getMessage());
        }
        return pedidos;
    }

    @Override
    public List<Pedido> listarPorUsuario(int idUsuario) {
        List<Pedido> pedidos = new ArrayList<>();
        try {
            String respuesta = ConexionSupabase.get(TABLA,
                    "select=*&id_usuario=eq." + ConexionSupabase.codificar(String.valueOf(idUsuario))
                            + "&order=id_pedido");
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null) {
                for (int i = 0; i < filas.size(); i++) {
                    pedidos.add(mapearPedido(filas.get(i).getAsJsonObject()));
                }
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al listar los pedidos del usuario: " + excepcion.getMessage());
        }
        return pedidos;
    }

    @Override
    public List<Pedido> listarPorRepartidor(int idRepartidor) {
        List<Pedido> pedidos = new ArrayList<>();
        try {
            String respuesta = ConexionSupabase.get(TABLA,
                    "select=*&id_repartidor=eq." + ConexionSupabase.codificar(String.valueOf(idRepartidor))
                            + "&order=id_pedido");
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null) {
                for (int i = 0; i < filas.size(); i++) {
                    pedidos.add(mapearPedido(filas.get(i).getAsJsonObject()));
                }
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al listar los pedidos del repartidor: " + excepcion.getMessage());
        }
        return pedidos;
    }

    @Override
    public void actualizar(Pedido pedido) {
        try {
            JsonObject cuerpo = new JsonObject();
            cuerpo.addProperty("id_usuario", pedido.getIdUsuario());
            if (pedido.getIdRepartidor() > 0) {
                cuerpo.addProperty("id_repartidor", pedido.getIdRepartidor());
            }
            if (pedido.getFechaPedido() != null) {
                cuerpo.addProperty("fecha_pedido", pedido.getFechaPedido().toInstant().toString());
            }
            cuerpo.addProperty("total", pedido.getTotal());
            if (pedido.getEstado() != null) {
                cuerpo.addProperty("estado", pedido.getEstado());
            }
            String filtro = "id_pedido=eq." + ConexionSupabase.codificar(String.valueOf(pedido.getIdPedido()));
            ConexionSupabase.actualizar(TABLA, filtro, cuerpo.toString());
        } catch (RuntimeException excepcion) {
            System.err.println("Error al actualizar el pedido: " + excepcion.getMessage());
        }
    }

    @Override
    public void eliminar(int idPedido) {
        try {
            String filtro = "id_pedido=eq." + ConexionSupabase.codificar(String.valueOf(idPedido));
            ConexionSupabase.eliminar(TABLA, filtro);
        } catch (RuntimeException excepcion) {
            System.err.println("Error al eliminar el pedido: " + excepcion.getMessage());
        }
    }

    private Pedido mapearPedido(JsonObject fila) {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(fila.get("id_pedido").getAsInt());
        pedido.setIdUsuario(fila.get("id_usuario").getAsInt());
        pedido.setIdRepartidor(entero(fila, "id_repartidor"));
        pedido.setFechaPedido(tiempo(fila, "fecha_pedido"));
        pedido.setTotal(decimal(fila, "total"));
        pedido.setEstado(texto(fila, "estado"));
        return pedido;
    }

    private String texto(JsonObject fila, String columna) {
        return fila.has(columna) && !fila.get(columna).isJsonNull() ? fila.get(columna).getAsString() : null;
    }

    private int entero(JsonObject fila, String columna) {
        return fila.has(columna) && !fila.get(columna).isJsonNull() ? fila.get(columna).getAsInt() : 0;
    }

    private BigDecimal decimal(JsonObject fila, String columna) {
        return fila.has(columna) && !fila.get(columna).isJsonNull() ? fila.get(columna).getAsBigDecimal() : null;
    }

    private Timestamp tiempo(JsonObject fila, String columna) {
        if (!fila.has(columna) || fila.get(columna).isJsonNull()) {
            return null;
        }
        try {
            return Timestamp.from(OffsetDateTime.parse(fila.get(columna).getAsString()).toInstant());
        } catch (Exception excepcion) {
            return null;
        }
    }
}
