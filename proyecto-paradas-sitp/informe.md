# Informe — Consumo de API REST pública desde PHP

**Proyecto Paradas SITP**

## Endpoint utilizado

Se consumió el servicio REST tipo **ArcGIS** llamado **"Paraderos Zonales SITP"**, publicado por Catastro Bogotá (capa 8 del mapa `Mapa_Referencia`):

```
https://serviciosgis.catastrobogota.gov.co/arcgis/rest/services/Mapa_Referencia/Mapa_Referencia/MapServer/8/query
```

La consulta se construye con parámetros:

| Parámetro | Valor usado | Qué hace |
|---|---|---|
| `where` | `UPPER(LOCALIDAD)='KENNEDY'` | Filtra los paraderos por localidad. El valor se envía en **mayúsculas** porque así lo exige la API. |
| `outFields` | `NOMBRE,LOCALIDAD,DIRECCION_,LATITUD,LONGITUD,CENEFA` | Campos que se desean recibir por cada paradero. |
| `f` | `geojson` | Formato de respuesta GeoJSON. |

## Datos obtenidos

La API responde en **formato GeoJSON**: un objeto `FeatureCollection` cuyo arreglo `features` contiene un elemento por paradero, con sus coordenadas (punto) y sus atributos. De cada paradero se obtuvo:

- **CENEFA**: código identificador del paradero.
- **NOMBRE**: nombre del paradero (ej.: "Pq. Alto de los Lagartos").
- **DIRECCION_**: dirección o cruce aproximado (ej.: "AV. Boyacá - AC 127").
- **LATITUD** y **LONGITUD**: coordenadas geográficas.
- **LOCALIDAD**: localidad a la que pertenece.

Por ejemplo, la consulta para la localidad **Kennedy** devolvió alrededor de **896 paraderos**, y para **Suba** unos **829**. La página los presenta en una tabla HTML con código, nombre, dirección y coordenadas, e incluye manejo de errores:

- si la API no responde,
- si no hay resultados,
- si la localidad no existe o está mal escrita.

## Nota

El servicio de paraderos puede volverse lento o responder vacío en momentos de carga alta (es una consideración indicada en el propio enunciado de la actividad). En esos casos basta con reintentar la consulta, y la página muestra un mensaje amigable en lugar de un error de PHP.