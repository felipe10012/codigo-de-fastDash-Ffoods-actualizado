# =====================================================================
# FastDash Ffoods - Generador del Informe Tecnico (.docx)
# Evidencia GA7-220501096-AA2-EV01
#
# Genera el documento Word directamente como paquete Open XML usando
# System.IO.Packaging (no requiere abrir Microsoft Word, evitando
# cuelgues de COM detectados en este equipo).
#
# Uso: powershell -ExecutionPolicy Bypass -File generar_informe.ps1
# =====================================================================

$ErrorActionPreference = "Stop"
Add-Type -AssemblyName WindowsBase

$raizProyecto = Split-Path -Parent (Split-Path -Parent $PSScriptRoot)
$rutaSalida   = Join-Path $PSScriptRoot "Informe_Tecnico_FastDash_Ffoods.docx"

$NS_W   = "http://schemas.openxmlformats.org/wordprocessingml/2006/main"
$CT_DOC = "application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"
$CT_STY = "application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml"
$REL_DOC= "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument"
$REL_STY= "http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles"

function Escapar-Xml {
    param([string]$Texto)
    return [System.Security.SecurityElement]::Escape($Texto)
}

# ---------------------------------------------------------------------
# Constructores de XML de parrafos
# ---------------------------------------------------------------------
function Xml-Parrafo {
    param([string]$Texto, [string]$Estilo)
    $t = Escapar-Xml $Texto
    if ($Estilo) {
        return "<w:p><w:pPr><w:pStyle w:val=`"$Estilo`"/></w:pPr><w:r><w:t xml:space=`"preserve`">$t</w:t></w:r></w:p>"
    }
    return "<w:p><w:r><w:t xml:space=`"preserve`">$t</w:t></w:r></w:p>"
}

function Xml-Vineta {
    param([string]$Texto)
    $t = Escapar-Xml $Texto
    return ("<w:p><w:pPr><w:ind w:left=`"360`" w:hanging=`"180`"/></w:pPr>" +
            "<w:r><w:t xml:space=`"preserve`">- $t</w:t></w:r></w:p>")
}

function Xml-Codigo {
    param([string]$Linea)
    $t = Escapar-Xml $Linea
    return ("<w:p><w:pPr><w:ind w:left=`"360`"/></w:pPr>" +
            "<w:r><w:rPr><w:rFonts w:ascii=`"Consolas`" w:hAnsi=`"Consolas`"/><w:sz w:val=`"17`"/></w:rPr>" +
            "<w:t xml:space=`"preserve`">$t</w:t></w:r></w:p>")
}

function Xml-Salto-Pagina {
    return '<w:p><w:r><w:br w:type="page"/></w:r></w:p>'
}

function Xml-Centrado {
    param([string]$Texto, [int]$TamanoMediosPuntos, [switch]$Negrita)
    $t = Escapar-Xml $Texto
    $b = ""
    if ($Negrita) { $b = "<w:b/>" }
    return ("<w:p><w:pPr><w:jc w:val=`"center`"/></w:pPr>" +
            "<w:r><w:rPr>$b<w:sz w:val=`"$TamanoMediosPuntos`"/></w:rPr>" +
            "<w:t xml:space=`"preserve`">$t</w:t></w:r></w:p>")
}

function Xml-Tabla {
    param([string[][]]$Filas)
    $numFilas = $Filas.Count
    $numCol = $Filas[0].Count

    $bordes = ('<w:tblBorders>' +
        '<w:top w:val="single" w:sz="4" w:color="666666"/>' +
        '<w:left w:val="single" w:sz="4" w:color="666666"/>' +
        '<w:bottom w:val="single" w:sz="4" w:color="666666"/>' +
        '<w:right w:val="single" w:sz="4" w:color="666666"/>' +
        '<w:insideH w:val="single" w:sz="4" w:color="666666"/>' +
        '<w:insideV w:val="single" w:sz="4" w:color="666666"/>' +
        '</w:tblBorders>')
    $ancho = '<w:tblW w:w="5000" w:type="pct"/>'

    $sb = New-Object System.Text.StringBuilder
    [void]$sb.Append("<w:tbl><w:tblPr>$ancho$bordes</w:tblPr>")
    for ($f = 0; $f -lt $numFilas; $f++) {
        [void]$sb.Append('<w:tr>')
        for ($c = 0; $c -lt $numCol; $c++) {
            $t = Escapar-Xml $Filas[$f][$c]
            if ($f -eq 0) {
                [void]$sb.Append(("<w:tc><w:tcPr><w:shd w:val=`"clear`" w:fill=`"E7DAF5`"/></w:tcPr>" +
                    "<w:p><w:r><w:rPr><w:b/></w:rPr><w:t xml:space=`"preserve`">$t</w:t></w:r></w:p></w:tc>"))
            } else {
                [void]$sb.Append(("<w:tc><w:tcPr/>" +
                    "<w:p><w:r><w:t xml:space=`"preserve`">$t</w:t></w:r></w:p></w:tc>"))
            }
        }
        [void]$sb.Append('</w:tr>')
    }
    [void]$sb.Append('</w:tbl><w:p/>')
    return $sb.ToString()
}

# =====================================================================
# CONSTRUCCION DEL CUERPO DEL DOCUMENTO
# =====================================================================
$cuerpo = New-Object System.Text.StringBuilder

Write-Host "Construyendo contenido..."

# ----- PORTADA -----
[void]$cuerpo.Append((Xml-Centrado "SERVICIO NACIONAL DE APRENDIZAJE - SENA" 36 -Negrita))
[void]$cuerpo.Append((Xml-Centrado "Tecnologia en Analisis y Desarrollo de Software" 24))
[void]$cuerpo.Append((Xml-Parrafo ""))
[void]$cuerpo.Append((Xml-Centrado "FastDash Ffoods" 48 -Negrita))
[void]$cuerpo.Append((Xml-Centrado "Informe tecnico del plan de trabajo para la construccion de software" 24))
[void]$cuerpo.Append((Xml-Centrado "con tecnologias seleccionadas - Codificacion de modulos con JDBC y MySQL" 24))
[void]$cuerpo.Append((Xml-Parrafo ""))
[void]$cuerpo.Append((Xml-Centrado "Evidencia: GA7-220501096-AA2-EV01" 24 -Negrita))
[void]$cuerpo.Append((Xml-Parrafo ""))
[void]$cuerpo.Append((Xml-Centrado "Aprendiz: Felipe Andrade" 24))
[void]$cuerpo.Append((Xml-Centrado "Instructor: Juan Antonio Hernandez" 24))
[void]$cuerpo.Append((Xml-Centrado "Fecha: $(Get-Date -Format 'dd/MM/yyyy')" 24))
[void]$cuerpo.Append((Xml-Salto-Pagina))

# ----- 1. INTRODUCCION -----
[void]$cuerpo.Append((Xml-Parrafo "1. Introduccion" "Titulo1"))
[void]$cuerpo.Append((Xml-Parrafo "El presente documento describe el plan de trabajo seguido para la construccion del modulo de gestion de FastDash Ffoods, una aplicacion orientada a la administracion de restaurantes, productos, usuarios, pedidos y repartidores. La codificacion se realizo en Java con conexion a la base de datos MySQL mediante la API JDBC (Java Database Connectivity), cumpliendo los requisitos de la evidencia GA7-220501096-AA2-EV01." "Normal"))
[void]$cuerpo.Append((Xml-Parrafo "El proyecto se desarrollo bajo control de versiones Git con publicacion en GitHub, aplicando estandares de codificacion en el nombramiento de paquetes, clases, metodos y variables, e implementando las funcionalidades de insercion, consulta, actualizacion y eliminacion (CRUD) para cada modulo del sistema." "Normal"))

# ----- 2. OBJETIVOS -----
[void]$cuerpo.Append((Xml-Parrafo "2. Objetivos" "Titulo1"))
[void]$cuerpo.Append((Xml-Parrafo "2.1 Objetivo general" "Titulo2"))
[void]$cuerpo.Append((Xml-Parrafo "Codificar el modulo del proyecto FastDash Ffoods estableciendo conexion con una base de datos MySQL por medio de JDBC, siguiendo estandares de codificacion y utilizando herramientas de versionamiento." "Normal"))
[void]$cuerpo.Append((Xml-Parrafo "2.2 Objetivos especificos" "Titulo2"))
[void]$cuerpo.Append((Xml-Vineta "Disenar la arquitectura por capas del modulo (modelo, DAO, servicio y presentacion de consola)."))
[void]$cuerpo.Append((Xml-Vineta "Establecer la conexion a MySQL con JDBC usando DriverManager y PreparedStatement."))
[void]$cuerpo.Append((Xml-Vineta "Implementar las operaciones CRUD sobre las cinco entidades del sistema."))
[void]$cuerpo.Append((Xml-Vineta "Aplicar convenciones de nombramiento de variables, metodos, clases y paquetes."))
[void]$cuerpo.Append((Xml-Vineta "Versionar el codigo fuente con Git y GitHub registrando avances incrementales."))

# ----- 3. ALCANCE -----
[void]$cuerpo.Append((Xml-Parrafo "3. Alcance del modulo" "Titulo1"))
[void]$cuerpo.Append((Xml-Parrafo "La aplicacion de consola permite gestionar cinco entidades: restaurante, producto, usuario, pedido y repartidor. Para cada entidad se ofrecen las operaciones de registrar (INSERT), consultar por id (SELECT), listar (SELECT), actualizar (UPDATE) y eliminar (DELETE), con validaciones de datos en la capa de servicio." "Normal"))

# ----- 4. TECNOLOGIAS -----
[void]$cuerpo.Append((Xml-Parrafo "4. Tecnologias seleccionadas y justificacion" "Titulo1"))
[void]$cuerpo.Append((Xml-Tabla @(
    @("Tecnologia", "Version", "Uso en el proyecto", "Justificacion"),
    @("Java SE", "17+", "Lenguaje principal del modulo", "Lenguaje orientado a objetos visto en el componente formativo, multiplataforma y con gran documentacion."),
    @("JDBC API", "4.2", "Conexion y consultas a la base de datos", "API estandar de Java para acceso a datos relacionales; es la tecnologia solicitada en la evidencia."),
    @("MySQL Connector/J", "8.0.33", "Driver JDBC para MySQL", "Driver oficial de MySQL para Java, distribuido como archivo JAR."),
    @("MySQL Server", "8.0+", "Base de datos relacional", "Sistema gestor relacional gratuito, robusto y ampliamente usado."),
    @("Git / GitHub", "2.x", "Control de versiones", "Herramienta obligatoria de versionamiento; GitHub aloja el repositorio remoto."),
    @("HTML / CSS / JS", "-", "Prototipos de interfaz", "Permitieron construir prototipos navegables de las pantallas antes de la codificacion.")
)))

# ----- 5. PLAN DE TRABAJO -----
[void]$cuerpo.Append((Xml-Parrafo "5. Plan de trabajo" "Titulo1"))
[void]$cuerpo.Append((Xml-Tabla @(
    @("Fase", "Actividades", "Entregable"),
    @("1. Planeacion", "Definicion del alcance, historias de usuario y seleccion de tecnologias.", "Documento de alcance y HU."),
    @("2. Analisis y diseno", "Diagrama de clases, diagrama de casos de uso, modelo relacional y prototipos de pantallas.", "Diagramas y prototipos HTML."),
    @("3. Preparacion del entorno", "Instalacion de JDK, MySQL Server, Git; creacion del repositorio remoto en GitHub.", "Repositorio inicial con primer commit."),
    @("4. Base de datos", "Creacion del script SQL con tablas, llaves foraneas y datos de prueba.", "sql/fastdash.sql ejecutado en MySQL."),
    @("5. Codificacion", "Capa modelo, capa DAO con JDBC, capa de servicios y menu de consola; operaciones CRUD.", "Codigo fuente compilado en src/."),
    @("6. Pruebas", "Pruebas de conexion y CRUD completo contra MySQL desde la aplicacion.", "Evidencias de ejecucion."),
    @("7. Documentacion y entrega", "Informe tecnico, README y publicacion de commits en GitHub.", "Repositorio publicado.")
)))

# ----- 6. ARQUITECTURA -----
[void]$cuerpo.Append((Xml-Parrafo "6. Arquitectura del software" "Titulo1"))
[void]$cuerpo.Append((Xml-Parrafo "El modulo se organizo en capas con responsabilidades bien definidas, lo que facilita el mantenimiento y separa el acceso a datos de la logica de negocio:" "Normal"))
[void]$cuerpo.Append((Xml-Vineta "Capa de presentacion (com.fastdash.Main): menus de consola e interaccion con el usuario."))
[void]$cuerpo.Append((Xml-Vineta "Capa de servicios (com.fastdash.service): reglas de negocio y validaciones."))
[void]$cuerpo.Append((Xml-Vineta "Capa de acceso a datos (com.fastdash.dao): interfaces DAO y su fabrica DaoFactory."))
[void]$cuerpo.Append((Xml-Vineta "Implementacion JDBC (com.fastdash.dao.impl.jdbc): consultas SQL con PreparedStatement."))
[void]$cuerpo.Append((Xml-Vineta "Utilidades (com.fastdash.util): ConexionBD (conexion JDBC) y Validaciones."))
[void]$cuerpo.Append((Xml-Vineta "Modelo (com.fastdash.model): entidades Restaurante, Producto, Usuario, Pedido y Repartidor."))
foreach ($linea in @(
    "+--------------------------------------------------+",
    "|  Main.java  (consola / presentacion)             |",
    "+------------------------+-------------------------+",
    "                         v",
    "|  Services  (validaciones y reglas de negocio)    |",
    "+------------------------+-------------------------+",
    "                         v",
    "|  DaoFactory -> interfaces DAO                    |",
    "+------------------------+-------------------------+",
    "                         v",
    "|  *DaoImplJdbc  (PreparedStatement + SQL)         |",
    "+------------------------+-------------------------+",
    "                         v",
    "|  ConexionBD  -> DriverManager.getConnection()    |",
    "+------------------------+-------------------------+",
    "                         v",
    "|  MySQL Server  (base fastdash_foods)             |",
    "+--------------------------------------------------+"
)) { [void]$cuerpo.Append((Xml-Codigo $linea)) }

# ----- 7. CONEXION JDBC -----
[void]$cuerpo.Append((Xml-Parrafo "7. Conexion a la base de datos con JDBC" "Titulo1"))
[void]$cuerpo.Append((Xml-Parrafo "La clase ConexionBD (paquete com.fastdash.util) centraliza la obtencion de conexiones. Los parametros se leen del archivo db.properties para no publicar credenciales en el repositorio. Todas las consultas usan sentencias preparadas (PreparedStatement), lo que evita inyeccion SQL, y bloques try-with-resources que cierran automaticamente Connection, Statement y ResultSet." "Normal"))
foreach ($linea in @(
    'public static Connection obtenerConexion() throws SQLException {',
    '    String url = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DE_DATOS',
    '            + "?useSSL=false&serverTimezone=UTC"',
    '            + "&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";',
    '    return DriverManager.getConnection(url, USUARIO, CONTRASENA);',
    '}'
)) { [void]$cuerpo.Append((Xml-Codigo $linea)) }

# ----- 8. ESTANDARES -----
[void]$cuerpo.Append((Xml-Parrafo "8. Estandares de codificacion aplicados" "Titulo1"))
[void]$cuerpo.Append((Xml-Parrafo "8.1 Nombramiento de paquetes" "Titulo2"))
[void]$cuerpo.Append((Xml-Parrafo "Los paquetes se escriben en minusculas y siguen la convencion de dominio invertido:" "Normal"))
[void]$cuerpo.Append((Xml-Tabla @(
    @("Paquete", "Contenido"),
    @("com.fastdash", "Clase principal Main."),
    @("com.fastdash.model", "Entidades del dominio."),
    @("com.fastdash.dao", "Interfaces de acceso a datos."),
    @("com.fastdash.dao.impl.jdbc", "Implementaciones JDBC de los DAO."),
    @("com.fastdash.service", "Servicios con reglas de negocio."),
    @("com.fastdash.util", "Utilidades (conexion, validaciones).")
)))
[void]$cuerpo.Append((Xml-Parrafo "8.2 Nombramiento de clases" "Titulo2"))
[void]$cuerpo.Append((Xml-Parrafo "Las clases usan notacion PascalCase con sufijos que indican su rol:" "Normal"))
[void]$cuerpo.Append((Xml-Tabla @(
    @("Clase", "Convencion aplicada"),
    @("Restaurante", "Entidad: sustantivo en singular."),
    @("RestauranteDao", "Interfaz DAO: sufijo Dao."),
    @("RestauranteDaoImplJdbc", "Implementacion: sufijos ImplJdbc."),
    @("RestauranteService", "Servicio: sufijo Service."),
    @("ConexionBD", "Utilidad descriptiva PascalCase."),
    @("DaoFactory", "Patron de creacion Factory.")
)))
[void]$cuerpo.Append((Xml-Parrafo "8.3 Nombramiento de metodos" "Titulo2"))
[void]$cuerpo.Append((Xml-Parrafo "Los metodos usan camelCase iniciando con verbo en infinitivo:" "Normal"))
[void]$cuerpo.Append((Xml-Tabla @(
    @("Metodo", "Operacion CRUD"),
    @("insertar(Restaurante)", "INSERT"),
    @("consultarPorId(int)", "SELECT por llave primaria"),
    @("listarTodos()", "SELECT de todos los registros"),
    @("actualizar(Restaurante)", "UPDATE"),
    @("eliminar(int)", "DELETE"),
    @("mapearRestaurante(ResultSet)", "Mapeo fila-objeto (privado)")
)))
[void]$cuerpo.Append((Xml-Parrafo "8.4 Nombramiento de variables" "Titulo2"))
[void]$cuerpo.Append((Xml-Tabla @(
    @("Tipo", "Variable", "Convencion"),
    @("Constante", "TECLADO, ARCHIVO_PROPIEDADES", "MAYUSCULAS_CON_GUIONES_BAJOS."),
    @("Objeto", "restaurante, sentencia, resultado", "camelCase descriptivo."),
    @("Excepcion", "excepcion", "camelCase, sin abreviaturas."),
    @("SQL", "String sql", "camelCase corto y claro.")
)))

# ----- 9. CRUD -----
[void]$cuerpo.Append((Xml-Parrafo "9. Funcionalidades CRUD implementadas" "Titulo1"))
[void]$cuerpo.Append((Xml-Parrafo "Todas las operaciones se ejecutan contra MySQL mediante JDBC y fueron probadas desde la aplicacion:" "Normal"))
[void]$cuerpo.Append((Xml-Tabla @(
    @("Modulo", "Insertar", "Consultar", "Actualizar", "Eliminar"),
    @("Restaurantes", "Si", "Por id y listado", "Si", "Si"),
    @("Productos", "Si", "Por id, listado y por restaurante", "Si", "Si"),
    @("Usuarios", "Si", "Por id, por email y listado", "Si", "Si"),
    @("Pedidos", "Si", "Por id, por usuario y por repartidor", "Si", "Si"),
    @("Repartidores", "Si", "Por id, listado y por restaurante", "Si", "Si")
)))
[void]$cuerpo.Append((Xml-Parrafo "Ejemplo de operacion INSERT con JDBC (RestauranteDaoImplJdbc.insertar):" "Normal"))
foreach ($linea in @(
    'public void insertar(Restaurante restaurante) {',
    '    String sql = "INSERT INTO restaurante (nombre, direccion, telefono)"',
    '            + " VALUES (?, ?, ?)";',
    '    try (Connection conexion = ConexionBD.obtenerConexion();',
    '         PreparedStatement sentencia = conexion.prepareStatement(sql,',
    '                 Statement.RETURN_GENERATED_KEYS)) {',
    '        sentencia.setString(1, restaurante.getNombre());',
    '        sentencia.setString(2, restaurante.getDireccion());',
    '        sentencia.setString(3, restaurante.getTelefono());',
    '        sentencia.executeUpdate();',
    '        try (ResultSet llaves = sentencia.getGeneratedKeys()) { ... }',
    '    } catch (SQLException excepcion) { ... }',
    '}'
)) { [void]$cuerpo.Append((Xml-Codigo $linea)) }

# ----- 10. HISTORIAS DE USUARIO -----
[void]$cuerpo.Append((Xml-Parrafo "10. Historias de usuario atendidas" "Titulo1"))
[void]$cuerpo.Append((Xml-Tabla @(
    @("Codigo", "Historia de usuario", "Criterio de aceptacion"),
    @("HU-01", "Como administrador quiero registrar restaurantes para incorporarlos al sistema.", "El restaurante queda guardado en MySQL con su identificador generado."),
    @("HU-02", "Como administrador quiero gestionar productos de cada restaurante.", "CRUD completo disponible y filtrado por restaurante."),
    @("HU-03", "Como administrador quiero registrar usuarios con rol cliente o admin.", "El email es unico y la contrasena cumple longitud minima."),
    @("HU-04", "Como cliente quiero crear pedidos y conocer su estado.", "El pedido guarda fecha, total, estado y repartidor asignado."),
    @("HU-05", "Como administrador quiero gestionar repartidores por restaurante.", "CRUD completo con vehiculo y telefono del repartidor.")
)))

# ----- 11. VERSIONAMIENTO -----
[void]$cuerpo.Append((Xml-Parrafo "11. Gestion del versionamiento" "Titulo1"))
[void]$cuerpo.Append((Xml-Parrafo "El proyecto se creo con Git desde el primer dia y se publica en GitHub. Cada funcionalidad se registro en commits independientes con mensajes descriptivos. El historial principal del repositorio es:" "Normal"))
try {
    Push-Location $raizProyecto
    $commits = @(git log --pretty=format:"%h - %s (%ad)" --date=short)
    Pop-Location
    foreach ($commit in ($commits | Select-Object -First 12)) {
        [void]$cuerpo.Append((Xml-Vineta $commit))
    }
} catch {
    [void]$cuerpo.Append((Xml-Vineta "(No fue posible leer el historial Git al generar este documento.)"))
}
[void]$cuerpo.Append((Xml-Parrafo "Adicionalmente, el archivo db.properties con credenciales locales esta ignorado por Git mediante .gitignore, publicando solo la plantilla db.properties.example." "Normal"))

# ----- 12. INSTALACION -----
[void]$cuerpo.Append((Xml-Parrafo "12. Instrucciones de instalacion y ejecucion" "Titulo1"))
[void]$cuerpo.Append((Xml-Vineta "Requisitos: JDK 17 o superior, MySQL Server 8+ y Git."))
[void]$cuerpo.Append((Xml-Vineta "Crear la base de datos ejecutando el script sql/fastdash.sql."))
[void]$cuerpo.Append((Xml-Vineta "Copiar db.properties.example como db.properties y colocar usuario y contrasena de MySQL."))
[void]$cuerpo.Append((Xml-Vineta "Compilar con compilar.bat (javac con el driver mysql-connector-j-8.0.33.jar)."))
[void]$cuerpo.Append((Xml-Vineta "Ejecutar con ejecutar.bat (java com.fastdash.Main)."))

# ----- 13. CONCLUSIONES -----
[void]$cuerpo.Append((Xml-Parrafo "13. Conclusiones" "Titulo1"))
[void]$cuerpo.Append((Xml-Vineta "Se construyo un modulo funcional con arquitectura por capas que conecta Java y MySQL mediante JDBC."))
[void]$cuerpo.Append((Xml-Vineta "Las operaciones CRUD funcionan sobre las cinco entidades y quedaron verificadas con pruebas de ejecucion."))
[void]$cuerpo.Append((Xml-Vineta "El uso de PreparedStatement y try-with-resources garantiza consultas seguras y cierre correcto de recursos."))
[void]$cuerpo.Append((Xml-Vineta "El versionamiento en Git permitio trazabilidad completa del desarrollo y recuperacion de archivos cuando fue necesario."))
[void]$cuerpo.Append((Xml-Vineta "El cumplimiento de estandares de nombramiento hace el codigo legible y mantenible."))

# =====================================================================
# ENSAMBLADO DEL PAQUETE DOCX (Open XML)
# =====================================================================
$xmlDocumento = ('<?xml version="1.0" encoding="UTF-8" standalone="yes"?>' +
    "<w:document xmlns:w=`"$NS_W`"><w:body>" + $cuerpo.ToString() +
    '<w:sectPr><w:pgSz w:w="11906" w:h="16838"/>' +
    '<w:pgMar w:top="1440" w:right="1440" w:bottom="1440" w:left="1440"/></w:sectPr>' +
    '</w:body></w:document>')

$xmlEstilos = ('<?xml version="1.0" encoding="UTF-8" standalone="yes"?>' +
    "<w:styles xmlns:w=`"$NS_W`">" +
    '<w:docDefaults><w:rPrDefault><w:rPr>' +
    '<w:rFonts w:ascii="Calibri" w:hAnsi="Calibri"/><w:sz w:val="22"/>' +
    '</w:rPr></w:rPrDefault></w:docDefaults>' +
    '<w:style w:type="paragraph" w:default="1" w:styleId="Normal"><w:name w:val="Normal"/>' +
    '<w:pPr><w:spacing w:after="120"/></w:pPr></w:style>' +
    '<w:style w:type="paragraph" w:styleId="Titulo1"><w:name w:val="heading 1"/>' +
    '<w:basedOn w:val="Normal"/><w:pPr><w:keepNext/><w:outlineLvl w:val="0"/>' +
    '<w:spacing w:before="360" w:after="160"/></w:pPr>' +
    '<w:rPr><w:b/><w:color w:val="5B21A8"/><w:sz w:val="32"/></w:rPr></w:style>' +
    '<w:style w:type="paragraph" w:styleId="Titulo2"><w:name w:val="heading 2"/>' +
    '<w:basedOn w:val="Normal"/><w:pPr><w:keepNext/><w:outlineLvl w:val="1"/>' +
    '<w:spacing w:before="280" w:after="120"/></w:pPr>' +
    '<w:rPr><w:b/><w:color w:val="7C3AED"/><w:sz w:val="26"/></w:rPr></w:style>' +
    '</w:styles>')

# Validar que los XML esten bien formados antes de empaquetar
[xml]$xmlDocumento | Out-Null
[xml]$xmlEstilos | Out-Null

if (Test-Path $rutaSalida) { Remove-Item $rutaSalida -Force }

$codificacion = New-Object System.Text.UTF8Encoding($false)
$paquete = [System.IO.Packaging.Package]::Open($rutaSalida, [System.IO.FileMode]::Create)

try {
    $uriDoc = New-Object System.Uri("/word/document.xml", [System.UriKind]::RelativeOrAbsolute)
    $parteDoc = $paquete.CreatePart($uriDoc, $CT_DOC)
    $flujoDoc = $parteDoc.GetStream()
    $bytesDoc = $codificacion.GetBytes($xmlDocumento)
    $flujoDoc.Write($bytesDoc, 0, $bytesDoc.Length)
    $flujoDoc.Close()

    $uriSty = New-Object System.Uri("/word/styles.xml", [System.UriKind]::RelativeOrAbsolute)
    $parteSty = $paquete.CreatePart($uriSty, $CT_STY)
    $flujoSty = $parteSty.GetStream()
    $bytesSty = $codificacion.GetBytes($xmlEstilos)
    $flujoSty.Write($bytesSty, 0, $bytesSty.Length)
    $flujoSty.Close()

    $relSty = New-Object System.Uri("styles.xml", [System.UriKind]::Relative)
    $parteDoc.CreateRelationship($relSty, [System.IO.Packaging.TargetMode]::Internal, $REL_STY) | Out-Null

    $uriTargetDoc = New-Object System.Uri("word/document.xml", [System.UriKind]::Relative)
    $paquete.CreateRelationship($uriTargetDoc, [System.IO.Packaging.TargetMode]::Internal, $REL_DOC) | Out-Null

    $paquete.Flush()
} finally {
    $paquete.Close()
}

$tamano = (Get-Item $rutaSalida).Length
Write-Host "Informe generado en: $rutaSalida ($tamano bytes)"
