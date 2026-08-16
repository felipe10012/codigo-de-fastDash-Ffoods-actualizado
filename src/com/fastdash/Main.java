package com.fastdash;

import com.fastdash.model.Pedido;
import com.fastdash.model.Producto;
import com.fastdash.model.Repartidor;
import com.fastdash.model.Restaurante;
import com.fastdash.model.Usuario;
import com.fastdash.service.PedidoService;
import com.fastdash.service.ProductoService;
import com.fastdash.service.RepartidorService;
import com.fastdash.service.RestauranteService;
import com.fastdash.service.UsuarioService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner TECLADO = new Scanner(System.in);
    private static final UsuarioService usuarioService = new UsuarioService();
    private static final RestauranteService restauranteService = new RestauranteService();
    private static final ProductoService productoService = new ProductoService();
    private static final PedidoService pedidoService = new PedidoService();
    private static final RepartidorService repartidorService = new RepartidorService();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    menuRestaurantes();
                    break;
                case 2:
                    menuProductos();
                    break;
                case 3:
                    menuUsuarios();
                    break;
                case 4:
                    menuPedidos();
                    break;
                case 5:
                    menuRepartidores();
                    break;
                case 6:
                    System.out.println("Hasta luego!");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 6);
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n===== FASTDASH FFOODS =====");
        System.out.println("1. Gestion de Restaurantes");
        System.out.println("2. Gestion de Productos");
        System.out.println("3. Gestion de Usuarios");
        System.out.println("4. Gestion de Pedidos");
        System.out.println("5. Gestion de Repartidores");
        System.out.println("6. Salir");
    }

    private static void menuRestaurantes() {
        int opcion;
        do {
            System.out.println("\n--- RESTAURANTES ---");
            System.out.println("1. Registrar restaurante");
            System.out.println("2. Consultar restaurante por id");
            System.out.println("3. Listar restaurantes");
            System.out.println("4. Actualizar restaurante");
            System.out.println("5. Eliminar restaurante");
            System.out.println("6. Volver");
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    crearRestaurante();
                    break;
                case 2:
                    consultarRestaurante();
                    break;
                case 3:
                    listarRestaurantes();
                    break;
                case 4:
                    actualizarRestaurante();
                    break;
                case 5:
                    eliminarRestaurante();
                    break;
            }
        } while (opcion != 6);
    }

    private static void crearRestaurante() {
        Restaurante restaurante = new Restaurante();
        System.out.print("Nombre: ");
        restaurante.setNombre(TECLADO.nextLine());
        System.out.print("Direccion: ");
        restaurante.setDireccion(TECLADO.nextLine());
        System.out.print("Telefono: ");
        restaurante.setTelefono(TECLADO.nextLine());
        restauranteService.registrar(restaurante);
        System.out.println("Restaurante registrado con id " + restaurante.getIdRestaurante());
    }

    private static void consultarRestaurante() {
        int id = leerEntero("Id del restaurante: ");
        Restaurante restaurante = restauranteService.consultarPorId(id);
        System.out.println(restaurante != null ? restaurante : "No existe el restaurante.");
    }

    private static void listarRestaurantes() {
        List<Restaurante> restaurantes = restauranteService.listarTodos();
        if (restaurantes.isEmpty()) {
            System.out.println("No hay restaurantes registrados.");
        } else {
            restaurantes.forEach(System.out::println);
        }
    }

    private static void actualizarRestaurante() {
        Restaurante restaurante = restauranteService.consultarPorId(
                leerEntero("Id del restaurante a actualizar: "));
        if (restaurante == null) {
            System.out.println("No existe el restaurante.");
            return;
        }
        System.out.print("Nuevo nombre: ");
        restaurante.setNombre(TECLADO.nextLine());
        System.out.print("Nueva direccion: ");
        restaurante.setDireccion(TECLADO.nextLine());
        System.out.print("Nuevo telefono: ");
        restaurante.setTelefono(TECLADO.nextLine());
        restauranteService.actualizar(restaurante);
        System.out.println("Restaurante actualizado.");
    }

    private static void eliminarRestaurante() {
        int id = leerEntero("Id del restaurante a eliminar: ");
        restauranteService.eliminar(id);
        System.out.println("Restaurante eliminado.");
    }

    private static void menuProductos() {
        int opcion;
        do {
            System.out.println("\n--- PRODUCTOS ---");
            System.out.println("1. Registrar producto");
            System.out.println("2. Consultar producto por id");
            System.out.println("3. Listar productos");
            System.out.println("4. Listar productos por restaurante");
            System.out.println("5. Actualizar producto");
            System.out.println("6. Eliminar producto");
            System.out.println("7. Volver");
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    crearProducto();
                    break;
                case 2:
                    consultarProducto();
                    break;
                case 3:
                    listarProductos();
                    break;
                case 4:
                    listarProductosPorRestaurante();
                    break;
                case 5:
                    actualizarProducto();
                    break;
                case 6:
                    eliminarProducto();
                    break;
            }
        } while (opcion != 7);
    }

    private static void crearProducto() {
        Producto producto = new Producto();
        producto.setIdRestaurante(leerEntero("Id del restaurante: "));
        System.out.print("Nombre: ");
        producto.setNombre(TECLADO.nextLine());
        System.out.print("Descripcion: ");
        producto.setDescripcion(TECLADO.nextLine());
        producto.setPrecio(leerDecimal("Precio: "));
        System.out.print("Categoria: ");
        producto.setCategoria(TECLADO.nextLine());
        productoService.registrar(producto);
        System.out.println("Producto registrado con id " + producto.getIdProducto());
    }

    private static void consultarProducto() {
        int id = leerEntero("Id del producto: ");
        Producto producto = productoService.consultarPorId(id);
        System.out.println(producto != null ? producto : "No existe el producto.");
    }

    private static void listarProductos() {
        List<Producto> productos = productoService.listarTodos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
        } else {
            productos.forEach(System.out::println);
        }
    }

    private static void listarProductosPorRestaurante() {
        int idRestaurante = leerEntero("Id del restaurante: ");
        List<Producto> productos = productoService.listarPorRestaurante(idRestaurante);
        if (productos.isEmpty()) {
            System.out.println("El restaurante no tiene productos.");
        } else {
            productos.forEach(System.out::println);
        }
    }

    private static void actualizarProducto() {
        Producto producto = productoService.consultarPorId(leerEntero("Id del producto a actualizar: "));
        if (producto == null) {
            System.out.println("No existe el producto.");
            return;
        }
        producto.setIdRestaurante(leerEntero("Nuevo id del restaurante: "));
        System.out.print("Nuevo nombre: ");
        producto.setNombre(TECLADO.nextLine());
        System.out.print("Nueva descripcion: ");
        producto.setDescripcion(TECLADO.nextLine());
        producto.setPrecio(leerDecimal("Nuevo precio: "));
        System.out.print("Nueva categoria: ");
        producto.setCategoria(TECLADO.nextLine());
        productoService.actualizar(producto);
        System.out.println("Producto actualizado.");
    }

    private static void eliminarProducto() {
        int id = leerEntero("Id del producto a eliminar: ");
        productoService.eliminar(id);
        System.out.println("Producto eliminado.");
    }

    private static void menuUsuarios() {
        int opcion;
        do {
            System.out.println("\n--- USUARIOS ---");
            System.out.println("1. Registrar usuario");
            System.out.println("2. Consultar usuario por id");
            System.out.println("3. Consultar usuario por email");
            System.out.println("4. Listar usuarios");
            System.out.println("5. Actualizar usuario");
            System.out.println("6. Eliminar usuario");
            System.out.println("7. Volver");
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    crearUsuario();
                    break;
                case 2:
                    consultarUsuarioPorId();
                    break;
                case 3:
                    consultarUsuarioPorEmail();
                    break;
                case 4:
                    listarUsuarios();
                    break;
                case 5:
                    actualizarUsuario();
                    break;
                case 6:
                    eliminarUsuario();
                    break;
            }
        } while (opcion != 7);
    }

    private static void crearUsuario() {
        Usuario usuario = new Usuario();
        System.out.print("Nombre completo: ");
        usuario.setNombreCompleto(TECLADO.nextLine());
        System.out.print("Email: ");
        usuario.setEmail(TECLADO.nextLine());
        System.out.print("Contrasena: ");
        usuario.setContrasena(TECLADO.nextLine());
        System.out.print("Rol (cliente/admin): ");
        usuario.setRol(TECLADO.nextLine());
        usuarioService.registrar(usuario);
        System.out.println("Usuario registrado con id " + usuario.getIdUsuario());
    }

    private static void consultarUsuarioPorId() {
        int id = leerEntero("Id del usuario: ");
        Usuario usuario = usuarioService.consultarPorId(id);
        System.out.println(usuario != null ? usuario : "No existe el usuario.");
    }

    private static void consultarUsuarioPorEmail() {
        System.out.print("Email del usuario: ");
        Usuario usuario = usuarioService.consultarPorEmail(TECLADO.nextLine());
        System.out.println(usuario != null ? usuario : "No existe el usuario.");
    }

    private static void listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
        } else {
            usuarios.forEach(System.out::println);
        }
    }

    private static void actualizarUsuario() {
        Usuario usuario = usuarioService.consultarPorId(leerEntero("Id del usuario a actualizar: "));
        if (usuario == null) {
            System.out.println("No existe el usuario.");
            return;
        }
        System.out.print("Nuevo nombre completo: ");
        usuario.setNombreCompleto(TECLADO.nextLine());
        System.out.print("Nuevo email: ");
        usuario.setEmail(TECLADO.nextLine());
        System.out.print("Nueva contrasena: ");
        usuario.setContrasena(TECLADO.nextLine());
        System.out.print("Nuevo rol (cliente/admin): ");
        usuario.setRol(TECLADO.nextLine());
        usuarioService.actualizar(usuario);
        System.out.println("Usuario actualizado.");
    }

    private static void eliminarUsuario() {
        int id = leerEntero("Id del usuario a eliminar: ");
        usuarioService.eliminar(id);
        System.out.println("Usuario eliminado.");
    }

    private static void menuRepartidores() {
        int opcion;
        do {
            System.out.println("\n--- REPARTIDORES ---");
            System.out.println("1. Registrar repartidor");
            System.out.println("2. Consultar repartidor por id");
            System.out.println("3. Listar repartidores");
            System.out.println("4. Listar repartidores por restaurante");
            System.out.println("5. Actualizar repartidor");
            System.out.println("6. Eliminar repartidor");
            System.out.println("7. Volver");
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    crearRepartidor();
                    break;
                case 2:
                    consultarRepartidor();
                    break;
                case 3:
                    listarRepartidores();
                    break;
                case 4:
                    listarRepartidoresPorRestaurante();
                    break;
                case 5:
                    actualizarRepartidor();
                    break;
                case 6:
                    eliminarRepartidor();
                    break;
            }
        } while (opcion != 7);
    }

    private static void crearRepartidor() {
        Repartidor repartidor = new Repartidor();
        repartidor.setIdRestaurante(leerEntero("Id del restaurante: "));
        System.out.print("Nombre: ");
        repartidor.setNombre(TECLADO.nextLine());
        System.out.print("Telefono: ");
        repartidor.setTelefono(TECLADO.nextLine());
        System.out.print("Vehiculo (moto/bicicleta/carro): ");
        repartidor.setVehiculo(TECLADO.nextLine());
        repartidorService.registrar(repartidor);
        System.out.println("Repartidor registrado con id " + repartidor.getIdRepartidor());
    }

    private static void consultarRepartidor() {
        int id = leerEntero("Id del repartidor: ");
        Repartidor repartidor = repartidorService.consultarPorId(id);
        System.out.println(repartidor != null ? repartidor : "No existe el repartidor.");
    }

    private static void listarRepartidores() {
        List<Repartidor> repartidores = repartidorService.listarTodos();
        if (repartidores.isEmpty()) {
            System.out.println("No hay repartidores registrados.");
        } else {
            repartidores.forEach(System.out::println);
        }
    }

    private static void listarRepartidoresPorRestaurante() {
        int idRestaurante = leerEntero("Id del restaurante: ");
        List<Repartidor> repartidores = repartidorService.listarPorRestaurante(idRestaurante);
        if (repartidores.isEmpty()) {
            System.out.println("El restaurante no tiene repartidores.");
        } else {
            repartidores.forEach(System.out::println);
        }
    }

    private static void actualizarRepartidor() {
        Repartidor repartidor = repartidorService.consultarPorId(leerEntero("Id del repartidor a actualizar: "));
        if (repartidor == null) {
            System.out.println("No existe el repartidor.");
            return;
        }
        repartidor.setIdRestaurante(leerEntero("Nuevo id del restaurante: "));
        System.out.print("Nuevo nombre: ");
        repartidor.setNombre(TECLADO.nextLine());
        System.out.print("Nuevo telefono: ");
        repartidor.setTelefono(TECLADO.nextLine());
        System.out.print("Nuevo vehiculo: ");
        repartidor.setVehiculo(TECLADO.nextLine());
        repartidorService.actualizar(repartidor);
        System.out.println("Repartidor actualizado.");
    }

    private static void eliminarRepartidor() {
        int id = leerEntero("Id del repartidor a eliminar: ");
        repartidorService.eliminar(id);
        System.out.println("Repartidor eliminado.");
    }

    private static void menuPedidos() {
        int opcion;
        do {
            System.out.println("\n--- PEDIDOS ---");
            System.out.println("1. Registrar pedido");
            System.out.println("2. Consultar pedido por id");
            System.out.println("3. Listar pedidos");
            System.out.println("4. Listar pedidos por usuario");
            System.out.println("5. Listar pedidos por repartidor");
            System.out.println("6. Actualizar pedido");
            System.out.println("7. Eliminar pedido");
            System.out.println("8. Volver");
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    crearPedido();
                    break;
                case 2:
                    consultarPedido();
                    break;
                case 3:
                    listarPedidos();
                    break;
                case 4:
                    listarPedidosPorUsuario();
                    break;
                case 5:
                    listarPedidosPorRepartidor();
                    break;
                case 6:
                    actualizarPedido();
                    break;
                case 7:
                    eliminarPedido();
                    break;
            }
        } while (opcion != 8);
    }

    private static void crearPedido() {
        Pedido pedido = new Pedido();
        pedido.setIdUsuario(leerEntero("Id del usuario: "));
        pedido.setFechaPedido(new Timestamp(System.currentTimeMillis()));
        pedido.setTotal(leerDecimal("Total: "));
        System.out.print("Estado (pendiente/en_preparacion/enviado/entregado/cancelado): ");
        pedido.setEstado(TECLADO.nextLine());
        pedido.setIdRepartidor(leerEntero("Id del repartidor (0 = sin asignar): "));
        pedidoService.registrar(pedido);
        System.out.println("Pedido registrado con id " + pedido.getIdPedido());
    }

    private static void consultarPedido() {
        int id = leerEntero("Id del pedido: ");
        Pedido pedido = pedidoService.consultarPorId(id);
        System.out.println(pedido != null ? pedido : "No existe el pedido.");
    }

    private static void listarPedidos() {
        List<Pedido> pedidos = pedidoService.listarTodos();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
        } else {
            pedidos.forEach(System.out::println);
        }
    }

    private static void listarPedidosPorUsuario() {
        int idUsuario = leerEntero("Id del usuario: ");
        List<Pedido> pedidos = pedidoService.listarPorUsuario(idUsuario);
        if (pedidos.isEmpty()) {
            System.out.println("El usuario no tiene pedidos.");
        } else {
            pedidos.forEach(System.out::println);
        }
    }

    private static void listarPedidosPorRepartidor() {
        int idRepartidor = leerEntero("Id del repartidor: ");
        List<Pedido> pedidos = pedidoService.listarPorRepartidor(idRepartidor);
        if (pedidos.isEmpty()) {
            System.out.println("El repartidor no tiene pedidos.");
        } else {
            pedidos.forEach(System.out::println);
        }
    }

    private static void actualizarPedido() {
        Pedido pedido = pedidoService.consultarPorId(leerEntero("Id del pedido a actualizar: "));
        if (pedido == null) {
            System.out.println("No existe el pedido.");
            return;
        }
        pedido.setIdUsuario(leerEntero("Nuevo id del usuario: "));
        pedido.setTotal(leerDecimal("Nuevo total: "));
        System.out.print("Nuevo estado: ");
        pedido.setEstado(TECLADO.nextLine());
        pedido.setIdRepartidor(leerEntero("Nuevo id del repartidor (0 = sin asignar): "));
        pedidoService.actualizar(pedido);
        System.out.println("Pedido actualizado.");
    }

    private static void eliminarPedido() {
        int id = leerEntero("Id del pedido a eliminar: ");
        pedidoService.eliminar(id);
        System.out.println("Pedido eliminado.");
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        int valor = Integer.parseInt(TECLADO.nextLine().trim());
        return valor;
    }

    private static BigDecimal leerDecimal(String mensaje) {
        System.out.print(mensaje);
        return new BigDecimal(TECLADO.nextLine().trim());
    }
}
