package com.fastdash.dao;

import com.fastdash.dao.impl.jdbc.PedidoDaoImplJdbc;
import com.fastdash.dao.impl.jdbc.ProductoDaoImplJdbc;
import com.fastdash.dao.impl.jdbc.RepartidorDaoImplJdbc;
import com.fastdash.dao.impl.jdbc.RestauranteDaoImplJdbc;
import com.fastdash.dao.impl.jdbc.UsuarioDaoImplJdbc;

public class DaoFactory {

    public static RestauranteDao crearRestauranteDao() {
        return new RestauranteDaoImplJdbc();
    }

    public static ProductoDao crearProductoDao() {
        return new ProductoDaoImplJdbc();
    }

    public static UsuarioDao crearUsuarioDao() {
        return new UsuarioDaoImplJdbc();
    }

    public static PedidoDao crearPedidoDao() {
        return new PedidoDaoImplJdbc();
    }

    public static RepartidorDao crearRepartidorDao() {
        return new RepartidorDaoImplJdbc();
    }
}
