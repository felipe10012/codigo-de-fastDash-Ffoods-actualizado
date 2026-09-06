// ---------------------------------------------------------------------
// rutas/auth.js - Rutas de autenticacion del servicio web
//   POST /api/auth/registro  -> crea una cuenta nueva
//   POST /api/auth/login     -> valida email y contrasena
// ---------------------------------------------------------------------

const express = require('express');
const bcrypt = require('bcryptjs');
const pool = require('../db');

const router = express.Router();

// ---------------------------------------------------------------------
// POST /api/auth/registro
// Recibe: { nombreCompleto, email, contrasena, rol (opcional) }
// ---------------------------------------------------------------------
router.post('/registro', async (req, res) => {
  // 1. Extrae los datos que envio el cliente
  const { nombreCompleto, email, contrasena, rol } = req.body;

  // 2. Valida que llegaron todos los campos obligatorios
  if (!nombreCompleto || !email || !contrasena) {
    return res.status(400).json({
      mensaje: 'Error al registrar: nombre, email y contrasena son obligatorios.'
    });
  }

  try {
    // 3. Comprueba si el email ya esta registrado (es unico en la BD)
    const [existentes] = await pool.query(
      'SELECT id_usuario FROM usuario WHERE email = ?',
      [email]
    );

    if (existentes.length > 0) {
      return res.status(409).json({
        mensaje: 'Error al registrar: el email ya se encuentra registrado.'
      });
    }

    // 4. Encripta la contrasena con bcrypt (se guarda el hash, nunca el texto plano)
    const hash = await bcrypt.hash(contrasena, 10);

    // 5. Inserta el nuevo usuario en la base de datos
    const rolFinal = rol && rol !== '' ? rol : 'cliente';
    const [resultado] = await pool.query(
      'INSERT INTO usuario (nombre_completo, email, contrasena, rol) VALUES (?, ?, ?, ?)',
      [nombreCompleto, email, hash, rolFinal]
    );

    // 6. Responde con una confirmacion
    res.status(201).json({
      mensaje: 'Usuario registrado satisfactoriamente.',
      id_usuario: resultado.insertId
    });
  } catch (error) {
    // 7. Cualquier error inesperado se captura aqui
    console.error('Error en /registro:', error);
    res.status(500).json({ mensaje: 'Error interno del servidor.' });
  }
});

// ---------------------------------------------------------------------
// POST /api/auth/login
// Recibe: { email, contrasena }
// ---------------------------------------------------------------------
router.post('/login', async (req, res) => {
  // 1. Extrae los datos que envio el cliente
  const { email, contrasena } = req.body;

  // 2. Valida que llegaron los campos obligatorios
  if (!email || !contrasena) {
    return res.status(400).json({
      mensaje: 'Error en la autenticacion: email y contrasena son obligatorios.'
    });
  }

  try {
    // 3. Busca al usuario por su email
    const [resultados] = await pool.query(
      'SELECT id_usuario, nombre_completo, email, contrasena, rol FROM usuario WHERE email = ?',
      [email]
    );

    // 4. Si el email no existe, la autenticacion falla
    if (resultados.length === 0) {
      return res.status(401).json({ mensaje: 'Error en la autenticacion.' });
    }

    const usuario = resultados[0];

    // 5. Compara la contrasena recibida con el hash guardado en la BD
    const coinciden = await bcrypt.compare(contrasena, usuario.contrasena);

    if (!coinciden) {
      return res.status(401).json({ mensaje: 'Error en la autenticacion.' });
    }

    // 6. Autenticacion correcta: responde con el mensaje de exito
    res.status(200).json({
      mensaje: 'Autenticacion satisfactoria.',
      usuario: {
        id_usuario: usuario.id_usuario,
        nombre_completo: usuario.nombre_completo,
        email: usuario.email,
        rol: usuario.rol
      }
    });
  } catch (error) {
    // 7. Cualquier error inesperado se captura aqui
    console.error('Error en /login:', error);
    res.status(500).json({ mensaje: 'Error interno del servidor.' });
  }
});

module.exports = router;