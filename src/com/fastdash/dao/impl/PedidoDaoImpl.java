package com.fastdash.dao.impl;

import com.fastdash.dao.PedidoDao;
import com.fastdash.model.Pedido;
import com.fastdash.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDaoImpl implements PedidoDao {

    private static final String INSERTAR_SQL =
            "INSERT INTO pedido (id_usuario, fecha_pedido, total, estado) VALUES (?, ?, ?, ?)";
    private static final String CONSULTAR_POR_ID_SQL =
            "SELECT id_pedido, id_usuario, fecha_pedido, total, estado FROM pedido WHERE id_pedido = ?";
    private static final String LISTAR_TODOS_SQL =
            "SELECT id_pedido, id_usuario, fecha_pedido, total, estado FROM pedido";
    private static final String LISTAR_POR_USUARIO_SQL =
            "SELECT id_pedido, id_usuario, fecha_pedido, total, estado FROM pedido WHERE id_usuario = ?";
    private static final String ACTUALIZAR_SQL =
            "UPDATE pedido SET id_usuario = ?, fecha_pedido = ?, total = ?, estado = ? WHERE id_pedido = ?";
    private static final String ELIMINAR_SQL =
            "DELETE FROM pedido WHERE id_pedido = ?";

    @Override
    public void insertar(Pedido pedido) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(
                     INSERTAR_SQL, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setInt(1, pedido.getIdUsuario());
            sentencia.setTimestamp(2, pedido.getFechaPedido());
            sentencia.setBigDecimal(3, pedido.getTotal());
            sentencia.setString(4, pedido.getEstado());
            sentencia.executeUpdate();
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (claves.next()) {
                    pedido.setIdPedido(claves.getInt(1));
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al insertar el pedido: " + excepcion.getMessage());
        }
    }

    @Override
    public Pedido consultarPorId(int idPedido) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(CONSULTAR_POR_ID_SQL)) {
            sentencia.setInt(1, idPedido);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return mapearPedido(resultado);
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al consultar el pedido por id: " + excepcion.getMessage());
        }
        return null;
    }

    @Override
    public List<Pedido> listarTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(LISTAR_TODOS_SQL);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                pedidos.add(mapearPedido(resultado));
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al listar los pedidos: " + excepcion.getMessage());
        }
        return pedidos;
    }

    @Override
    public List<Pedido> listarPorUsuario(int idUsuario) {
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(LISTAR_POR_USUARIO_SQL)) {
            sentencia.setInt(1, idUsuario);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    pedidos.add(mapearPedido(resultado));
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al listar los pedidos del usuario: " + excepcion.getMessage());
        }
        return pedidos;
    }

    @Override
    public void actualizar(Pedido pedido) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(ACTUALIZAR_SQL)) {
            sentencia.setInt(1, pedido.getIdUsuario());
            sentencia.setTimestamp(2, pedido.getFechaPedido());
            sentencia.setBigDecimal(3, pedido.getTotal());
            sentencia.setString(4, pedido.getEstado());
            sentencia.setInt(5, pedido.getIdPedido());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            System.err.println("Error al actualizar el pedido: " + excepcion.getMessage());
        }
    }

    @Override
    public void eliminar(int idPedido) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(ELIMINAR_SQL)) {
            sentencia.setInt(1, idPedido);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            System.err.println("Error al eliminar el pedido: " + excepcion.getMessage());
        }
    }

    private Pedido mapearPedido(ResultSet resultado) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(resultado.getInt("id_pedido"));
        pedido.setIdUsuario(resultado.getInt("id_usuario"));
        pedido.setFechaPedido(resultado.getTimestamp("fecha_pedido"));
        pedido.setTotal(resultado.getBigDecimal("total"));
        pedido.setEstado(resultado.getString("estado"));
        return pedido;
    }
}
