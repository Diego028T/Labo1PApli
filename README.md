# Labo1PApli

Proyecto Java/Swing para la gestion de eventos.

## Estado actual de persistencia

Se empezo a incorporar persistencia con JPA, Hibernate y PostgreSQL.

Por ahora la persistencia esta aplicada solamente al primer bloque de usuarios:

- Usuario
- Asistente
- Organizador

El resto del sistema todavia sigue funcionando mayormente en memoria:

- Instituciones
- Eventos
- Ediciones
- Tipos de registro
- Registros
- Patrocinios

Esto es intencional para avanzar de a poco y no romper todos los casos de uso al mismo tiempo.

## Requisitos

- JDK 25
- IntelliJ IDEA
- PostgreSQL instalado localmente
- pgAdmin 4, opcional pero recomendado

## Configuracion de PostgreSQL

Cada integrante debe tener en su propia PC una base local con estos datos:

```text
Host: localhost
Puerto: 5432
Base de datos: eventosuy
Usuario: eventosuy
Password: eventosuy
```

GitHub no guarda la base de datos ni los datos cargados. GitHub solo guarda el codigo y la configuracion. Cada integrante tiene su propia base local.

## Crear usuario y base en pgAdmin

Primero entrar al servidor PostgreSQL local con el usuario administrador, normalmente:

```text
Username: postgres
Password: la password elegida al instalar PostgreSQL
```

Luego crear el rol:

```text
Login/Group Roles
  Create -> Login/Group Role
  Name: eventosuy
  Password: eventosuy
  Can login: Yes
```

Despues crear la base:

```text
Databases
  Create -> Database
  Database: eventosuy
  Owner: eventosuy
```

## Archivos agregados para persistencia

### pom.xml

Archivo de Maven ubicado en la raiz del proyecto.

Sirve para que IntelliJ/Maven sepan:

- Que version de Java usar
- Que librerias externas necesita el proyecto
- Cual es la clase principal

Dependencias importantes:

- `jakarta.persistence-api`: API de JPA
- `hibernate-core`: implementacion de JPA
- `postgresql`: driver para conectar Java con PostgreSQL

### src/main/resources/META-INF/persistence.xml

Archivo de configuracion de JPA.

Define la unidad de persistencia:

```text
eventosuy
```

Y la conexion a PostgreSQL:

```text
jdbc:postgresql://localhost:5432/eventosuy
```

Tambien indica que Hibernate debe crear o actualizar tablas automaticamente:

```xml
<property name="hibernate.hbm2ddl.auto" value="update"/>
```

Con `update`, Hibernate reutiliza las tablas existentes y crea las que falten. No deberia borrar los datos.

## Clases mapeadas por ahora

### Usuario

Se marco como entidad JPA:

```java
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
```

La estrategia `JOINED` crea una tabla principal `usuario` y tablas hijas para `asistente` y `organizador`.

Tambien se agrego:

- `id` como clave primaria
- `nickname` unico
- `correo` unico
- constructor vacio `protected` requerido por JPA

### Asistente

Se marco como entidad:

```java
@Entity
@Table(name = "asistente")
```

Por ahora `institucion` y `registros` quedaron como `@Transient`.

Esto significa que siguen existiendo en Java, pero todavia no se guardan en la base. Es provisorio hasta mapear bien `Institucion` y `Registro`.

### Organizador

Se marco como entidad:

```java
@Entity
@Table(name = "organizador")
```

Sus datos propios son:

- descripcion
- enlace

## Clases agregadas en persistencia

### JPAUtil

Ubicacion:

```text
src/main/java/logica/Persistencia/JPAUtil.java
```

Se encarga de crear y centralizar el `EntityManagerFactory`.

En criollo: prepara la conexion JPA usando la unidad `eventosuy` definida en `persistence.xml`.

### UsuarioDAO

Ubicacion:

```text
src/main/java/logica/Persistencia/UsuarioDAO.java
```

Es el objeto de acceso a datos para usuarios.

Tiene operaciones como:

- guardar usuario
- buscar por nickname
- verificar si existe nickname
- verificar si existe correo
- listar usuarios
- modificar usuario

## Cambios en Sistema

La clase `Sistema` ahora usa `UsuarioDAO` para la parte de usuarios.

Operaciones conectadas a PostgreSQL:

- `chequearUsuario`
- `altaAsistente`
- `altaOrganizador`
- `listarUsuarios`
- `listarAsistentes`
- `mostrarDatosUsuario`
- `modificarDatosUsuario`

Se mantiene una copia en memoria para no romper casos de uso que todavia dependen de objetos en memoria, especialmente registros, eventos e instituciones.

## Datos iniciales

Los usuarios de prueba siguen existiendo:

- `MatiB`
- `juanchi`

Pero ahora se cargan con cuidado:

```text
Si ya existen en la base, no se vuelven a crear.
Si no existen, se crean.
```

Esto evita errores por nickname o correo repetido al cerrar y abrir la aplicacion.

## Como correr

1. Abrir el proyecto en IntelliJ.
2. Confirmar que IntelliJ haya cargado Maven desde `pom.xml`.
3. Verificar que PostgreSQL este corriendo.
4. Verificar que exista la base `eventosuy` con owner `eventosuy`.
5. Ejecutar:

```text
logica.Principal.Main
```

Al iniciar, Hibernate deberia conectarse a PostgreSQL y crear las tablas si no existen.

Tablas esperadas por ahora:

```text
usuario
asistente
organizador
```

## Warnings esperados

Puede aparecer un warning similar a:

```text
PostgreSQLDialect does not need to be specified explicitly
```

No rompe la ejecucion. Solo indica que Hibernate puede detectar PostgreSQL automaticamente.

Tambien puede aparecer un warning al crear restricciones unicas si la base estaba vacia:

```text
no existe la restriccion ..., omitiendo
```

Esto suele pasar cuando Hibernate intenta limpiar o ajustar constraints antes de crearlos. Si despues crea las tablas y la aplicacion corre, no es un problema.

## Para reiniciar la prueba desde cero

Desde pgAdmin, en la base `eventosuy`, ejecutar:

```sql
DROP TABLE IF EXISTS asistente CASCADE;
DROP TABLE IF EXISTS organizador CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;
```

Luego correr la aplicacion de nuevo. Hibernate volvera a crear las tablas y se volveran a cargar los usuarios iniciales si no existen.

## Resumen de lo hecho

Durante esta etapa se hizo lo siguiente:

- Se instalo/configuro PostgreSQL local.
- Se creo la base `eventosuy`.
- Se creo el usuario/owner `eventosuy`.
- Se agrego Maven mediante `pom.xml`.
- Se agregaron dependencias de JPA, Hibernate y PostgreSQL.
- Se agrego `persistence.xml`.
- Se empezo el mapeo JPA de `Usuario`, `Asistente` y `Organizador`.
- Se dejo `Institucion` y `Registro` fuera de persistencia por ahora usando `@Transient` en `Asistente`.
- Se creo `JPAUtil`.
- Se creo `UsuarioDAO`.
- Se conecto `Sistema` con `UsuarioDAO` para persistir usuarios.
- Se verifico que Hibernate cree las tablas `usuario`, `asistente` y `organizador`.
- Se verifico que los usuarios persistan al cerrar y volver a abrir la aplicacion.
