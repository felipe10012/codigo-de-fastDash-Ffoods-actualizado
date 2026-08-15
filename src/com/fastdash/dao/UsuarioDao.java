package com.fastdash.dao;

import com.fastdash.model.Usuario;
import java.util.List;

public interface UsuarioDao {

    void insertar(Usuario usuario);

    Usuario consultarPorId(int idUsuario);

    List<Usuario> listarTodos();

    Usuario consultarPorEmail(String email);

    void actualizar(Usuario usuario);

    void eliminar(int idUsuario);
}
