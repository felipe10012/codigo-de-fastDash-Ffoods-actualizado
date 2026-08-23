package com.fastdash.service;

import com.fastdash.dao.DaoFactory;
import com.fastdash.dao.RepartidorDao;
import com.fastdash.model.Repartidor;
import com.fastdash.util.Validaciones;

import java.util.List;

public class RepartidorService {

    private final RepartidorDao repartidorDao;

    public RepartidorService() {
        this.repartidorDao = DaoFactory.crearRepartidorDao();
    }

    public void registrar(Repartidor repartidor) {
        if (!datosValidos(repartidor)) {
            return;
        }
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
        if (!datosValidos(repartidor)) {
            return;
        }
        repartidorDao.actualizar(repartidor);
    }

    public void eliminar(int idRepartidor) {
        repartidorDao.eliminar(idRepartidor);
    }

    private boolean datosValidos(Repartidor repartidor) {
        if (!Validaciones.enteroPositivo(repartidor.getIdRestaurante())) {
            System.out.println("El id del restaurante debe ser un numero mayor a cero.");
            return false;
        }
        if (!Validaciones.textoValido(repartidor.getNombre())) {
            System.out.println("El nombre del repartidor es obligatorio.");
            return false;
        }
        return true;
    }
}
