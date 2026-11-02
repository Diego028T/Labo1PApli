package logica.Persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import logica.Clases.Asistente;
import logica.Clases.Categoria;
import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.Clases.Institucion;
import logica.Clases.Organizador;
import logica.Clases.Patrocinio;
import logica.Clases.Registro;
import logica.Clases.TipoRegistro;
import logica.Clases.Usuario;
import logica.DataTypes.DTFecha;
import logica.DataTypes.EstadoEdicion;
import logica.DataTypes.NivelPatrocinio;

import java.time.LocalDate;

public final class DatosIniciales {

    private static final String CONTRASENA_DEMO = "Demo2026!";

    private DatosIniciales() {
    }

    public static void cargar(EntityManagerFactory entityManagerFactory) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            completarEstadoEdiciones(em);

            if (yaCargados(em)) {
                return;
            }

            em.getTransaction().begin();

            // Instituciones.
            Institucion utec = obtenerInstitucion(
                    em,
                    "UTEC",
                    "Universidad Tecnológica del Uruguay",
                    "https://utec.edu.uy"
            );

            Institucion antel = obtenerInstitucion(
                    em,
                    "ANTEL",
                    "Empresa nacional de telecomunicaciones",
                    "https://www.antel.com.uy"
            );

            obtenerInstitucion(
                    em,
                    "Instituto de Innovación",
                    "Institución disponible para probar nuevos patrocinios.",
                    "https://instituto.example"
            );

            // Organizadores.
            Organizador juanchi = obtenerOrganizador(
                    em,
                    "juanchi",
                    "Juancito Pérez",
                    "juancito@eventosuy.test",
                    "Organizador de conferencias tecnológicas.",
                    "https://eventosuy.test/organizadores/juanchi"
            );

            Organizador sofia = obtenerOrganizador(
                    em,
                    "sofia.eventos",
                    "Sofía Rodríguez",
                    "sofia.rodriguez@eventosuy.test",
                    "Organizadora de encuentros sobre datos abiertos.",
                    "https://eventosuy.test/organizadores/sofia"
            );

            // Asistentes con registros iniciales.
            Asistente mati = obtenerAsistente(
                    em,
                    "MatiB",
                    "Matías",
                    "matias.bragio@eventosuy.test",
                    "Bragio",
                    LocalDate.of(2000, 5, 10),
                    utec
            );

            Asistente lucia = obtenerAsistente(
                    em,
                    "lucia.test",
                    "Lucía",
                    "lucia.gomez@eventosuy.test",
                    "Gómez",
                    LocalDate.of(1998, 8, 22),
                    antel
            );

            Asistente diego = obtenerAsistente(
                    em,
                    "diego.utec",
                    "Diego",
                    "diego@eventosuy.test",
                    "Pérez",
                    LocalDate.of(2001, 4, 15),
                    utec
            );

            // Asistentes disponibles para hacer nuevas inscripciones.
            obtenerAsistente(
                    em,
                    "ana.utec",
                    "Ana",
                    "ana@eventosuy.test",
                    "Silva",
                    LocalDate.of(2002, 7, 20),
                    utec
            );

            obtenerAsistente(
                    em,
                    "bruno.antel",
                    "Bruno",
                    "bruno@eventosuy.test",
                    "López",
                    LocalDate.of(1999, 9, 12),
                    antel
            );

            obtenerAsistente(
                    em,
                    "carla.libre",
                    "Carla",
                    "carla@eventosuy.test",
                    "Suárez",
                    LocalDate.of(2000, 3, 8),
                    null
            );

            // Cultura queda sin eventos para probar un filtro vacío.
            Categoria tecnologia = obtenerCategoria(em, "Tecnología");
            Categoria educacion = obtenerCategoria(em, "Educación");
            Categoria negocios = obtenerCategoria(em, "Negocios");
            obtenerCategoria(em, "Cultura");

            // Eventos.
            Evento javaUy = obtenerEvento(
                    em,
                    "Conferencia Java Uruguay",
                    "Jornadas sobre desarrollo Java, arquitectura y buenas prácticas.",
                    "JVUY26",
                    new DTFecha(2026, 1, 15)
            );

            asociarCategoria(javaUy, tecnologia);
            asociarCategoria(javaUy, educacion);

            Evento datosAbiertos = obtenerEvento(
                    em,
                    "Datos Abiertos y Ciudadanía",
                    "Encuentro sobre datos abiertos, innovación y servicios digitales.",
                    "DAT26",
                    new DTFecha(2026, 2, 1)
            );

            asociarCategoria(datosAbiertos, tecnologia);
            asociarCategoria(datosAbiertos, negocios);

            Evento encuentroNegocios = obtenerEvento(
                    em,
                    "Encuentro de Negocios",
                    "Evento que todavía no tiene ediciones.",
                    "NEG26",
                    new DTFecha(2026, 2, 10)
            );

            asociarCategoria(encuentroNegocios, negocios);

            // Ediciones de Juanchi.
            Edicion java2026 = obtenerEdicion(
                    em,
                    "Java Uruguay 2026",
                    "JV26",
                    new DTFecha(2026, 12, 8),
                    new DTFecha(2026, 12, 10),
                    new DTFecha(2026, 2, 2),
                    "Montevideo",
                    "Uruguay",
                    juanchi,
                    javaUy,
                    EstadoEdicion.APROBADA
            );

            obtenerEdicion(
                    em,
                    "Java Uruguay 2027",
                    "JV27",
                    new DTFecha(2027, 3, 8),
                    new DTFecha(2027, 3, 10),
                    new DTFecha(2026, 4, 1),
                    "Montevideo",
                    "Uruguay",
                    juanchi,
                    javaUy,
                    EstadoEdicion.INGRESADA
            );

            obtenerEdicion(
                    em,
                    "Java Regional 2027",
                    "JVR27",
                    new DTFecha(2027, 4, 15),
                    new DTFecha(2027, 4, 16),
                    new DTFecha(2026, 4, 2),
                    "Colonia",
                    "Uruguay",
                    juanchi,
                    javaUy,
                    EstadoEdicion.INGRESADA
            );

            obtenerEdicion(
                    em,
                    "Java Cancelada 2026",
                    "JVC26",
                    new DTFecha(2026, 12, 20),
                    new DTFecha(2026, 12, 21),
                    new DTFecha(2026, 4, 3),
                    "Salto",
                    "Uruguay",
                    juanchi,
                    javaUy,
                    EstadoEdicion.RECHAZADA
            );

            // Ediciones de Sofía.
            Edicion datos2026 = obtenerEdicion(
                    em,
                    "Datos Abiertos 2026",
                    "DA26",
                    new DTFecha(2026, 11, 12),
                    new DTFecha(2026, 11, 13),
                    new DTFecha(2026, 2, 5),
                    "Montevideo",
                    "Uruguay",
                    sofia,
                    datosAbiertos,
                    EstadoEdicion.APROBADA
            );

            // Queda sin tipos, patrocinios ni registros.
            obtenerEdicion(
                    em,
                    "Taller de Datos 2026",
                    "TD26",
                    new DTFecha(2026, 12, 15),
                    new DTFecha(2026, 12, 16),
                    new DTFecha(2026, 2, 10),
                    "Durazno",
                    "Uruguay",
                    sofia,
                    datosAbiertos,
                    EstadoEdicion.APROBADA
            );

            // Tipos de registro.
            TipoRegistro generalJava = obtenerTipoRegistro(
                    em,
                    java2026,
                    "Entrada general",
                    "Acceso a todas las charlas.",
                    150,
                    20
            );

            TipoRegistro estudianteJava = obtenerTipoRegistro(
                    em,
                    java2026,
                    "Estudiante",
                    "Entrada bonificada para estudiantes.",
                    80,
                    20
            );

            TipoRegistro generalDatos = obtenerTipoRegistro(
                    em,
                    datos2026,
                    "Participante",
                    "Acceso a las actividades del encuentro.",
                    120,
                    20
            );

            TipoRegistro grupoReducido = obtenerTipoRegistro(
                    em,
                    datos2026,
                    "Grupo reducido",
                    "Actividad con un único lugar, ocupado desde la carga inicial.",
                    120,
                    1
            );

            // Patrocinios: respetan el máximo del 20 % del aporte.
            Patrocinio patrocinioUtecJava = obtenerPatrocinio(
                    em,
                    "UTEC-JV26-001",
                    new DTFecha(2026, 3, 1),
                    3000,
                    2,
                    NivelPatrocinio.ORO,
                    utec,
                    java2026,
                    estudianteJava
            );

            Patrocinio patrocinioAntelJava = obtenerPatrocinio(
                    em,
                    "ANTEL-JV26-001",
                    new DTFecha(2026, 3, 2),
                    1500,
                    1,
                    NivelPatrocinio.PLATA,
                    antel,
                    java2026,
                    generalJava
            );

            obtenerPatrocinio(
                    em,
                    "UTEC-DA26-001",
                    new DTFecha(2026, 3, 5),
                    2000,
                    2,
                    NivelPatrocinio.BRONCE,
                    utec,
                    datos2026,
                    generalDatos
            );

            // Registro general de Matías en Java.
            obtenerRegistro(
                    em,
                    mati,
                    java2026,
                    generalJava,
                    null,
                    150,
                    false
            );

            // Ocupa el único lugar del tipo Grupo reducido.
            obtenerRegistro(
                    em,
                    mati,
                    datos2026,
                    grupoReducido,
                    null,
                    120,
                    false
            );

            // Consume el único uso del patrocinio de ANTEL.
            obtenerRegistro(
                    em,
                    lucia,
                    java2026,
                    generalJava,
                    patrocinioAntelJava,
                    0,
                    true
            );

            // Consume uno de los dos usos del patrocinio de UTEC.
            obtenerRegistro(
                    em,
                    diego,
                    java2026,
                    estudianteJava,
                    patrocinioUtecJava,
                    0,
                    true
            );

            em.getTransaction().commit();

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw new IllegalStateException(
                    "No se pudieron cargar los datos iniciales.", e
            );
        } finally {
            em.close();
        }
    }

    private static boolean yaCargados(EntityManager em) {
        return existe(
                em,
                "SELECT COUNT(i) FROM Institucion i "
                        + "WHERE LOWER(i.nombre) = 'utec'"
        ) && existe(
                em,
                "SELECT COUNT(i) FROM Institucion i "
                        + "WHERE LOWER(i.nombre) = 'antel'"
        ) && existe(
                em,
                "SELECT COUNT(u) FROM Usuario u "
                        + "WHERE LOWER(u.nickname) = 'lucia.test'"
        ) && existe(
                em,
                "SELECT COUNT(c) FROM Categoria c "
                        + "WHERE LOWER(c.nombre) = 'negocios'"
        ) && existe(
                em,
                "SELECT COUNT(e) FROM Evento e "
                        + "WHERE LOWER(e.nombre) = 'conferencia java uruguay'"
        ) && existe(
                em,
                "SELECT COUNT(e) FROM Edicion e "
                        + "WHERE LOWER(e.nombre) = 'java uruguay 2026'"
        ) && existe(
                em,
                "SELECT COUNT(t) FROM TipoRegistro t "
                        + "WHERE LOWER(t.nombre) = 'entrada general'"
        ) && existe(
                em,
                "SELECT COUNT(p) FROM Patrocinio p "
                        + "WHERE LOWER(p.codigo) = 'utec-jv26-001'"
        ) && existe(
                em,
                "SELECT COUNT(r) FROM Registro r "
                        + "WHERE r.patrocinado = true"
        );
    }

    private static boolean existe(EntityManager em, String consulta) {
        Long cantidad = em.createQuery(consulta, Long.class)
                .getSingleResult();

        return cantidad != null && cantidad > 0;
    }

    private static Institucion obtenerInstitucion(
            EntityManager em,
            String nombre,
            String descripcion,
            String sitioWeb
    ) {
        Institucion institucion = em.createQuery(
                        "SELECT i FROM Institucion i "
                                + "WHERE LOWER(i.nombre) = LOWER(:nombre)",
                        Institucion.class
                )
                .setParameter("nombre", nombre)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (institucion == null) {
            institucion = new Institucion(
                    nombre,
                    descripcion,
                    sitioWeb
            );

            em.persist(institucion);
        }

        return institucion;
    }

    private static Usuario obtenerUsuario(
            EntityManager em,
            String nickname
    ) {
        return em.createQuery(
                        "SELECT u FROM Usuario u "
                                + "WHERE LOWER(u.nickname) = LOWER(:nickname)",
                        Usuario.class
                )
                .setParameter("nickname", nickname)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    private static Organizador obtenerOrganizador(
            EntityManager em,
            String nickname,
            String nombre,
            String correo,
            String descripcion,
            String enlace
    ) {
        Usuario usuario = obtenerUsuario(em, nickname);

        if (usuario == null) {
            Organizador organizador = new Organizador(
                    nombre,
                    nickname,
                    correo,
                    descripcion,
                    enlace
            );

            organizador.establecerContrasena(
                    CONTRASENA_DEMO.toCharArray()
            );

            em.persist(organizador);
            return organizador;
        }

        if (!(usuario instanceof Organizador organizador)) {
            throw new IllegalStateException(
                    "El usuario '" + nickname
                            + "' existe pero no es organizador."
            );
        }

        return organizador;
    }

    private static Asistente obtenerAsistente(
            EntityManager em,
            String nickname,
            String nombre,
            String correo,
            String apellido,
            LocalDate fechaNacimiento,
            Institucion institucion
    ) {
        Usuario usuario = obtenerUsuario(em, nickname);

        if (usuario == null) {
            Asistente asistente = new Asistente(
                    nombre,
                    nickname,
                    correo,
                    apellido,
                    fechaNacimiento
            );

            asistente.setInstitucion(institucion);

            asistente.establecerContrasena(
                    CONTRASENA_DEMO.toCharArray()
            );

            em.persist(asistente);
            return asistente;
        }

        if (!(usuario instanceof Asistente asistente)) {
            throw new IllegalStateException(
                    "El usuario '" + nickname
                            + "' existe pero no es asistente."
            );
        }

        if (asistente.getInstitucion() == null) {
            asistente.setInstitucion(institucion);
        }

        return asistente;
    }

    private static Categoria obtenerCategoria(
            EntityManager em,
            String nombre
    ) {
        Categoria categoria = em.createQuery(
                        "SELECT c FROM Categoria c "
                                + "WHERE LOWER(c.nombre) = LOWER(:nombre)",
                        Categoria.class
                )
                .setParameter("nombre", nombre)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (categoria == null) {
            categoria = new Categoria(nombre);
            em.persist(categoria);
        }

        return categoria;
    }

    private static Evento obtenerEvento(
            EntityManager em,
            String nombre,
            String descripcion,
            String sigla,
            DTFecha fechaAlta
    ) {
        Evento evento = em.createQuery(
                        "SELECT DISTINCT e FROM Evento e "
                                + "LEFT JOIN FETCH e.categorias "
                                + "WHERE LOWER(e.nombre) = LOWER(:nombre)",
                        Evento.class
                )
                .setParameter("nombre", nombre)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (evento == null) {
            evento = new Evento(
                    nombre,
                    descripcion,
                    sigla,
                    fechaAlta
            );

            em.persist(evento);
        }

        return evento;
    }

    private static void asociarCategoria(
            Evento evento,
            Categoria categoria
    ) {
        boolean asociada = evento.getCategorias().stream()
                .anyMatch(actual ->
                        actual.getNombre().equalsIgnoreCase(
                                categoria.getNombre()
                        )
                );

        if (!asociada) {
            evento.agregarCategoria(categoria);
        }
    }

    private static Edicion obtenerEdicion(
            EntityManager em,
            String nombre,
            String sigla,
            DTFecha fechaInicio,
            DTFecha fechaFin,
            DTFecha fechaAlta,
            String ciudad,
            String pais,
            Organizador organizador,
            Evento evento,
            EstadoEdicion estadoInicial
    ) {
        Edicion edicion = em.createQuery(
                        "SELECT e FROM Edicion e "
                                + "WHERE LOWER(e.nombre) = LOWER(:nombre)",
                        Edicion.class
                )
                .setParameter("nombre", nombre)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (edicion == null) {
            edicion = new Edicion(
                    nombre,
                    sigla,
                    fechaInicio,
                    fechaFin,
                    fechaAlta,
                    ciudad,
                    pais,
                    organizador,
                    evento
            );

            // Solo asignamos el estado al crear la edición.
            edicion.setEstado(estadoInicial);

            em.persist(edicion);
        }

        return edicion;
    }

    private static TipoRegistro obtenerTipoRegistro(
            EntityManager em,
            Edicion edicion,
            String nombre,
            String descripcion,
            float costo,
            int cupo
    ) {
        TipoRegistro tipo = em.createQuery(
                        "SELECT t FROM TipoRegistro t "
                                + "WHERE t.edicion.id = :edicionId "
                                + "AND LOWER(t.nombre) = LOWER(:nombre)",
                        TipoRegistro.class
                )
                .setParameter("edicionId", edicion.getId())
                .setParameter("nombre", nombre)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (tipo == null) {
            tipo = new TipoRegistro(
                    nombre,
                    descripcion,
                    costo,
                    cupo
            );

            tipo.setEdicion(edicion);

            em.persist(tipo);
        }

        return tipo;
    }

    private static Patrocinio obtenerPatrocinio(
            EntityManager em,
            String codigo,
            DTFecha fecha,
            float monto,
            int cantidadRegistros,
            NivelPatrocinio nivel,
            Institucion institucion,
            Edicion edicion,
            TipoRegistro tipoRegistro
    ) {
        Patrocinio patrocinio = em.createQuery(
                        "SELECT p FROM Patrocinio p "
                                + "WHERE LOWER(p.codigo) = LOWER(:codigo)",
                        Patrocinio.class
                )
                .setParameter("codigo", codigo)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (patrocinio == null) {
            patrocinio = new Patrocinio(
                    codigo,
                    fecha,
                    monto,
                    cantidadRegistros,
                    nivel,
                    institucion,
                    edicion,
                    tipoRegistro
            );

            em.persist(patrocinio);
        }

        return patrocinio;
    }

    private static Registro obtenerRegistro(
            EntityManager em,
            Asistente asistente,
            Edicion edicion,
            TipoRegistro tipoRegistro,
            Patrocinio patrocinio,
            double costo,
            boolean patrocinado
    ) {
        Registro registro = em.createQuery(
                        "SELECT r FROM Registro r "
                                + "WHERE r.asistente.id = :asistenteId "
                                + "AND r.edicion.id = :edicionId",
                        Registro.class
                )
                .setParameter("asistenteId", asistente.getId())
                .setParameter("edicionId", edicion.getId())
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (registro == null) {
            LocalDate hoy = LocalDate.now();

            DTFecha fechaRegistro = new DTFecha(
                    hoy.getYear(),
                    hoy.getMonthValue(),
                    hoy.getDayOfMonth()
            );

            registro = new Registro(
                    fechaRegistro,
                    costo,
                    patrocinado,
                    asistente,
                    tipoRegistro,
                    edicion,
                    patrocinio
            );

            em.persist(registro);
        }

        return registro;
    }

    private static void completarEstadoEdiciones(EntityManager em) {
        em.getTransaction().begin();

        em.createQuery(
                        "UPDATE Edicion e SET e.estado = :estado "
                                + "WHERE e.estado IS NULL"
                )
                .setParameter("estado", EstadoEdicion.INGRESADA)
                .executeUpdate();

        em.getTransaction().commit();
    }
}