$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName WindowsBase

$ruta = Join-Path $PSScriptRoot 'Informe_Tecnico_FastDash_Ffoods.docx'

Write-Host '[1] Validando estructura del paquete...'
$paquete = [System.IO.Packaging.Package]::Open($ruta, [System.IO.FileMode]::Open, [System.IO.FileAccess]::Read)
$partes = $paquete.GetParts() | ForEach-Object { $_.Uri.ToString() }
$partes | ForEach-Object { Write-Host "    parte: $_" }
$parteDoc = $paquete.GetPart([System.IO.Packaging.PackUriHelper]::CreatePartUri([System.Uri]'word/document.xml'))
$xml = New-Object System.Xml.XmlDocument
$xml.Load($parteDoc.GetStream())
Write-Host ("    parrafos w:p: " + ($xml.GetElementsByTagName('p').Count))
Write-Host ("    tablas w:tbl: " + ($xml.GetElementsByTagName('tbl').Count))
$paquete.Close()
Write-Host '[1] Estructura OK'

Write-Host '[2] Abriendo con Microsoft Word (solo lectura)...'
$word = New-Object -ComObject Word.Application
$word.Visible = $false
$word.DisplayAlerts = 0
try {
    $doc = $word.Documents.Open($ruta, $false, $true)
    $texto = $doc.Content.Text
    Write-Host ('    Caracteres de texto: ' + $texto.Length)
    Write-Host ('    Contiene seccion JDBC: ' + $texto.Contains('JDBC'))
    Write-Host ('    Contiene tabla CRUD: ' + $texto.Contains('Restaurantes'))
    $doc.Close($false)
} finally {
    $word.Quit()
}
Write-Host '[2] El documento abre correctamente en Word.'
