# Plan compartido - Laboratorio 2

Relevamiento: 8 de octubre de 2026. Rama revisada: Lab2-matias.
Objetivo: completar eventos.uy con sitio conectado, Swing actualizado, persistencia, pruebas y documentacion.

## 1. Alcance y estado inicial

El grupo acordo con el docente ir directamente a la conexion real. No se construyen datos ni sesiones simulados en JavaScript. Los requisitos funcionales, roles, imagenes y validaciones se resuelven contra el servidor.

Esto no elimina los datos persistidos para JUnit y defensa: se cargan en PostgreSQL y se consultan mediante la logica.

- Primera entrega: 13 de octubre de 2026, 23:59, por Moodle, sin defensa; aplicar el acuerdo informado de conectar directamente.
- Entrega final: 3 de noviembre de 2026, 23:59, por Moodle.
- La fecha de defensa no esta definida en la letra.

Fuentes: Laboratorio 2 - 2026.pdf, secciones 3, 4, 6, 7 y 8; Server Side.pdf; Teorico Web - Client Side.pdf. El acuerdo de omitir simulacion fue informado por el usuario.

| Parte | Estado encontrado |
|---|---|
| Maven, JPA, entidades y DAO del laboratorio 1 | Existen; ampliar y conservar. |
| Fabrica e interfaz con DTO | Incorporadas. |
| Categoria jerarquica y DTCategoria | Existen; conservar. |
| Diseno web | 16 HTML, Bootstrap local y CSS propio. |
| Proyecto web Maven, JSP y Servlets | No encontrados todavia. |
| Credenciales, autenticacion y sesion real | No encontradas implementadas. |
| Estados de edicion e imagenes en el modelo | No encontrados implementados. |
| JavaScript de validacion | No encontrado en sitioweb. |
| JUnit | No se encontraron pruebas en el codigo revisado. PruebaSistema.java es manual y no reemplaza JUnit. |

## 2. Direccion comun

Navegador -> Servlet -> ISistema/Sistema -> DAO/JPA -> PostgreSQL.
El resultado vuelve como DTO al servlet; la JSP produce el HTML.

1. Administrador solo en Swing, sin login en el sitio.
2. Logica embebida como JAR; sin web services en esta iteracion.
3. Swing y Tomcat independientes, misma base y misma version de biblioteca.
4. Identidad en HttpSession, nunca un usuario actual en el singleton Sistema.
5. Servlets llaman casos de uso, no DAO/JPA. JSP muestran DTO, no entidades ni reglas de negocio.
6. La identidad de una accion sale de la sesion, no del nickname del formulario.
7. Roles y pertenencia se controlan en servidor/logica, no solo ocultando botones.
8. Mantener las consultas administrativas Swing y agregar las restricciones de web.
9. Fecha de alta, registro o patrocinio asignada en logica con el dia actual, salvo indicacion del caso.
10. Codigo de patrocinio fuera de respuestas publicas, aunque la JSP pudiera ocultarlo.
11. Framework, CSS y JS locales para la defensa.
12. Reutilizar el diseno existente y retirar datos de entidades fijados en HTML al conectar cada pagina.

## 3. Archivos y reparto

Estructura propuesta; los nombres nuevos todavia no existen:

    Labo1PApli/
    +-- pom.xml                         Biblioteca actual
    +-- src/main/java/logica/
    |   +-- Clases/                     Entidades
    |   +-- DataTypes/                  DTO y enums
    |   +-- Persistencia/               DAO, JPAUtil, precarga persistida
    |   +-- sistema01/                  Fabrica, ISistema, Sistema
    |   +-- Presentacion/               Swing
    +-- src/main/resources/META-INF/persistence.xml
    +-- src/test/java/                  JUnit
    +-- src/test/resources/             Base de pruebas
    +-- eventos.uy/
    |   +-- sitioweb/                   Referencia visual
    |   +-- servidorweb/
    |       +-- pom.xml                 WAR
    |       +-- src/main/java/presentacionweb/
    |       |   +-- controladores/      Servlets
    |       |   +-- filtros/            Controles comunes
    |       |   +-- InicioAplicacionListener.java
    |       +-- src/main/webapp/
    |           +-- bootstrap/
    |           +-- estilos/
    |           +-- js/                Validacion/interaccion
    |           +-- WEB-INF/vistas/
    |               +-- includes/
    |               +-- ...jsp
    +-- docs/                          Analisis/diseno/verificacion
    +-- README.md
    +-- PLAN_LABORATORIO_2.md

La web depende de uy.edu.utec:eventosuy:1.0-SNAPSHOT y tiene un artifactId distinto, por ejemplo eventosuy-web. Inicialmente se reutiliza el JAR sin ejecutar su Main. Separar Swing a otro modulo es una mejora, no una condicion para conectar.

No copiar Sistema/entidades al proyecto web. Cuando cambia la biblioteca, instalar nuevamente el JAR y reconstruir/desplegar el WAR.

| Frente | Responsabilidad | Primeras tareas |
|---|---|---|
| A - Integracion/servidor | Maven, JAR, WAR, Tomcat y base web | T01-T14 |
| B - Usuarios/sesion | Credenciales, perfiles y permisos propios | T15-T17, T28-T34, T46-T47 |
| C - Ediciones/operaciones | Estados, tipos, patrocinios y registros | T18-T20, T22-T27, T35-T45, T49 |
| D - Presentacion/imagenes | JSP, estilos, validacion y multimedia | T13, T21, T30-T31, T48-T50 |
| E - Verificacion/documentacion | JUnit, cobertura, diagramas y entrega | T51-T64 |

Los frentes no obligan a tener cinco integrantes. Una persona puede tomar varios.

Coordinar ediciones de ISistema.java, Sistema.java, pom.xml, persistence.xml y DatosIniciales.java. Elegir un integrador y comunicar firmas antes de cambiarlas. No pegar versiones completas distintas de Sistema simultaneamente.

Usar ramas de tarea y cambios pequenos; mantener compilable la rama comun. Cambiar una firma incluye actualizar consumidores Swing y web.

Estados: Pendiente, En curso, En revision, Terminada, Bloqueada. Anotar dependencia concreta cuando se bloquea y responsable antes de empezar.

## 4. Acciones paso a paso

El numero ordena la construccion; las dependencias permiten paralelo. Cada tarea incluye archivos y criterio de cierre.

### Etapa A - Base comun

### 1. T01 - Fijar version inicial
**Accion:** elegir rama comun, comprobar fabrica/DTO/persistencia/categorias y ejecutar Swing.
**Archivos:** proyecto actual.
**Depende de:** ninguna.
**Cierre:** todos pueden ejecutar la misma version; fallas previas registradas aparte.

### 2. T02 - Acordar estructura y versiones
**Accion:** fijar estructura, Java 25 y versiones compatibles de Tomcat/Servlet/JSP/JSTL. Propuesta: Tomcat 10.1, Servlet 6.0, JSP 3.1. Acordar contexto y nombre del WAR.
**Archivos:** ambos pom.xml, README.
**Depende de:** T01.
**Cierre:** mismas versiones y nombres para todos; una sola biblioteca de logica.

### 3. T03 - Definir contratos
**Accion:** acordar metodos nuevos de ISistema, entradas, DTO y errores; distinguir operaciones administrativas/publicas/autenticadas.
**Archivos:** ISistema.java, DataTypes, docs/contratos-web.md.
**Depende de:** T01.
**Cierre:** logica y web conocen las mismas firmas y comunican cambios antes de integrar.

### 4. T04 - Definir roles y privacidad
**Accion:** acordar datos/acciones por rol, pertenencia, registros, estados y codigo de patrocinio.
**Archivos:** docs/roles-y-permisos.md, contratos.
**Depende de:** T03.
**Cierre:** cada consulta y accion tiene destinatarios y datos permitidos definidos.

### 5. T05 - Separar bases y preparacion
**Accion:** configurar desarrollo y una base independiente para JUnit; acordar credenciales, migracion y datos de defensa.
**Archivos:** persistence.xml, JPAUtil.java si necesita configuracion, README.
**Depende de:** T01.
**Cierre:** las pruebas no borran datos del grupo y los arranques no destruyen la base.

### 6. T06 - Asignar responsables
**Accion:** completar tablero, elegir integrador de archivos compartidos y revisiones frecuentes.
**Archivos:** este plan y convenciones del repositorio.
**Depende de:** T02, T03, T04.
**Cierre:** tarea activa con responsable, rama y dependencias conocidas.

### Etapa B - Primera pagina real

### 7. T07 - Construir biblioteca Java
**Accion:** Maven install; comprobar JAR, META-INF/persistence.xml, clases y dependencias JPA/Hibernate/driver.
**Archivos:** pom.xml y recursos actuales.
**Depende de:** T02, T05.
**Cierre:** otro proyecto consume la biblioteca sin copiar clases ni abrir Swing.

### 8. T08 - Crear proyecto web
**Accion:** crear servidorweb/pom.xml, WAR, Java 25, dependencia de logica, Servlet provided y JSTL compatible; finalName eventosuy-web.
**Archivos:** eventos.uy/servidorweb/pom.xml y src/main.
**Depende de:** T02, T07.
**Cierre:** IntelliJ reconoce ambos proyectos y se construye eventosuy-web.war.

### 9. T09 - Preparar Tomcat
**Accion:** configurar JDK/puerto/contexto y desplegar WAR inicial.
**Archivos:** configuracion local y README.
**Depende de:** T08.
**Cierre:** todos pueden iniciar/detener y abrir la aplicacion por HTTP.

### 10. T10 - Reutilizar recursos visuales
**Accion:** copiar Bootstrap/CSS a webapp, preparar js y URLs de contexto; corregir intituciones al migrar sin romper enlaces.
**Archivos:** webapp/bootstrap, estilos, js.
**Depende de:** T08.
**Cierre:** recursos desde el WAR, sin CDN ni rutas personales.

### 11. T11 - Arranque y cierre web
**Accion:** listener que obtiene ISistema por fabrica, lo deja disponible en contexto y cierra JPA al detener.
**Archivos:** InicioAplicacionListener.java, Fabrica.java, JPAUtil.java si necesita ajuste.
**Depende de:** T07, T08, T09.
**Cierre:** conecta sin Main; errores de arranque visibles; cerrar Tomcat no cierra el proceso Swing.

### 12. T12 - Listar eventos reales
**Accion:** EventoServlet GET /eventos -> listarEventos() -> DTO en request -> eventos.jsp. Retirar tarjetas fijas.
**Archivos:** EventoServlet.java, WEB-INF/vistas/eventos.jsp.
**Depende de:** T10, T11.
**Cierre:** eventos de PostgreSQL y listado vacio funcionan. No implica terminar todo Consulta de Evento.

### 13. T13 - Vistas y errores comunes
**Accion:** extraer encabezado/pie, preparar errores, mensajes, navegacion y escape de texto ingresado por usuarios.
**Archivos:** encabezado.jsp, pie.jsp, error.jsp, no_encontrado.jsp, estilos.css.
**Depende de:** T10, T12.
**Cierre:** ID inexistente, parametro invalido y falla tecnica tienen respuesta comprensible, sin trazas al usuario.

### 14. T14 - Compartir recorrido reproducible
**Accion:** comprobar navegador -> servlet -> logica -> base -> JSP; documentar actualizacion de JAR/WAR.
**Archivos:** README.
**Depende de:** T12, T13.
**Cierre:** un segundo integrante reproduce la conexion.

### Etapa C - Logica del laboratorio 2

### 15. T15 - Persistir credenciales
**Accion:** agregar hash de contrasena a Usuario y elegir un mecanismo con sal mediante una implementacion adecuada; prever usuarios existentes. Validar confirmacion, no almacenarla.
**Archivos:** Usuario.java, UsuarioDAO.java, auxiliares de credenciales, pom.xml si requiere biblioteca.
**Depende de:** T03, T05, T07.
**Cierre:** no hay contrasenas en texto plano ni hash en DTO de consulta, logs o URLs.

### 16. T16 - Autenticar por nickname o correo
**Accion:** validar identificador/contrasena y devolver identidad/rol; agregar busqueda por correo y error controlado para credenciales invalidas.
**Archivos:** ISistema.java, Sistema.java, UsuarioDAO.java, DTUsuarioSesion.java y RolUsuario.java si se adoptan.
**Depende de:** T15.
**Cierre:** ingreso por ambos identificadores, distincion de asistente/organizador y ningun hash devuelto.

### 17. T17 - Ampliar alta y modificacion de usuario
**Accion:** contrasena y confirmacion obligatorias en alta, cambio opcional en modificacion, nickname/correo inmutables y fecha de alta actual indicada por la letra. Reutilizar reglas desde ambos clientes.
**Archivos:** Usuario.java, DTO, ISistema.java, Sistema.java, UsuarioDAO.java.
**Depende de:** T16.
**Cierre:** duplicados y confirmaciones distintas se rechazan en logica; modificar sin contrasena nueva conserva la anterior.

### 18. T18 - Estados de edicion
**Accion:** crear EstadoEdicion compartido, mapear en Edicion y extender DTEdicion. Todas las altas, Swing y web, quedan Ingresada.
**Archivos:** EstadoEdicion.java, Edicion.java, DTEdicion.java, Sistema.java, EdicionDAO.java.
**Depende de:** T03, T05.
**Cierre:** estado persistido y ninguna alta permite imponer Aceptada desde el formulario.

### 19. T19 - Aceptar/rechazar ediciones
**Accion:** listar Ingresada de un evento y permitir solo Ingresada -> Aceptada o Ingresada -> Rechazada.
**Archivos:** ISistema.java, Sistema.java, EdicionDAO.java, DTO.
**Depende de:** T18.
**Cierre:** cambios persistidos; estados finales no vuelven a Ingresada. No publicar esta operacion administrativa como ruta web.

### 20. T20 - Migrar base y precarga sin perder datos
**Accion:** resolver usuarios sin hash/ediciones sin estado; revisar DatosIniciales y su retorno temprano cuando considera la carga completa. Documentar migracion y carga persistida ampliada.
**Archivos:** DatosIniciales.java, persistence.xml, scripts de migracion si hacen falta, README.
**Depende de:** T15, T18.
**Cierre:** base nueva y anterior arrancan; no duplican ni sobrescriben datos reales en cada inicio. Se documenta como se asignaron estados iniciales.

### 21. T21 - Imagenes en modelo y persistencia
**Accion:** almacenar imagen opcional de Usuario, Evento, Edicion e Institucion y permitir lectura/asociacion mediante logica. Propuesta para simplificar dos clientes: contenido binario y tipo de imagen en PostgreSQL, con referencia en DTO. Si se eligen archivos, usar almacenamiento compartido fuera del WAR y documentarlo.
**Archivos:** cuatro entidades, DTO, DAO, ISistema/Sistema y auxiliar/entidad de imagen solo si reduce duplicacion.
**Depende de:** T03, T05, T17, T18.
**Cierre:** imagen opcional/reemplazable y recuperable tras reiniciar, no solamente nombre de archivo local. Documentar limites y formatos admitidos.

### 22. T22 - Privacidad de consultas web
**Accion:** operaciones adaptadas al consultante; filtrar estados, registros y codigo en la logica. Comprobar tambien el detalle solicitado directamente por ID.
**Archivos:** ISistema.java, Sistema.java, DTO web, DAO de edicion/registro/patrocinio/usuario.
**Depende de:** T04, T16, T18, T19.
**Cierre:** se cumple la matriz de este plan y una URL directa no revela datos privados. Swing conserva consultas administrativas.

### 23. T23 - Datos frescos y concurrencia
**Accion:** revisar mapas/colecciones de Sistema, especialmente buscarPorNickname e instituciones, para no devolver entidades previas a cambios hechos por el otro proceso. Revisar acceso concurrente desde servlets; EntityManager por operacion, no compartido entre peticiones.
**Archivos:** Sistema.java, DAO, JPAUtil.java.
**Depende de:** T05, T11, T17, T19.
**Cierre:** cambios Swing/web visibles en el otro sin reiniciar; usuarios no comparten identidad ni seleccion.

### 24. T24 - Eventos por categoria
**Accion:** implementar/adaptar el filtro por categoria existente y su DTO. Aprovechar DTCategoria y jerarquia actuales; no inventar inclusion de descendientes sin acuerdo.
**Archivos:** ISistema.java, Sistema.java, EventoDAO.java, CategoriaDAO.java, DTO.
**Depende de:** T03.
**Cierre:** devuelve pertenencia a la categoria elegida y maneja cero coincidencias.

### 25. T25 - Reglas de patrocinio para web
**Accion:** conservar codigo global unico, institucion unica por edicion, tipo de esa edicion, valores validos y regalos no mayores al 20% del aporte. Verificar organizador propietario y permitir altas en ediciones propias de cualquier estado.
**Archivos:** Sistema.java, PatrocinioDAO.java, tipoRegistroDAO.java, contratos/DTO.
**Depende de:** T04, T16, T18.
**Cierre:** reglas en logica aunque no valide el navegador. Igualdad con 20% admitida; exceso rechazado.

### 26. T26 - Reglas de registro general/patrocinado
**Accion:** exigir asistente autenticado, edicion Aceptada, tipo correspondiente, un registro por edicion y cupo. Codigo valido para edicion/tipo/institucion y con usos; costo cero solo en ese caso.
**Archivos:** Sistema.java, RegistroDAO.java, PatrocinioDAO.java, TipoRegistro.java, DTO.
**Depende de:** T04, T16, T18, T25.
**Cierre:** nadie registra a otro por cambiar un parametro; sin institucion solo general. Revisar transaccion para no superar cupos/usos con pedidos simultaneos.

### 27. T27 - Fechas actuales y altas de tipos
**Accion:** fecha de alta/registro/patrocinio en logica; inicio/fin ingresados por organizador. Tipo unico dentro de la edicion, descripcion/costo/cupo validos y edicion del organizador.
**Archivos:** ISistema.java, Sistema.java, tipoRegistroDAO.java, DTO y consumidores.
**Depende de:** T17, T18, T25, T26.
**Cierre:** mismos criterios de fechas en ambos clientes; tipos en edicion propia de cualquier estado, nunca ajena.

### Etapa D - Sesion y comportamiento comun

### 28. T28 - Inicio de sesion real
**Accion:** LoginServlet/login.jsp con nickname o correo y POST; autenticar y mantener DTO de identidad en HttpSession. Renovar la identidad de sesion al ingresar.
**Archivos:** LoginServlet.java, login.jsp, encabezado.jsp.
**Depende de:** T13, T16.
**Cierre:** ingresan ambos roles; error permite corregir/cancelar; contrasena fuera de URL y respuesta.

### 29. T29 - Cierre de sesion
**Accion:** LogoutServlet invalida HttpSession y devuelve estado visitante; preparar Salir.
**Archivos:** LogoutServlet.java, encabezado.jsp.
**Depende de:** T28.
**Cierre:** una nueva peticion a acciones privadas ya no conserva acceso.

### 30. T30 - Encabezado por rol y categorias
**Accion:** visitante ve iniciar/registrarse; autenticado ve nombre/perfil/salir. Obtener categorias de logica y enlazar filtro. Adaptar menu movil y acciones de rol.
**Archivos:** encabezado.jsp, pie.jsp, estilos.css y ayudantes HTTP si corresponden.
**Depende de:** T24, T28, T29.
**Cierre:** marco comun y categorias funcionales. Buscador fuera de alcance de este laboratorio.

### 31. T31 - Acceso, formularios e imagenes HTTP
**Accion:** controles comunes de acceso, identidad desde sesion y pertenencia en caso de uso. Validaciones.js para campos/formato/confirmacion, mensajes y conservacion de campos corregibles. Subida multipart y ImagenServlet que obtiene contenido mediante logica y respeta privacidad; POST para cambios y redireccion tras exito.
**Archivos:** filtros, validaciones.js, JSP comunes, ImagenServlet.java y controladores de subida.
**Depende de:** T13, T21, T22, T28, T29.
**Cierre:** ningun servlet usa DAO ni escribe imagen por fuera de logica; valida en navegador/servidor y resuelve imagen ausente/invalida. No duplicar reglas de negocio completas en JS.

### Etapa E - Todos los casos de uso web

### 32. T32 - Alta de usuario web
**Accion:** elegir asistente/organizador; nickname/nombre/correo/contrasena/confirmacion/imagen; asistente con apellido/nacimiento/institucion opcional; organizador con descripcion y sitio opcional.
**Archivos:** AltaUsuarioServlet.java, registro_usuario.jsp, validaciones.js.
**Depende de:** T17, T21, T31.
**Cierre:** visitante crea ambos roles, corrige duplicados/confirmacion o cancela; usuarios persistidos pueden autenticarse.

### 33. T33 - Consulta de usuario
**Accion:** listado/perfil con imagen; organizador publico solo Aceptada, propietario tambien otros estados; registros solo al asistente propietario. Enlazar ediciones/registros.
**Archivos:** UsuarioServlet.java, usuarios.jsp, perfil.jsp.
**Depende de:** T22, T30, T31.
**Cierre:** ningun hash ni registro privado o edicion no aceptada ajena en respuestas.

### 34. T34 - Modificar perfil propio
**Accion:** campos por rol, contrasena/confirmacion e imagen opcionales; nickname/correo inmutables; usuario objetivo desde sesion.
**Archivos:** ModificarUsuarioServlet.java, editar_perfil.jsp.
**Depende de:** T17, T21, T31, T33.
**Cierre:** modifica solo su perfil; sin contrasena nueva conserva anterior; cambios visibles en web/Swing.

### 35. T35 - Alta de evento web
**Accion:** organizador ingresa nombre/descripcion/sigla, al menos una categoria existente e imagen opcional.
**Archivos:** AltaEventoServlet.java, evento_nuevo.jsp.
**Depende de:** T21, T24, T27, T31.
**Cierre:** nombre unico, fecha actual en logica, corregir/cancelar y evento persistido visible.

### 36. T36 - Consulta de evento completa
**Accion:** ampliar T12 con categoria, seleccion por ID, datos completos, imagen y ediciones Aceptada enlazadas.
**Archivos:** EventoServlet.java, eventos.jsp, evento_detalle.jsp, encabezado.jsp.
**Depende de:** T12, T22, T24, T30, T31.
**Cierre:** visitante recorre listado -> evento -> edicion, sin datos fijos ni estados privados.

### 37. T37 - Alta de edicion web
**Accion:** elegir evento, ingresar nombre/sigla/ciudad/pais/inicio/fin/imagen opcional; organizador desde sesion, fecha actual y estado Ingresada en logica.
**Archivos:** AltaEdicionServlet.java, edicion_nueva.jsp.
**Depende de:** T18, T21, T27, T31, T36.
**Cierre:** nombre unico y fechas validas; corregir/cancelar; no seleccionar otro organizador por parametro.

### 38. T38 - Consulta de edicion segun consultante
**Accion:** evento -> Aceptada -> detalle de datos/organizador/imagen/tipos/patrocinios. Propietario organizador ve registros; asistente inscripto su registro. Propietario abre Ingresada/Rechazada desde perfil.
**Archivos:** EdicionServlet.java, edicion_detalle.jsp y componentes reutilizados.
**Depende de:** T22, T31, T33, T36.
**Cierre:** detalle publico y propio correctos; URL de edicion no aceptada bloqueada a terceros.

### 39. T39 - Alta de tipo de registro
**Accion:** elegir edicion propia de cualquier estado; nombre/descripcion/costo/cupo; duplicado permite corregir/cancelar.
**Archivos:** AltaTipoRegistroServlet.java, tipo_registro_nuevo.jsp.
**Depende de:** T27, T31, T38.
**Cierre:** tipo persistido; ID manipulado no permite crear en edicion ajena.

### 40. T40 - Consulta de tipo de registro
**Accion:** evento -> edicion Aceptada -> tipo y todos sus datos; reutilizar desde detalle de edicion.
**Archivos:** TipoRegistroServlet.java, tipo_registro_detalle.jsp.
**Depende de:** T22, T31, T38.
**Cierre:** los tres roles publicos consultan datos permitidos por ID y vuelven al contenido relacionado.

### 41. T41 - Registro a edicion web
**Accion:** elegir evento/edicion Aceptada/tipo y general o con codigo; identidad de sesion, costos y errores de cupo/duplicado/codigo.
**Archivos:** AltaRegistroServlet.java, registro.jsp, validaciones.js.
**Depende de:** T26, T27, T31, T38.
**Cierre:** general con costo y patrocinado cero persistidos; sin institucion solo general, comprobado por logica.

### 42. T42 - Consulta de registro de ambos roles
**Accion:** asistente lista sus ediciones registradas y detalle; organizador lista ediciones propias/asistentes/detalle. Fecha/tipo/costo/uso de codigo y accesos desde perfil/edicion.
**Archivos:** RegistroServlet.java, registros.jsp si se necesita, registro_detalle.jsp, perfil.jsp.
**Depende de:** T22, T31, T33, T38, T41.
**Cierre:** ambos recorridos funcionan; no hay registros ajenos salvo organizador de esa edicion.

### 43. T43 - Alta de institucion web
**Accion:** organizador completa nombre/descripcion/sitio/imagen opcional; duplicado y retorno relacionados.
**Archivos:** AltaInstitucionServlet.java, institucion_nueva.jsp.
**Depende de:** T21, T31.
**Cierre:** persistida y disponible en selecciones de usuario/patrocinio.

### 44. T44 - Alta de patrocinio web
**Accion:** evento -> edicion propia de cualquier estado; institucion/tipo existentes, nivel incluido Platino, aporte/cantidad/codigo; corregir/cancelar.
**Archivos:** AltaPatrocinioServlet.java, patrocinio_nuevo.jsp.
**Depende de:** T25, T27, T31, T38, T39, T43.
**Cierre:** persiste con fecha actual y logica rechaza duplicados y exceso del 20% sin depender de JS.

### 45. T45 - Consulta de patrocinio
**Accion:** evento -> Aceptada -> patrocinio con institucion/nivel/aporte/tipo/cantidad/fecha. Solo organizador propietario ve codigo; reutilizar desde ediciones propias privadas.
**Archivos:** PatrocinioServlet.java, patrocinio_detalle.jsp.
**Depende de:** T22, T31, T38.
**Cierre:** codigo ausente de HTML/atributos/respuestas a terceros; propietario lo consulta.

### Etapa F - Estacion de Trabajo

### 46. T46 - Alta de usuario Swing
**Accion:** contrasena/confirmacion/imagen opcional, conservar campos por subtipo e institucion opcional.
**Archivos:** AltaUsuarioInternalFrame.java y .form.
**Depende de:** T17, T21.
**Cierre:** administrador crea ambos roles y se autentican en web.

### 47. T47 - Modificar usuario Swing
**Accion:** cambio opcional de contrasena/imagen, nickname/correo inmutables y contrasena existente no visible.
**Archivos:** ModificarUsuarioInternalFrame.java y .form.
**Depende de:** T17, T21.
**Cierre:** web reconoce la credencial nueva y muestra imagen actualizada.

### 48. T48 - Imagen en altas de evento/edicion/institucion
**Accion:** selector opcional de archivo y envio a logica; retirar fechas manuales de alta donde corresponde asignacion automatica.
**Archivos:** AltaEvento.java/.form, AltaEdicionInternalFrame.java/.form, AltaInstitucionInternalFrame.java/.form.
**Depende de:** T21, T27.
**Cierre:** altas con/sin imagen recuperables desde web.

### 49. T49 - Aceptar/Rechazar Edicion Swing
**Accion:** menu nuevo, eventos, ediciones Ingresada y acciones aceptar/rechazar.
**Archivos:** VentanaPrincipal.java, AceptarRechazarEdicionInternalFrame.java y .form.
**Depende de:** T19.
**Cierre:** cambio administrativo altera visibilidad web sin reiniciarla.

### 50. T50 - Consultas Swing y regresiones
**Accion:** mostrar imagen/estado donde corresponde; acceso administrativo a tres estados y conservar categorias, usuarios, eventos, tipos, registros y patrocinios.
**Archivos:** consultas InternalFrame/.form y FormatoDetalles.java.
**Depende de:** T18, T21, T46, T47, T48, T49.
**Cierre:** todos los casos anteriores funcionan y botones Volver disponibles; filtros web no limitan las consultas administrativas.

### Etapa G - Verificacion

### 51. T51 - JUnit y base aislada
**Accion:** JUnit en logica y ejecucion Maven/IntelliJ; preparar/limpiar datos solo en base de pruebas. Conservar pruebas del laboratorio 1 que existan en otras ramas.
**Archivos:** pom.xml, src/test/java y src/test/resources.
**Depende de:** T05, T07.
**Cierre:** prueba reproducible sin tocar desarrollo. Se puede empezar mientras se implementan funcionalidades.

### 52. T52 - Pruebas de usuarios y estados
**Accion:** nickname/correo, credenciales invalidas, alta por rol, confirmacion, duplicados y cambios de credenciales. Alta Ingresada, ambas transiciones y rechazo de cambios posteriores.
**Archivos:** UsuarioTest.java, EdicionTest.java o equivalentes.
**Depende de:** T17, T19, T20, T51.
**Cierre:** pruebas positivas/negativas de operaciones reales de logica, con persistencia cuando corresponde.

### 53. T53 - Pruebas de registros
**Accion:** general, codigo valido, de otra institucion/edicion/tipo, inexistente/sin usos, cupo agotado, duplicado, sin institucion y edicion no Aceptada. Costos/fechas y no consumir cupo ante error.
**Archivos:** RegistroTest.java y preparacion de datos.
**Depende de:** T26, T27, T51.
**Cierre:** casos minimos de la letra cubiertos; errores no dejan registros guardados.

### 54. T54 - Pruebas de altas y patrocinio
**Accion:** nombres unicos, categoria obligatoria, tipo unico por edicion, IDs ajenos, patrocinio/codigo repetidos y limite del 20%, incluida igualdad; persistencia.
**Archivos:** EventoTest.java, TipoRegistroTest.java, PatrocinioTest.java, InstitucionTest.java o equivalentes.
**Depende de:** T24, T25, T27, T51.
**Cierre:** reglas no dependen de selecciones/validacion previas del navegador.

### 55. T55 - Pruebas de privacidad, imagen y datos frescos
**Accion:** respuestas por rol/propiedad, ausencia de hash/codigo indebidos, detalle por ID, imagen opcional/reemplazada y modificaciones desde otra instancia de acceso a base.
**Archivos:** ConsultasTest.java, ImagenTest.java, pruebas de persistencia o equivalentes.
**Depende de:** T21, T22, T23, T51.
**Cierre:** pruebas detectarian datos privados o desactualizados en respuestas.

### 56. T56 - Cobertura de logica
**Accion:** Run with Coverage en IntelliJ; completar ramas y operaciones no comprobadas con pruebas significativas hasta al menos 80% de lineas de logica.
**Archivos:** configuracion de pruebas y docs/verificacion.
**Depende de:** T52, T53, T54, T55.
**Cierre:** evidencia con version, paquetes y porcentaje; no excluir reglas para maquillar el numero.

### 57. T57 - Integracion web/Swing
**Accion:** todos los casos por rol, escrituras en ambos procesos, reinicios, uno con el otro detenido, URL directa y pedidos simultaneos de cupo/usos.
**Archivos:** checklist en docs/verificacion; correcciones en modulos responsables.
**Depende de:** T23, T32, T33, T34, T35, T36, T37, T38, T39, T40, T41, T42, T43, T44, T45, T50.
**Cierre:** matriz completa con datos reales, sin reinicios para ver cambios. La letra no exige pruebas automaticas de Swing/servidor web, pero el recorrido manual es necesario.

### Etapa H - Documentacion y cierre

### 58. T58 - Analisis y UML
**Accion:** credenciales/imagenes/estados en modelo; aspectos cambiados y secuencias de sistema de los casos fundamentales elegidos por el grupo.
**Archivos:** docs/analisis y diagramas existentes.
**Depende de:** T03, T04.
**Cierre:** reflejan implementacion; revisar nuevamente al completar T17/T19/T21/T26. Puede empezarse temprano.

### 59. T59 - Diseno web y distribucion
**Accion:** MVC, DTO, sesion, JAR/WAR, imagenes, dos procesos y PostgreSQL; diagrama de componentes/distribucion.
**Archivos:** docs/diseno.
**Depende de:** T02, T03, T11, T21, T28.
**Cierre:** cualquiera explica consulta/alta sin afirmar que el navegador ejecuta el JAR.

### 60. T60 - Instrucciones reproducibles
**Accion:** JDK/Maven/Tomcat, crear base/usuario, credenciales locales, persistencia, JAR install, WAR/despliegue, URL, Swing, migracion y pruebas.
**Archivos:** README.md y scripts de preparacion si se necesitan.
**Depende de:** T14, T20, T21, T51.
**Cierre:** otro integrante sigue instrucciones desde copia nueva, sin rutas personales ni ajustes ocultos del IDE.

### 61. T61 - Compilar y desplegar desde copia limpia
**Accion:** abrir ambos proyectos en IntelliJ, construir JAR/WAR y desplegar; revisar binarios viejos, fuentes duplicadas, recursos externos y restos temporales.
**Archivos:** pom.xml, .gitignore, recursos y configuracion de entrega.
**Depende de:** T50, T56, T57, T60.
**Cierre:** lo compilado corresponde al fuente entregado; el sitio usa recursos locales en defensa.

### 62. T62 - Datos persistidos y guion de defensa
**Accion:** usuarios de ambos roles con credenciales ficticias documentadas, visitante, tres estados, imagenes/categorias/tipos/patrocinios, registro general y con codigo; ejemplos negativos y recorrido entre clientes.
**Archivos:** DatosIniciales.java o carga explicita documentada, docs/guion-defensa.md.
**Depende de:** T20, T21, T25, T26, T57.
**Cierre:** todos los casos se recorren sin improvisar datos; preparacion no borra trabajo real.

### 63. T63 - Cerrar matriz de cumplimiento
**Accion:** contrastar funciones, roles, recursos, cobertura y documentos con secciones 6/7/8; revisar privacidad por URL, movil y navegacion sin callejones.
**Archivos:** matrices de este plan y evidencias.
**Depende de:** T56, T57, T58, T59, T60, T61, T62.
**Cierre:** requisito obligatorio con evidencia/responsable; no disfrazar un pendiente como mejora opcional.

### 64. T64 - Entregar version reproducible
**Accion:** integrar ramas terminadas, identificar commit, preparar fuente/documentacion, verificar paquete y entregar por Moodle.
**Archivos:** repositorio, paquete final y README.
**Depende de:** T63.
**Cierre:** paquete coincide con version ensayada y entregado antes del plazo. La primera entrega tambien se prepara segun acuerdo docente; un listado conectado no equivale a todos los casos completos.

## 5. Hitos y paralelo

| Hito | Resultado | Tareas |
|---|---|---|
| H0 | Base y contratos comunes | T01-T06 |
| H1 | Primera pagina con datos reales | T07-T14 |
| H2 | Reglas nuevas disponibles en biblioteca | T15-T27 |
| H3 | Sesion real y marco web | T28-T31 |
| H4 | Todos los casos web | T32-T45 |
| H5 | Swing actualizado | T46-T50 |
| H6 | Verificacion y cobertura | T51-T57 |
| H7 | Entrega reproducible | T58-T64 |

Despues de T03, logica desarrolla credenciales/estados mientras servidor prepara T07-T14. T51 y T58 pueden empezar temprano; pruebas/UML no se dejan para el final.

Las JSP se preparan con nombres de atributos/DTO acordados, pero solo terminan conectadas y con permisos correctos. No se agrega simulacion para declararlas listas.

Despues de T31 repartir familias: usuarios T32-T34; eventos/ediciones T35-T38; tipos/registros T39-T42; instituciones/patrocinios T43-T45. Respetar dependencias especificas.

Swing comienza cuando esta disponible su operacion de logica; no espera a todas las JSP.

Planificar fechas por tarea segun responsables/disponibilidad. Los hitos no inventan un plazo distinto de la letra. El contenido de la primera entrega depende del acuerdo comunicado con el docente.

## 6. Matriz de privacidad y roles

| Situacion | Resultado obligatorio |
|---|---|
| Visitante | Login, alta de usuario y consultas publicas. |
| Asistente autenticado | Modificar propio perfil, registrarse y consultar sus registros. |
| Organizador autenticado | Altas de su rol, modificar perfil y registros de ediciones propias. |
| Administrador | Solo Swing, consultas de todos los estados. |
| Edicion nueva desde cualquier cliente | Ingresada con fecha de alta actual. |
| Edicion publica | Aceptada; filtro en listado y detalle por ID. |
| Organizador en su propio perfil | Sus ediciones Ingresada, Aceptada, Rechazada. |
| Tercero en perfil de organizador | Solo ediciones Aceptada. |
| Asistente en propio perfil | Sus registros y detalles. |
| Tercero en perfil de asistente | Sin listado privado de registros. |
| Organizador propietario en edicion | Lista de registros de esa edicion. |
| Asistente inscripto en edicion | Detalle de su propio registro. |
| Otra persona en edicion | Sin registros de asistentes. |
| Patrocinio publico | Sin codigo. |
| Patrocinio consultado por propietario | Tambien codigo. |
| Perfil editable | Nickname/correo inmutables, imagen/contrasena modificables. |
| Asistente sin institucion | Solo registro general. |
| Estado final Aceptada/Rechazada | No vuelve a Ingresada. |

Aplicar restricciones a los DTO y accesos directos, no solo a botones.

## 7. Cobertura de los casos de uso de la letra

| Caso seccion 6.2 | Tarea web | Logica principal |
|---|---|---|
| Inicio de Sesion | T28 | T15-T16 |
| Cierre de Sesion | T29 | T28 |
| Alta de Usuario | T32 | T17, T21 |
| Consulta de Usuario | T33 | T22 |
| Modificar Datos de Usuario | T34 | T17, T21, T22 |
| Alta de Evento | T35 | T21, T24, T27 |
| Consulta de Evento | T36 | T12, T22, T24 |
| Alta de Edicion de Evento | T37 | T18, T21, T27 |
| Consulta de Edicion de Evento | T38 | T22 |
| Alta de Tipo de Registro | T39 | T27 |
| Consulta de Tipo de Registro | T40 | T22 |
| Registro a Edicion de Evento | T41 | T26-T27 |
| Consulta de Registro | T42 | T22, T26 |
| Alta de Institucion | T43 | T21 |
| Alta de Patrocinio | T44 | T25, T27 |
| Consulta de Patrocinio | T45 | T22, T25 |

Son 16 casos web. No exige exactamente 16 JSP o servlets: una vista puede reunir casos relacionados y un caso usar varias vistas.

## 8. Archivos HTTP propuestos

Prefijar rutas con contexto acordado. JSP dentro de WEB-INF abiertas por el controlador. GET muestra, POST confirma; cancelar no persiste y exito redirige para que recargar no repita un alta.

| Controlador | Ruta propuesta | Vista |
|---|---|---|
| EventoServlet | GET /eventos; id/categoria opcionales | eventos.jsp, evento_detalle.jsp |
| AltaEventoServlet | GET/POST /eventos/alta | evento_nuevo.jsp |
| EdicionServlet | GET /ediciones?id=... | edicion_detalle.jsp |
| AltaEdicionServlet | GET/POST /ediciones/alta | edicion_nueva.jsp |
| TipoRegistroServlet | GET /tipos-registro?id=... | tipo_registro_detalle.jsp |
| AltaTipoRegistroServlet | GET/POST /tipos-registro/alta | tipo_registro_nuevo.jsp |
| UsuarioServlet | GET /usuarios; nickname opcional | usuarios.jsp, perfil.jsp |
| AltaUsuarioServlet | GET/POST /usuarios/alta | registro_usuario.jsp |
| ModificarUsuarioServlet | GET/POST /usuarios/editar | editar_perfil.jsp |
| LoginServlet | GET/POST /login | login.jsp |
| LogoutServlet | POST /logout | Redireccion publica |
| RegistroServlet | GET /registros; id opcional | registros.jsp, registro_detalle.jsp |
| AltaRegistroServlet | GET/POST /registros/alta | registro.jsp |
| AltaInstitucionServlet | GET/POST /instituciones/alta | institucion_nueva.jsp |
| PatrocinioServlet | GET /patrocinios?id=... | patrocinio_detalle.jsp |
| AltaPatrocinioServlet | GET/POST /patrocinios/alta | patrocinio_nuevo.jsp |
| ImagenServlet | GET /imagenes con ID acordado | Contenido obtenido mediante logica |
| Inicio | GET / | Redireccion o vista con acceso al contenido |

## 9. Criterio comun para terminar una tarea

- Compila con la rama comun y no rompe consumidores Swing.
- Opera contra logica real, sin entidades fijadas en JSP/JS.
- Cambios persistidos se recuperan y sobreviven al reinicio.
- Recorrido correcto, vacio si aplica y error significativo comprobados.
- Rol/propiedad/estado comprobados tanto al listar como por ID.
- Se puede corregir/cancelar y volver al contenido relacionado.
- Pruebas/contratos/documentos afectados actualizados.
- Revisada por otro integrante antes de integrar.

Si solo termina parcialmente, mantener En curso o En revision y anotar lo pendiente.

## 10. Seguimiento

Completar Responsable y Estado. Anotar rama, impedimento o evidencia en Observaciones. Las tareas son futuras; no se atribuyen avances a integrantes.

| Tarea | Responsable | Estado | Observaciones |
|---|---|---|---|
| T01 | Por asignar | Pendiente | |
| T02 | Por asignar | Pendiente | |
| T03 | Por asignar | Pendiente | |
| T04 | Por asignar | Pendiente | |
| T05 | Por asignar | Pendiente | |
| T06 | Por asignar | Pendiente | |
| T07 | Por asignar | Pendiente | |
| T08 | Por asignar | Pendiente | |
| T09 | Por asignar | Pendiente | |
| T10 | Por asignar | Pendiente | |
| T11 | Por asignar | Pendiente | |
| T12 | Por asignar | Pendiente | |
| T13 | Por asignar | Pendiente | |
| T14 | Por asignar | Pendiente | |
| T15 | Por asignar | Pendiente | |
| T16 | Por asignar | Pendiente | |
| T17 | Por asignar | Pendiente | |
| T18 | Por asignar | Pendiente | |
| T19 | Por asignar | Pendiente | |
| T20 | Por asignar | Pendiente | |
| T21 | Por asignar | Pendiente | |
| T22 | Por asignar | Pendiente | |
| T23 | Por asignar | Pendiente | |
| T24 | Por asignar | Pendiente | |
| T25 | Por asignar | Pendiente | |
| T26 | Por asignar | Pendiente | |
| T27 | Por asignar | Pendiente | |
| T28 | Por asignar | Pendiente | |
| T29 | Por asignar | Pendiente | |
| T30 | Por asignar | Pendiente | |
| T31 | Por asignar | Pendiente | |
| T32 | Por asignar | Pendiente | |
| T33 | Por asignar | Pendiente | |
| T34 | Por asignar | Pendiente | |
| T35 | Por asignar | Pendiente | |
| T36 | Por asignar | Pendiente | |
| T37 | Por asignar | Pendiente | |
| T38 | Por asignar | Pendiente | |
| T39 | Por asignar | Pendiente | |
| T40 | Por asignar | Pendiente | |
| T41 | Por asignar | Pendiente | |
| T42 | Por asignar | Pendiente | |
| T43 | Por asignar | Pendiente | |
| T44 | Por asignar | Pendiente | |
| T45 | Por asignar | Pendiente | |
| T46 | Por asignar | Pendiente | |
| T47 | Por asignar | Pendiente | |
| T48 | Por asignar | Pendiente | |
| T49 | Por asignar | Pendiente | |
| T50 | Por asignar | Pendiente | |
| T51 | Por asignar | Pendiente | |
| T52 | Por asignar | Pendiente | |
| T53 | Por asignar | Pendiente | |
| T54 | Por asignar | Pendiente | |
| T55 | Por asignar | Pendiente | |
| T56 | Por asignar | Pendiente | |
| T57 | Por asignar | Pendiente | |
| T58 | Por asignar | Pendiente | |
| T59 | Por asignar | Pendiente | |
| T60 | Por asignar | Pendiente | |
| T61 | Por asignar | Pendiente | |
| T62 | Por asignar | Pendiente | |
| T63 | Por asignar | Pendiente | |
| T64 | Por asignar | Pendiente | |

## 11. Checklist final

- [ ] Los 16 casos web se recorren desde el inicio con rol correcto.
- [ ] Administrador solo Swing; visitante/asistente/organizador diferenciados.
- [ ] Login por nickname/correo y logout operativos.
- [ ] Altas/modificaciones persistidas y validacion en navegador/logica.
- [ ] Nickname/correo inmutables; contrasenas fuera de consultas.
- [ ] Estados/transiciones correctos en ambos clientes.
- [ ] Privacidad comprobada tambien con URL directa.
- [ ] Registro general y con codigo, costo/cupo/usos correctos.
- [ ] Imagenes opcionales de usuario/evento/edicion/institucion persistidas y visibles.
- [ ] Categorias del encabezado filtran eventos.
- [ ] Seleccion conserva ID/nickname y abre el detalle correcto.
- [ ] Recursos del framework locales.
- [ ] JSP/servlets sin entidades, DAO, JPA ni SQL directos.
- [ ] JAR embebido, WAR en Tomcat y Swing independiente.
- [ ] Ambos clientes leen la misma base y cambios sin reiniciar.
- [ ] Sin identidad de usuario global en singleton.
- [ ] JUnit aislado y al menos 80% de lineas de logica.
- [ ] Casos no modificados del laboratorio 1 siguen funcionando.
- [ ] UML/secuencias/diseno reflejan implementacion.
- [ ] README permite crear base, conocer credenciales y ejecutar.
- [ ] Datos persistidos suficientes para roles, estados y ambos registros.
- [ ] Fuente abre/compila/ejecuta en IntelliJ; JAR/WAR reproducibles.
- [ ] Version, paquete y evidencia coinciden; Moodle dentro del plazo.

Fuera de alcance de la letra: video de edicion, web services entre procesos y barra de busqueda. No agregarlos al camino critico.
