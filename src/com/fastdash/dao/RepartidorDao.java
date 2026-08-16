package com.fastdash.dao;

import com.fastdash.model.Repartidor;
import java.util.List;

public interface RepartidorDao {

    void insertar(Repartidor repartidor);

    Repartidor consultarPorId(int idRepartidor);

    List<Repartidor> listarTodos();

    List<Repartidor> listarPorRestaurante(int idRestaurante);

    void actualizar(Repartidor repartidor);

    void eliminar(int idRepartidor);
}
