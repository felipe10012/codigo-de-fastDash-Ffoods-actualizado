package com.fastdash.service;

import com.fastdash.dao.PedidoDao;
import com.fastdash.dao.impl.PedidoDaoImpl;
import com.fastdash.model.Pedido;

import java.util.List;

public class PedidoService {

    private final PedidoDao pedidoDao;

    public PedidoService() {
        this.pedidoDao = new PedidoDaoImpl();
    }

    public void registrar(Pedido pedido) {
        pedidoDao.insertar(pedido);
    }

    public Pedido consultarPorId(int idPedido) {
        return pedidoDao.consultarPorId(idPedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoDao.listarTodos();
    }

    public List<Pedido> listarPorUsuario(int idUsuario) {
        return pedidoDao.listarPorUsuario(idUsuario);
    }

    public List<Pedido> listarPorRepartidor(int idRepartidor) {
        return pedidoDao.listarPorRepartidor(idRepartidor);
    }

    public void actualizar(Pedido pedido) {
        pedidoDao.actualizar(pedido);
    }

    public void eliminar(int idPedido) {
        pedidoDao.eliminar(idPedido);
    }
}
