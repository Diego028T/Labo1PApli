# Desarrollo web: entorno de trabajo y punto de partida

Esta guia explica como preparar otra computadora para continuar el Laboratorio 2,
que se construyo hasta ahora y en que direccion seguimos. No reemplaza la letra
del laboratorio ni describe casos de uso web que todavia no estan implementados.

## 1. Que proyectos tenemos y por que

Tenemos **dos proyectos Maven**, no dos versiones independientes de la logica:

| Ubicacion | Responsabilidad | Resultado de Maven |
| --- | --- | --- |
| Raiz del repositorio: `pom.xml` y `src/` | Aplicacion Java existente: logica, DTO, DAO, JPA y presentacion Swing. | `target/eventosuy-1.0-SNAPSHOT.jar` |
| `eventos.uy/servidorweb/` | Presentacion web con servlets, JSP y recursos visuales. Depende del proyecto Java anterior. | `target/eventosuy-web.war`, dentro de `servidorweb` |

El WAR incluye el JAR de la aplicacion y sus dependencias. No hay que copiar el
JAR por separado a Tomcat ni ejecutar el `Main` de Swing para levantar la web.
La logica Java se ejecuta en el mismo proceso de Tomcat; no estamos usando una API
remota entre los dos proyectos.

`eventos.uy/sitioweb/` es el prototipo visual HTML/CSS del grupo, **no un tercer
proyecto Maven**. Sus HTML serviran de base para las JSP. Sus CSS se incorporan
automaticamente al WAR mediante el `maven-war-plugin` del POM web:

- `bootstrap/bootstrap.min.css`
- `estilos/estilos.css`

Por eso hay que descargar el repositorio completo y conservar `sitioweb` al lado
de `servidorweb`. No borrar esa carpeta ni mantener copias manuales de sus CSS en
el proyecto web. El POM actual no incorpora al WAR los HTML del prototipo.

## 2. Requisitos de cada computadora

- **JDK 25:** ambos proyectos compilan con `maven.compiler.release=25`.
- **IntelliJ IDEA y Maven:** puede utilizarse el Maven incluido en IntelliJ.
- **PostgreSQL local:** la prueba actual utiliza PostgreSQL 18.6.
- **pgAdmin, opcional:** es una herramienta para administrar PostgreSQL, no el
  servidor de base de datos. No es necesario dejar pgAdmin abierto para usar la app.
- **Apache Tomcat 10.1.x:** corresponde a Jakarta Servlet 6.0 y JSP 3.1.
- Un navegador y acceso a Internet para descargar herramientas y dependencias Maven.

Las rutas de Java y Tomcat dependen de cada PC. No copiar rutas personales de otro
integrante como si fueran parte obligatoria del proyecto.

## 3. Preparacion por primera vez

### Paso 1. Obtener el repositorio y abrir los dos proyectos Maven

Descargar o actualizar el repositorio completo. Para actualizar una copia existente,
revisar primero la rama y los cambios locales; no sobrescribir trabajo sin guardar.
Trabajar en una rama acordada con el grupo.

Abrir en IntelliJ la carpeta que contiene el `pom.xml` principal. Luego incorporar
`eventos.uy/servidorweb/pom.xml` como proyecto Maven, si no aparece automaticamente:
abrir su menu contextual y elegir **Add as Maven Project**, o agregarlo desde el
panel Maven. Los nombres exactos del menu pueden variar segun la version del IDE.

El panel Maven debe mostrar estos dos proyectos:

```text
eventosuy
eventosuy-web
```

### Paso 2. Configurar Java 25

En IntelliJ revisar **File -> Project Structure -> Project SDK** y seleccionar
JDK 25. Revisar tambien el JDK utilizado por Maven en **Settings -> Build,
Execution, Deployment -> Build Tools -> Maven -> Runner**.

Sincronizar los proyectos Maven despues de modificar sus POM.
**Sync no equivale a install ni a package:** actualiza la configuracion y las
dependencias del IDE, pero no sustituye los pasos de construccion siguientes.

### Paso 3. Crear la base de datos local

El archivo que utiliza la logica es:

```text
src/main/resources/META-INF/persistence.xml
```

La configuracion actual para desarrollo es:

| Dato | Valor |
| --- | --- |
| Host | `localhost` |
| Puerto | `5432` |
| Base | `eventosuy` |
| Usuario de conexion y propietario de la base | `eventosuy` |
| Contrasena | `eventosuy` |
| URL JDBC | `jdbc:postgresql://localhost:5432/eventosuy` |

Estas son credenciales de desarrollo local, no credenciales recomendadas para una
publicacion en Internet. La contrasena del administrador `postgres` es la que cada
integrante eligio al instalar PostgreSQL; no tiene por que ser `eventosuy`.

En pgAdmin, conectarse al servidor PostgreSQL local con una cuenta administradora
y realizar estos pasos, solo si el rol y la base todavia no existen:

1. En **Login/Group Roles**, crear un rol llamado `eventosuy`, con contrasena
   `eventosuy` y permiso **Can login?** activado. No necesita ser superusuario.
2. En **Databases**, crear una base llamada `eventosuy` y seleccionar como
   **Owner** al rol `eventosuy`.

Alternativamente, desde Query Tool conectado a la base `postgres` con permisos
administrativos, ejecutar primero:

```sql
CREATE ROLE eventosuy WITH LOGIN PASSWORD 'eventosuy';
```

Despues ejecutar esta sentencia por separado, fuera de un bloque de transaccion:

```sql
CREATE DATABASE eventosuy OWNER eventosuy;
```

No repetir estas sentencias si ya existen esos objetos. No es necesario borrar
una base existente para continuar el desarrollo.

Hibernate crea o ajusta las tablas al inicializar JPA porque la configuracion es
`hibernate.hbm2ddl.auto=update`. No cambiar a `create` o `create-drop` para resolver
un problema de arranque: puede provocar perdida de datos.

La inicializacion de `Sistema` tambien llama a `DatosIniciales.cargar(...)`, que
comprueba si corresponde cargar datos de prueba. No es una simulacion del navegador:
son datos persistidos. Cada integrante tiene su propia base; **Git no comparte
los datos de PostgreSQL**.

Si se modifica `persistence.xml`, hay que reconstruir e instalar el JAR y despues
volver a empaquetar y desplegar el WAR: Tomcat lee la copia incluida en el JAR.

### Paso 4. Construir e instalar el JAR de la aplicacion Java

En el panel Maven de IntelliJ ejecutar:

```text
eventosuy -> Lifecycle -> install
```

Esperar **BUILD SUCCESS**. Esto compila, genera
`target/eventosuy-1.0-SNAPSHOT.jar` y lo instala en el repositorio Maven local
de esa computadora, normalmente bajo la carpeta de usuario `.m2/repository`.
No lo publica en Internet ni lo instala en las PC del resto del grupo.

Sus coordenadas Maven son `uy.edu.utec:eventosuy:1.0-SNAPSHOT`.
El POM web las utiliza para encontrar esa biblioteca. Por eso cada integrante debe
hacer este paso en su computadora, aunque otro ya haya generado el JAR.

### Paso 5. Construir el WAR web

En el panel Maven ejecutar sobre el otro proyecto:

```text
eventosuy-web -> Lifecycle -> package
```

Esperar **BUILD SUCCESS**. El resultado debe ser:

```text
eventos.uy/servidorweb/target/eventosuy-web.war
```

Verificar que la fecha y hora correspondan a la construccion reciente. El paquete
contiene JSP, configuracion web, clases web compiladas, CSS y las bibliotecas bajo
`WEB-INF/lib`, incluido el JAR del proyecto original.

### Paso 6. Descargar y preparar Tomcat

Descargar Tomcat **10.1.x** desde la [pagina oficial](https://tomcat.apache.org/download-10.cgi).
En Windows de 64 bits elegir **Binary Distributions -> Core -> 64-bit Windows zip**.
No hace falta el instalador como servicio ni el paquete Deployer para esta guia.

Descomprimirlo fuera del repositorio. Debe existir una carpeta principal con
`bin`, `conf`, `logs`, `webapps`, etc. Tomcat no debe subirse a Git.

Abrir PowerShell y definir las rutas reales de esa computadora. Este es un ejemplo:

```powershell
$env:JAVA_HOME = "C:\ruta\al\jdk-25"
$env:CATALINA_HOME = "C:\Herramientas\apache-tomcat-10.1.x"
```

**Reemplazar ambas rutas:** la primera debe contener `bin\java.exe` y la segunda
`bin\catalina.bat`. No agregar `\bin` a los valores de las variables.
Estas variables duran solamente durante esa sesion de PowerShell.

El proyecto necesita Java 25 para ejecutarse, aunque Tomcat por si solo admita
versiones anteriores. No alcanza con tener configurado el JDK dentro de IntelliJ:
Tomcat tambien debe utilizar el adecuado.

Por defecto Tomcat utiliza el puerto HTTP **8080**. En la prueba de esta etapa se
utilizo **8081** porque 8080 estaba ocupado por Docker Desktop. Para usar el mismo
puerto, editar `conf/server.xml` de la instalacion local de Tomcat y cambiar solamente
el puerto del conector HTTP activo:

```xml
<Connector port="8081" protocol="HTTP/1.1"
```

Conservar los demas atributos y el resto del archivo. Ese fragmento es solo el
inicio de una etiqueta existente, no reemplaza la etiqueta completa.
El puerto 8081 tambien debe estar libre. Si se deja 8080, utilizar 8080 en las URL.

Arrancar Tomcat desde la misma ventana de PowerShell:

```powershell
& "$env:CATALINA_HOME\bin\catalina.bat" run
```

Dejar la ventana abierta y comprobar la bienvenida en <http://localhost:8081/>.
Para detenerlo, presionar **Ctrl+C** en esa ventana. Si se abre otra sesion de
PowerShell, volver a definir las variables antes de arrancarlo.

### Paso 7. Desplegar nuestra aplicacion

1. Detener Tomcat con **Ctrl+C** si esta ejecutandose.
2. Copiar el WAR generado en el paso 5 dentro de la carpeta `webapps` de Tomcat.
   Si ya habia una version anterior, reemplazar ese WAR.
3. Mantener el nombre `eventosuy-web.war`. No descomprimirlo manualmente ni copiar
   el JAR aparte. Tomcat normalmente crea la carpeta desplegada al arrancar.
4. Comprobar que el servicio PostgreSQL este funcionando.
5. Arrancar nuevamente Tomcat con el comando anterior y revisar los mensajes.
6. Abrir <http://localhost:8081/eventosuy-web/>.

La pagina inicial debe mostrar `eventos.uy`, el encabezado, el pie y los estilos.
El listener se ejecuta durante el despliegue e inicializa la logica y JPA; si falla
la conexion a PostgreSQL, la aplicacion puede no llegar a arrancar.

No ejecutar `logica.Principal.Main` para levantar la web. Ese `Main` abre Swing,
que sigue siendo otra forma de utilizar la aplicacion.

`localhost` significa la propia computadora. Estos pasos son para desarrollo
local, no publican automaticamente el sitio para usuarios de Internet.

## 4. Que hay dentro de servidorweb y por que

```text
eventos.uy/servidorweb/
|-- pom.xml
`-- src/main/
    |-- java/presentacionweb/
    |   |-- InicioAplicacionListener.java
    |   `-- CodificacionFilter.java
    `-- webapp/
        |-- index.jsp
        |-- imagenes/
        `-- WEB-INF/
            |-- web.xml
            `-- vistas/
                |-- error.jsp
                |-- no_encontrado.jsp
                `-- includes/
                    |-- encabezado.jsp
                    `-- pie.jsp
```

La carpeta `imagenes` esta reservada; si esta vacia, Git no la conserva por si sola.
Los estilos no figuran en este arbol de fuentes porque Maven los toma de `sitioweb`.

| Archivo | Funcion |
| --- | --- |
| `pom.xml` | Define el WAR, Java 25, la dependencia al JAR, Servlet y JSTL. Incorpora los CSS originales al empaquetar. |
| `InicioAplicacionListener.java` | Al desplegar, prepara JPA, obtiene `ISistema` mediante `Fabrica` y lo guarda en el contexto con la clave `sistema`. Al detener la aplicacion, cierra JPA. |
| `CodificacionFilter.java` | Aplica UTF-8 a peticiones y respuestas antes de continuar el procesamiento. No verifica permisos ni realiza operaciones de negocio. |
| `web.xml` | Declara `index.jsp` como bienvenida y asocia las respuestas 404 y 500 a sus paginas. Listener y filtro se registran mediante anotaciones Java. |
| `index.jsp` | Pagina inicial publica. Incluye encabezado y pie; todavia no consulta datos reales para mostrarlos. |
| `encabezado.jsp` y `pie.jsp` | Fragmentos compartidos para no repetir esa presentacion en cada JSP. |
| `no_encontrado.jsp` | Presenta una respuesta entendible para una ruta inexistente dentro de la aplicacion. |
| `error.jsp` | Presenta un mensaje generico para errores internos, sin mostrar excepciones ni datos sensibles. |

`WEB-INF` no se abre directamente desde el navegador. Sus vistas y fragmentos
son utilizados internamente por la aplicacion o por el mecanismo de errores.
Las expresiones `${pageContext.request.contextPath}` construyen enlaces y rutas
de recursos incluyendo el nombre de despliegue, sin fijarlo manualmente.

## 5. Resumen del avance y direccion comun

Durante esta etapa se preparo la infraestructura comun antes de conectar casos de uso:

1. Se reutilizo el proyecto Java como biblioteca mediante su JAR, sin duplicar la logica.
2. Se creo `servidorweb` como segundo proyecto Maven para separar la presentacion web.
3. Se configuraron el arranque y cierre de la logica, la codificacion UTF-8 y las rutas de error.
4. Se completaron inicio, encabezado, pie y paginas de error.
5. Se reutilizaron los CSS del prototipo desde el POM, evitando mantener copias manuales.
6. Se genero y desplego el WAR en Tomcat 10.1 con Java 25, usando el puerto 8081.
7. Se comprobo la pagina inicial con estilos y los registros de inicializacion de la logica y PostgreSQL.

**Todavia no hay servlets de casos de uso ni listados web con datos reales.**
Ver el inicio no implica que ya esten migradas las pantallas HTML.
Las respuestas de error estan configuradas; para probar el 404, solicitar
<http://localhost:8081/eventosuy-web/no-existe>. Debe verse nuestra pagina de error.
No provocar un fallo en la base ni modificar la logica solo para probar el 500.

El proximo recorrido acordado es **listar eventos**:

```text
Navegador solicita /eventos
    -> EventoServlet llama a ISistema.listarEventos()
    -> Sistema obtiene los datos mediante la persistencia
    -> devuelve DTO al servlet
    -> el servlet entrega los DTO a eventos.jsp
    -> la JSP genera HTML para el navegador
```

Se tomara `sitioweb/eventos/eventos.html` como base, conservando el diseno y
reemplazando los datos fijos por DTO. No alcanza con cambiar la extension del archivo.
Los formularios y enlaces deberan apuntar a los servlets correspondientes.

El criterio para continuar es mantener las responsabilidades:

- **Servlet:** recibe la peticion y coordina las llamadas a `ISistema`.
- **Sistema y clases del dominio:** reglas de negocio y operaciones existentes;
  los DAO encapsulan el acceso a PostgreSQL.
- **DTO:** datos que cruzan hacia la presentacion; no pasar entidades JPA a las JSP.
- **JSP:** presenta resultados, formularios y mensajes; no consulta DAO directamente.
- **CSS y JavaScript:** estilos e interacciones del navegador, segun se necesiten.

Las reglas existentes se reutilizan; si la nueva letra requiere ampliar la logica,
se hara en esa capa, no escondiendo esas reglas en la vista.
Las validaciones esperables, como un nombre duplicado, deben mostrarse en el
formulario correspondiente y no tratarse automaticamente como errores 500.

## 6. Como actualizar lo que esta ejecutando Tomcat

Editar el codigo no actualiza el WAR que ya se copio al servidor.

| Cambio realizado | Orden para probar la nueva version |
| --- | --- |
| JSP, servlet, filtro, listener, CSS o configuracion web | `package` de `eventosuy-web` y volver a desplegar el WAR. |
| Logica, DTO, DAO, entidades o `persistence.xml` | `install` de `eventosuy`, luego `package` de `eventosuy-web` y volver a desplegar el WAR. |
| Puerto o configuracion de Tomcat | Detener Tomcat, editar su configuracion local y reiniciarlo. No requiere reconstruir el WAR. |

Para desplegar otra version: detener Tomcat, reemplazar el WAR en `webapps` por el
recien generado y volver a arrancarlo. Verificar la fecha del paquete y recargar
el navegador. No editar las copias desplegadas ni `target` para desarrollar:
los cambios se hacen en las fuentes.

Cada integrante debe repetir el `install` si cambia la biblioteca Java o recibe
cambios de esa parte por Git. El WAR no recoge automaticamente el codigo fuente
actual del proyecto original: utiliza el JAR que Maven resuelve como dependencia.

## 7. Problemas que encontramos durante la preparacion

| Sintoma | Que revisar |
| --- | --- |
| No se encuentra `catalina.bat` | Ubicacion real donde se descomprimio Tomcat. La ruta de ejemplo no crea la carpeta. |
| `CATALINA_HOME ... is not defined correctly` | Definir `CATALINA_HOME` con la carpeta principal correcta antes de ejecutar el comando. |
| `JAVA_HOME` incorrecto o version de clases incompatible | Usar un JDK adecuado tanto para Maven como para Tomcat, en este proyecto Java 25. |
| `Address already in use: bind` | El puerto esta ocupado. En nuestra prueba Docker utilizaba 8080; se configuro 8081 para Tomcat. No detener procesos ajenos sin identificarlos. |
| Pagina completamente blanca | Verificar el WAR: el problema observado fue desplegar el paquete antiguo con JSP vacias. Volver a ejecutar `package` y copiar el paquete actualizado. |
| Maven no encuentra `uy.edu.utec:eventosuy:1.0-SNAPSHOT` | Ejecutar primero `install` del proyecto original en esa computadora. |
| Error de dependencia JSTL `3.0.0-RC1` | La implementacion antigua lo solicitaba. El POM actual utiliza `org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1` y la API `3.0.2`. Sincronizar Maven con ese POM. |
| Bienvenida de Tomcat funciona, pero nuestra app no inicia | Revisar los registros, PostgreSQL, credenciales y mensajes del listener. No confundir el arranque de Tomcat con el de cada aplicacion. |
| Cambios en la logica no aparecen | Reconstruir e instalar el JAR antes de volver a generar y desplegar el WAR. |

Los registros estan en la carpeta `logs` de Tomcat, especialmente los archivos
`catalina.<fecha>.log` y `localhost.<fecha>.log`. El nombre del mes o el idioma de
los mensajes puede variar. El 404 de nuestra aplicacion se prueba dentro de
`/eventosuy-web/`, no solicitando una aplicacion inexistente fuera de ese contexto.

## 8. Que se comparte por Git y que es local

Compartir las fuentes, los POM, las vistas, los recursos visuales y esta guia.
Las instalaciones de Java/Tomcat/PostgreSQL, las variables de cada terminal, el
repositorio Maven `.m2` y los datos de cada base son locales.

Las carpetas `target` contienen resultados generados, no fuentes, y no hace falta
compartirlas para reconstruir el proyecto. **Al escribir esta guia se detectaron
archivos de `servidorweb/target` ya versionados**; no asumir que un WAR recibido
por Git esta actualizado. La limpieza de ese seguimiento y de los patrones de
exclusion queda como tarea separada: esta guia no modifica `.gitignore` ni esos archivos.

## Referencias

- [Descargas oficiales de Tomcat 10.1](https://tomcat.apache.org/download-10.cgi)
- [Compatibilidad de Tomcat 10.1 con Servlet 6.0 y JSP 3.1](https://tomcat.apache.org/migration-10.1.html)
- [Arranque y variables de Tomcat](https://tomcat.apache.org/tomcat-10.1-doc/RUNNING.txt)
- [Despliegue de aplicaciones WAR en Tomcat](https://tomcat.apache.org/tomcat-10.1-doc/deployer-howto.html)
