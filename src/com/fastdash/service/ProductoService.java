package com.fastdash.service;

import com.fastdash.dao.ProductoDao;
import com.fastdash.dao.impl.ProductoDaoImpl;
import com.fastdash.model.Producto;

import java.util.List;

public class ProductoService {

    private final ProductoDao productoDao;

    public ProductoService() {
        this.productoDao = new ProductoDaoImpl();
    }

    public void registrar(Producto producto) {
        productoDao.insertar(producto);
    }

    public Producto consultarPorId(int idProducto) {
        return productoDao.consultarPorId(idProducto);
    }

    public List<Producto> listarTodos() {
        return productoDao.listarTodos();
    }

    public List<Producto> listarPorRestaurante(int idRestaurante) {
        return productoDao.listarPorRestaurante(idRestaurante);
    }

    public void actualizar(Producto producto) {
        productoDao.actualizar(producto);
    }

    public void eliminar(int idProducto) {
        productoDao.eliminar(idProducto);
    }
}
