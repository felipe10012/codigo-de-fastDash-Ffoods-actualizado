package com.fastdash.dao;

import com.fastdash.model.Pedido;
import java.util.List;

public interface PedidoDao {

    void insertar(Pedido pedido);

    Pedido consultarPorId(int idPedido);

    List<Pedido> listarTodos();

    List<Pedido> listarPorUsuario(int idUsuario);

    List<Pedido> listarPorRepartidor(int idRepartidor);

    void actualizar(Pedido pedido);

    void eliminar(int idPedido);
}
