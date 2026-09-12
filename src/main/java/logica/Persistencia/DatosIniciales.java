package logica.Persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import logica.Clases.Asistente;
import logica.Clases.Categoria;
import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.Clases.Institucion;
import logica.Clases.NivelPatrocinio;
import logica.Clases.Organizador;
import logica.Clases.Patrocinio;
import logica.Clases.Registro;
import logica.Clases.TipoRegistro;
import logica.Clases.Usuario;
import logica.DataTypes.DTFecha;

import java.time.LocalDate;

public final class DatosIniciales {

    private DatosIniciales() {
    }

    public static void cargar(EntityManagerFactory entityManagerFactory) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            if (yaCargados(em)) {
                return;
            }

            em.getTransaction().begin();

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

            Organizador juanchi = obtenerOrganizador(
                    em,
                    "juanchi",
                    "Juancito Pérez",
                    "juancito@eventosuy.test",
                    "Organizador de conferencias y actividades tecnológicas",
                    "https://eventosuy.test/organizadores/juanchi"
            );
            obtenerOrganizador(
                    em,
                    "sofia.eventos",
                    "Sofía Rodríguez",
                    "sofia.rodriguez@eventosuy.test",
                    "Organizadora de eventos académicos",
                    "https://eventosuy.test/organizadores/sofia"
            );

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

            Categoria tecnologia = obtenerCategoria(em, "Tecnología");
            Categoria educacion = obtenerCategoria(em, "Educación");
            Categoria negocios = obtenerCategoria(em, "Negocios");

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

            Edicion java2026 = obtenerEdicion(
                    em,
                    "Java Uruguay 2026",
                    "JV26",
                    new DTFecha(2026, 10, 8),
                    new DTFecha(2026, 10, 10),
                    new DTFecha(2026, 1, 10),
                    "Montevideo",
                    "Uruguay",
                    juanchi,
                    javaUy
            );
            Edicion datos2026 = obtenerEdicion(
                    em,
                    "Datos Abiertos 2026",
                    "DA26",
                    new DTFecha(2026, 11, 12),
                    new DTFecha(2026, 11, 13),
                    new DTFecha(2026, 2, 5),
                    "Montevideo",
                    "Uruguay",
                    juanchi,
                    datosAbiertos
            );

            TipoRegistro generalJava = obtenerTipoRegistro(
                    em,
                    java2026,
                    "Entrada general",
                    "Acceso a todas las charlas de la edición.",
                    150,
                    300
            );
            TipoRegistro estudianteJava = obtenerTipoRegistro(
                    em,
                    java2026,
                    "Estudiante",
                    "Entrada bonificada para estudiantes.",
                    80,
                    150
            );
            TipoRegistro generalDatos = obtenerTipoRegistro(
                    em,
                    datos2026,
                    "Participante",
                    "Acceso a las actividades del encuentro.",
                    120,
                    200
            );

            Patrocinio patrocinioUtec = obtenerPatrocinio(
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
            obtenerPatrocinio(
                    em,
                    "ANTEL-DA26-001",
                    new DTFecha(2026, 3, 5),
                    2500,
                    2,
                    NivelPatrocinio.PLATA,
                    antel,
                    datos2026,
                    generalDatos
            );

            obtenerRegistro(
                    em,
                    mati,
                    java2026,
                    generalJava,
                    null,
                    150,
                    false
            );
            obtenerRegistro(
                    em,
                    lucia,
                    java2026,
                    estudianteJava,
                    patrocinioUtec,
                    0,
                    true
            );
            obtenerRegistro(
                    em,
                    mati,
                    datos2026,
                    generalDatos,
                    null,
                    120,
                    false
            );

            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new IllegalStateException("No se pudieron cargar los datos iniciales.", e);
        } finally {
            em.close();
        }
    }

    private static boolean yaCargados(EntityManager em) {
        return existe(em, "SELECT COUNT(i) FROM Institucion i WHERE LOWER(i.nombre) = 'utec'")
                && existe(em, "SELECT COUNT(i) FROM Institucion i WHERE LOWER(i.nombre) = 'antel'")
                && existe(em, "SELECT COUNT(u) FROM Usuario u WHERE LOWER(u.nickname) = 'lucia.test'")
                && existe(em, "SELECT COUNT(c) FROM Categoria c WHERE LOWER(c.nombre) = 'negocios'")
                && existe(em, "SELECT COUNT(e) FROM Evento e WHERE LOWER(e.nombre) = 'conferencia java uruguay'")
                && existe(em, "SELECT COUNT(e) FROM Edicion e WHERE LOWER(e.nombre) = 'java uruguay 2026'")
                && existe(em, "SELECT COUNT(t) FROM TipoRegistro t WHERE LOWER(t.nombre) = 'entrada general'")
                && existe(em, "SELECT COUNT(p) FROM Patrocinio p WHERE LOWER(p.codigo) = 'utec-jv26-001'")
                && existe(em, "SELECT COUNT(r) FROM Registro r WHERE r.patrocinado = true");
    }

    private static boolean existe(EntityManager em, String consulta) {
        Long cantidad = em.createQuery(consulta, Long.class).getSingleResult();
        return cantidad != null && cantidad > 0;
    }

    private static Institucion obtenerInstitucion(
            EntityManager em,
            String nombre,
            String descripcion,
            String sitioWeb
    ) {
        Institucion institucion = em.createQuery(
                        "SELECT i FROM Institucion i WHERE LOWER(i.nombre) = LOWER(:nombre)",
                        Institucion.class
                )
                .setParameter("nombre", nombre)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (institucion == null) {
            institucion = new Institucion(nombre, descripcion, sitioWeb);
            em.persist(institucion);
        }

        return institucion;
    }

    private static Usuario obtenerUsuario(EntityManager em, String nickname) {
        return em.createQuery(
                        "SELECT u FROM Usuario u WHERE LOWER(u.nickname) = LOWER(:nickname)",
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
            em.persist(organizador);
            return organizador;
        }
        if (!(usuario instanceof Organizador organizador)) {
            throw new IllegalStateException(
                    "El usuario '" + nickname + "' existe pero no es organizador."
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
            em.persist(asistente);
            return asistente;
        }
        if (!(usuario instanceof Asistente asistente)) {
            throw new IllegalStateException(
                    "El usuario '" + nickname + "' existe pero no es asistente."
            );
        }
        if (asistente.getInstitucion() == null) {
            asistente.setInstitucion(institucion);
        }
        return asistente;
    }

    private static Categoria obtenerCategoria(EntityManager em, String nombre) {
        Categoria categoria = em.createQuery(
                        "SELECT c FROM Categoria c WHERE LOWER(c.nombre) = LOWER(:nombre)",
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
                        "SELECT DISTINCT e FROM Evento e LEFT JOIN FETCH e.categorias " +
                                "WHERE LOWER(e.nombre) = LOWER(:nombre)",
                        Evento.class
                )
                .setParameter("nombre", nombre)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (evento == null) {
            evento = new Evento(nombre, descripcion, sigla, fechaAlta);
            em.persist(evento);
        }

        return evento;
    }

    private static void asociarCategoria(Evento evento, Categoria categoria) {
        boolean asociada = evento.getCategorias().stream()
                .anyMatch(actual -> actual.getNombre().equalsIgnoreCase(categoria.getNombre()));
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
            Evento evento
    ) {
        Edicion edicion = em.createQuery(
                        "SELECT e FROM Edicion e WHERE LOWER(e.nombre) = LOWER(:nombre)",
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
                        "SELECT t FROM TipoRegistro t " +
                                "WHERE t.edicion.id = :edicionId " +
                                "AND LOWER(t.nombre) = LOWER(:nombre)",
                        TipoRegistro.class
                )
                .setParameter("edicionId", edicion.getId())
                .setParameter("nombre", nombre)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (tipo == null) {
            tipo = new TipoRegistro(nombre, descripcion, costo, cupo);
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
                        "SELECT p FROM Patrocinio p WHERE LOWER(p.codigo) = LOWER(:codigo)",
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
                        "SELECT r FROM Registro r " +
                                "WHERE r.asistente.id = :asistenteId " +
                                "AND r.edicion.id = :edicionId",
                        Registro.class
                )
                .setParameter("asistenteId", asistente.getId())
                .setParameter("edicionId", edicion.getId())
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (registro == null) {
            LocalDate hoy = LocalDate.now();
            registro = new Registro(
                    new DTFecha(hoy.getYear(), hoy.getMonthValue(), hoy.getDayOfMonth()),
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
}
