package com.fastdash.service;

import com.fastdash.dao.UsuarioDao;
import com.fastdash.dao.impl.UsuarioDaoImpl;
import com.fastdash.model.Usuario;

import java.util.List;

public class UsuarioService {

    private final UsuarioDao usuarioDao;

    public UsuarioService() {
        this.usuarioDao = new UsuarioDaoImpl();
    }

    public void registrar(Usuario usuario) {
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
        usuarioDao.actualizar(usuario);
    }

    public void eliminar(int idUsuario) {
        usuarioDao.eliminar(idUsuario);
    }
}
