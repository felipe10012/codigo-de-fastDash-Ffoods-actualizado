package com.fastdash.dao.impl;

import com.fastdash.dao.ProductoDao;
import com.fastdash.model.Producto;
import com.fastdash.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoDaoImpl implements ProductoDao {

    private static final String INSERTAR_SQL =
            "INSERT INTO producto (id_restaurante, nombre, descripcion, precio, categoria) VALUES (?, ?, ?, ?, ?)";
    private static final String CONSULTAR_POR_ID_SQL =
            "SELECT id_producto, id_restaurante, nombre, descripcion, precio, categoria "
                    + "FROM producto WHERE id_producto = ?";
    private static final String LISTAR_TODOS_SQL =
            "SELECT id_producto, id_restaurante, nombre, descripcion, precio, categoria FROM producto";
    private static final String LISTAR_POR_RESTAURANTE_SQL =
            "SELECT id_producto, id_restaurante, nombre, descripcion, precio, categoria "
                    + "FROM producto WHERE id_restaurante = ?";
    private static final String ACTUALIZAR_SQL =
            "UPDATE producto SET id_restaurante = ?, nombre = ?, descripcion = ?, precio = ?, categoria = ? "
                    + "WHERE id_producto = ?";
    private static final String ELIMINAR_SQL =
            "DELETE FROM producto WHERE id_producto = ?";

    @Override
    public void insertar(Producto producto) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(
                     INSERTAR_SQL, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setInt(1, producto.getIdRestaurante());
            sentencia.setString(2, producto.getNombre());
            sentencia.setString(3, producto.getDescripcion());
            sentencia.setBigDecimal(4, producto.getPrecio());
            sentencia.setString(5, producto.getCategoria());
            sentencia.executeUpdate();
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (claves.next()) {
                    producto.setIdProducto(claves.getInt(1));
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al insertar el producto: " + excepcion.getMessage());
        }
    }

    @Override
    public Producto consultarPorId(int idProducto) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(CONSULTAR_POR_ID_SQL)) {
            sentencia.setInt(1, idProducto);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return mapearProducto(resultado);
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al consultar el producto por id: " + excepcion.getMessage());
        }
        return null;
    }

    @Override
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(LISTAR_TODOS_SQL);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                productos.add(mapearProducto(resultado));
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al listar los productos: " + excepcion.getMessage());
        }
        return productos;
    }

    @Override
    public List<Producto> listarPorRestaurante(int idRestaurante) {
        List<Producto> productos = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(LISTAR_POR_RESTAURANTE_SQL)) {
            sentencia.setInt(1, idRestaurante);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    productos.add(mapearProducto(resultado));
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al listar los productos del restaurante: " + excepcion.getMessage());
        }
        return productos;
    }

    @Override
    public void actualizar(Producto producto) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(ACTUALIZAR_SQL)) {
            sentencia.setInt(1, producto.getIdRestaurante());
            sentencia.setString(2, producto.getNombre());
            sentencia.setString(3, producto.getDescripcion());
            sentencia.setBigDecimal(4, producto.getPrecio());
            sentencia.setString(5, producto.getCategoria());
            sentencia.setInt(6, producto.getIdProducto());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            System.err.println("Error al actualizar el producto: " + excepcion.getMessage());
        }
    }

    @Override
    public void eliminar(int idProducto) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(ELIMINAR_SQL)) {
            sentencia.setInt(1, idProducto);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            System.err.println("Error al eliminar el producto: " + excepcion.getMessage());
        }
    }

    private Producto mapearProducto(ResultSet resultado) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(resultado.getInt("id_producto"));
        producto.setIdRestaurante(resultado.getInt("id_restaurante"));
        producto.setNombre(resultado.getString("nombre"));
        producto.setDescripcion(resultado.getString("descripcion"));
        producto.setPrecio(resultado.getBigDecimal("precio"));
        producto.setCategoria(resultado.getString("categoria"));
        return producto;
    }
}
