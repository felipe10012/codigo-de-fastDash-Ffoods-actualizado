package com.fastdash.dao.impl.jdbc;

import com.fastdash.dao.RestauranteDao;
import com.fastdash.model.Restaurante;
import com.fastdash.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RestauranteDaoImplJdbc implements RestauranteDao {

    @Override
    public void insertar(Restaurante restaurante) {
        String sql = "INSERT INTO restaurante (nombre, direccion, telefono) VALUES (?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, restaurante.getNombre());
            sentencia.setString(2, restaurante.getDireccion());
            sentencia.setString(3, restaurante.getTelefono());
            sentencia.executeUpdate();
            try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                if (llaves.next()) {
                    restaurante.setIdRestaurante(llaves.getInt(1));
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al insertar el restaurante: " + excepcion.getMessage());
        }
    }

    @Override
    public Restaurante consultarPorId(int idRestaurante) {
        String sql = "SELECT id_restaurante, nombre, direccion, telefono "
                + "FROM restaurante WHERE id_restaurante = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idRestaurante);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return mapearRestaurante(resultado);
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al consultar el restaurante por id: " + excepcion.getMessage());
        }
        return null;
    }

    @Override
    public List<Restaurante> listarTodos() {
        List<Restaurante> restaurantes = new ArrayList<>();
        String sql = "SELECT id_restaurante, nombre, direccion, telefono "
                + "FROM restaurante ORDER BY id_restaurante";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                restaurantes.add(mapearRestaurante(resultado));
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al listar los restaurantes: " + excepcion.getMessage());
        }
        return restaurantes;
    }

    @Override
    public void actualizar(Restaurante restaurante) {
        String sql = "UPDATE restaurante SET nombre = ?, direccion = ?, telefono = ? "
                + "WHERE id_restaurante = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, restaurante.getNombre());
            sentencia.setString(2, restaurante.getDireccion());
            sentencia.setString(3, restaurante.getTelefono());
            sentencia.setInt(4, restaurante.getIdRestaurante());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            System.err.println("Error al actualizar el restaurante: " + excepcion.getMessage());
        }
    }

    @Override
    public void eliminar(int idRestaurante) {
        String sql = "DELETE FROM restaurante WHERE id_restaurante = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idRestaurante);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            System.err.println("Error al eliminar el restaurante: " + excepcion.getMessage());
        }
    }

    private Restaurante mapearRestaurante(ResultSet resultado) throws SQLException {
        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(resultado.getInt("id_restaurante"));
        restaurante.setNombre(resultado.getString("nombre"));
        restaurante.setDireccion(resultado.getString("direccion"));
        restaurante.setTelefono(resultado.getString("telefono"));
        return restaurante;
    }
}
