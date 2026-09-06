// ---------------------------------------------------------------------
// app.js - Configuracion del servidor Express (la API)
// Aqui se define el puerto, el uso de JSON y las rutas de la API.
// ---------------------------------------------------------------------

const express = require('express');
const rutasAuth = require('./rutas/auth');

// Crea la aplicacion Express
const app = express();

// Permite que el servidor entienda las peticiones en formato JSON
app.use(express.json());

// Ruta de bienvenida para comprobar que el servidor esta vivo
app.get('/', (req, res) => {
  res.json({ mensaje: 'Bienvenido a la API de FastDash Ffoods' });
});

// Conecta todas las rutas de autenticacion bajo /api/auth
// Ejemplos: /api/auth/registro  y  /api/auth/login
app.use('/api/auth', rutasAuth);

// Exporta la aplicacion para que server.js la ponga a escuchar
module.exports = app;