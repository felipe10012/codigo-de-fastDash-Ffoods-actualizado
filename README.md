# FastDash Ffoods

Sistema de gestion de restaurantes, productos, usuarios, pedidos y repartidores.
Modulo de consola en **Java** con conexion a base de datos **MySQL** mediante **JDBC**.

Evidencia SENA: GA7-220501096-AA2-EV01 - Codificacion de modulos del software.

## Estructura del proyecto

```
FastDash-Ffoods/
├── src/com/fastdash/
│   ├── Main.java                  # Menu de consola (capa de presentacion)
│   ├── model/                     # Entidades: Restaurante, Producto, Usuario, Pedido, Repartidor
│   ├── dao/                       # Interfaces de acceso a datos + DaoFactory
│   │   └── impl/jdbc/             # Implementaciones JDBC (PreparedStatement)
│   ├── service/                   # Reglas de negocio y validaciones
│   └── util/                      # ConexionBD (JDBC), Validaciones
├── sql/fastdash.sql               # Script de creacion de la BD y datos de prueba
├── docs/prototipos/               # Prototipos HTML (login y modulo de consola)
├── docs/informe_tecnico/          # Informe tecnico (.docx) y script generador
├── lib/mysql-connector-j-8.0.33.jar
├── db.properties.example          # Plantilla de credenciales (db.properties va ignorado por Git)
├── compilar.bat / ejecutar.bat / abrir_mysql.bat
└── README.md
```

## Requisitos

- JDK 17 o superior
- MySQL Server 8+
- Driver JDBC: `lib/mysql-connector-j-8.0.33.jar`

## Instalacion y ejecucion

1. Crear la base de datos:

   ```
   sql\fastdash.sql
   ```
   (ejecutarlo desde MySQL Workbench o la consola MySQL)

2. Configurar credenciales: copiar `db.properties.example` como `db.properties`
   y colocar tu usuario y contrasena de MySQL.

3. Compilar:

   ```
   compilar.bat
   ```

4. Ejecutar:

   ```
   ejecutar.bat
   ```

## Funcionalidades CRUD disponibles

| Modulo       | Insertar | Consultar                          | Actualizar | Eliminar |
|--------------|----------|------------------------------------|------------|----------|
| Restaurantes | Si       | Por id / listado                   | Si         | Si       |
| Productos    | Si       | Por id / listado / por restaurante | Si         | Si       |
| Usuarios     | Si       | Por id / por email / listado       | Si         | Si       |
| Pedidos      | Si       | Por id / por usuario / repartidor  | Si         | Si       |
| Repartidores | Si       | Por id / listado / por restaurante | Si         | Si       |

## Estandares de codificacion

- Paquetes en minusculas con dominio invertido: `com.fastdash.dao.impl.jdbc`.
- Clases en PascalCase con sufijos de rol: `RestauranteDaoImplJdbc`, `UsuarioService`.
- Metodos en camelCase con verbo infinitivo: `insertar`, `consultarPorId`, `listarTodos`, `actualizar`, `eliminar`.
- Constantes en MAYUSCULAS_CON_GUIONES: `TECLADO`, `ARCHIVO_PROPIEDADES`.
- Variables locales descriptivas en camelCase: `sentencia`, `resultado`, `excepcion`.

## Versionamiento

El proyecto se desarrolla con Git y se publica en GitHub. Los commits siguen
mensajes descriptivos en espanol, uno por funcionalidad.
