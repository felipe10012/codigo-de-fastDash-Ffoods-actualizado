package com.fastdash.service;

import com.fastdash.dao.DaoFactory;
import com.fastdash.dao.ProductoDao;
import com.fastdash.model.Producto;
import com.fastdash.util.Validaciones;

import java.util.List;

public class ProductoService {

    private final ProductoDao productoDao;

    public ProductoService() {
        this.productoDao = DaoFactory.crearProductoDao();
    }

    public void registrar(Producto producto) {
        if (!datosValidos(producto)) {
            return;
        }
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
        if (!datosValidos(producto)) {
            return;
        }
        productoDao.actualizar(producto);
    }

    public void eliminar(int idProducto) {
        productoDao.eliminar(idProducto);
    }

    private boolean datosValidos(Producto producto) {
        if (!Validaciones.enteroPositivo(producto.getIdRestaurante())) {
            System.out.println("El id del restaurante debe ser un numero mayor a cero.");
            return false;
        }
        if (!Validaciones.textoValido(producto.getNombre())) {
            System.out.println("El nombre del producto es obligatorio.");
            return false;
        }
        if (!Validaciones.decimalNoNegativo(producto.getPrecio())) {
            System.out.println("El precio debe ser un valor mayor o igual a cero.");
            return false;
        }
        return true;
    }
}
