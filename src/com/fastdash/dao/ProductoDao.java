package com.fastdash.dao;

import com.fastdash.model.Producto;
import java.util.List;

public interface ProductoDao {

    void insertar(Producto producto);

    Producto consultarPorId(int idProducto);

    List<Producto> listarTodos();

    List<Producto> listarPorRestaurante(int idRestaurante);

    void actualizar(Producto producto);

    void eliminar(int idProducto);
}
