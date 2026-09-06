// ---------------------------------------------------------------------
// hashear_contrasenas.js - Convierte las contrasenas viejas (texto
// plano) en hashes con bcrypt. Se ejecuta UNA sola vez: node api/hashear_contrasenas.js
// ---------------------------------------------------------------------

const bcrypt = require('bcryptjs');
const pool = require('./db');

// Funcion asincrona principal
async function main() {
  // Lee todos los usuarios de la tabla
  const [usuarios] = await pool.query('SELECT id_usuario, contrasena FROM usuario');

  for (const usuario of usuarios) {
    // Si la contrasena no empieza con $2, es texto plano -> hay que encriptarla
    if (!usuario.contrasena.startsWith('$2')) {
      const hash = await bcrypt.hash(usuario.contrasena, 10);
      await pool.query(
        'UPDATE usuario SET contrasena = ? WHERE id_usuario = ?',
        [hash, usuario.id_usuario]
      );
      console.log('Encriptada la contrasena del usuario id', usuario.id_usuario);
    } else {
      console.log('El usuario id', usuario.id_usuario, 'ya tenia hash. Sin cambios.');
    }
  }

  console.log('Proceso terminado. Contrasenas listas para el login.');
  await pool.end();
}

main().catch((error) => {
  console.error('Error:', error.message);
  process.exit(1);
});