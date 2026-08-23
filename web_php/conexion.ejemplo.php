<?php
// PLANTILLA de conexion - NO contiene contraseñas reales
// 1. Copia este archivo con el nombre "conexion.php"
// 2. Coloca tus credenciales reales
// 3. conexion.php esta en .gitignore y nunca se sube a GitHub

define('DB_HOST', 'localhost');
define('DB_NAME', 'nombre_de_tu_base');
define('DB_USER', 'tu_usuario');
define('DB_PASS', 'tu_contraseña');

function obtenerConexion(): PDO {
    try {
        $dsn = 'mysql:host=' . DB_HOST . ';dbname=' . DB_NAME . ';charset=utf8mb4';
        $opciones = [
            PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC
        ];
        return new PDO($dsn, DB_USER, DB_PASS, $opciones);
    } catch (PDOException $e) {
        die('Error de conexion: ' . $e->getMessage());
    }
}
