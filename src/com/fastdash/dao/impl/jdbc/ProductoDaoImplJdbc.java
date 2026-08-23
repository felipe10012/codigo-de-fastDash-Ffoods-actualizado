package com.fastdash.dao.impl.jdbc;

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

public class ProductoDaoImplJdbc implements ProductoDao {

    @Override
    public void insertar(Producto producto) {
        String sql = "INSERT INTO producto (id_restaurante, nombre, descripcion, precio, categoria) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setInt(1, producto.getIdRestaurante());
            sentencia.setString(2, producto.getNombre());
            sentencia.setString(3, producto.getDescripcion());
            sentencia.setBigDecimal(4, producto.getPrecio());
            sentencia.setString(5, producto.getCategoria());
            sentencia.executeUpdate();
            try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                if (llaves.next()) {
                    producto.setIdProducto(llaves.getInt(1));
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al insertar el producto: " + excepcion.getMessage());
        }
    }

    @Override
    public Producto consultarPorId(int idProducto) {
        String sql = "SELECT id_producto, id_restaurante, nombre, descripcion, precio, categoria "
                + "FROM producto WHERE id_producto = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
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
        String sql = "SELECT id_producto, id_restaurante, nombre, descripcion, precio, categoria "
                + "FROM producto ORDER BY id_producto";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
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
        String sql = "SELECT id_producto, id_restaurante, nombre, descripcion, precio, categoria "
                + "FROM producto WHERE id_restaurante = ? ORDER BY id_producto";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
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
        String sql = "UPDATE producto SET id_restaurante = ?, nombre = ?, descripcion = ?, "
                + "precio = ?, categoria = ? WHERE id_producto = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
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
        String sql = "DELETE FROM producto WHERE id_producto = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
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
