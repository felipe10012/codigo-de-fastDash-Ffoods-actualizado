<?php
define('DB_HOST', 'localhost');
define('DB_NAME', 'fastdash_foods');
define('DB_USER', 'root');
define('DB_PASS', 'Felipe213550');

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