-- =====================================================================
-- FastDash Ffoods - Script de creación de la base de datos
-- Motor: MySQL 8.0+
-- =====================================================================

CREATE DATABASE IF NOT EXISTS fastdash_foods
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE fastdash_foods;

-- ---------------------------------------------------------------------
-- Tabla: restaurante
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS restaurante (
    id_restaurante   INT AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL,
    direccion        VARCHAR(200),
    telefono         VARCHAR(20)
);

-- ---------------------------------------------------------------------
-- Tabla: usuario
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario      INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    contrasena      VARCHAR(255) NOT NULL,
    rol             ENUM('cliente', 'admin') NOT NULL DEFAULT 'cliente'
);

-- ---------------------------------------------------------------------
-- Tabla: producto
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS producto (
    id_producto    INT AUTO_INCREMENT PRIMARY KEY,
    id_restaurante INT NOT NULL,
    nombre         VARCHAR(100) NOT NULL,
    descripcion    VARCHAR(255),
    precio         DECIMAL(10, 2) NOT NULL,
    categoria      VARCHAR(50),
    CONSTRAINT fk_producto_restaurante
        FOREIGN KEY (id_restaurante) REFERENCES restaurante (id_restaurante)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ---------------------------------------------------------------------
-- Tabla: repartidor (cada repartidor pertenece a un restaurante)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS repartidor (
    id_repartidor  INT AUTO_INCREMENT PRIMARY KEY,
    id_restaurante INT NOT NULL,
    nombre         VARCHAR(100) NOT NULL,
    telefono       VARCHAR(20),
    vehiculo       VARCHAR(20),
    CONSTRAINT fk_repartidor_restaurante
        FOREIGN KEY (id_restaurante) REFERENCES restaurante (id_restaurante)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ---------------------------------------------------------------------
-- Tabla: pedido
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pedido (
    id_pedido     INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario    INT NOT NULL,
    id_repartidor INT NULL,
    fecha_pedido  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total         DECIMAL(10, 2) NOT NULL,
    estado        ENUM('pendiente', 'en_preparacion', 'enviado', 'entregado', 'cancelado')
                  NOT NULL DEFAULT 'pendiente',
    CONSTRAINT fk_pedido_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_pedido_repartidor
        FOREIGN KEY (id_repartidor) REFERENCES repartidor (id_repartidor)
        ON DELETE SET NULL ON UPDATE CASCADE
);

-- ---------------------------------------------------------------------
-- Tabla: pedido_producto (detalle de los pedidos)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pedido_producto (
    id_pedido   INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad    INT NOT NULL DEFAULT 1,
    PRIMARY KEY (id_pedido, id_producto),
    CONSTRAINT fk_detalle_pedido
        FOREIGN KEY (id_pedido) REFERENCES pedido (id_pedido)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (id_producto) REFERENCES producto (id_producto)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ---------------------------------------------------------------------
-- Datos iniciales de ejemplo
-- ---------------------------------------------------------------------
INSERT INTO restaurante (nombre, direccion, telefono) VALUES
    ('Burger King Sabor', 'Calle 10 # 5-22', '3001112233'),
    ('Pizzas del Valle',   'Av. Siempre Viva 742', '3004445566');

INSERT INTO usuario (nombre_completo, email, contrasena, rol) VALUES
    ('Felipe Andrade', 'felipe@fastdash.com', '123456', 'admin'),
    ('Laura Gomez',    'laura@fastdash.com',  '123456', 'cliente');

INSERT INTO producto (id_restaurante, nombre, descripcion, precio, categoria) VALUES
    (1, 'Hamburguesa Clásica', 'Pan, carne, queso y vegetales', 15000.00, 'Hamburguesas'),
    (1, 'Papas Fritas Medianas', 'Papas crocantes con sal',       7000.00, 'Acompañantes'),
    (2, 'Pizza de Pepperoni', 'Queso mozzarella y pepperoni',   28000.00, 'Pizzas'),
    (2, 'Refresco 1L', 'Bebida gaseosa familiar',                9000.00, 'Bebidas');

INSERT INTO pedido (id_usuario, total, estado) VALUES
    (2, 22000.00, 'pendiente');

INSERT INTO pedido_producto (id_pedido, id_producto, cantidad) VALUES
    (1, 1, 1),
    (1, 2, 1);

INSERT INTO repartidor (id_restaurante, nombre, telefono, vehiculo) VALUES
    (1, 'Carlos Perez',  '3005557788', 'moto'),
    (1, 'Pedro Jimenez', '3005551122', 'carro'),
    (2, 'Ana Torres',    '3005559900', 'bicicleta');

UPDATE pedido SET id_repartidor = 1 WHERE id_pedido = 1;
