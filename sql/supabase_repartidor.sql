-- =====================================================================
-- FastDash Ffoods - Migracion: tabla repartidor + enlace con pedidos
-- Ejecutar UNA sola vez en el SQL Editor de Supabase
-- (ya tienes las tablas creadas con supabase_fastdash.sql)
-- =====================================================================

-- 1) Tabla repartidor (cada repartidor pertenece a un restaurante)
CREATE TABLE IF NOT EXISTS repartidor (
    id_repartidor  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_restaurante BIGINT NOT NULL,
    nombre         TEXT NOT NULL,
    telefono       TEXT,
    vehiculo       TEXT,
    CONSTRAINT fk_repartidor_restaurante
        FOREIGN KEY (id_restaurante) REFERENCES restaurante (id_restaurante)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 2) Agregar id_repartidor a pedido (nullable: un pedido puede no tener repartidor aun)
ALTER TABLE pedido ADD COLUMN IF NOT EXISTS id_repartidor BIGINT
    REFERENCES repartidor (id_repartidor)
    ON DELETE SET NULL ON UPDATE CASCADE;

-- 3) Datos de ejemplo (opcional; no lo ejecutes 2 veces o se duplican)
INSERT INTO repartidor (id_restaurante, nombre, telefono, vehiculo) VALUES
    (1, 'Carlos Perez',  '3005557788', 'moto'),
    (1, 'Pedro Jimenez', '3005551122', 'carro'),
    (2, 'Ana Torres',    '3005559900', 'bicicleta');

-- 4) Asignar el pedido de ejemplo al repartidor 1
UPDATE pedido SET id_repartidor = 1 WHERE id_pedido = 1;
