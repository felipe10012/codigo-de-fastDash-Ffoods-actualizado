package com.fastdash.service;

import com.fastdash.dao.DaoFactory;
import com.fastdash.dao.UsuarioDao;
import com.fastdash.model.Usuario;
import com.fastdash.util.Validaciones;

import java.util.List;

public class UsuarioService {

    private static final int LONGITUD_MINIMA_CONTRASENA = 6;
    private static final String ROL_CLIENTE = "cliente";
    private static final String ROL_ADMIN = "admin";

    private final UsuarioDao usuarioDao;

    public UsuarioService() {
        this.usuarioDao = DaoFactory.crearUsuarioDao();
    }

    public void registrar(Usuario usuario) {
        if (!datosValidos(usuario)) {
            return;
        }
        usuarioDao.insertar(usuario);
    }

    public Usuario consultarPorId(int idUsuario) {
        return usuarioDao.consultarPorId(idUsuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioDao.listarTodos();
    }

    public Usuario consultarPorEmail(String email) {
        return usuarioDao.consultarPorEmail(email);
    }

    public void actualizar(Usuario usuario) {
        if (!datosValidos(usuario)) {
            return;
        }
        usuarioDao.actualizar(usuario);
    }

    public void eliminar(int idUsuario) {
        usuarioDao.eliminar(idUsuario);
    }

    private boolean datosValidos(Usuario usuario) {
        if (!Validaciones.textoValido(usuario.getNombreCompleto())) {
            System.out.println("El nombre completo del usuario es obligatorio.");
            return false;
        }
        if (!Validaciones.emailValido(usuario.getEmail())) {
            System.out.println("El email no tiene un formato valido (ejemplo: nombre@dominio.com).");
            return false;
        }
        if (usuario.getContrasena() == null || usuario.getContrasena().length() < LONGITUD_MINIMA_CONTRASENA) {
            System.out.println("La contrasena debe tener al menos " + LONGITUD_MINIMA_CONTRASENA + " caracteres.");
            return false;
        }
        String rol = usuario.getRol() == null ? "" : usuario.getRol().trim().toLowerCase();
        if (!rol.equals(ROL_CLIENTE) && !rol.equals(ROL_ADMIN)) {
            System.out.println("El rol solo puede ser 'cliente' o 'admin'.");
            return false;
        }
        usuario.setRol(rol);
        return true;
    }
}
