package com.fastdash.dao.impl.jdbc;

import com.fastdash.dao.RepartidorDao;
import com.fastdash.model.Repartidor;
import com.fastdash.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDaoImplJdbc implements RepartidorDao {

    @Override
    public void insertar(Repartidor repartidor) {
        String sql = "INSERT INTO repartidor (id_restaurante, nombre, telefono, vehiculo) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setInt(1, repartidor.getIdRestaurante());
            sentencia.setString(2, repartidor.getNombre());
            sentencia.setString(3, repartidor.getTelefono());
            sentencia.setString(4, repartidor.getVehiculo());
            sentencia.executeUpdate();
            try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                if (llaves.next()) {
                    repartidor.setIdRepartidor(llaves.getInt(1));
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al insertar el repartidor: " + excepcion.getMessage());
        }
    }

    @Override
    public Repartidor consultarPorId(int idRepartidor) {
        String sql = "SELECT id_repartidor, id_restaurante, nombre, telefono, vehiculo "
                + "FROM repartidor WHERE id_repartidor = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idRepartidor);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return mapearRepartidor(resultado);
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al consultar el repartidor por id: " + excepcion.getMessage());
        }
        return null;
    }

    @Override
    public List<Repartidor> listarTodos() {
        List<Repartidor> repartidores = new ArrayList<>();
        String sql = "SELECT id_repartidor, id_restaurante, nombre, telefono, vehiculo "
                + "FROM repartidor ORDER BY id_repartidor";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                repartidores.add(mapearRepartidor(resultado));
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al listar los repartidores: " + excepcion.getMessage());
        }
        return repartidores;
    }

    @Override
    public List<Repartidor> listarPorRestaurante(int idRestaurante) {
        List<Repartidor> repartidores = new ArrayList<>();
        String sql = "SELECT id_repartidor, id_restaurante, nombre, telefono, vehiculo "
                + "FROM repartidor WHERE id_restaurante = ? ORDER BY id_repartidor";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idRestaurante);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    repartidores.add(mapearRepartidor(resultado));
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al listar los repartidores del restaurante: " + excepcion.getMessage());
        }
        return repartidores;
    }

    @Override
    public void actualizar(Repartidor repartidor) {
        String sql = "UPDATE repartidor SET id_restaurante = ?, nombre = ?, telefono = ?, vehiculo = ? "
                + "WHERE id_repartidor = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, repartidor.getIdRestaurante());
            sentencia.setString(2, repartidor.getNombre());
            sentencia.setString(3, repartidor.getTelefono());
            sentencia.setString(4, repartidor.getVehiculo());
            sentencia.setInt(5, repartidor.getIdRepartidor());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            System.err.println("Error al actualizar el repartidor: " + excepcion.getMessage());
        }
    }

    @Override
    public void eliminar(int idRepartidor) {
        String sql = "DELETE FROM repartidor WHERE id_repartidor = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idRepartidor);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            System.err.println("Error al eliminar el repartidor: " + excepcion.getMessage());
        }
    }

    private Repartidor mapearRepartidor(ResultSet resultado) throws SQLException {
        Repartidor repartidor = new Repartidor();
        repartidor.setIdRepartidor(resultado.getInt("id_repartidor"));
        repartidor.setIdRestaurante(resultado.getInt("id_restaurante"));
        repartidor.setNombre(resultado.getString("nombre"));
        repartidor.setTelefono(resultado.getString("telefono"));
        repartidor.setVehiculo(resultado.getString("vehiculo"));
        return repartidor;
    }
}
