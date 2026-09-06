<?php
/* =====================================================================
   PROYECTO PARADAS SITP - Consumo de una API REST pública desde PHP
   =====================================================================
   QUE HACE:
   Página web dinámica en PHP que consume un servicio REST público
   (ArcGIS/GeoJSON) con los paraderos del SITP de Bogotá, filtra por
   localidad y muestra los resultados en una tabla HTML estilizada.

   PARA QUE SIRVE:
   Demuestra el flujo completo de consumo de una API:
     1. El usuario ingresa una localidad en un formulario.
     2. PHP construye la URL de la API con ese dato.
     3. PHP llama a la API con file_get_contents().
     4. PHP interpreta la respuesta JSON con json_decode().
     5. PHP recorre el arreglo con foreach y genera una tabla HTML.
     6. El navegador recibe solo HTML (el PHP ya se ejecutó en el servidor).

   EN QUE CASOS PUEDO USARLO:
   Sirve de base o plantilla para consumir CUALQUIER API pública REST:
   - Endpoints que devuelven GeoJSON (ArcGIS, OpenStreetMap, etc.).
   - APIs JSON de clima, países, transporte, datos abiertos de gobierno.
   - Aplicaciones donde hay que filtrar datos por un parámetro (aquí, la
     localidad) y mostrarlos como tabla.

   ENDPOINT UTILIZADO:
   https://serviciosgis.catastrobogota.gov.co/arcgis/rest/services/
       Mapa_Referencia/Mapa_Referencia/MapServer/8/query
   Capa: "Paraderos Zonales SITP" (capa 8)
   Parámetros: where (filtro), outFields (campos), f (formato geojson)

   COMO EJECUTARLO:
   Estando en la carpeta del archivo:
       php -S localhost:8000
   Luego abrir: http://localhost:8000/paraderos_sitp.php
   (Requiere PHP con la extensión OpenSSL habilitada en php.ini.)
   ===================================================================== */

// ---------------------------------------------------------------------
// SECCION 1: RECOGER EL DATO DEL FORMULARIO
// ---------------------------------------------------------------------
// El formulario envía la localidad por GET (más abajo, en el HTML).
// isset() evita errores si aún no se ha enviado nada.
$localidad = isset($_GET['localidad']) ? $_GET['localidad'] : '';

// trim() quita espacios al inicio y final (" Suba " -> "Suba").
// strip_tags() elimina cualquier etiqueta HTML/script que el usuario
// pudiera escribir. Es una medida básica de seguridad contra inyección.
$localidad = trim(strip_tags($localidad));

// ---------------------------------------------------------------------
// SECCION 2: VARIABLE DE SALIDA
// ---------------------------------------------------------------------
// $mensaje acumula TODO lo que se mostrará en la zona de resultados:
// mensajes de error, avisos o la tabla completa de paraderos.
$mensaje = '';

// ---------------------------------------------------------------------
// SECCION 3: VALIDACION DEL DATO INGRESADO
// ---------------------------------------------------------------------
// Si el usuario envió el formulario pero dejó la localidad vacía,
// mostramos un aviso amigable en lugar de continuar con la consulta.
if ($_SERVER['REQUEST_METHOD'] === 'GET' && isset($_GET['localidad']) && $localidad === '') {
    $mensaje = '<p class="error">Escribe el nombre de una localidad para consultar.</p>';
}

// ---------------------------------------------------------------------
// SECCION 4: CONSUMO DE LA API
// ---------------------------------------------------------------------
// Solo consultamos si escribieron una localidad y no hay ya un error.
// La condición $mensaje === '' evita consultar cuando ya mostramos aviso.
if ($localidad !== '' && $mensaje === '') {

    // 4.1 Construir la URL del servicio REST
    // La API espera un filtro estilo SQL en el parámetro "where".
    // strtoupper() pasa la localidad a MAYÚSCULAS porque la API solo
    // compara contra valores en mayúsculas (verificado empíricamente).
    // urlencode() codifica la URL para que acentos y espacios sean válidos.
    $url = 'https://serviciosgis.catastrobogota.gov.co/arcgis/rest/services/'
         . 'Mapa_Referencia/Mapa_Referencia/MapServer/8/query'
         . '?where=' . urlencode("UPPER(LOCALIDAD)='" . strtoupper($localidad) . "'")
         . '&outFields=NOMBRE,LOCALIDAD,DIRECCION_,LATITUD,LONGITUD,CENEFA'
         . '&f=geojson';

    // 4.2 Contexto HTTP de la llamada
    // stream_context_create() permite configurar opciones de red:
    //   timeout => 15   -> esperar máximo 15 segundos por la respuesta.
    //   ignore_errors   -> no salta si la API devuelve errores HTTP.
    $contexto = stream_context_create([
        'http' => ['timeout' => 15, 'ignore_errors' => true]
    ]);

    // 4.3 Hacer la llamada a la API
    // file_get_contents() es la forma más simple de consumo HTTP en PHP.
    // Devuelve el contenido (JSON) o "false" si la conexión falla.
    // El símbolo @ silencia los warnings de PHP (los manejamos nosotros).
    $respuesta = @file_get_contents($url, false, $contexto);

    // 4.4 Caso de error: la API no respondió
    // Si "false", mostramos un mensaje amigable. El usuario no debería
    // ver jamás un error técnico de PHP en pantalla (requisito 5 del
    // enunciado: manejo de errores).
    if ($respuesta === false) {
        $mensaje = '<p class="error">No se pudo conectar con el servicio de la API. Inténtalo más tarde.</p>';
    } else {

        // 4.5 Convertir el JSON en un arreglo de PHP
        // json_decode() con "true" devuelve arreglos asociativos en vez
        // de objetos. La estructura queda:
        //   [features] => [ 0 => [properties => [NOMBRE => ..., ...]] ]
        $datos = json_decode($respuesta, true);

        // 4.6 Extraer la lista de paraderos
        // El arreglo "features" del GeoJSON contiene un elemento por
        // paradero. Si la clave no existe, usamos un arreglo vacío.
        $paraderos = isset($datos['features']) ? $datos['features'] : [];

        // 4.7 Caso: sin resultados
        // Puede pasar si la localidad no existe o la API está saturada.
        // Avisamos sin romper la página (la API es pesada y a veces
        // responde vacía; basta con reintentar).
        if (count($paraderos) === 0) {
            $mensaje = '<p class="info">No se encontraron paraderos del SITP para la localidad "'
                     . htmlspecialchars($localidad) .
                     '". Verifica si la escribiste bien o inténtalo de nuevo.</p>';
        } else {
            // 4.8 Construir la tabla HTML
            // Empezamos con un mensaje resumen (cuántos se encontraron) y
            // abrimos la tabla. La clase "tabla-caja" añade scroll con CSS.
            $mensaje = '<p class="ok">Se encontraron ' . count($paraderos)
                     . ' paraderos en la localidad "' . htmlspecialchars($localidad) . '".</p>';
            $mensaje .= '<div class="tabla-caja"><table>';
            $mensaje .= '<thead><tr><th>Código</th><th>Nombre del paradero</th>'
                      . '<th>Dirección / ubicación</th><th>Latitud</th><th>Longitud</th></tr></thead>';
            $mensaje .= '<tbody>';

            // 4.9 Recorrer cada paradero con foreach
            // Por cada elemento extraemos sus propiedades. El operador ??
            // (o isset) nos da un valor por defecto si el campo falta.
            foreach ($paraderos as $paradero) {
                $prop   = $paradero['properties'];
                $codigo = isset($prop['CENEFA']) ? $prop['CENEFA'] : '-';
                $nombre = isset($prop['NOMBRE']) ? $prop['NOMBRE'] : 'Sin nombre';
                $dir    = isset($prop['DIRECCION_']) ? $prop['DIRECCION_'] : '-';
                $lat    = isset($prop['LATITUD']) ? $prop['LATITUD'] : '-';
                $lon    = isset($prop['LONGITUD']) ? $prop['LONGITUD'] : '-';

                // htmlspecialchars() es clave en seguridad: convierte
                // caracteres como < > " a entidades HTML, evitando que un
                // dato de la API escape a HTML (XSS).
                $mensaje .= '<tr>';
                $mensaje .= '<td>' . htmlspecialchars($codigo) . '</td>';
                $mensaje .= '<td>' . htmlspecialchars($nombre) . '</td>';
                $mensaje .= '<td>' . htmlspecialchars($dir) . '</td>';
                $mensaje .= '<td>' . htmlspecialchars($lat) . '</td>';
                $mensaje .= '<td>' . htmlspecialchars($lon) . '</td>';
                $mensaje .= '</tr>';
            }

            // 4.10 Cierre de la tabla
            $mensaje .= '</tbody></table></div>';
        }
    }
}
?>

<!DOCTYPE html>
<!--
  A partir de aquí termina la lógica PHP y empieza la presentación HTML.
  El navegador recibe SOLO este HTML: el bloque PHP de arriba ya se
  ejecutó en el servidor. Por eso al inspeccionar la página no se ve PHP.
-->
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Paraderos SITP</title>

    <!-- CSS: colores, disposición y estilos de la página.
         El CSS usa las etiquetas estructurales <header>, <main>,
         las filas de la tabla y las clases .error / .info / .ok. -->
    <style>
        /* Estilos generales */
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            margin: 0;
            padding: 0;
            background: #f0f4f8;
            color: #1f2933;
        }

        /* Encabezado de la página */
        header {
            background: #0e7490;
            color: #ffffff;
            padding: 20px;
            text-align: center;
        }

        header h1 {
            margin: 0;
            font-size: 26px;
        }

        /* Contenedor principal (centra y limita el ancho) */
        main {
            max-width: 1100px;
            margin: 20px auto;
            padding: 0 15px;
        }

        /* Formulario */
        form {
            background: #ffffff;
            border: 1px solid #d9e2ec;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            display: flex;
            gap: 10px;
            align-items: center;
            flex-wrap: wrap;
        }

        form label {
            font-weight: bold;
        }

        form input[type="text"] {
            flex: 1;
            min-width: 220px;
            padding: 10px;
            border: 1px solid #bcccdc;
            border-radius: 6px;
            font-size: 15px;
        }

        form button {
            padding: 10px 22px;
            background: #0e7490;
            color: #ffffff;
            border: none;
            border-radius: 6px;
            font-size: 15px;
            cursor: pointer;
        }

        form button:hover {
            background: #155e75;
        }

        /* Mensajes de error, aviso y éxito */
        .error {
            background: #fee2e2;
            color: #991b1b;
            border: 1px solid #fecaca;
            padding: 12px;
            border-radius: 6px;
        }

        .info {
            background: #ffedd5;
            color: #9a3412;
            border: 1px solid #fed7aa;
            padding: 12px;
            border-radius: 6px;
        }

        .ok {
            background: #dcfce7;
            color: #166534;
            border: 1px solid #bbf7d0;
            padding: 12px;
            border-radius: 6px;
        }

        /* Caja de la tabla: scroll vertical cuando hay muchos paraderos
           (algunas localidades superan los 800 registros). */
        .tabla-caja {
            max-height: 500px;
            overflow: auto;
            border-radius: 8px;
            border: 1px solid #d9e2ec;
            background: #ffffff;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            font-size: 14px;
        }

        /* Encabezado fijo al hacer scroll */
        table thead th {
            background: #0e7490;
            color: #ffffff;
            padding: 10px;
            text-align: left;
            position: sticky;
            top: 0;
        }

        table tbody td {
            padding: 8px 10px;
            border-bottom: 1px solid #e4e7eb;
        }

        /* Filas alternadas para facilitar la lectura */
        table tbody tr:nth-child(even) {
            background: #f4f9fb;
        }

        table tbody tr:hover {
            background: #e0f2fe;
        }
    </style>
</head>
<body>
    <!-- Encabezado visual de la página -->
    <header>
        <h1>Paraderos del SITP en Bogotá</h1>
    </header>

    <!-- Contenido principal -->
    <main>
        <!--
          Formulario de consulta. "method=get" envía los datos en la URL
          (...paraderos_sitp.php?localidad=Suba), que es lo que lee el
          PHP en $_GET. "action" apunta al mismo archivo.
        -->
        <form method="get" action="paraderos_sitp.php">
            <label for="localidad">Localidad de Bogotá:</label>

            <!-- Campo de texto. "list" lo enlaza con el datalist siguiente.
                 El usuario puede escribir libremente (así probamos el
                 manejo de errores) o elegir una sugerencia. -->
            <input type="text" id="localidad" name="localidad"
                   list="localidades" placeholder="Ej: Suba, Kennedy, Chapinero">

            <!-- Datalist: sugerencias de las 20 localidades de Bogotá.
                 Es solo un asistente: no impide escribir otro valor. -->
            <datalist id="localidades">
                <option value="Usaquén"><option value="Chapinero">
                <option value="Santa Fe"><option value="San Cristóbal">
                <option value="Usme"><option value="Tunjuelito">
                <option value="Bosa"><option value="Kennedy">
                <option value="Fontibón"><option value="Engativá">
                <option value="Suba"><option value="Barrios Unidos">
                <option value="Teusaquillo"><option value="Los Mártires">
                <option value="Antonio Nariño"><option value="Puente Aranda">
                <option value="La Candelaria"><option value="Rafael Uribe Uribe">
                <option value="Ciudad Bolívar"><option value="Sumapaz">
            </datalist>

            <button type="submit">Consultar</button>
        </form>

        <!-- Zona de resultados: aquí PHP imprime $mensaje con el error,
             el aviso sin resultados o la tabla de paraderos. -->
        <div id="resultado">
            <?php echo $mensaje; ?>
        </div>
    </main>
</body>
</html>