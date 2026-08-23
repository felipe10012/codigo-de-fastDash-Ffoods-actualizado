package com.fastdash.dao;

import com.fastdash.dao.impl.PedidoDaoImpl;
import com.fastdash.dao.impl.ProductoDaoImpl;
import com.fastdash.dao.impl.RepartidorDaoImpl;
import com.fastdash.dao.impl.RestauranteDaoImpl;
import com.fastdash.dao.impl.UsuarioDaoImpl;
import com.fastdash.dao.impl.jdbc.PedidoDaoImplJdbc;
import com.fastdash.dao.impl.jdbc.ProductoDaoImplJdbc;
import com.fastdash.dao.impl.jdbc.RepartidorDaoImplJdbc;
import com.fastdash.dao.impl.jdbc.RestauranteDaoImplJdbc;
import com.fastdash.dao.impl.jdbc.UsuarioDaoImplJdbc;

public class DaoFactory {

    // Implementacion activa: "jdbc" (MySQL local) o "supabase" (REST API).
    // Se puede cambiar por linea de comandos con:
    //   java -Dfastdash.dao=supabase -jar app.jar
    private static final String DAO_ACTIVO =
            System.getProperty("fastdash.dao", "jdbc");

    private DaoFactory() {
    }

    public static RestauranteDao crearRestauranteDao() {
        return DAO_ACTIVO.equalsIgnoreCase("supabase")
                ? new RestauranteDaoImpl()
                : new RestauranteDaoImplJdbc();
    }

    public static ProductoDao crearProductoDao() {
        return DAO_ACTIVO.equalsIgnoreCase("supabase")
                ? new ProductoDaoImpl()
                : new ProductoDaoImplJdbc();
    }

    public static UsuarioDao crearUsuarioDao() {
        return DAO_ACTIVO.equalsIgnoreCase("supabase")
                ? new UsuarioDaoImpl()
                : new UsuarioDaoImplJdbc();
    }

    public static PedidoDao crearPedidoDao() {
        return DAO_ACTIVO.equalsIgnoreCase("supabase")
                ? new PedidoDaoImpl()
                : new PedidoDaoImplJdbc();
    }

    public static RepartidorDao crearRepartidorDao() {
        return DAO_ACTIVO.equalsIgnoreCase("supabase")
                ? new RepartidorDaoImpl()
                : new RepartidorDaoImplJdbc();
    }
}
