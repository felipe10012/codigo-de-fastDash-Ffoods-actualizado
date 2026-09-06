// ---------------------------------------------------------------------
// server.js - Arranca el servidor Express en el puerto 3000
// ---------------------------------------------------------------------

const app = require('./app');

// Puerto donde el servidor escuchara las peticiones
const PUERTO = process.env.PORT || 3000;

// Inicia el servidor y muestra un mensaje en la consola
app.listen(PUERTO, () => {
  console.log('Servidor web corriendo en http://localhost:' + PUERTO);
});