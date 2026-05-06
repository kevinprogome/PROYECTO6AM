# Data Model

This document contains the JSON model, ER description, and data dictionary derived from the current JPA entities.

## 1) Modelo JSON (esquema representativo)

```json
{
  "Usuario": {
    "id": "long",
    "nombre": "string(120)",
    "email": "string(180)",
    "rol": "enum[ADMIN, OPERADOR]",
    "provider": "enum[GOOGLE]",
    "providerId": "string(120)",
    "activo": "boolean",
    "createdAt": "datetime",
    "updatedAt": "datetime",
    "invernaderos": ["Invernadero"]
  },
  "Invernadero": {
    "id": "long",
    "usuarioId": "long (FK Usuario.id)",
    "nombre": "string(120)",
    "ubicacion": "string(180)",
    "descripcion": "string(300)",
    "areaM2": "decimal(10,2)",
    "createdAt": "datetime",
    "updatedAt": "datetime",
    "plantas": ["Planta"],
    "alertas": ["Alerta"]
  },
  "Planta": {
    "id": "long",
    "invernaderoId": "long (FK Invernadero.id)",
    "nombreComun": "string(120)",
    "nombreCientifico": "string(180)",
    "variedad": "string(120)",
    "fechaSiembra": "date",
    "fechaUltimoRiego": "date",
    "frecuenciaRiegoDias": "int",
    "fechaUltimaFertilizacion": "date",
    "frecuenciaFertilizacionDias": "int",
    "estadoActual": "enum[OPTIMO, VIGILANCIA, CRITICO]",
    "observaciones": "string(500)",
    "activo": "boolean",
    "createdAt": "datetime",
    "updatedAt": "datetime",
    "registrosRiego": ["RegistroRiego"],
    "registrosFertilizacion": ["RegistroFertilizacion"],
    "estadosCultivo": ["EstadoCultivo"],
    "alertas": ["Alerta"]
  },
  "RegistroRiego": {
    "id": "long",
    "plantaId": "long (FK Planta.id)",
    "fechaRiego": "datetime",
    "volumenLitros": "decimal(8,2)",
    "metodo": "string(60)",
    "responsable": "string(120)",
    "notas": "string(300)",
    "createdAt": "datetime"
  },
  "RegistroFertilizacion": {
    "id": "long",
    "plantaId": "long (FK Planta.id)",
    "fechaFertilizacion": "datetime",
    "tipoFertilizante": "string(120)",
    "dosis": "decimal(8,2)",
    "unidad": "string(20)",
    "responsable": "string(120)",
    "notas": "string(300)",
    "createdAt": "datetime"
  },
  "EstadoCultivo": {
    "id": "long",
    "plantaId": "long (FK Planta.id)",
    "fechaRegistro": "datetime",
    "estado": "enum[OPTIMO, VIGILANCIA, CRITICO]",
    "alturaCm": "decimal(6,2)",
    "humedadSustratoPct": "decimal(5,2)",
    "temperaturaC": "decimal(5,2)",
    "observaciones": "string(500)",
    "createdAt": "datetime"
  },
  "Alerta": {
    "id": "long",
    "plantaId": "long (FK Planta.id)",
    "invernaderoId": "long (FK Invernadero.id)",
    "tipo": "enum[RIEGO, FERTILIZACION, ESTADO, GENERAL]",
    "severidad": "enum[BAJA, MEDIA, ALTA]",
    "mensaje": "string(300)",
    "activa": "boolean",
    "fechaGeneracion": "datetime",
    "fechaResolucion": "datetime",
    "resueltaPorUsuarioId": "long (FK Usuario.id)"
  }
}
```

## 2) Diagrama ER (descripcion textual)

- Usuario (1) --- (N) Invernadero
  - FK: invernaderos.usuario_id -> usuarios.id
- Invernadero (1) --- (N) Planta
  - FK: plantas.invernadero_id -> invernaderos.id
- Planta (1) --- (N) RegistroRiego
  - FK: registros_riego.planta_id -> plantas.id
- Planta (1) --- (N) RegistroFertilizacion
  - FK: registros_fertilizacion.planta_id -> plantas.id
- Planta (1) --- (N) EstadoCultivo
  - FK: estados_cultivo.planta_id -> plantas.id
- Planta (1) --- (N) Alerta
  - FK: alertas.planta_id -> plantas.id
- Invernadero (1) --- (N) Alerta
  - FK: alertas.invernadero_id -> invernaderos.id
- Usuario (1) --- (N) Alerta (opcional, resolucion)
  - FK: alertas.resuelta_por_usuario_id -> usuarios.id

## 3) Diccionario de datos

| Entidad | Campo | Tipo | Longitud | Nulo | PK/FK | Descripcion |
| --- | --- | --- | --- | --- | --- | --- |
| Usuario | id | bigint | - | No | PK | Identificador del usuario |
| Usuario | nombre | varchar | 120 | No | - | Nombre completo |
| Usuario | email | varchar | 180 | No | - | Correo unico |
| Usuario | rol | enum | 20 | No | - | Rol de acceso |
| Usuario | provider | enum | 20 | No | - | Proveedor OAuth2 |
| Usuario | provider_id | varchar | 120 | No | - | Id externo del proveedor |
| Usuario | activo | boolean | - | No | - | Estado activo |
| Usuario | created_at | timestamp | - | No | - | Fecha de creacion |
| Usuario | updated_at | timestamp | - | No | - | Fecha de actualizacion |
| Invernadero | id | bigint | - | No | PK | Identificador del invernadero |
| Invernadero | usuario_id | bigint | - | No | FK | Usuario propietario |
| Invernadero | nombre | varchar | 120 | No | - | Nombre del invernadero |
| Invernadero | ubicacion | varchar | 180 | No | - | Ubicacion |
| Invernadero | descripcion | varchar | 300 | Si | - | Descripcion |
| Invernadero | area_m2 | decimal | 10,2 | Si | - | Area en m2 |
| Invernadero | created_at | timestamp | - | No | - | Fecha de creacion |
| Invernadero | updated_at | timestamp | - | No | - | Fecha de actualizacion |
| Planta | id | bigint | - | No | PK | Identificador de la planta |
| Planta | invernadero_id | bigint | - | No | FK | Invernadero asociado |
| Planta | nombre_comun | varchar | 120 | No | - | Nombre comun |
| Planta | nombre_cientifico | varchar | 180 | Si | - | Nombre cientifico |
| Planta | variedad | varchar | 120 | Si | - | Variedad |
| Planta | fecha_siembra | date | - | Si | - | Fecha de siembra |
| Planta | fecha_ultimo_riego | date | - | Si | - | Ultimo riego |
| Planta | frecuencia_riego_dias | int | - | No | - | Frecuencia de riego en dias |
| Planta | fecha_ultima_fertilizacion | date | - | Si | - | Ultima fertilizacion |
| Planta | frecuencia_fertilizacion_dias | int | - | No | - | Frecuencia de fertilizacion en dias |
| Planta | estado_actual | enum | 20 | No | - | Estado del cultivo |
| Planta | observaciones | varchar | 500 | Si | - | Observaciones |
| Planta | activo | boolean | - | No | - | Estado activo |
| Planta | created_at | timestamp | - | No | - | Fecha de creacion |
| Planta | updated_at | timestamp | - | No | - | Fecha de actualizacion |
| RegistroRiego | id | bigint | - | No | PK | Identificador del registro |
| RegistroRiego | planta_id | bigint | - | No | FK | Planta asociada |
| RegistroRiego | fecha_riego | timestamp | - | No | - | Fecha del riego |
| RegistroRiego | volumen_litros | decimal | 8,2 | Si | - | Volumen en litros |
| RegistroRiego | metodo | varchar | 60 | Si | - | Metodo de riego |
| RegistroRiego | responsable | varchar | 120 | Si | - | Operador responsable |
| RegistroRiego | notas | varchar | 300 | Si | - | Notas |
| RegistroRiego | created_at | timestamp | - | No | - | Fecha de creacion |
| RegistroFertilizacion | id | bigint | - | No | PK | Identificador del registro |
| RegistroFertilizacion | planta_id | bigint | - | No | FK | Planta asociada |
| RegistroFertilizacion | fecha_fertilizacion | timestamp | - | No | - | Fecha de fertilizacion |
| RegistroFertilizacion | tipo_fertilizante | varchar | 120 | No | - | Tipo de fertilizante |
| RegistroFertilizacion | dosis | decimal | 8,2 | Si | - | Dosis aplicada |
| RegistroFertilizacion | unidad | varchar | 20 | Si | - | Unidad de medida |
| RegistroFertilizacion | responsable | varchar | 120 | Si | - | Operador responsable |
| RegistroFertilizacion | notas | varchar | 300 | Si | - | Notas |
| RegistroFertilizacion | created_at | timestamp | - | No | - | Fecha de creacion |
| EstadoCultivo | id | bigint | - | No | PK | Identificador del registro |
| EstadoCultivo | planta_id | bigint | - | No | FK | Planta asociada |
| EstadoCultivo | fecha_registro | timestamp | - | No | - | Fecha del estado |
| EstadoCultivo | estado | enum | 20 | No | - | Estado del cultivo |
| EstadoCultivo | altura_cm | decimal | 6,2 | Si | - | Altura en cm |
| EstadoCultivo | humedad_sustrato_pct | decimal | 5,2 | Si | - | Humedad del sustrato (%) |
| EstadoCultivo | temperatura_c | decimal | 5,2 | Si | - | Temperatura en celsius |
| EstadoCultivo | observaciones | varchar | 500 | Si | - | Observaciones |
| EstadoCultivo | created_at | timestamp | - | No | - | Fecha de creacion |
| Alerta | id | bigint | - | No | PK | Identificador de la alerta |
| Alerta | planta_id | bigint | - | No | FK | Planta asociada |
| Alerta | invernadero_id | bigint | - | No | FK | Invernadero asociado |
| Alerta | tipo | enum | 20 | No | - | Tipo de alerta |
| Alerta | severidad | enum | 10 | No | - | Severidad |
| Alerta | mensaje | varchar | 300 | No | - | Mensaje de alerta |
| Alerta | activa | boolean | - | No | - | Estado activo |
| Alerta | fecha_generacion | timestamp | - | No | - | Fecha de generacion |
| Alerta | fecha_resolucion | timestamp | - | Si | - | Fecha de resolucion |
| Alerta | resuelta_por_usuario_id | bigint | - | Si | FK | Usuario que resolvio |
