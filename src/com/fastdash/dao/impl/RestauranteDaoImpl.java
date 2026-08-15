package com.fastdash.dao.impl;

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

public class RestauranteDaoImpl implements RestauranteDao {

    private static final String INSERTAR_SQL =
            "INSERT INTO restaurante (nombre, direccion, telefono) VALUES (?, ?, ?)";
    private static final String CONSULTAR_POR_ID_SQL =
            "SELECT id_restaurante, nombre, direccion, telefono FROM restaurante WHERE id_restaurante = ?";
    private static final String LISTAR_TODOS_SQL =
            "SELECT id_restaurante, nombre, direccion, telefono FROM restaurante";
    private static final String ACTUALIZAR_SQL =
            "UPDATE restaurante SET nombre = ?, direccion = ?, telefono = ? WHERE id_restaurante = ?";
    private static final String ELIMINAR_SQL =
            "DELETE FROM restaurante WHERE id_restaurante = ?";

    @Override
    public void insertar(Restaurante restaurante) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(
                     INSERTAR_SQL, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, restaurante.getNombre());
            sentencia.setString(2, restaurante.getDireccion());
            sentencia.setString(3, restaurante.getTelefono());
            sentencia.executeUpdate();
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (claves.next()) {
                    restaurante.setIdRestaurante(claves.getInt(1));
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al insertar el restaurante: " + excepcion.getMessage());
        }
    }

    @Override
    public Restaurante consultarPorId(int idRestaurante) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(CONSULTAR_POR_ID_SQL)) {
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
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(LISTAR_TODOS_SQL);
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
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(ACTUALIZAR_SQL)) {
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
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(ELIMINAR_SQL)) {
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
