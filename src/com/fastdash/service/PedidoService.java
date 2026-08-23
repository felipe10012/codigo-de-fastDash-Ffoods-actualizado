package com.fastdash.service;

import com.fastdash.dao.DaoFactory;
import com.fastdash.dao.PedidoDao;
import com.fastdash.model.Pedido;
import com.fastdash.util.Validaciones;

import java.util.Arrays;
import java.util.List;

public class PedidoService {

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList(
            "pendiente", "en_preparacion", "enviado", "entregado", "cancelado");

    private final PedidoDao pedidoDao;

    public PedidoService() {
        this.pedidoDao = DaoFactory.crearPedidoDao();
    }

    public void registrar(Pedido pedido) {
        if (!datosValidos(pedido)) {
            return;
        }
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
        if (!datosValidos(pedido)) {
            return;
        }
        pedidoDao.actualizar(pedido);
    }

    public void eliminar(int idPedido) {
        pedidoDao.eliminar(idPedido);
    }

    private boolean datosValidos(Pedido pedido) {
        if (!Validaciones.enteroPositivo(pedido.getIdUsuario())) {
            System.out.println("El id del usuario debe ser un numero mayor a cero.");
            return false;
        }
        if (!Validaciones.decimalNoNegativo(pedido.getTotal())) {
            System.out.println("El total debe ser un valor mayor o igual a cero.");
            return false;
        }
        String estado = pedido.getEstado() == null ? "" : pedido.getEstado().trim().toLowerCase();
        if (!ESTADOS_VALIDOS.contains(estado)) {
            System.out.println("El estado debe ser uno de: " + ESTADOS_VALIDOS + ".");
            return false;
        }
        pedido.setEstado(estado);
        return true;
    }
}
