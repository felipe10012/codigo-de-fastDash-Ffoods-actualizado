package com.fastdash.dao;

import com.fastdash.model.Restaurante;
import java.util.List;

public interface RestauranteDao {

    void insertar(Restaurante restaurante);

    Restaurante consultarPorId(int idRestaurante);

    List<Restaurante> listarTodos();

    void actualizar(Restaurante restaurante);

    void eliminar(int idRestaurante);
}
