package com.fastdash.service;

import com.fastdash.dao.RestauranteDao;
import com.fastdash.dao.impl.RestauranteDaoImpl;
import com.fastdash.model.Restaurante;

import java.util.List;

public class RestauranteService {

    private final RestauranteDao restauranteDao;

    public RestauranteService() {
        this.restauranteDao = new RestauranteDaoImpl();
    }

    public void registrar(Restaurante restaurante) {
        restauranteDao.insertar(restaurante);
    }

    public Restaurante consultarPorId(int idRestaurante) {
        return restauranteDao.consultarPorId(idRestaurante);
    }

    public List<Restaurante> listarTodos() {
        return restauranteDao.listarTodos();
    }

    public void actualizar(Restaurante restaurante) {
        restauranteDao.actualizar(restaurante);
    }

    public void eliminar(int idRestaurante) {
        restauranteDao.eliminar(idRestaurante);
    }
}
