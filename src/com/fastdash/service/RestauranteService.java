package com.fastdash.service;

import com.fastdash.dao.DaoFactory;
import com.fastdash.dao.RestauranteDao;
import com.fastdash.model.Restaurante;
import com.fastdash.util.Validaciones;

import java.util.List;

public class RestauranteService {

    private final RestauranteDao restauranteDao;

    public RestauranteService() {
        this.restauranteDao = DaoFactory.crearRestauranteDao();
    }

    public void registrar(Restaurante restaurante) {
        if (!datosValidos(restaurante)) {
            return;
        }
        restauranteDao.insertar(restaurante);
    }

    public Restaurante consultarPorId(int idRestaurante) {
        return restauranteDao.consultarPorId(idRestaurante);
    }

    public List<Restaurante> listarTodos() {
        return restauranteDao.listarTodos();
    }

    public void actualizar(Restaurante restaurante) {
        if (!datosValidos(restaurante)) {
            return;
        }
        restauranteDao.actualizar(restaurante);
    }

    public void eliminar(int idRestaurante) {
        restauranteDao.eliminar(idRestaurante);
    }

    private boolean datosValidos(Restaurante restaurante) {
        if (!Validaciones.textoValido(restaurante.getNombre())) {
            System.out.println("El nombre del restaurante es obligatorio.");
            return false;
        }
        return true;
    }
}
