package com.fastdash.dao.impl;

import com.fastdash.dao.ProductoDao;
import com.fastdash.model.Producto;
import com.fastdash.util.ConexionSupabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductoDaoImpl implements ProductoDao {

    private static final String TABLA = "producto";
    private final Gson gson = new Gson();

    @Override
    public void insertar(Producto producto) {
        try {
            JsonObject cuerpo = new JsonObject();
            cuerpo.addProperty("id_restaurante", producto.getIdRestaurante());
            cuerpo.addProperty("nombre", producto.getNombre());
            if (producto.getDescripcion() != null) {
                cuerpo.addProperty("descripcion", producto.getDescripcion());
            }
            cuerpo.addProperty("precio", producto.getPrecio());
            if (producto.getCategoria() != null) {
                cuerpo.addProperty("categoria", producto.getCategoria());
            }
            String respuesta = ConexionSupabase.insertar(TABLA, cuerpo.toString(), true);
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null && filas.size() > 0) {
                producto.setIdProducto(filas.get(0).getAsJsonObject().get("id_producto").getAsInt());
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al insertar el producto: " + excepcion.getMessage());
        }
    }

    @Override
    public Producto consultarPorId(int idProducto) {
        try {
            String respuesta = ConexionSupabase.get(
                    TABLA, "select=*&id_producto=eq." + ConexionSupabase.codificar(String.valueOf(idProducto)));
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null && filas.size() > 0) {
                return mapearProducto(filas.get(0).getAsJsonObject());
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al consultar el producto por id: " + excepcion.getMessage());
        }
        return null;
    }

    @Override
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        try {
            String respuesta = ConexionSupabase.get(TABLA, "select=*&order=id_producto");
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null) {
                for (int i = 0; i < filas.size(); i++) {
                    productos.add(mapearProducto(filas.get(i).getAsJsonObject()));
                }
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al listar los productos: " + excepcion.getMessage());
        }
        return productos;
    }

    @Override
    public List<Producto> listarPorRestaurante(int idRestaurante) {
        List<Producto> productos = new ArrayList<>();
        try {
            String respuesta = ConexionSupabase.get(TABLA,
                    "select=*&id_restaurante=eq." + ConexionSupabase.codificar(String.valueOf(idRestaurante))
                            + "&order=id_producto");
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null) {
                for (int i = 0; i < filas.size(); i++) {
                    productos.add(mapearProducto(filas.get(i).getAsJsonObject()));
                }
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al listar los productos del restaurante: " + excepcion.getMessage());
        }
        return productos;
    }

    @Override
    public void actualizar(Producto producto) {
        try {
            JsonObject cuerpo = new JsonObject();
            cuerpo.addProperty("id_restaurante", producto.getIdRestaurante());
            cuerpo.addProperty("nombre", producto.getNombre());
            if (producto.getDescripcion() != null) {
                cuerpo.addProperty("descripcion", producto.getDescripcion());
            }
            cuerpo.addProperty("precio", producto.getPrecio());
            if (producto.getCategoria() != null) {
                cuerpo.addProperty("categoria", producto.getCategoria());
            }
            String filtro = "id_producto=eq." + ConexionSupabase.codificar(String.valueOf(producto.getIdProducto()));
            ConexionSupabase.actualizar(TABLA, filtro, cuerpo.toString());
        } catch (RuntimeException excepcion) {
            System.err.println("Error al actualizar el producto: " + excepcion.getMessage());
        }
    }

    @Override
    public void eliminar(int idProducto) {
        try {
            String filtro = "id_producto=eq." + ConexionSupabase.codificar(String.valueOf(idProducto));
            ConexionSupabase.eliminar(TABLA, filtro);
        } catch (RuntimeException excepcion) {
            System.err.println("Error al eliminar el producto: " + excepcion.getMessage());
        }
    }

    private Producto mapearProducto(JsonObject fila) {
        Producto producto = new Producto();
        producto.setIdProducto(fila.get("id_producto").getAsInt());
        producto.setIdRestaurante(fila.get("id_restaurante").getAsInt());
        producto.setNombre(texto(fila, "nombre"));
        producto.setDescripcion(texto(fila, "descripcion"));
        producto.setPrecio(decimal(fila, "precio"));
        producto.setCategoria(texto(fila, "categoria"));
        return producto;
    }

    private String texto(JsonObject fila, String columna) {
        return fila.has(columna) && !fila.get(columna).isJsonNull() ? fila.get(columna).getAsString() : null;
    }

    private BigDecimal decimal(JsonObject fila, String columna) {
        return fila.has(columna) && !fila.get(columna).isJsonNull() ? fila.get(columna).getAsBigDecimal() : null;
    }
}
