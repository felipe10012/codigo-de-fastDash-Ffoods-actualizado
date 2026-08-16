package com.fastdash.dao.impl;

import com.fastdash.dao.UsuarioDao;
import com.fastdash.model.Usuario;
import com.fastdash.util.ConexionSupabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDaoImpl implements UsuarioDao {

    private static final String TABLA = "usuario";
    private final Gson gson = new Gson();

    @Override
    public void insertar(Usuario usuario) {
        try {
            JsonObject cuerpo = new JsonObject();
            cuerpo.addProperty("nombre_completo", usuario.getNombreCompleto());
            cuerpo.addProperty("email", usuario.getEmail());
            cuerpo.addProperty("contrasena", usuario.getContrasena());
            if (usuario.getRol() != null) {
                cuerpo.addProperty("rol", usuario.getRol());
            }
            String respuesta = ConexionSupabase.insertar(TABLA, cuerpo.toString(), true);
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null && filas.size() > 0) {
                usuario.setIdUsuario(filas.get(0).getAsJsonObject().get("id_usuario").getAsInt());
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al insertar el usuario: " + excepcion.getMessage());
        }
    }

    @Override
    public Usuario consultarPorId(int idUsuario) {
        try {
            String respuesta = ConexionSupabase.get(
                    TABLA, "select=*&id_usuario=eq." + ConexionSupabase.codificar(String.valueOf(idUsuario)));
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null && filas.size() > 0) {
                return mapearUsuario(filas.get(0).getAsJsonObject());
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al consultar el usuario por id: " + excepcion.getMessage());
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            String respuesta = ConexionSupabase.get(TABLA, "select=*&order=id_usuario");
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null) {
                for (int i = 0; i < filas.size(); i++) {
                    usuarios.add(mapearUsuario(filas.get(i).getAsJsonObject()));
                }
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al listar los usuarios: " + excepcion.getMessage());
        }
        return usuarios;
    }

    @Override
    public Usuario consultarPorEmail(String email) {
        try {
            String respuesta = ConexionSupabase.get(
                    TABLA, "select=*&email=eq." + ConexionSupabase.codificar(email));
            JsonArray filas = gson.fromJson(respuesta, JsonArray.class);
            if (filas != null && filas.size() > 0) {
                return mapearUsuario(filas.get(0).getAsJsonObject());
            }
        } catch (RuntimeException excepcion) {
            System.err.println("Error al consultar el usuario por email: " + excepcion.getMessage());
        }
        return null;
    }

    @Override
    public void actualizar(Usuario usuario) {
        try {
            JsonObject cuerpo = new JsonObject();
            cuerpo.addProperty("nombre_completo", usuario.getNombreCompleto());
            cuerpo.addProperty("email", usuario.getEmail());
            cuerpo.addProperty("contrasena", usuario.getContrasena());
            if (usuario.getRol() != null) {
                cuerpo.addProperty("rol", usuario.getRol());
            }
            String filtro = "id_usuario=eq." + ConexionSupabase.codificar(String.valueOf(usuario.getIdUsuario()));
            ConexionSupabase.actualizar(TABLA, filtro, cuerpo.toString());
        } catch (RuntimeException excepcion) {
            System.err.println("Error al actualizar el usuario: " + excepcion.getMessage());
        }
    }

    @Override
    public void eliminar(int idUsuario) {
        try {
            String filtro = "id_usuario=eq." + ConexionSupabase.codificar(String.valueOf(idUsuario));
            ConexionSupabase.eliminar(TABLA, filtro);
        } catch (RuntimeException excepcion) {
            System.err.println("Error al eliminar el usuario: " + excepcion.getMessage());
        }
    }

    private Usuario mapearUsuario(JsonObject fila) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(fila.get("id_usuario").getAsInt());
        usuario.setNombreCompleto(texto(fila, "nombre_completo"));
        usuario.setEmail(texto(fila, "email"));
        usuario.setContrasena(texto(fila, "contrasena"));
        usuario.setRol(texto(fila, "rol"));
        return usuario;
    }

    private String texto(JsonObject fila, String columna) {
        return fila.has(columna) && !fila.get(columna).isJsonNull() ? fila.get(columna).getAsString() : null;
    }
}
