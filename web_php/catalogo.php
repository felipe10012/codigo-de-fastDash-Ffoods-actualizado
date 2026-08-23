<?php
session_start();
require 'conexion.php';

// Proteccion: sin sesion activa, de vuelta al login
if (!isset($_SESSION['id_usuario'])) {
    header('Location: login.php');
    exit;
}

$restaurantes = obtenerConexion()->query('SELECT * FROM restaurante ORDER BY nombre')->fetchAll();
?>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Catálogo | FastDash Ffoods</title>
<style>
    * { margin: 0; padding: 0; box-sizing: border-box; font-family: Arial, sans-serif; }
    body { background: #f4f4f8; min-height: 100vh; }
    header {
        background: linear-gradient(135deg, #6a11cb, #e63946);
        color: #fff; padding: 18px 30px;
        display: flex; justify-content: space-between; align-items: center;
    }
    header h1 { font-size: 22px; }
    .usuario { font-size: 14px; display: flex; gap: 15px; align-items: center; }
    .usuario a {
        color: #fff; background: rgba(255,255,255,.2); padding: 7px 14px;
        border-radius: 20px; text-decoration: none;
    }
    .usuario a:hover { background: rgba(255,255,255,.35); }
    main { max-width: 900px; margin: 30px auto; padding: 0 20px; }
    h2 { color: #333; margin-bottom: 20px; }
    .rejilla { display: grid; grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); gap: 20px; }
    .tarjeta-restaurante {
        background: #fff; border-radius: 12px; padding: 22px;
        box-shadow: 0 4px 12px rgba(0,0,0,.08); transition: transform .2s;
    }
    .tarjeta-restaurante:hover { transform: translateY(-4px); }
    .tarjeta-restaurante h3 { color: #6a11cb; margin-bottom: 10px; font-size: 18px; }
    .tarjeta-restaurante p { color: #555; font-size: 14px; margin-bottom: 6px; }
    .vacio { text-align: center; color: #888; padding: 40px; }
</style>
</head>
<body>
<header>
    <h1>FastDash Ffoods 🍔</h1>
    <div class="usuario">
        <span>Hola, <?= htmlspecialchars($_SESSION['nombre']) ?> (<?= $_SESSION['rol'] ?>)</span>
        <a href="cerrar_sesion.php">Cerrar sesión</a>
    </div>
</header>

<main>
    <h2>Restaurantes disponibles</h2>

    <?php if (count($restaurantes) === 0): ?>
        <p class="vacio">Aún no hay restaurantes registrados.</p>
    <?php else: ?>
        <div class="rejilla">
            <?php foreach ($restaurantes as $r): ?>
                <article class="tarjeta-restaurante">
                    <h3><?= htmlspecialchars($r['nombre']) ?></h3>
                    <p>📍 <?= htmlspecialchars($r['direccion']) ?></p>
                    <p>📞 <?= htmlspecialchars($r['telefono']) ?></p>
                </article>
            <?php endforeach; ?>
        </div>
    <?php endif; ?>
</main>
</body>
</html>