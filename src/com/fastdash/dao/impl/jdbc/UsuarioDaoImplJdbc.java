package com.fastdash.dao.impl.jdbc;

import com.fastdash.dao.UsuarioDao;
import com.fastdash.model.Usuario;
import com.fastdash.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDaoImplJdbc implements UsuarioDao {

    @Override
    public void insertar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre_completo, email, contrasena, rol) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, usuario.getNombreCompleto());
            sentencia.setString(2, usuario.getEmail());
            sentencia.setString(3, usuario.getContrasena());
            sentencia.setString(4, usuario.getRol());
            sentencia.executeUpdate();
            try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                if (llaves.next()) {
                    usuario.setIdUsuario(llaves.getInt(1));
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al insertar el usuario: " + excepcion.getMessage());
        }
    }

    @Override
    public Usuario consultarPorId(int idUsuario) {
        String sql = "SELECT id_usuario, nombre_completo, email, contrasena, rol "
                + "FROM usuario WHERE id_usuario = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idUsuario);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return mapearUsuario(resultado);
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al consultar el usuario por id: " + excepcion.getMessage());
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre_completo, email, contrasena, rol "
                + "FROM usuario ORDER BY id_usuario";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                usuarios.add(mapearUsuario(resultado));
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al listar los usuarios: " + excepcion.getMessage());
        }
        return usuarios;
    }

    @Override
    public Usuario consultarPorEmail(String email) {
        String sql = "SELECT id_usuario, nombre_completo, email, contrasena, rol "
                + "FROM usuario WHERE email = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, email);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return mapearUsuario(resultado);
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al consultar el usuario por email: " + excepcion.getMessage());
        }
        return null;
    }

    @Override
    public void actualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nombre_completo = ?, email = ?, contrasena = ?, rol = ? "
                + "WHERE id_usuario = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, usuario.getNombreCompleto());
            sentencia.setString(2, usuario.getEmail());
            sentencia.setString(3, usuario.getContrasena());
            sentencia.setString(4, usuario.getRol());
            sentencia.setInt(5, usuario.getIdUsuario());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            System.err.println("Error al actualizar el usuario: " + excepcion.getMessage());
        }
    }

    @Override
    public void eliminar(int idUsuario) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idUsuario);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            System.err.println("Error al eliminar el usuario: " + excepcion.getMessage());
        }
    }

    private Usuario mapearUsuario(ResultSet resultado) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(resultado.getInt("id_usuario"));
        usuario.setNombreCompleto(resultado.getString("nombre_completo"));
        usuario.setEmail(resultado.getString("email"));
        usuario.setContrasena(resultado.getString("contrasena"));
        usuario.setRol(resultado.getString("rol"));
        return usuario;
    }
}
