# Guía de pruebas manuales con los datos iniciales

Esta guía corresponde a la nueva carga de `DatosIniciales.java`. Describe el estado que queda al cargarla sobre una base vacía.

Las pruebas son manuales y permiten recorrer la aplicación durante el desarrollo y la defensa. No sustituyen las pruebas JUnit ni la medición de cobertura.

**1. Preparación**

Antes de comenzar:

1. Tener incorporada la nueva versión de `DatosIniciales.java`.
2. Generar e instalar el JAR actualizado de la lógica.
3. Generar el WAR del proyecto web y desplegarlo en Tomcat.
4. Vaciar los datos anteriores, si se quiere empezar desde el estado descrito aquí.
5. Iniciar la aplicación para ejecutar la carga inicial.

La dirección utilizada en nuestra configuración es:

```text
http://localhost:8081/eventosuy-web/
```

Para cambiar de usuario, cerrar la sesión anterior e iniciar sesión con la cuenta correspondiente.

**2. Usuarios disponibles**

Todos los usuarios iniciales tienen la contraseña:

```text
Demo2026!
```

| Nickname | Rol | Institución | Situación inicial |
|---|---|---|---|
| `juanchi` | Organizador | No corresponde | Organiza las ediciones de Java |
| `sofia.eventos` | Organizador | No corresponde | Organiza las ediciones de Datos Abiertos |
| `MatiB` | Asistente | UTEC | Tiene dos registros generales |
| `lucia.test` | Asistente | ANTEL | Tiene un registro patrocinado |
| `diego.utec` | Asistente | UTEC | Tiene un registro patrocinado |
| `ana.utec` | Asistente | UTEC | No tiene registros |
| `bruno.antel` | Asistente | ANTEL | No tiene registros |
| `carla.libre` | Asistente | Sin institución | No tiene registros |

El administrador utiliza la estación Swing. Esta carga no crea una cuenta web de administrador.

Para probar como visitante, navegar sin iniciar sesión.

**3. Eventos y ediciones**

| Evento | Edición | Organizador | Estado inicial |
|---|---|---|---|
| Conferencia Java Uruguay | Java Uruguay 2026 | `juanchi` | APROBADA |
| Conferencia Java Uruguay | Java Uruguay 2027 | `juanchi` | INGRESADA |
| Conferencia Java Uruguay | Java Regional 2027 | `juanchi` | INGRESADA |
| Conferencia Java Uruguay | Java Cancelada 2026 | `juanchi` | RECHAZADA |
| Datos Abiertos y Ciudadanía | Datos Abiertos 2026 | `sofia.eventos` | APROBADA |
| Datos Abiertos y Ciudadanía | Taller de Datos 2026 | `sofia.eventos` | APROBADA |
| Encuentro de Negocios | Sin ediciones | — | — |

En el código, `APROBADA` representa el estado “Aceptada” mencionado en la letra.

Las dos ediciones ingresadas permiten demostrar una aceptación y un rechazo desde Swing.

**4. Tipos de registro y lugares disponibles**

| Edición | Tipo | Costo normal | Cupo total | Ocupados inicialmente |
|---|---|---:|---:|---:|
| Java Uruguay 2026 | Entrada general | 150 | 20 | 2 |
| Java Uruguay 2026 | Estudiante | 80 | 20 | 1 |
| Datos Abiertos 2026 | Participante | 120 | 20 | 0 |
| Datos Abiertos 2026 | Grupo reducido | 120 | 1 | 1 |

“Grupo reducido” ya está lleno.

“Taller de Datos 2026” comienza sin tipos de registro, patrocinios ni inscripciones.

**5. Códigos de patrocinio**

| Código | Institución | Edición | Tipo | Usos disponibles inicialmente |
|---|---|---|---|---:|
| `UTEC-JV26-001` | UTEC | Java Uruguay 2026 | Estudiante | 1 |
| `ANTEL-JV26-001` | ANTEL | Java Uruguay 2026 | Entrada general | 0 |
| `UTEC-DA26-001` | UTEC | Datos Abiertos 2026 | Participante | 2 |

El código debe corresponder simultáneamente a la institución del asistente, la edición y el tipo seleccionado. Además, debe tener usos disponibles.

**6. Inscripciones que ya existen**

| Asistente | Edición | Tipo | Modalidad | Costo |
|---|---|---|---|---:|
| `MatiB` | Java Uruguay 2026 | Entrada general | General | 150 |
| `MatiB` | Datos Abiertos 2026 | Grupo reducido | General | 120 |
| `lucia.test` | Java Uruguay 2026 | Entrada general | Patrocinio ANTEL | 0 |
| `diego.utec` | Java Uruguay 2026 | Estudiante | Patrocinio UTEC | 0 |

La fecha de estos registros es la fecha en que se ejecuta la carga inicial.

**7. Pruebas de inscripción que deben rechazarse**

Conviene realizar estas pruebas antes de las inscripciones exitosas.

| Situación | Usuario | Selección y código | Resultado esperado |
|---|---|---|---|
| Código inexistente | `ana.utec` | Java Uruguay 2026 → Estudiante → `NO-EXISTE` | Rechazar el código |
| Tipo incorrecto | `ana.utec` | Java Uruguay 2026 → Entrada general → `UTEC-JV26-001` | Rechazar el código |
| Edición incorrecta | `ana.utec` | Java Uruguay 2026 → Estudiante → `UTEC-DA26-001` | Rechazar el código |
| Institución incorrecta | `bruno.antel` | Java Uruguay 2026 → Estudiante → `UTEC-JV26-001` | Rechazar por institución |
| Código agotado | `bruno.antel` | Java Uruguay 2026 → Entrada general → `ANTEL-JV26-001` | Informar que no quedan usos |
| Cupo agotado | `bruno.antel` | Datos Abiertos 2026 → Grupo reducido → modalidad general | Informar que no hay cupos |
| Registro duplicado | `MatiB` | Java Uruguay 2026 → Entrada general → modalidad general | Informar que ya está registrado |

Cada rechazo debe permitir corregir la selección o cancelar, sin crear una inscripción.

**8. Inscripciones exitosas**

Para probar un registro general:

1. Iniciar sesión como `carla.libre`.
2. Entrar a **Ediciones → Registro a edición**.
3. Seleccionar **Conferencia Java Uruguay → Java Uruguay 2026 → Entrada general**.
4. Comprobar que no se ofrece la modalidad con patrocinio, porque Carla no tiene institución.
5. Confirmar la inscripción.
6. Consultar el registro y comprobar costo **150**, fecha actual y sin patrocinio.

Para probar un registro patrocinado:

1. Iniciar sesión como `ana.utec`.
2. Seleccionar **Conferencia Java Uruguay → Java Uruguay 2026 → Estudiante**.
3. Elegir la modalidad con código.
4. Ingresar `UTEC-JV26-001`.
5. Confirmar la inscripción.
6. Consultar el registro y comprobar costo **cero**, fecha actual y con patrocinio.

Esta inscripción de Ana consume el último uso disponible de ese código.

**9. Consulta de registro y perfiles**

Como asistente:

- Con `MatiB`, comprobar que aparecen sus dos registros y que puede consultar sus detalles.
- Con `ana.utec`, antes de inscribirla, comprobar el mensaje de listado vacío.
- Consultar el perfil de otro asistente y comprobar que no se muestran sus registros privados.

Como organizador:

- Con `juanchi`, consultar los registros de Java Uruguay 2026.
- Con `sofia.eventos`, consultar el registro de Datos Abiertos 2026.
- Comprobar que cada organizador accede a las ediciones que le corresponden.

Para revisar la visibilidad de estados:

- `juanchi` debe ver todas sus ediciones en su propio perfil.
- Otro usuario o un visitante debe ver únicamente sus ediciones aprobadas.

**10. Cambios de estado desde Swing**

1. Abrir **Ediciones → Modificar estado de edición**.
2. Seleccionar **Conferencia Java Uruguay**.
3. Aceptar **Java Uruguay 2027**.
4. Rechazar **Java Regional 2027**.
5. Actualizar las consultas web y comprobar que reflejan los cambios.
6. Comprobar que una edición ya aprobada o rechazada no permite otra transición.

Las ediciones disponibles para esta demostración cambian después de realizarla.

**11. Otras consultas y altas**

Los datos también permiten probar:

- **Consulta de tipo de registro:** Java Uruguay 2026 tiene dos tipos; Taller de Datos 2026 no tiene ninguno.
- **Consulta de patrocinio:** entrar al detalle de Java Uruguay 2026 y seleccionar uno de sus patrocinios.
- **Evento sin ediciones:** consultar Encuentro de Negocios.
- **Categoría sin resultados:** seleccionar Cultura.
- **Alta de tipo de registro:** iniciar sesión como `sofia.eventos` y crear uno para Taller de Datos 2026.
- **Nombre de tipo repetido:** intentar crear “Entrada general” nuevamente dentro de Java Uruguay 2026.
- **Alta de institución:** intentar registrar UTEC para comprobar el rechazo del nombre duplicado; después probar con un nombre nuevo.

Para crear un patrocinio nuevo puede utilizarse la institución **Instituto de Innovación**, que inicialmente no tiene ninguno.

Ejemplo válido:

```text
Organizador: juanchi
Evento: Conferencia Java Uruguay
Edición: Java Uruguay 2026
Tipo: Entrada general
Institución: Instituto de Innovación
Nivel: BRONCE
Aporte: 1000
Registros gratuitos: 1
Código: INNOV-JV26-001
```

El registro gratuito vale 150 y no supera el 20 % del aporte, que es 200.

Con la navegación actual, el menú de alta de patrocinio redirige al listado de eventos. Desde allí se utiliza el botón **Crear patrocinio** del evento correspondiente.

**12. Repetición y limpieza**

Las pruebas exitosas modifican la base:

- Una inscripción ocupa un lugar.
- Un registro patrocinado consume un uso.
- Un cambio de estado modifica la edición.
- Las altas agregan nuevos datos.

Reiniciar la aplicación no devuelve la base a su estado inicial.

Para repetir toda la demostración desde cero, detener Swing y Tomcat y ejecutar lo siguiente en **Query Tool de la base `eventosuy`**:

```sql
BEGIN;

TRUNCATE TABLE
    registro,
    patrocinio,
    tipo_registro,
    edicion,
    evento,
    asistente,
    organizador,
    usuario,
    institucion,
    categoria
RESTART IDENTITY CASCADE;

COMMIT;
```

Esto elimina todos los datos de esas tablas y sus dependencias, conservando su estructura e índices.

Después, iniciar una de las aplicaciones con la lógica actualizada para ejecutar nuevamente la carga inicial.

Si se utiliza Maven `install`, conviene hacerlo antes de la limpieza final: las pruebas existentes pueden crear datos en la base configurada.