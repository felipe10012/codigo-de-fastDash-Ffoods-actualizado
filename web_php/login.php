<?php
session_start();
require 'conexion.php';

$error = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $correo = trim($_POST['correo'] ?? '');
    $clave  = $_POST['contrasena'] ?? '';

    if ($correo === '' || $clave === '') {
        $error = 'Por favor completa todos los campos.';
    } else {
        $sql = 'SELECT id_usuario, nombre_completo, rol
                FROM usuario
                WHERE email = ? AND contrasena = ?';
        $stmt = obtenerConexion()->prepare($sql);
        $stmt->execute([$correo, $clave]);
        $usuario = $stmt->fetch();

        if ($usuario) {
            $_SESSION['id_usuario'] = $usuario['id_usuario'];
            $_SESSION['nombre']     = $usuario['nombre_completo'];
            $_SESSION['rol']        = $usuario['rol'];
            header('Location: catalogo.php');
            exit;
        } else {
            $error = 'Correo o contraseña incorrectos.';
        }
    }
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Iniciar sesión | FastDash Ffoods</title>
<style>
    * { margin: 0; padding: 0; box-sizing: border-box; font-family: Arial, sans-serif; }
    body {
        min-height: 100vh; display: flex; align-items: center; justify-content: center;
        background: linear-gradient(135deg, #6a11cb, #e63946, #ff8c42);
    }
    .tarjeta {
        background: #fff; padding: 40px; border-radius: 16px;
        width: 90%; max-width: 400px; box-shadow: 0 10px 30px rgba(0,0,0,.3);
    }
    h1 { color: #6a11cb; font-size: 26px; margin-bottom: 8px; }
    p.sub { color: #666; margin-bottom: 24px; font-size: 14px; }
    label { display: block; margin-bottom: 6px; color: #333; font-size: 14px; }
    input {
        width: 100%; padding: 12px; margin-bottom: 16px;
        border: 1px solid #ccc; border-radius: 8px; font-size: 14px;
    }
    input:focus { outline: none; border-color: #6a11cb; }
    button {
        width: 100%; padding: 13px; border: none; border-radius: 8px;
        background: linear-gradient(135deg, #6a11cb, #e63946);
        color: #fff; font-size: 15px; cursor: pointer;
    }
    button:hover { opacity: .9; }
    .error {
        background: #ffe5e5; color: #b00020; padding: 10px;
        border-radius: 8px; margin-bottom: 16px; font-size: 14px;
    }
</style>
</head>
<body>
<main class="tarjeta">
    <h1>FastDash Ffoods 🍔</h1>
    <p class="sub">Ingresa tus credenciales para continuar</p>

    <?php if ($error): ?>
        <div class="error" role="alert"><?= htmlspecialchars($error) ?></div>
    <?php endif; ?>

    <form method="post" action="login.php" autocomplete="on">
        <label for="correo">Correo electrónico</label>
        <input type="email" id="correo" name="correo" required>

        <label for="contrasena">Contraseña</label>
        <input type="password" id="contrasena" name="contrasena" required minlength="6">

        <button type="submit">Ingresar</button>
    </form>
</main>
</body>
</html>