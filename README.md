# Labo1PApli

Lógica de eventos.uy, persistencia con JPA y Estación de Trabajo (aplicación de escritorio en Swing). Requiere **JDK 25**, **Maven** y **PostgreSQL** local.

El sitio web del Laboratorio 2 está en otro repositorio, [Lab2PApli](https://github.com/metoc1899/Lab2PApli), y usa esta lógica como JAR. Su README explica cómo instalar y desplegar todo en Linux.

## Base de datos

Crear en PostgreSQL una base con estos datos (tablas: Hibernate las genera al arrancar):

Host:     localhost

Puerto:   5432

Nombre:   eventosuy

Usuario:  eventosuy

Password: eventosuy

En Linux:

```bash
sudo apt update
sudo apt install -y openjdk-25-jdk-headless maven git postgresql
sudo service postgresql start
sudo -u postgres psql -c "CREATE ROLE eventosuy WITH LOGIN PASSWORD 'eventosuy';"
sudo -u postgres psql -c "CREATE DATABASE eventosuy OWNER eventosuy;"
```

Revisar con `pg_lsclusters` que PostgreSQL esté en el puerto 5432.

Al arrancar por primera vez sobre una base vacía se cargan los datos de prueba. Están descritos en [README_DatosIniciales.md](README_DatosIniciales.md).

## Estación de Trabajo

Clase principal: `logica.Principal.Main`.

Hay que ejecutarla **desde IntelliJ IDEA**: abrir el proyecto, hacer **Build → Rebuild Project** y correr `Main`.

No se ejecuta con Maven porque las ventanas se diseñaron con archivos `.form`, y el código que crea sus componentes lo genera el compilador de IntelliJ. Si se compila con Maven, las ventanas fallan con `NullPointerException`. Por eso, después de cualquier comando de Maven hay que volver a hacer **Rebuild Project** antes de abrir Swing.

Si el error persiste, revisar en IntelliJ que esté destildado **Settings → Build Tools → Maven → Runner → Delegate IDE build/run actions to Maven**.

## JAR para el sitio web

```bash
mvn -DskipTests clean install
```

Esto deja la lógica disponible para que `Lab2PApli` la incluya en su WAR.

## Pruebas

Las pruebas JUnit están en `src/test/java`. Con PostgreSQL funcionando:

```bash
mvn test
```

La cobertura se mide en IntelliJ con **Run with Coverage**, sobre la capa lógica. Se excluyen `logica.Presentacion`, `logica.Principal` y `logica.PruebaSistema`.

Las pruebas usan la misma base y dejan datos con nombres `Test-...`, `org-...` y `asis-...`. Antes de una demostración conviene partir de una base vacía.
