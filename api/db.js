// ---------------------------------------------------------------------
// db.js - Conexion del servicio web con la base de datos MySQL
// Usa el archivo db.properties (local) para no repetir credenciales.
// ---------------------------------------------------------------------

const mysql = require('mysql2/promise');
const fs = require('fs');
const path = require('path');

// Carga los datos de conexion desde el archivo db.properties
function cargarConfiguracion() {
  const ruta = path.join(__dirname, '..', 'db.properties');
  const contenido = fs.readFileSync(ruta, 'utf8');
  const config = {};

  contenido.split(/\r?\n/).forEach((linea) => {
    const lineaLimpia = linea.trim();
    // Solo tomamos lineas con el formato clave=valor y sin comentarios (#)
    if (lineaLimpia && !lineaLimpia.startsWith('#')) {
      const [clave, ...resto] = lineaLimpia.split('=');
      config[clave.trim()] = resto.join('=').trim();
    }
  });

  return config;
}

const config = cargarConfiguracion();

// Crea un "pool" de conexiones: el servidor reutiliza conexiones
// en lugar de abrir una nueva cada vez (mas rapido y estable).
const pool = mysql.createPool({
  host: config['db.host'],
  port: Number(config['db.puerto']),
  database: config['db.nombre'],
  user: config['db.usuario'],
  password: config['db.contrasena'],
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});

module.exports = pool;