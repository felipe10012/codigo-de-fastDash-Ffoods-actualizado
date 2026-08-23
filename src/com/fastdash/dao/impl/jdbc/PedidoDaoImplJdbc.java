package com.fastdash.dao.impl.jdbc;

import com.fastdash.dao.PedidoDao;
import com.fastdash.model.Pedido;
import com.fastdash.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PedidoDaoImplJdbc implements PedidoDao {

    private static final String COLUMNAS =
            "id_pedido, id_usuario, id_repartidor, fecha_pedido, total, estado";

    @Override
    public void insertar(Pedido pedido) {
        String sql = "INSERT INTO pedido (id_usuario, id_repartidor, fecha_pedido, total, estado) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setInt(1, pedido.getIdUsuario());
            asignarIdRepartidor(sentencia, 2, pedido.getIdRepartidor());
            sentencia.setTimestamp(3, pedido.getFechaPedido());
            sentencia.setBigDecimal(4, pedido.getTotal());
            sentencia.setString(5, pedido.getEstado());
            sentencia.executeUpdate();
            try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                if (llaves.next()) {
                    pedido.setIdPedido(llaves.getInt(1));
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al insertar el pedido: " + excepcion.getMessage());
        }
    }

    @Override
    public Pedido consultarPorId(int idPedido) {
        String sql = "SELECT " + COLUMNAS + " FROM pedido WHERE id_pedido = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
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
        String sql = "SELECT " + COLUMNAS + " FROM pedido ORDER BY id_pedido";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
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
        String sql = "SELECT " + COLUMNAS + " FROM pedido WHERE id_usuario = ? ORDER BY id_pedido";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
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
    public List<Pedido> listarPorRepartidor(int idRepartidor) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT " + COLUMNAS + " FROM pedido WHERE id_repartidor = ? ORDER BY id_pedido";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idRepartidor);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    pedidos.add(mapearPedido(resultado));
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al listar los pedidos del repartidor: " + excepcion.getMessage());
        }
        return pedidos;
    }

    @Override
    public void actualizar(Pedido pedido) {
        String sql = "UPDATE pedido SET id_usuario = ?, id_repartidor = ?, fecha_pedido = ?, "
                + "total = ?, estado = ? WHERE id_pedido = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, pedido.getIdUsuario());
            asignarIdRepartidor(sentencia, 2, pedido.getIdRepartidor());
            sentencia.setTimestamp(3, pedido.getFechaPedido());
            sentencia.setBigDecimal(4, pedido.getTotal());
            sentencia.setString(5, pedido.getEstado());
            sentencia.setInt(6, pedido.getIdPedido());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            System.err.println("Error al actualizar el pedido: " + excepcion.getMessage());
        }
    }

    @Override
    public void eliminar(int idPedido) {
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idPedido);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            System.err.println("Error al eliminar el pedido: " + excepcion.getMessage());
        }
    }

    private void asignarIdRepartidor(PreparedStatement sentencia, int indice, int idRepartidor) throws SQLException {
        if (idRepartidor > 0) {
            sentencia.setInt(indice, idRepartidor);
        } else {
            sentencia.setNull(indice, Types.INTEGER);
        }
    }

    private Pedido mapearPedido(ResultSet resultado) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(resultado.getInt("id_pedido"));
        pedido.setIdUsuario(resultado.getInt("id_usuario"));
        pedido.setIdRepartidor(resultado.getInt("id_repartidor"));
        pedido.setFechaPedido(resultado.getTimestamp("fecha_pedido"));
        pedido.setTotal(resultado.getBigDecimal("total"));
        pedido.setEstado(resultado.getString("estado"));
        return pedido;
    }
}
