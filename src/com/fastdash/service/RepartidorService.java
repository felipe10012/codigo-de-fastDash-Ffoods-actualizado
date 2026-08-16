package com.fastdash.service;

import com.fastdash.dao.RepartidorDao;
import com.fastdash.dao.impl.RepartidorDaoImpl;
import com.fastdash.model.Repartidor;

import java.util.List;

public class RepartidorService {

    private final RepartidorDao repartidorDao;

    public RepartidorService() {
        this.repartidorDao = new RepartidorDaoImpl();
    }

    public void registrar(Repartidor repartidor) {
        repartidorDao.insertar(repartidor);
    }

    public Repartidor consultarPorId(int idRepartidor) {
        return repartidorDao.consultarPorId(idRepartidor);
    }

    public List<Repartidor> listarTodos() {
        return repartidorDao.listarTodos();
    }

    public List<Repartidor> listarPorRestaurante(int idRestaurante) {
        return repartidorDao.listarPorRestaurante(idRestaurante);
    }

    public void actualizar(Repartidor repartidor) {
        repartidorDao.actualizar(repartidor);
    }

    public void eliminar(int idRepartidor) {
        repartidorDao.eliminar(idRepartidor);
    }
}
