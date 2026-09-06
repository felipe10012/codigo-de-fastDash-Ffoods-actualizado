# =====================================================================
# FastDash Ffoods - Generador de la evidencia: Servicio web de
# registro e inicio de sesion (Construccion API - Express + MySQL)
#
# Reutiliza el mismo estilo de las evidencias anteriores:
#   - Cuerpo Arial 12 pt
#   - Titulos Arial negrita centrados
#   - Portada con nombre, programa, ficha y fecha
#   - Tablas con bordes grises y encabezado F2F2F2
#   - Bloques de codigo en caja gris (Consolas)
#
# No requiere Microsoft Word (empaqueta Open XML directamente).
# Uso: powershell -ExecutionPolicy Bypass -File generar_informe_api.ps1
# =====================================================================

$ErrorActionPreference = "Stop"
Add-Type -AssemblyName WindowsBase
Add-Type -AssemblyName System.IO.Compression.FileSystem

$raizProyecto  = Split-Path -Parent (Split-Path -Parent $PSScriptRoot)
$rutaSalida    = Join-Path $PSScriptRoot "Evidencia_Construccion_API_Registro_Login.docx"

$documentoReferencia = Join-Path $env:USERPROFILE "OneDrive\Escritorio\docs\GA7-220501096-AA2-EV02_modulos_codificados_y_probados.docx"
if (-not (Test-Path $documentoReferencia)) {
    throw "No se encontro el documento de referencia con los estilos: $documentoReferencia"
}

$NS_W    = "http://schemas.openxmlformats.org/wordprocessingml/2006/main"
$CT_DOC  = "application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"
$REL_NS  = "http://schemas.openxmlformats.org/officeDocument/2006/relationships"
$FUENTE_ARIAL = '<w:rFonts w:ascii="Arial" w:eastAsia="Times New Roman" w:hAnsi="Arial" w:cs="Arial"/><w:lang w:val="es-ES"/>'

function Escapar-Xml {
    param([string]$Texto)
    return [System.Security.SecurityElement]::Escape($Texto)
}

function Leer-Parte-Referencia {
    param([string]$Parte)
    $zip = [System.IO.Compression.ZipFile]::OpenRead($documentoReferencia)
    try {
        $entrada = $zip.GetEntry($Parte)
        $lector = New-Object System.IO.StreamReader($entrada.Open())
        return $lector.ReadToEnd()
    } finally {
        $zip.Dispose()
    }
}

# Lee un archivo del proyecto y devuelve sus lineas (para bloques de codigo)
function Leer-Archivo {
    param([string]$RutaRelativa)
    $ruta = Join-Path $raizProyecto $RutaRelativa
    return (Get-Content $ruta -Encoding UTF8)
}

# ---------------------------------------------------------------------
# Constructores XML (convenciones del documento de referencia)
# ---------------------------------------------------------------------
function Xml-Titulo-Seccion {
    param([string]$Texto, [int]$Tamano = 28)
    $t = Escapar-Xml $Texto
    return ("<w:p><w:pPr><w:pStyle w:val=`"Ttulo2`"/><w:jc w:val=`"center`"/>" +
            "<w:rPr>$FUENTE_ARIAL<w:sz w:val=`"$Tamano`"/><w:szCs w:val=`"$Tamano`"/></w:rPr></w:pPr>" +
            "<w:r><w:rPr>$FUENTE_ARIAL<w:sz w:val=`"$Tamano`"/><w:szCs w:val=`"$Tamano`"/></w:rPr>" +
            "<w:t xml:space=`"preserve`">$t</w:t></w:r></w:p>")
}

function Xml-Parrafo {
    param([string]$Texto)
    $t = Escapar-Xml $Texto
    return "<w:p><w:r><w:t xml:space=`"preserve`">$t</w:t></w:r></w:p>"
}

function Xml-Paso {
    param([string]$Texto)
    $t = Escapar-Xml $Texto
    return "<w:p><w:pPr><w:ind w:left=`"360`"/></w:pPr><w:r><w:t xml:space=`"preserve`">$t</w:t></w:r></w:p>"
}

function Xml-Bloque-Codigo {
    param([string[]]$Lineas)
    $sb = New-Object System.Text.StringBuilder
    [void]$sb.Append('<w:p><w:pPr><w:pStyle w:val="captura"/></w:pPr>' +
        '<w:r><w:rPr><w:rFonts w:ascii="Consolas" w:hAnsi="Consolas" w:cs="Consolas"/>' +
        '<w:sz w:val="18"/><w:szCs w:val="18"/></w:rPr>')
    $primera = $true
    foreach ($linea in $Lineas) {
        if (-not $primera) { [void]$sb.Append('<w:br/>') }
        $primera = $false
        [void]$sb.Append("<w:t xml:space=`"preserve`">$(Escapar-Xml $linea)</w:t>")
    }
    [void]$sb.Append('</w:r></w:p>')
    return $sb.ToString()
}

function Xml-Celda {
    param([string]$Texto, [switch]$Encabezado)
    $t = Escapar-Xml $Texto
    $sombra = ""
    $negrita = ""
    $alineacion = ""
    if ($Encabezado) {
        $sombra = '<w:shd w:val="clear" w:color="auto" w:fill="F2F2F2"/>'
        $negrita = "<w:rPr><w:b/><w:bCs/>$FUENTE_ARIAL</w:rPr>"
        $alineacion = '<w:jc w:val="center"/>'
    }
    return ("<w:tc><w:tcPr><w:tcW w:w=`"0`" w:type=`"auto`"/>" +
            '<w:tcBorders>' +
            '<w:top w:val="single" w:sz="6" w:space="0" w:color="666666"/>' +
            '<w:left w:val="single" w:sz="6" w:space="0" w:color="666666"/>' +
            '<w:bottom w:val="single" w:sz="6" w:space="0" w:color="666666"/>' +
            '<w:right w:val="single" w:sz="6" w:space="0" w:color="666666"/>' +
            "</w:tcBorders>$sombra" +
            '<w:tcMar><w:top w:w="90" w:type="dxa"/><w:left w:w="90" w:type="dxa"/>' +
            '<w:bottom w:w="90" w:type="dxa"/><w:right w:w="90" w:type="dxa"/></w:tcMar>' +
            "<w:hideMark/></w:tcPr>" +
            "<w:p><w:pPr><w:spacing w:before=`"180`" w:after=`"180`"/>$alineacion</w:pPr>" +
            "<w:r>$negrita<w:t xml:space=`"preserve`">$t</w:t></w:r></w:p></w:tc>")
}

function Xml-Tabla {
    param([string[][]]$Filas)
    $numFilas = $Filas.Count
    $numCol = $Filas[0].Count
    $sb = New-Object System.Text.StringBuilder
    [void]$sb.Append('<w:tbl><w:tblPr><w:tblW w:w="5000" w:type="pct"/>' +
        '<w:tblCellMar><w:top w:w="15" w:type="dxa"/><w:left w:w="15" w:type="dxa"/>' +
        '<w:bottom w:w="15" w:type="dxa"/><w:right w:w="15" w:type="dxa"/></w:tblCellMar>' +
        '<w:tblLook w:val="04A0" w:firstRow="1" w:lastRow="0" w:firstColumn="1"' +
        ' w:lastColumn="0" w:noHBand="0" w:noVBand="1"/></w:tblPr>')
    for ($f = 0; $f -lt $numFilas; $f++) {
        [void]$sb.Append('<w:tr>')
        for ($c = 0; $c -lt $numCol; $c++) {
            [void]$sb.Append((Xml-Celda $Filas[$f][$c] -Encabezado:($f -eq 0)))
        }
        [void]$sb.Append('</w:tr>')
    }
    [void]$sb.Append('</w:tbl><w:p/>')
    return $sb.ToString()
}

# =====================================================================
# CONSTRUCCION DEL CUERPO
# =====================================================================
$cuerpo = New-Object System.Text.StringBuilder
$culturaEs = New-Object System.Globalization.CultureInfo("es-CO")
$fechaLarga = (Get-Date).ToString("dd 'de' MMMM 'de' yyyy", $culturaEs)

Write-Host "Construyendo contenido..."

# ----- PORTADA -----
[void]$cuerpo.Append(("<w:p><w:pPr><w:pStyle w:val=`"Ttulo1`"/>" +
    "<w:rPr>$FUENTE_ARIAL</w:rPr></w:pPr>" +
    "<w:r><w:rPr>$FUENTE_ARIAL</w:rPr>" +
    "<w:t>Servicio web de registro e inicio de sesion</w:t></w:r>" +
    "<w:r><w:rPr>$FUENTE_ARIAL</w:rPr><w:br/>" +
    "<w:t>GA7-220501096 - Construccion API</w:t></w:r></w:p>"))
[void]$cuerpo.Append(("<w:p><w:pPr><w:pStyle w:val=`"centro`"/>" +
    "<w:rPr>$FUENTE_ARIAL<w:color w:val=`"000000`"/><w:sz w:val=`"22`"/><w:szCs w:val=`"22`"/></w:rPr></w:pPr>" +
    "<w:r><w:rPr>$FUENTE_ARIAL<w:b/><w:bCs/><w:color w:val=`"000000`"/>" +
    "<w:sz w:val=`"22`"/><w:szCs w:val=`"22`"/></w:rPr>" +
    "<w:t>Express con MySQL - Proyecto FastDash Ffoods</w:t></w:r></w:p>"))
[void]$cuerpo.Append(("<w:p><w:pPr><w:pStyle w:val=`"centro`"/>" +
    "<w:rPr>$FUENTE_ARIAL<w:color w:val=`"000000`"/><w:sz w:val=`"22`"/><w:szCs w:val=`"22`"/></w:rPr></w:pPr>" +
    "<w:r><w:rPr>$FUENTE_ARIAL<w:color w:val=`"000000`"/><w:sz w:val=`"22`"/><w:szCs w:val=`"22`"/></w:rPr>" +
    "<w:t>Brandon Felipe Rey Flechas</w:t><w:br/>" +
    "<w:t>Análisis y Desarrollo de Software (ADSO) - Ficha 3235869</w:t><w:br/></w:r></w:p>"))
[void]$cuerpo.Append(("<w:p><w:pPr><w:pStyle w:val=`"centro`"/>" +
    "<w:rPr>$FUENTE_ARIAL<w:color w:val=`"000000`"/><w:sz w:val=`"22`"/><w:szCs w:val=`"22`"/></w:rPr></w:pPr>" +
    "<w:r><w:rPr>$FUENTE_ARIAL<w:color w:val=`"000000`"/><w:sz w:val=`"22`"/><w:szCs w:val=`"22`"/></w:rPr>" +
    "<w:t>$fechaLarga</w:t></w:r></w:p>"))
[void]$cuerpo.Append('<w:p><w:pPr><w:pStyle w:val="centro"/></w:pPr></w:p>')
[void]$cuerpo.Append('<w:p><w:r><w:br w:type="page"/></w:r></w:p>')

# ----- INTRODUCCION -----
[void]$cuerpo.Append((Xml-Titulo-Seccion "Introducción" 32))
[void]$cuerpo.Append((Xml-Parrafo "El presente documento describe el diseño y la codificación de un servicio web de registro e inicio de sesión para el proyecto FastDash Ffoods, un sistema de pedidos de comida rápida construido en el marco del programa Análisis y Desarrollo de Software. El servicio se desarrolló con Node.js, el framework web Express y el driver mysql2 sobre la base de datos MySQL, en cumplimiento de lo visto en el componente formativo Construcción API."))
[void]$cuerpo.Append((Xml-Parrafo "El servicio expone dos rutas: POST /api/auth/registro, que crea una cuenta en la tabla usuario con la contraseña encriptada con bcrypt, y POST /api/auth/login, que valida las credenciales recibidas. Si la autenticación es correcta, responde con el mensaje Autenticacion satisfactoria; en caso contrario, devuelve Error en la autenticacion. Todo el código incluye comentarios explicativos y el desarrollo se registró con herramientas de versionamiento (Git)."))

# ----- OBJETIVOS -----
[void]$cuerpo.Append((Xml-Titulo-Seccion "Objetivos"))
[void]$cuerpo.Append((Xml-Parrafo "Objetivo general: diseñar y codificar un servicio web de registro e inicio de sesión para FastDash Ffoods, que reciba un usuario y una contraseña y valide la autenticación contra la base de datos MySQL, respondiendo satisfactoria o con error según corresponda."))
[void]$cuerpo.Append((Xml-Parrafo "Como objetivos específicos se planteó crear un servidor Express con rutas de registro y login, conectar el servicio con la base de datos mediante el driver mysql2, encriptar las contraseñas con bcrypt, documentar el código con comentarios y gestionar el proyecto con herramientas de versionamiento."))

# ----- ARQUITECTURA Y COMPONENTES -----
[void]$cuerpo.Append((Xml-Titulo-Seccion "Arquitectura y componentes del servicio"))
[void]$cuerpo.Append((Xml-Tabla @(
    @("Componente", "Archivo(s)", "Función"),
    @("Servidor", "api/server.js", "Inicia y pone a escuchar la aplicación Express en el puerto 3000."),
    @("Aplicación Express", "api/app.js", "Configura el middleware JSON y conecta las rutas de autenticación bajo /api/auth."),
    @("Rutas de autenticación", "api/rutas/auth.js", "Implementa POST /registro y POST /login con validaciones y respuestas JSON."),
    @("Conexión a MySQL", "api/db.js", "Crea un pool de conexiones con mysql2/promise leyendo las credenciales de db.properties."),
    @("Migración de contraseñas", "api/hashear_contrasenas.js", "Convierte las contraseñas en texto plano de la tabla usuario a hashes bcrypt."),
    @("Base de datos", "sql/fastdash.sql", "Esquema MySQL de FastDash Ffoods; el servicio usa la tabla usuario.")
)))

# ----- DISEÑO DEL SERVICIO WEB -----
[void]$cuerpo.Append((Xml-Titulo-Seccion "Diseño del servicio web"))
[void]$cuerpo.Append((Xml-Parrafo "El servicio expone dos rutas que reciben y responden en formato JSON:"))
[void]$cuerpo.Append((Xml-Tabla @(
    @("Método", "Ruta", "Descripción", "Respuesta"),
    @("GET", "/", "Comprobación de que el servidor está activo.", "200 - Bienvenido a la API."),
    @("POST", "/api/auth/registro", "Recibe nombreCompleto, email, contrasena y rol (opcional); crea la cuenta encriptando la contraseña.", "201 - Usuario registrado satisfactoriamente."),
    @("POST", "/api/auth/login", "Recibe email y contrasena; valida contra la tabla usuario.", "200 - Autenticacion satisfactoria / 401 - Error en la autenticacion.")
)))
[void]$cuerpo.Append((Xml-Parrafo "Ejemplo de petición de inicio de sesión (JSON):"))
[void]$cuerpo.Append((Xml-Bloque-Codigo @(
    '{',
    '  "email": "ana@fastdash.com",',
    '  "contrasena": "clave123"',
    '}'
)))

# ----- BASE DE DATOS Y CONTRASEÑAS -----
[void]$cuerpo.Append((Xml-Titulo-Seccion "Base de datos y seguridad de contraseñas"))
[void]$cuerpo.Append((Xml-Parrafo "El servicio se conecta a la base de datos fastdash_foods y utiliza la tabla usuario, cuya estructura es:"))
[void]$cuerpo.Append((Xml-Bloque-Codigo @(
    'CREATE TABLE IF NOT EXISTS usuario (',
    '    id_usuario      INT AUTO_INCREMENT PRIMARY KEY,',
    '    nombre_completo VARCHAR(100) NOT NULL,',
    '    email           VARCHAR(100) NOT NULL UNIQUE,',
    '    contrasena      VARCHAR(255) NOT NULL,',
    '    rol             ENUM(''cliente'', ''admin'') NOT NULL DEFAULT ''cliente''',
    ');'
)))
[void]$cuerpo.Append((Xml-Parrafo "Las contraseñas se guardan como hash generado con el algoritmo bcrypt (librería bcryptjs, 10 rondas de salt). De este modo nunca se almacena la contraseña en texto plano. El archivo db.properties, que contiene las credenciales de acceso a MySQL, está excluido del repositorio mediante .gitignore."))

# ----- CODIGO FUENTE -----
[void]$cuerpo.Append((Xml-Titulo-Seccion "Código fuente comentado"))
[void]$cuerpo.Append((Xml-Parrafo "A continuación se presenta el código de los archivos principales del servicio. Todos incluyen comentarios que explican su funcionamiento, cumpliendo el estándar de documentación."))

[void]$cuerpo.Append((Xml-Paso "api/server.js"))
[void]$cuerpo.Append((Xml-Bloque-Codigo (Leer-Archivo 'api/server.js')))
[void]$cuerpo.Append((Xml-Paso "api/app.js"))
[void]$cuerpo.Append((Xml-Bloque-Codigo (Leer-Archivo 'api/app.js')))
[void]$cuerpo.Append((Xml-Paso "api/db.js"))
[void]$cuerpo.Append((Xml-Bloque-Codigo (Leer-Archivo 'api/db.js')))
[void]$cuerpo.Append((Xml-Paso "api/rutas/auth.js"))
[void]$cuerpo.Append((Xml-Bloque-Codigo (Leer-Archivo 'api/rutas/auth.js')))
[void]$cuerpo.Append((Xml-Paso "api/hashear_contrasenas.js"))
[void]$cuerpo.Append((Xml-Bloque-Codigo (Leer-Archivo 'api/hashear_contrasenas.js')))

# ----- PRUEBAS -----
[void]$cuerpo.Append((Xml-Titulo-Seccion "Pruebas funcionales realizadas"))
[void]$cuerpo.Append((Xml-Parrafo "El servicio se probó ejecutando el servidor y enviando peticiones HTTP reales desde PowerShell. Los resultados obtenidos fueron:"))
[void]$cuerpo.Append((Xml-Tabla @(
    @("Prueba", "Petición", "Respuesta obtenida"),
    @("Servidor activo", "GET /", "Bienvenido a la API de FastDash Ffoods."),
    @("Registro", "POST /api/auth/registro (Ana Torres, ana@fastdash.com)", "201 - Usuario registrado satisfactoriamente. (id_usuario 4)"),
    @("Login correcto", "POST /api/auth/login (ana@fastdash.com / clave123)", "200 - Autenticacion satisfactoria."),
    @("Login erróneo", "POST /api/auth/login (ana@fastdash.com / contrasena incorrecta)", "401 - Error en la autenticacion.")
)))
[void]$cuerpo.Append((Xml-Paso "Comando de prueba del login con credenciales correctas:"))
[void]$cuerpo.Append((Xml-Bloque-Codigo @(
    'Invoke-RestMethod -Uri "http://localhost:3000/api/auth/login" -Method Post',
    '  -ContentType "application/json"',
    '  -Body ''{"email":"ana@fastdash.com","contrasena":"clave123"}'''
)))
[void]$cuerpo.Append((Xml-Paso "Respuesta obtenida (JSON):"))
[void]$cuerpo.Append((Xml-Bloque-Codigo @(
    '{"mensaje":"Autenticacion satisfactoria.",',
    ' "usuario":{"id_usuario":4,"nombre_completo":"Ana Torres",',
    '            "email":"ana@fastdash.com","rol":"cliente"}}'
)))

# ----- VERSIONAMIENTO -----
[void]$cuerpo.Append((Xml-Titulo-Seccion "Gestión del versionamiento"))
[void]$cuerpo.Append((Xml-Parrafo "El proyecto utiliza Git desde su creación con repositorio remoto en GitHub (codigo-de-fastDash-Ffoods-actualizado). El desarrollo de este servicio web se registra en el mismo repositorio con commits descriptivos. El historial principal es:"))
try {
    Push-Location $raizProyecto
    $commits = @(git log --pretty=format:"%h - %s (%ad)" --date=short)
    Pop-Location
    foreach ($commit in ($commits | Select-Object -First 12)) {
        [void]$cuerpo.Append((Xml-Paso "- $commit"))
    }
} catch {
    [void]$cuerpo.Append((Xml-Paso "- (No fue posible leer el historial Git al generar este documento.)"))
}
[void]$cuerpo.Append((Xml-Parrafo "La carpeta node_modules y el archivo db.properties con credenciales locales están excluidos del versionamiento mediante .gitignore y se regeneran localmente con el comando npm install."))

# ----- INSTALACION -----
[void]$cuerpo.Append((Xml-Titulo-Seccion "Instrucciones de instalación y ejecución"))
[void]$cuerpo.Append((Xml-Paso "1. Instalar Node.js 24+ y tener MySQL Server corriendo."))
[void]$cuerpo.Append((Xml-Paso "2. Copiar db.properties.example como db.properties y colocar usuario y contraseña de MySQL."))
[void]$cuerpo.Append((Xml-Paso "3. Ejecutar npm install para instalar express, nodemon, mysql2 y bcryptjs."))
[void]$cuerpo.Append((Xml-Paso "4. Ejecutar node api/hashear_contrasenas.js para encriptar las contraseñas existentes."))
[void]$cuerpo.Append((Xml-Paso "5. Ejecutar npm run dev para poner el servidor en modo desarrollo."))
[void]$cuerpo.Append((Xml-Paso "6. Abrir http://localhost:3000 y probar los endpoints de la tabla de diseño."))

# ----- CONCLUSIONES -----
[void]$cuerpo.Append((Xml-Titulo-Seccion "Conclusiones"))
[void]$cuerpo.Append((Xml-Parrafo "Se diseñó y codificó un servicio web funcional de registro e inicio de sesión con Node.js y Express, conectado a MySQL mediante el driver mysql2. Las rutas de registro y login responden correctamente: la autenticación satisfactoria y el error en la autenticación se devuelven según coincidan o no las credenciales, cumpliendo lo solicitado en el enunciado."))
[void]$cuerpo.Append((Xml-Parrafo "El cifrado con bcrypt protege las contraseñas almacenadas, las consultas usan sentencias preparadas (parámetros ?), que evitan la inyección SQL, el código está documentado con comentarios y el proyecto se mantiene bajo control de versiones con Git."))

# =====================================================================
# ENSAMBLADO DEL PAQUETE DOCX
# =====================================================================
$docReferenciaXml = Leer-Parte-Referencia 'word/document.xml'
$coincidencia = [regex]::Match($docReferenciaXml, '<w:sectPr.*?</w:sectPr>')
if (-not $coincidencia.Success) { throw 'No se encontro sectPr en el documento de referencia.' }
$sectPr = $coincidencia.Value

$estilos = Leer-Parte-Referencia 'word/styles.xml'
$estilos = $estilos.Replace(
    'w:ascii="Times New Roman" w:eastAsia="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman"',
    'w:ascii="Arial" w:eastAsia="Arial" w:hAnsi="Arial" w:cs="Arial"')
$estilos = $estilos.Replace('Times New Roman', 'Arial')

$tema = Leer-Parte-Referencia 'word/theme/theme1.xml'
$tema = [regex]::Replace($tema, '(<a:latin typeface=")[^"]*(")', '$1Arial$2')

$xmlDocumento = ('<?xml version="1.0" encoding="UTF-8" standalone="yes"?>' +
    "<w:document xmlns:w=`"$NS_W`" xmlns:r=`"http://schemas.openxmlformats.org/officeDocument/2006/relationships`">" +
    "<w:body>" + $cuerpo.ToString() + $sectPr + "</w:body></w:document>")

[xml]$xmlDocumento | Out-Null

if (Test-Path $rutaSalida) { Remove-Item $rutaSalida -Force }

$codificacion = New-Object System.Text.UTF8Encoding($false)
$paquete = [System.IO.Packaging.Package]::Open($rutaSalida, [System.IO.FileMode]::Create)
try {
    function Agregar-Parte {
        param([string]$RutaParte, [string]$ContentType, [string]$Contenido)
        $uri = New-Object System.Uri($RutaParte, [System.UriKind]::RelativeOrAbsolute)
        $parte = $paquete.CreatePart($uri, $ContentType)
        $flujo = $parte.GetStream()
        $bytes = $codificacion.GetBytes($Contenido)
        $flujo.Write($bytes, 0, $bytes.Length)
        $flujo.Close()
        return $parte
    }

    $parteDoc = Agregar-Parte '/word/document.xml' $CT_DOC $xmlDocumento

    foreach ($par in @(
        @{ nombre = 'styles.xml';      tipo = "$REL_NS/styles" },
        @{ nombre = 'settings.xml';    tipo = "$REL_NS/settings" },
        @{ nombre = 'fontTable.xml';   tipo = "$REL_NS/fontTable" },
        @{ nombre = 'webSettings.xml'; tipo = "$REL_NS/webSettings" }
    )) {
        $contenidoParte = switch ($par.nombre) {
            'styles.xml' { $estilos }
            default      { Leer-Parte-Referencia "word/$($par.nombre)" }
        }
        $nombreTipo = ($par.tipo -split '/')[-1]
        $contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.$nombreTipo+xml"
        Agregar-Parte "/word/$($par.nombre)" $contentType $contenidoParte | Out-Null
        $relativa = New-Object System.Uri($par.nombre, [System.UriKind]::Relative)
        $parteDoc.CreateRelationship($relativa, [System.IO.Packaging.TargetMode]::Internal, $par.tipo) | Out-Null
    }

    Agregar-Parte '/word/theme/theme1.xml' 'application/vnd.openxmlformats-officedocument.theme+xml' $tema | Out-Null
    $relTema = New-Object System.Uri('theme/theme1.xml', [System.UriKind]::Relative)
    $parteDoc.CreateRelationship($relTema, [System.IO.Packaging.TargetMode]::Internal, "$REL_NS/theme") | Out-Null

    $uriTargetDoc = New-Object System.Uri("word/document.xml", [System.UriKind]::Relative)
    $paquete.CreateRelationship($uriTargetDoc, [System.IO.Packaging.TargetMode]::Internal, "$REL_NS/officeDocument") | Out-Null

    $paquete.Flush()
} finally {
    $paquete.Close()
}

$tamano = (Get-Item $rutaSalida).Length
Write-Host "Informe generado en: $rutaSalida ($tamano bytes)"