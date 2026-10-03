# Propuesta para separar presentacion y logica con DTO

Este documento es una propuesta para estudiar. No se aplico a los archivos Java de la aplicacion. El codigo completo propuesto para cada archivo aparece abajo.

## 1. La opcion sencilla

La presentacion conoce ISistema, DTO y valores simples. Sistema conoce las entidades y los DAO. Un DTO lleva datos; un ID o nickname identifica la seleccion. La fabrica ya incorporada conserva su papel de obtener ISistema al iniciar.

Usamos un DTO completo por elemento para reducir cambios. DTEvento sirve para el listado y el detalle; lo mismo ocurre con DTEdicion, DTTipoRegistro y DTPatrocinio. No agregamos versiones Min para cada pantalla ni un repositorio nuevo.

Los .form y la estructura visual se mantienen. Los DAO conservan sus operaciones; solo se agregan dos busquedas para resolver IDs. No cambia el esquema JPA ni persistence.xml.

La propuesta supone eventos, ediciones y tipos persistidos, como ocurre en el flujo actual de la aplicacion. Elimina el retorno silencioso a listas en memoria cuando una consulta falla: los errores de base de datos se deben comunicar, no convertirse en listados aparentemente vacios. No aborda otros problemas preexistentes de transacciones, concurrencia o validaciones.

## 2. Como viaja una seleccion

1. La pantalla llama a sistema.listarEventos().
2. Sistema consulta EventoDAO y convierte los Evento en DTEvento.
3. La pantalla llena JList<DTEvento>.
4. El administrador elige un DTO y la pantalla toma seleccionado.getId().
5. La pantalla llama a sistema.listarEdiciones(idEvento).
6. Sistema devuelve List<DTEdicion>.
7. Al confirmar un alta, la pantalla envia IDs y datos del formulario.
8. Sistema busca las entidades, valida y llama al DAO para guardar.

## 3. Como son los DTO

Son clases comunes con campos, constructor y getters. No tienen @Entity, @Id, EntityManager ni referencias a entidades. Los campos final impiden reemplazar sus valores despues de construirlos. List.copyOf conserva una copia no modificable de los nombres de categorias.

DTUsuario ya existe y es un record. Lo reutilizamos para listar organizadores: sus accesores son nickname() y nombre().

DTEvento y DTEdicion se reemplazan porque los actuales no alcanzan. DTEventoMin y DTEdicionMin pueden quedar como estan: no se usan en este flujo.

DTRegistro y DTRegistroMin no se cambian. DTRegistroEdicion es nuevo porque el listado de una edicion necesita nickname del asistente, costo y patrocinado.

NivelPatrocinio se mueve a DataTypes conservando sus valores. Hay que mover el archivo y actualizar imports, no mantener dos enums. Patrocinio conserva @Enumerated(EnumType.STRING); los valores guardados en PostgreSQL no cambian.

## 4. Auxiliares y conexion con la base

Los auxiliares buscar... resuelven IDs y dan un error claro si la seleccion no existe. buscarTipoRegistroPersistido tambien comprueba que el tipo corresponde a la edicion. Los convertir... copian los campos a DTO mediante bucles y constructores.

Los DAO de ediciones, patrocinios y registros ya usan JOIN FETCH para las relaciones necesarias. EventoDAO.listarEventos() ya carga categorias. Los DTO se construyen con esas relaciones disponibles, sin pedirle a la pantalla que las cargue. Esto no obliga a mantener abierto un EntityManager durante toda la vida de una ventana.

listarEdicionesOrganizador mueve el filtrado a Sistema. Reutiliza EdicionDAO.listarEdiciones(), que carga el organizador. Por simplicidad obtiene todas las ediciones y filtra con un bucle; si crece el volumen se puede agregar una consulta filtrada.

FormatoDetalles pertenece a presentacion y arma texto desde DTO. Evita repetir los mismos detalles en tres pantallas. No consulta la base ni modifica entidades.

## 5. Orden para aplicar mas adelante

1. DTO y traslado de NivelPatrocinio con sus imports.
2. Dos busquedas adicionales en EventoDAO y tipoRegistroDAO.
3. ISistema y Sistema.
4. FormatoDetalles y las once pantallas.
5. Compilar en IntelliJ, incluyendo los .form.
6. Probar altas de categoria, evento, edicion, tipo, patrocinio y registro; todas las consultas, sus selecciones y botones Volver.
7. Probar datos inexistentes, tipos de otra edicion, listados vacios y persistencia al reiniciar.

ISistema y todos sus consumidores deben actualizarse como conjunto para compilar. El orden sirve para estudiar, no para ejecutar una migracion aplicada a medias.

AltaUsuario, AltaInstitucion, ModificarUsuario y ConsultaRegistro ya respetan la frontera y no requieren cambios. VentanaPrincipal, Main, la fabrica y JPAUtil conservan su funcionamiento. PruebaSistema imprime los listados, por lo que admite los DTO sin cambios adicionales.

## 6. Indice por archivo

- [1. logica/DataTypes/DTEvento.java](#archivo-1)
- [2. logica/DataTypes/DTEdicion.java](#archivo-2)
- [3. logica/DataTypes/DTTipoRegistro.java](#archivo-3)
- [4. logica/DataTypes/DTPatrocinio.java](#archivo-4)
- [5. logica/DataTypes/DTRegistroEdicion.java](#archivo-5)
- [6. logica/DataTypes/NivelPatrocinio.java](#archivo-6)
- [7. logica/sistema01/ISistema.java](#archivo-7)
- [8. logica/Persistencia/EventoDAO.java](#archivo-8)
- [9. logica/Persistencia/tipoRegistroDAO.java](#archivo-9)
- [10. logica/sistema01/Sistema.java](#archivo-10)
- [11. logica/Presentacion/FormatoDetalles.java](#archivo-11)
- [12. logica/Presentacion/AltaCategoriaInternalFrame.java](#archivo-12)
- [13. logica/Presentacion/AltaEvento.java](#archivo-13)
- [14. logica/Presentacion/AltaEdicionInternalFrame.java](#archivo-14)
- [15. logica/Presentacion/AltaTipoRegistroInternalFrame.java](#archivo-15)
- [16. logica/Presentacion/AltaRegistroInternalFrame.java](#archivo-16)
- [17. logica/Presentacion/AltaPatrocinio.java](#archivo-17)
- [18. logica/Presentacion/ConsultaEventoInternalFrame.java](#archivo-18)
- [19. logica/Presentacion/ConsultaEdiciones.java](#archivo-19)
- [20. logica/Presentacion/ConsultaTipoRegistroInternalFrame.java](#archivo-20)
- [21. logica/Presentacion/ConsultaPatrocinioInternalFrame.java](#archivo-21)
- [22. logica/Presentacion/ConsultaUsuarioInternalFrame.java](#archivo-22)
- [23. logica/Clases/Patrocinio.java](#archivo-23)
- [24. logica/Persistencia/DatosIniciales.java](#archivo-24)

## 7. Codigo completo propuesto, archivo por archivo

<a id="archivo-1"></a>

### 1. logica/DataTypes/DTEvento.java

Reemplaza el DTO existente. Contiene los datos del evento y los nombres de sus categorias, sin referencias a Evento o Categoria.

```java
package logica.DataTypes;

import java.util.List;

public class DTEvento {

    private final Long id;
    private final String nombre;
    private final String descripcion;
    private final String sigla;
    private final DTFecha fechaAlta;
    private final List<String> categorias;

    public DTEvento(
            Long id,
            String nombre,
            String descripcion,
            String sigla,
            DTFecha fechaAlta,
            List<String> categorias) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.sigla = sigla;
        this.fechaAlta = fechaAlta;
        this.categorias = List.copyOf(categorias);
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getSigla() {
        return sigla;
    }

    public DTFecha getFechaAlta() {
        return fechaAlta;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
```

<a id="archivo-2"></a>

### 2. logica/DataTypes/DTEdicion.java

Reemplaza el DTO existente. Sirve para elegir una edicion y mostrar su detalle. El organizador se representa con nombre y nickname.

```java
package logica.DataTypes;

public class DTEdicion {

    private final Long id;
    private final String nombre;
    private final String sigla;
    private final DTFecha fechaInicio;
    private final DTFecha fechaFin;
    private final DTFecha fechaAlta;
    private final String ciudad;
    private final String pais;
    private final String nicknameOrganizador;
    private final String nombreOrganizador;

    public DTEdicion(
            Long id,
            String nombre,
            String sigla,
            DTFecha fechaInicio,
            DTFecha fechaFin,
            DTFecha fechaAlta,
            String ciudad,
            String pais,
            String nicknameOrganizador,
            String nombreOrganizador) {
        this.id = id;
        this.nombre = nombre;
        this.sigla = sigla;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaAlta = fechaAlta;
        this.ciudad = ciudad;
        this.pais = pais;
        this.nicknameOrganizador = nicknameOrganizador;
        this.nombreOrganizador = nombreOrganizador;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSigla() {
        return sigla;
    }

    public DTFecha getFechaInicio() {
        return fechaInicio;
    }

    public DTFecha getFechaFin() {
        return fechaFin;
    }

    public DTFecha getFechaAlta() {
        return fechaAlta;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getPais() {
        return pais;
    }

    public String getNicknameOrganizador() {
        return nicknameOrganizador;
    }

    public String getNombreOrganizador() {
        return nombreOrganizador;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
```

<a id="archivo-3"></a>

### 3. logica/DataTypes/DTTipoRegistro.java

DTO nuevo. Se utiliza en los listados y consultas de tipos de registro.

```java
package logica.DataTypes;

public class DTTipoRegistro {

    private final Long id;
    private final String nombre;
    private final String descripcion;
    private final float costo;
    private final int cupo;

    public DTTipoRegistro(
            Long id,
            String nombre,
            String descripcion,
            float costo,
            int cupo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        this.cupo = cupo;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public float getCosto() {
        return costo;
    }

    public int getCupo() {
        return cupo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
```

<a id="archivo-4"></a>

### 4. logica/DataTypes/DTPatrocinio.java

DTO nuevo. La institucion, edicion y tipo se transportan como nombres, nunca como entidades.

```java
package logica.DataTypes;

public class DTPatrocinio {

    private final Long id;
    private final String codigo;
    private final DTFecha fecha;
    private final String nombreInstitucion;
    private final String nombreEdicion;
    private final String nombreTipoRegistro;
    private final NivelPatrocinio nivelPatrocinio;
    private final float montoAportado;
    private final int cantRegistros;

    public DTPatrocinio(
            Long id,
            String codigo,
            DTFecha fecha,
            String nombreInstitucion,
            String nombreEdicion,
            String nombreTipoRegistro,
            NivelPatrocinio nivelPatrocinio,
            float montoAportado,
            int cantRegistros) {
        this.id = id;
        this.codigo = codigo;
        this.fecha = fecha;
        this.nombreInstitucion = nombreInstitucion;
        this.nombreEdicion = nombreEdicion;
        this.nombreTipoRegistro = nombreTipoRegistro;
        this.nivelPatrocinio = nivelPatrocinio;
        this.montoAportado = montoAportado;
        this.cantRegistros = cantRegistros;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public DTFecha getFecha() {
        return fecha;
    }

    public String getNombreInstitucion() {
        return nombreInstitucion;
    }

    public String getNombreEdicion() {
        return nombreEdicion;
    }

    public String getNombreTipoRegistro() {
        return nombreTipoRegistro;
    }

    public NivelPatrocinio getNivelPatrocinio() {
        return nivelPatrocinio;
    }

    public float getMontoAportado() {
        return montoAportado;
    }

    public int getCantRegistros() {
        return cantRegistros;
    }

    @Override
    public String toString() {
        return codigo;
    }
}
```

<a id="archivo-5"></a>

### 5. logica/DataTypes/DTRegistroEdicion.java

DTO nuevo para los registros dentro de Consulta de Edicion. Conserva DTRegistro y DTRegistroMin, utilizados por otras pantallas.

```java
package logica.DataTypes;

public class DTRegistroEdicion {

    private final Long id;
    private final DTFecha fecha;
    private final String nicknameAsistente;
    private final String nombreTipoRegistro;
    private final double costo;
    private final boolean patrocinado;

    public DTRegistroEdicion(
            Long id,
            DTFecha fecha,
            String nicknameAsistente,
            String nombreTipoRegistro,
            double costo,
            boolean patrocinado) {
        this.id = id;
        this.fecha = fecha;
        this.nicknameAsistente = nicknameAsistente;
        this.nombreTipoRegistro = nombreTipoRegistro;
        this.costo = costo;
        this.patrocinado = patrocinado;
    }

    public Long getId() {
        return id;
    }

    public DTFecha getFecha() {
        return fecha;
    }

    public String getNicknameAsistente() {
        return nicknameAsistente;
    }

    public String getNombreTipoRegistro() {
        return nombreTipoRegistro;
    }

    public double getCosto() {
        return costo;
    }

    public boolean isPatrocinado() {
        return patrocinado;
    }
}
```

<a id="archivo-6"></a>

### 6. logica/DataTypes/NivelPatrocinio.java

Mueve el enum existente desde Clases a DataTypes, conservando sus valores. Es un tipo compartido. Al aplicar, hay que mover el archivo, no dejar dos enums.

```java
package logica.DataTypes;

public enum NivelPatrocinio {
    PLATINO, ORO, PLATA, BRONCE
}
```

<a id="archivo-7"></a>

### 7. logica/sistema01/ISistema.java

Contrato completo propuesto. Devuelve DTO o valores simples y recibe identificadores donde antes recibia entidades.

```java
package logica.sistema01;

import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTEvento;
import logica.DataTypes.DTFecha;
import logica.DataTypes.DTPatrocinio;
import logica.DataTypes.DTRegistro;
import logica.DataTypes.DTRegistroEdicion;
import logica.DataTypes.DTRegistroMin;
import logica.DataTypes.DTTipoRegistro;
import logica.DataTypes.DTUsuario;
import logica.DataTypes.EstadoAltaUsuario;
import logica.DataTypes.NivelPatrocinio;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ISistema {

    void altaInstitucion(String nombre, String descripcion, String sitioWeb);
    List<String> listarNombresInstituciones();
    EstadoAltaUsuario chequearUsuario(String nickname, String correo);
    void altaAsistente(String nickname, String nombre, String correo,
                       String apellido, LocalDate fechaNacimiento,
                       String nombreInstitucion);
    void altaOrganizador(String nickname, String nombre, String correo,
                         String descripcion, String enlace);
    void altaEvento(String nombre, String descripcion, String sigla,
                    DTFecha fechaAlta, List<String> nombresCategorias);
    void altaEdicion(Long idEvento, String nicknameOrganizador,
                     String nombre, String sigla, DTFecha fechaInicio,
                     DTFecha fechaFin, DTFecha fechaAlta, String ciudad, String pais);
    void altaTipoRegistro(Long idEdicion, String nombre,
                          String descripcion, float costo, int cupo);
    void altaPatrocinio(Long idEdicion, String nombreInstitucion,
                        Long idTipoRegistro, NivelPatrocinio nivelPatrocinio,
                        float montoAportado, int cantRegistros,
                        String codigo, DTFecha fechaAlta);
    void altaRegistro(String nicknameAsistente, Long idEdicion,
                      Long idTipoRegistro, String codigoPatrocinio);
    List<DTEvento> listarEventos();
    List<DTEdicion> listarEdiciones(Long idEvento);
    List<DTEdicion> listarEdicionesOrganizador(String nickname);
    DTEdicion mostrarDatosEdicion(Long idEdicion);
    List<DTTipoRegistro> listarTiposRegistro(Long idEdicion);
    List<DTPatrocinio> listarPatrocinios(Long idEdicion);
    List<DTRegistroEdicion> listarRegistrosEdicion(Long idEdicion);
    List<DTUsuario> listarOrganizadores();
    List<String> listarNombresCategorias();
    void altaCategoria(String nombre);
    Set<DTUsuario> listarUsuarios();
    Set<DTUsuario> listarAsistentes();
    DTDatosUsuario mostrarDatosUsuario(String nickname);
    void modificarDatosUsuario(DTDatosUsuario datos);
    List<DTRegistroMin> listarRegistrosAsistente(String nickname);
    DTRegistro mostrarDatosRegistro(String nickname, Long idRegistro);
}
```

<a id="archivo-8"></a>

### 8. logica/Persistencia/EventoDAO.java

Agrega buscarPorId. Sistema lo necesita para resolver el ID recibido. Las categorias se cargan antes de cerrar el EntityManager.

```java
package logica.Persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import logica.Clases.Categoria;
import logica.Clases.Evento;

import java.util.List;

public class EventoDAO {

    private static final EntityManagerFactory entityManagerFactory =
            JPAUtil.getEntityManagerFactory();

    public static void guardarEvento(Evento evento) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            List<Categoria> categoriasGestionadas = evento.getCategorias().stream()
                    .map(cat -> em.contains(cat) ? cat : em.merge(cat))
                    .toList();
            evento.reemplazarCategorias(categoriasGestionadas);
            em.persist(evento);

            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public static boolean existeEventoPorNombre(String nombre) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(e) FROM Evento e WHERE LOWER(e.nombre) = LOWER(:nombre)",
                            Long.class
                    )
                    .setParameter("nombre", nombre.trim())
                    .getSingleResult();
            return count != null && count > 0;
        } finally {
            em.close();
        }
    }

    public static List<Evento> listarEventos() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT DISTINCT e FROM Evento e LEFT JOIN FETCH e.categorias ORDER BY e.nombre", Evento.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public static Evento buscarPorId(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT e FROM Evento e " +
                                    "LEFT JOIN FETCH e.categorias WHERE e.id = :id",
                            Evento.class)
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }
}
```

<a id="archivo-9"></a>

### 9. logica/Persistencia/tipoRegistroDAO.java

Agrega busqueda por ID y edicion. Comprueba que el tipo pertenece a la edicion seleccionada.

```java
package logica.Persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import logica.Clases.Edicion;
import logica.Clases.TipoRegistro;

import java.util.List;

public class tipoRegistroDAO {

    private static final EntityManagerFactory entityManagerFactory =
            JPAUtil.getEntityManagerFactory();

    public static void guardarConEdicion(TipoRegistro tipoRegistro, Edicion edicion) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            em.getTransaction().begin();

            Edicion edicionPersistida;

            if (edicion.getId() == null) {
                em.persist(edicion);
                edicionPersistida = edicion;
            } else {
                edicionPersistida = em.merge(edicion);
            }

            tipoRegistro.setEdicion(edicionPersistida);
            em.persist(tipoRegistro);

            em.getTransaction().commit();

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    public static List<TipoRegistro> listarPorEdicion(Edicion edicion) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT tr FROM TipoRegistro tr " +
                                    "WHERE tr.edicion.id = :edicionId " +
                                    "ORDER BY tr.nombre",
                            TipoRegistro.class
                    )
                    .setParameter("edicionId", edicion.getId())
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public static TipoRegistro buscarPorIdEnEdicion(
            Long idTipoRegistro, Long idEdicion) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT tr FROM TipoRegistro tr " +
                                    "JOIN FETCH tr.edicion " +
                                    "WHERE tr.id = :idTipoRegistro " +
                                    "AND tr.edicion.id = :idEdicion",
                            TipoRegistro.class)
                    .setParameter("idTipoRegistro", idTipoRegistro)
                    .setParameter("idEdicion", idEdicion)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }
}
```

<a id="archivo-10"></a>

### 10. logica/sistema01/Sistema.java

Ajusta firmas y agrega busquedas y conversiones explicitas. Las altas conservan su cuerpo y validaciones, precedidos por las busquedas. El filtrado de ediciones del organizador pasa a Sistema. Los listados consultan la base persistida; ya no ocultan errores de consulta devolviendo la coleccion en memoria.

En las altas cambia la firma y se agregan las busquedas al principio. Algunas comprobaciones de null quedan redundantes porque los auxiliares ya validan; se conservan para reducir el cambio sobre lo que funciona.

```java
package logica.sistema01;

import logica.Clases.*;
import logica.DataTypes.DTEvento;
import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTTipoRegistro;
import logica.DataTypes.DTPatrocinio;
import logica.DataTypes.DTRegistroEdicion;
import logica.DataTypes.NivelPatrocinio;
import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTRegistro;
import logica.DataTypes.DTRegistroMin;
import logica.DataTypes.DTUsuario;
import logica.DataTypes.DTUsuarioAsist;
import logica.DataTypes.DTUsuarioOrg;
import logica.DataTypes.DTFecha;
import logica.DataTypes.EstadoAltaUsuario;
import logica.Persistencia.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.time.LocalDate;

public class Sistema implements ISistema {

    private static final Sistema instancia = new Sistema();

    private final Map<String, Institucion> instituciones;
    private final Map<String, Usuario> usuariosPorNickname;
    private final Map<String, Usuario> usuariosPorCorreo;
    private final List<Evento> eventos;
    private final Map<String, Categoria> categorias;
    private final UsuarioDAO usuarioDAO;
    private final InstitucionDAO institucionDAO;
    private final RegistroDAO registroDAO;
    private final PatrocinioDAO patrocinioDAO;

    private int siguienteIdRegistro;

    private Sistema() {
        instituciones = new HashMap<>();
        usuariosPorNickname = new HashMap<>();
        usuariosPorCorreo = new HashMap<>();
        eventos = new ArrayList<>();
        categorias = new HashMap<>();
        usuarioDAO = new UsuarioDAO(JPAUtil.getEntityManagerFactory());
        institucionDAO = new InstitucionDAO(JPAUtil.getEntityManagerFactory());
        registroDAO = new RegistroDAO(JPAUtil.getEntityManagerFactory());
        patrocinioDAO = new PatrocinioDAO(JPAUtil.getEntityManagerFactory());
        siguienteIdRegistro = 1;

        cargarUsuariosPersistidos();
        cargarInstitucionesPersistidas();
        DatosIniciales.cargar(JPAUtil.getEntityManagerFactory());
        cargarUsuariosPersistidos();
        cargarInstitucionesPersistidas();
    }

    public static Sistema getInstancia() {
        return instancia;
    }

    private String claveNormalizada(String valor) {
        if (valor == null) {
            return "";
        }

        return valor.trim().toLowerCase();
    }

    private String textoObligatorio(String valor, String nombreCampo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " es obligatorio.");
        }

        return valor.trim();
    }

    private boolean correoValido(String correo) {
        return correo.matches(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        );
    }

    @Override
    public EstadoAltaUsuario chequearUsuario(String nickname, String correo) {
        String nicknameNormalizado = claveNormalizada(nickname);
        String correoNormalizado = claveNormalizada(correo);

        boolean nicknameRepetido = usuariosPorNickname.containsKey(nicknameNormalizado)
                || usuarioDAO.existeNickname(nickname);

        boolean correoRepetido = usuariosPorCorreo.containsKey(correoNormalizado)
                || usuarioDAO.existeCorreo(correo);

        if (nicknameRepetido && correoRepetido) {
            return EstadoAltaUsuario.NICKNAME_Y_CORREO_REPETIDOS;
        }

        if (nicknameRepetido) {
            return EstadoAltaUsuario.NICKNAME_REPETIDO;
        }

        if (correoRepetido) {
            return EstadoAltaUsuario.CORREO_REPETIDO;
        }

        return EstadoAltaUsuario.OK;
    }

    private Institucion buscarInstitucion(String nombreInstitucion) {
        String clave = claveNormalizada(nombreInstitucion);
        Institucion institucion = instituciones.get(clave);

        if (institucion == null) {
            institucion = institucionDAO.buscarPorNombre(nombreInstitucion);
        }

        if (institucion == null) {
            throw new IllegalArgumentException(
                    "No existe una institución con el nombre: " + nombreInstitucion
            );
        }

        instituciones.put(clave, institucion);

        return institucion;
    }

    private void asignarInstitucion(Asistente asistente, String nombreInstitucion) {
        if (nombreInstitucion == null || nombreInstitucion.isBlank()) {
            return;
        }

        Institucion institucion = buscarInstitucion(nombreInstitucion);
        asistente.setInstitucion(institucion);
    }

    @Override
    public void altaAsistente(
            String nickname,
            String nombre,
            String correo,
            String apellido,
            LocalDate fechaNacimiento,
            String nombreInstitucion) {

        String nicknameLimpio = textoObligatorio(nickname, "nickname");
        String nombreLimpio = textoObligatorio(nombre, "nombre");
        String correoLimpio = textoObligatorio(correo, "correo");
        String apellidoLimpio = textoObligatorio(apellido, "apellido");

        if (!correoValido(correoLimpio)) {
            throw new IllegalArgumentException(
                    "Ingrese un correo electrónico válido."
            );
        }

        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }

        EstadoAltaUsuario estado = chequearUsuario(nicknameLimpio, correoLimpio);

        if (estado != EstadoAltaUsuario.OK) {
            throw new IllegalArgumentException("El nickname o correo ya están en uso.");
        }

        Asistente asistente = new Asistente(
                nombreLimpio,
                nicknameLimpio,
                correoLimpio,
                apellidoLimpio,
                fechaNacimiento
        );

        asignarInstitucion(asistente, nombreInstitucion);

        usuarioDAO.guardar(asistente);
        usuariosPorNickname.put(claveNormalizada(nicknameLimpio), asistente);
        usuariosPorCorreo.put(claveNormalizada(correoLimpio), asistente);
    }

    @Override
    public void altaOrganizador(
            String nickname,
            String nombre,
            String correo,
            String descripcion,
            String enlace) {

        String nicknameLimpio = textoObligatorio(nickname, "nickname");
        String nombreLimpio = textoObligatorio(nombre, "nombre");
        String correoLimpio = textoObligatorio(correo, "correo");
        String descripcionLimpia = textoObligatorio(descripcion, "descripción");

        if (!correoValido(correoLimpio)) {
            throw new IllegalArgumentException(
                    "Ingrese un correo electrónico válido."
            );
        }

        EstadoAltaUsuario estado = chequearUsuario(nicknameLimpio, correoLimpio);

        if (estado != EstadoAltaUsuario.OK) {
            throw new IllegalArgumentException("El nickname o correo ya están en uso.");
        }

        Organizador organizador = new Organizador(
                nombreLimpio,
                nicknameLimpio,
                correoLimpio,
                descripcionLimpia,
                enlace == null ? null : enlace.trim()
        );

        usuarioDAO.guardar(organizador);
        usuariosPorNickname.put(claveNormalizada(nicknameLimpio), organizador);
        usuariosPorCorreo.put(claveNormalizada(correoLimpio), organizador);
    }

    @Override
    public void altaInstitucion(String nombre, String descripcion, String sitioWeb) {
        String nombreLimpio = textoObligatorio(nombre, "nombre de la institución");
        String clave = claveNormalizada(nombreLimpio);

        if (instituciones.containsKey(clave) || institucionDAO.existeNombre(nombreLimpio)) {
            throw new IllegalArgumentException(
                    "Ya existe una institución con ese nombre."
            );
        }

        Institucion nuevaInstitucion = new Institucion(
                nombreLimpio,
                descripcion,
                sitioWeb
        );

        institucionDAO.guardar(nuevaInstitucion);
        instituciones.put(clave, nuevaInstitucion);
    }

    @Override
    public List<String> listarNombresCategorias() {
        List<String> resultado = new ArrayList<>();
        for (Categoria categoria : CategoriaDAO.listarCategorias()) {
            resultado.add(categoria.getNombre());
        }
        return resultado;
    }

    @Override
    public void altaCategoria(String nombre) {
        String nombreLimpio = textoObligatorio(nombre, "nombre de la categoría");
        String clave = claveNormalizada(nombreLimpio);

        if (CategoriaDAO.buscarCategoriaPorNombre(clave) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una categoría con ese nombre."
            );
        }

        Categoria cat = new Categoria(clave);

        CategoriaDAO.guardarCategoria(cat);

    }

    @Override
    public void altaEvento(
            String nombre,
            String descripcion,
            String sigla,
            DTFecha fechaAlta,
            List<String> nombresCategorias
    ) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del evento es obligatorio.");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción del evento es obligatoria.");
        }
        if (sigla == null || sigla.isBlank()) {
            throw new IllegalArgumentException("La sigla del evento es obligatoria.");
        }
        if (fechaAlta == null) {
            throw new IllegalArgumentException("La fecha de alta es obligatoria.");
        }
        if (nombresCategorias == null || nombresCategorias.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos una categoría.");
        }

        boolean nombreRepetido = eventos.stream()
                .anyMatch(e -> e.getNombre().equalsIgnoreCase(nombre.trim())) || EventoDAO.existeEventoPorNombre(nombre);

        if (nombreRepetido) {
            throw new IllegalArgumentException("Ya existe un evento con ese nombre.");
        }

        Evento nuevoEvento = new Evento(
                nombre.trim(),
                descripcion.trim(),
                sigla.trim(),
                fechaAlta
        );

        for (String nombreCat : nombresCategorias) {
            Categoria categoria = categorias.get(claveNormalizada(nombreCat));
            if (categoria == null) {
                categoria = CategoriaDAO.buscarPorNombre(nombreCat);
            }

            if (categoria == null) {
                throw new IllegalArgumentException("La categoría '" + nombreCat + "' no existe en el sistema.");
            }

            nuevoEvento.agregarCategoria(categoria);
        }

        EventoDAO.guardarEvento(nuevoEvento);

        eventos.add(nuevoEvento);
    }

    @Override
    public List<String> listarNombresInstituciones() {
        return institucionDAO.listarNombres();
    }

    @Override
    public Set<DTUsuario> listarUsuarios() {
        Set<DTUsuario> resultado = new HashSet<>();

        for (Usuario u : usuarioDAO.listarUsuarios()) {
            DTUsuario dt = u.getDTUsuario();
            resultado.add(dt);
        }

        return resultado;
    }

    @Override
    public Set<DTUsuario> listarAsistentes() {
        Set<DTUsuario> resultado = new HashSet<>();

        for (Usuario usuario : usuarioDAO.listarUsuarios()) {
            if (usuario instanceof Asistente) {
                resultado.add(usuario.getDTUsuario());
            }
        }

        return resultado;
    }

    @Override
    public DTDatosUsuario mostrarDatosUsuario(String nickname) {
        Usuario usuario = buscarPorNickname(nickname);

        return usuario.getDTUsuarioDetallado();
    }

    @Override
    public void modificarDatosUsuario(DTDatosUsuario datos) {
        if (datos instanceof DTUsuarioAsist datosAsistente) {
            Usuario usuario = buscarPorNickname(datosAsistente.getNickname());

            if (!(usuario instanceof Asistente asistente)) {
                throw new IllegalArgumentException("El usuario seleccionado no es un asistente.");
            }

            usuario.setNombre(datosAsistente.getNombre());
            asistente.setApellido(datosAsistente.getApellido());
            asistente.setFechaNacimiento(datosAsistente.getFechaNacimiento());

            usuarioDAO.modificar(usuario);

        } else if (datos instanceof DTUsuarioOrg datosOrganizador) {
            Usuario usuario = buscarPorNickname(datosOrganizador.getNickname());

            if (!(usuario instanceof Organizador organizador)) {
                throw new IllegalArgumentException("El usuario seleccionado no es un organizador.");
            }

            usuario.setNombre(datosOrganizador.getNombre());
            organizador.setDescripcion(datosOrganizador.getDescripcion());
            organizador.setEnlace(datosOrganizador.getEnlace());

            usuarioDAO.modificar(usuario);
        }
    }

    @Override
    public List<DTRegistroMin> listarRegistrosAsistente(String nickname) {
        buscarAsistentePorNickname(nickname);

        List<Registro> registros = registroDAO.listarPorAsistente(nickname);
        List<DTRegistroMin> resultado = new ArrayList<>();

        for (Registro registro : registros) {
            resultado.add(new DTRegistroMin(
                    registro.getId(),
                    registro.getEdicion().getId(),
                    registro.getFecha(),
                    registro.getEdicion().getNombre(),
                    registro.getTipoRegistro().getNombre()
            ));
        }

        return resultado;
    }

    @Override
    public DTRegistro mostrarDatosRegistro(String nickname, Long idRegistro) {
        buscarAsistentePorNickname(nickname);

        Registro registro = registroDAO.buscarPorIdYAsistente(idRegistro, nickname);

        if (registro == null) {
            throw new IllegalArgumentException(
                    "No existe un registro con id: " + idRegistro
            );
        }

        return new DTRegistro(
                registro.getFecha(),
                registro.getCosto(),
                registro.isPatrocinado(),
                registro.getTipoRegistro().getNombre(),
                registro.getTipoRegistro().getDescripcion(),
                registro.getEdicion().getNombre()
        );
    }

    @Override
    public void altaRegistro(
            String nicknameAsistente,
            Long idEdicion,
            Long idTipoRegistro,
            String codigoPatrocinio
    ) {
        Edicion edicion = buscarEdicionPersistida(idEdicion);
        TipoRegistro tipoRegistro = buscarTipoRegistroPersistido(idTipoRegistro, idEdicion);

        if (edicion == null) {
            throw new IllegalArgumentException("Debe seleccionar una edición.");
        }

        if (tipoRegistro == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un tipo de registro.");
        }

        Asistente asistente = buscarAsistentePorNickname(nicknameAsistente);

        if (edicion.getId() == null || tipoRegistro.getId() == null) {
            throw new IllegalArgumentException(
                    "La edición y el tipo de registro deben estar persistidos.");
        }

        if (tipoRegistro.getEdicion() != null
                && tipoRegistro.getEdicion().getId() != null
                && !tipoRegistro.getEdicion().getId().equals(edicion.getId())) {
            throw new IllegalArgumentException(
                    "El tipo de registro no pertenece a la edición seleccionada.");
        }

        if (registroDAO.existeParaAsistenteYEdicion(asistente, edicion)) {
            throw new IllegalArgumentException(
                    "El asistente ya está registrado en esta edición.");
        }

        long cantidadActual = registroDAO.cantidadPorTipoRegistro(tipoRegistro);
        if (cantidadActual >= tipoRegistro.getCupo()) {
            throw new IllegalArgumentException(
                    "No hay cupos disponibles para el tipo de registro seleccionado.");
        }

        double costo = tipoRegistro.getCosto();
        boolean patrocinado = false;
        Patrocinio patrocinio = null;

        if (codigoPatrocinio != null && !codigoPatrocinio.isBlank()) {
            patrocinio = patrocinioDAO.buscarPorCodigoEdicionYTipo(
                    codigoPatrocinio,
                    edicion.getId(),
                    tipoRegistro.getId()
            );

            if (patrocinio == null) {
                throw new IllegalArgumentException(
                        "El código no es válido para esta edición y tipo."
                );
            }

            if (asistente.getInstitucion() == null
                    || !asistente.getInstitucion().getId()
                    .equals(patrocinio.getInstitucion().getId())) {
                throw new IllegalArgumentException(
                        "El asistente no pertenece a la institución patrocinadora."
                );
            }

            if (registroDAO.cantidadPorPatrocinio(patrocinio)
                    >= patrocinio.getCantRegistros()) {
                throw new IllegalArgumentException(
                        "No quedan cupos gratuitos para este patrocinio."
                );
            }

            costo = 0;
            patrocinado = true;
        }

        LocalDate hoy = LocalDate.now();

        DTFecha fechaRegistro = new DTFecha(
                hoy.getYear(),
                hoy.getMonthValue(),
                hoy.getDayOfMonth()
        );

        Registro registro = new Registro(
                fechaRegistro,
                costo,
                patrocinado,
                asistente,
                tipoRegistro,
                edicion,
                patrocinio
        );

        registroDAO.guardar(registro);
    }

    @Override
    public List<DTEvento> listarEventos() {
        List<DTEvento> resultado = new ArrayList<>();
        for (Evento evento : EventoDAO.listarEventos()) {
            resultado.add(convertirEvento(evento));
        }
        return resultado;
    }

    @Override
    public void altaEdicion(
            Long idEvento,
            String nicknameOrganizador,
            String nombre,
            String sigla,
            DTFecha fechaInicio,
            DTFecha fechaFin,
            DTFecha fechaAlta,
            String ciudad,
            String pais
    ) {
        Evento evento = buscarEventoPersistido(idEvento);
        Organizador organizador = buscarOrganizadorPersistido(nicknameOrganizador);

        if (evento == null) {
            throw new IllegalArgumentException("Debe seleccionar un evento.");
        }
        if (organizador == null) {
            throw new IllegalArgumentException("Debe seleccionar un organizador.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la edición es obligatorio.");
        }
        if (sigla == null || sigla.isBlank()) {
            throw new IllegalArgumentException("La sigla de la edición es obligatoria.");
        }
        if (ciudad == null || ciudad.isBlank()
                || pais == null || pais.isBlank()) {
            throw new IllegalArgumentException(
                    "La ciudad y el país son obligatorios.");
        }
        if (fechaInicio == null || fechaFin == null || fechaAlta == null) {
            throw new IllegalArgumentException(
                    "Las fechas de la edición son obligatorias.");
        }
        if (compararFechas(fechaAlta, fechaInicio) > 0) {
            throw new IllegalArgumentException(
                    "La fecha de alta no puede ser posterior a la fecha de inicio.");
        }
        if (compararFechas(fechaFin, fechaInicio) < 0) {
            throw new IllegalArgumentException(
                    "La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        boolean nombreRepetido = EdicionDAO.existeEdicionPorNombre(nombre)
                || eventos.stream()
                .filter(eventoExistente -> eventoExistente.getId() == null)
                .flatMap(eventoExistente -> eventoExistente.getEdiciones().stream())
                .anyMatch(edicion -> edicion.getNombre()
                        .equalsIgnoreCase(nombre.trim()));

        if (nombreRepetido) {
            throw new IllegalArgumentException(
                    "Ya existe una edición con ese nombre.");
        }

        Edicion nuevaEdicion = new Edicion(
                nombre.trim(),
                sigla.trim(),
                fechaInicio,
                fechaFin,
                fechaAlta,
                ciudad.trim(),
                pais.trim(),
                organizador,
                evento
        );

        evento.agregarEdicion(nuevaEdicion);
        EdicionDAO.guardarEdicion(nuevaEdicion);
    }

    private int compararFechas(DTFecha primera, DTFecha segunda) {
        if (primera.getAnio() != segunda.getAnio()) {
            return Integer.compare(primera.getAnio(), segunda.getAnio());
        }
        if (primera.getMes() != segunda.getMes()) {
            return Integer.compare(primera.getMes(), segunda.getMes());
        }
        return Integer.compare(primera.getDia(), segunda.getDia());
    }

    @Override
    public List<DTEdicion> listarEdiciones(Long idEvento) {
        buscarEventoPersistido(idEvento);
        List<DTEdicion> resultado = new ArrayList<>();
        for (Edicion edicion : EdicionDAO.listarPorEvento(idEvento)) {
            resultado.add(convertirEdicion(edicion));
        }
        return resultado;
    }

    @Override
    public List<DTTipoRegistro> listarTiposRegistro(Long idEdicion) {
        Edicion edicion = buscarEdicionPersistida(idEdicion);
        List<DTTipoRegistro> resultado = new ArrayList<>();
        for (TipoRegistro tipo : tipoRegistroDAO.listarPorEdicion(edicion)) {
            resultado.add(new DTTipoRegistro(
                    tipo.getId(), tipo.getNombre(), tipo.getDescripcion(),
                    tipo.getCosto(), tipo.getCupo()));
        }
        return resultado;
    }

    @Override
    public List<DTPatrocinio> listarPatrocinios(Long idEdicion) {
        Edicion edicion = buscarEdicionPersistida(idEdicion);
        List<DTPatrocinio> resultado = new ArrayList<>();
        for (Patrocinio patrocinio : patrocinioDAO.listarPorEdicion(edicion)) {
            resultado.add(new DTPatrocinio(
                    patrocinio.getId(), patrocinio.getCodigo(),
                    patrocinio.getFecha(), patrocinio.getInstitucion().getNombre(),
                    patrocinio.getEdicion().getNombre(),
                    patrocinio.getTipoRegistro().getNombre(),
                    patrocinio.getNivelPatrocinio(),
                    patrocinio.getMontoAportado(), patrocinio.getCantRegistros()));
        }
        return resultado;
    }

    @Override
    public List<DTRegistroEdicion> listarRegistrosEdicion(Long idEdicion) {
        Edicion edicion = buscarEdicionPersistida(idEdicion);
        List<DTRegistroEdicion> resultado = new ArrayList<>();
        for (Registro registro : registroDAO.listarPorEdicion(edicion)) {
            resultado.add(new DTRegistroEdicion(
                    registro.getId(), registro.getFecha(),
                    registro.getAsistente().getNickname(),
                    registro.getTipoRegistro().getNombre(),
                    registro.getCosto(), registro.isPatrocinado()));
        }
        return resultado;
    }

    @Override
    public List<DTUsuario> listarOrganizadores() {
        List<DTUsuario> resultado = new ArrayList<>();
        for (Usuario usuario : usuarioDAO.listarUsuarios()) {
            if (usuario instanceof Organizador) {
                resultado.add(usuario.getDTUsuario());
            }
        }
        return resultado;
    }

//    private void cargarCategoriasIniciales() {
//        categorias.put("tecnología", new Categoria("Tecnología"));
//        categorias.put("educación", new Categoria("Educación"));
//        categorias.put("negocios", new Categoria("Negocios"));
//    }
//
//    private void cargarDatosIniciales() {
//        cargarCategoriasIniciales();
//
//        if (!instituciones.containsKey(claveNormalizada("UTEC"))
//                && !institucionDAO.existeNombre("UTEC")) {
//            altaInstitucion(
//                    "UTEC",
//                    "Universidad Tecnológica del Uruguay",
//                    "https://utec.edu.uy"
//            );
//        }
//
//        if (!instituciones.containsKey(claveNormalizada("ANTEL"))
//                && !institucionDAO.existeNombre("ANTEL")) {
//            altaInstitucion(
//                    "ANTEL",
//                    "Empresa nacional de telecomunicaciones",
//                    "https://www.antel.com.uy"
//            );
//        }
//
//        if (!usuariosPorNickname.containsKey(claveNormalizada("MatiB"))
//                && !usuarioDAO.existeNickname("MatiB")) {
//            altaAsistente(
//                    "MatiB",
//                    "Matias",
//                    "matiasbragiotorres@gmail.com",
//                    "Bragio",
//                    LocalDate.of(2000, 5, 10),
//                    ""
//            );
//        }
//
//        if (!usuariosPorNickname.containsKey(claveNormalizada("juanchi"))
//                && !usuarioDAO.existeNickname("juanchi")) {
//            altaOrganizador(
//                    "juanchi",
//                    "Juancito",
//                    "juancito@gmail.com",
//                    "Organizador de conferencias",
//                    "https://orgconf.com"
//            );
//        }
//
//        Organizador organizadorInicial =
//                (Organizador) buscarPorNickname("juanchi");
//
//        if (eventos.stream().noneMatch(evento ->
//                evento.getNombre().equalsIgnoreCase("Conferencia Java"))) {
//            Evento conferenciaJava = new Evento(
//                    "Conferencia Java",
//                    "Conferencia sobre Java",
//                    "JV2026",
//                    new DTFecha(2026, 1, 15)
//            );
//
//            conferenciaJava.agregarCategoria(categorias.get("tecnología"));
//
//            conferenciaJava.agregarEdicion(new Edicion(
//                    "Java 2026",
//                    "JV26",
//                    new DTFecha(2026, 1, 15),
//                    new DTFecha(2026, 11, 12),
//                    new DTFecha(2026, 1, 10),
//                    "Montevideo",
//                    "Uruguay",
//                    organizadorInicial
//            ));
//
//            eventos.add(conferenciaJava);
//        }
//
//        if (eventos.stream().noneMatch(evento ->
//                evento.getNombre().equalsIgnoreCase("Conferencia Python"))) {
//            Evento conferenciaPython = new Evento(
//                    "Conferencia Python",
//                    "Conferencia sobre Python",
//                    "PY2026",
//                    new DTFecha(2026, 2, 1)
//            );
//
//            conferenciaPython.agregarCategoria(categorias.get("tecnología"));
//
//            eventos.add(conferenciaPython);
//        }
//    }

    private void cargarUsuariosPersistidos() {
        for (Usuario usuario : usuarioDAO.listarUsuarios()) {
            usuariosPorNickname.put(claveNormalizada(usuario.getNickname()), usuario);
            usuariosPorCorreo.put(claveNormalizada(usuario.getCorreo()), usuario);
        }
    }

    private void cargarInstitucionesPersistidas() {
        for (Institucion institucion : institucionDAO.listarInstituciones()) {
            instituciones.put(claveNormalizada(institucion.getNombre()), institucion);
        }
    }

    private Usuario buscarPorNickname(String nickname) {
        String clave = claveNormalizada(nickname);
        Usuario usuario = usuariosPorNickname.get(clave);

        if (usuario == null) {
            usuario = usuarioDAO.buscarPorNickname(nickname);
        }

        if (usuario == null) {
            throw new RuntimeException(
                    "No existe un usuario con el nickname: " + nickname
            );
        }

        usuariosPorNickname.put(claveNormalizada(usuario.getNickname()), usuario);
        usuariosPorCorreo.put(claveNormalizada(usuario.getCorreo()), usuario);

        return usuario;
    }

    private Asistente buscarAsistentePorNickname(String nickname) {
        Usuario usuario = buscarPorNickname(nickname);

        if (!(usuario instanceof Asistente asistente)) {
            throw new IllegalArgumentException(
                    "El usuario seleccionado no es un asistente."
            );
        }

        return asistente;
    }

    @Override
    public void altaTipoRegistro(
            Long idEdicion,
            String nombre,
            String descripcion,
            float costo,
            int cupo) {

        Edicion edicion = buscarEdicionPersistida(idEdicion);

        if (edicion == null) {
            throw new IllegalArgumentException("Debe seleccionar una edición");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }

        if (costo < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo");
        }

        if (cupo <= 0) {
            throw new IllegalArgumentException("El cupo debe ser mayor que cero");
        }

        TipoRegistro tipoRegistro = new TipoRegistro(
                nombre.trim(),
                descripcion.trim(),
                costo,
                cupo
        );

        edicion.agregarTipoRegistro(tipoRegistro);

        tipoRegistroDAO.guardarConEdicion(tipoRegistro, edicion);
    }

    @Override
    public void altaPatrocinio(
            Long idEdicion,
            String nombreInstitucion,
            Long idTipoRegistro,
            NivelPatrocinio nivelPatrocinio,
            float montoAportado,
            int cantRegistros,
            String codigo,
            DTFecha fechaAlta
    ) {
        Edicion edicion = buscarEdicionPersistida(idEdicion);
        TipoRegistro tipoRegistro = buscarTipoRegistroPersistido(idTipoRegistro, idEdicion);

        if (edicion == null) {
            throw new IllegalArgumentException("Debe seleccionar una edición.");
        }

        if (tipoRegistro == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de registro.");
        }

        if (nivelPatrocinio == null) {
            throw new IllegalArgumentException("Debe seleccionar un nivel de patrocinio.");
        }

        String codigoLimpio = textoObligatorio(codigo, "código de patrocinio");

        if (fechaAlta == null) {
            throw new IllegalArgumentException("La fecha de alta es obligatoria.");
        }

        if (montoAportado <= 0) {
            throw new IllegalArgumentException("El aporte económico debe ser mayor a cero.");
        }

        if (cantRegistros <= 0) {
            throw new IllegalArgumentException("La cantidad de registros gratuitos debe ser mayor a cero.");
        }

        Institucion institucion = buscarInstitucion(nombreInstitucion);

        if (patrocinioDAO.existeCodigo(codigoLimpio)) {
            throw new IllegalArgumentException("Ya existe un patrocinio con ese código.");
        }

        if (patrocinioDAO.existePorInstitucionYEdicion(institucion, edicion)) {
            throw new IllegalArgumentException(
                    "Ya existe un patrocinio de esa institución para la edición seleccionada."
            );
        }

        float costoRegistrosGratuitos = tipoRegistro.getCosto() * cantRegistros;
        float maximoPermitido = montoAportado * 0.20f;

        if (costoRegistrosGratuitos > maximoPermitido) {
            throw new IllegalArgumentException(
                    "El costo de los registros gratuitos supera el 20% del aporte económico."
            );
        }

        Patrocinio patrocinio = new Patrocinio(
                codigoLimpio,
                fechaAlta,
                montoAportado,
                cantRegistros,
                nivelPatrocinio,
                institucion,
                edicion,
                tipoRegistro
        );

        patrocinioDAO.guardar(patrocinio);
    }

    @Override
    public DTEdicion mostrarDatosEdicion(Long idEdicion) {
        return convertirEdicion(buscarEdicionPersistida(idEdicion));
    }


    private Evento buscarEventoPersistido(Long idEvento) {
        if (idEvento == null) {
            throw new IllegalArgumentException("Debe seleccionar un evento.");
        }
        Evento evento = EventoDAO.buscarPorId(idEvento);
        if (evento == null) {
            throw new IllegalArgumentException("No existe el evento seleccionado.");
        }
        return evento;
    }

    private Edicion buscarEdicionPersistida(Long idEdicion) {
        if (idEdicion == null) {
            throw new IllegalArgumentException("Debe seleccionar una edicion.");
        }
        Edicion edicion = EdicionDAO.buscarPorId(idEdicion);
        if (edicion == null) {
            throw new IllegalArgumentException("No existe la edicion seleccionada.");
        }
        return edicion;
    }

    private Organizador buscarOrganizadorPersistido(String nickname) {
        Usuario usuario = buscarPorNickname(nickname);
        if (!(usuario instanceof Organizador organizador)) {
            throw new IllegalArgumentException("Debe seleccionar un organizador.");
        }
        return organizador;
    }

    private TipoRegistro buscarTipoRegistroPersistido(
            Long idTipoRegistro, Long idEdicion) {
        if (idTipoRegistro == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de registro.");
        }
        TipoRegistro tipo = tipoRegistroDAO.buscarPorIdEnEdicion(idTipoRegistro, idEdicion);
        if (tipo == null) {
            throw new IllegalArgumentException(
                    "No existe ese tipo de registro para la edicion seleccionada.");
        }
        return tipo;
    }

    private DTEvento convertirEvento(Evento evento) {
        List<String> nombresCategorias = new ArrayList<>();
        for (Categoria categoria : evento.getCategorias()) {
            nombresCategorias.add(categoria.getNombre());
        }
        return new DTEvento(
                evento.getId(), evento.getNombre(), evento.getDescripcion(),
                evento.getSigla(), evento.getFechaAlta(), nombresCategorias);
    }

    private DTEdicion convertirEdicion(Edicion edicion) {
        Organizador organizador = edicion.getOrganizador();
        return new DTEdicion(
                edicion.getId(), edicion.getNombre(), edicion.getSigla(),
                edicion.getFechaInicio(), edicion.getFechaFin(), edicion.getFechaAlta(),
                edicion.getCiudad(), edicion.getPais(),
                organizador == null ? null : organizador.getNickname(),
                organizador == null ? "Sin asignar" : organizador.getNombre());
    }

    @Override
    public List<DTEdicion> listarEdicionesOrganizador(String nickname) {
        Organizador organizador = buscarOrganizadorPersistido(nickname);
        List<DTEdicion> resultado = new ArrayList<>();
        for (Edicion edicion : EdicionDAO.listarEdiciones()) {
            if (edicion.getOrganizador() != null
                    && edicion.getOrganizador().getNickname()
                            .equalsIgnoreCase(organizador.getNickname())) {
                resultado.add(convertirEdicion(edicion));
            }
        }
        return resultado;
    }
}
```

<a id="archivo-11"></a>

### 11. logica/Presentacion/FormatoDetalles.java

Auxiliar de presentacion. Solo transforma DTO en texto y evita repetir el mismo detalle en distintas consultas.

```java
package logica.Presentacion;

import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTPatrocinio;
import logica.DataTypes.DTTipoRegistro;

public final class FormatoDetalles {

    private FormatoDetalles() {
    }

    public static String edicion(DTEdicion datos) {
        return "Nombre: " + datos.getNombre()
                + "\nSigla: " + datos.getSigla()
                + "\nFecha de inicio: " + datos.getFechaInicio()
                + "\nFecha de fin: " + datos.getFechaFin()
                + "\nFecha de alta: " + datos.getFechaAlta()
                + "\nCiudad: " + datos.getCiudad()
                + "\nPais: " + datos.getPais()
                + "\nOrganizador: " + datos.getNombreOrganizador();
    }

    public static String tipoRegistro(DTTipoRegistro datos) {
        return "Nombre: " + datos.getNombre()
                + "\nDescripcion: " + datos.getDescripcion()
                + "\nCosto: " + datos.getCosto()
                + "\nCupo: " + datos.getCupo();
    }

    public static String patrocinio(DTPatrocinio datos) {
        return "Codigo: " + datos.getCodigo()
                + "\nFecha de alta: " + datos.getFecha()
                + "\nInstitucion: " + datos.getNombreInstitucion()
                + "\nEdicion: " + datos.getNombreEdicion()
                + "\nTipo de registro: " + datos.getNombreTipoRegistro()
                + "\nNivel: " + datos.getNivelPatrocinio()
                + "\nMonto aportado: " + datos.getMontoAportado()
                + "\nRegistros gratuitos: " + datos.getCantRegistros();
    }
}
```

<a id="archivo-12"></a>

### 12. logica/Presentacion/AltaCategoriaInternalFrame.java

El arbol recibe nombres de categorias. Se mantiene la decision del grupo de categorias planas.

```java
package logica.Presentacion;

import logica.sistema01.ISistema;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.List;

public class AltaCategoriaInternalFrame extends JInternalFrame {

    private JPanel principalPanel;
    private JLabel lblTitulo;
    private JTree arbolCategorias;
    private JLabel lblNombre;
    private JTextField txtNombre;
    private JButton btnAceptar;
    private JButton btnCancelar;

    private final ISistema sistema;

    public AltaCategoriaInternalFrame(ISistema sistema) {
        super("Alta de categoría", true, true, true, true);

        if (sistema == null) {
            throw new IllegalArgumentException("El sistema no puede ser null.");
        }

        this.sistema = sistema;

        setContentPane(principalPanel);

        cargarCategorias();

        btnAceptar.addActionListener(e -> confirmarAlta());
        btnCancelar.addActionListener(e -> dispose());

        pack();
        setLocation(100, 80);
    }

    private void cargarCategorias() {
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Categorías");
        List<String> categorias = sistema.listarNombresCategorias();

        if (categorias != null) {
            for (String categoria : categorias) {
                raiz.add(new DefaultMutableTreeNode(categoria));
            }
        }

        DefaultTreeModel modelo = new DefaultTreeModel(raiz);
        arbolCategorias.setModel(modelo);

        for (int i = 0; i < arbolCategorias.getRowCount(); i++) {
            arbolCategorias.expandRow(i);
        }
    }

    private void confirmarAlta() {
        try {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "El nombre de la categoría no puede estar vacío.",
                        "Error en alta de categoría",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            sistema.altaCategoria(nombre);

            JOptionPane.showMessageDialog(
                    this,
                    "Categoría dada de alta correctamente.",
                    "Alta de categoría",
                    JOptionPane.INFORMATION_MESSAGE
            );

            txtNombre.setText("");
            cargarCategorias();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error en alta de categoría",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
```

<a id="archivo-13"></a>

### 13. logica/Presentacion/AltaEvento.java

El listado recibe String. Los nombres seleccionados se envian directamente al alta.

```java
package logica.Presentacion;

import logica.DataTypes.DTFecha;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AltaEvento extends JInternalFrame {
    private JPanel PrincipalEvento;
    private JTextField campoNombre;
    private JTextField campoDescripcion;
    private JTextField campoSiglas;
    private JSpinner campoFecha;
    private JList<String> listaCategorias;
    private JButton btnConfirmar;
    private JButton btnCancelar;
    private JLabel textoNombre;
    private JLabel textoDescripcion;
    private JLabel textoSigla;
    private JLabel textoFecha;
    private JLabel textoCategoria;
    private final ISistema sistema;

    public AltaEvento(ISistema sistema) {
        super("Alta Evento", true, true, true, true);

        if (sistema == null) {
            throw new IllegalArgumentException("El sistema no puede ser null.");
        }

        this.sistema = sistema;

        configurarFecha();
        cargarCategorias();

        btnConfirmar.addActionListener(e -> confirmarAlta());
        btnCancelar.addActionListener(e -> dispose());

        setContentPane(PrincipalEvento);
        pack();
        setLocation(100, 80);
    }

    private void configurarFecha() {
        campoFecha.setModel(new SpinnerDateModel());
        campoFecha.setEditor(
                new JSpinner.DateEditor(campoFecha, "dd/MM/yyyy")
        );
    }

    private void cargarCategorias() {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        List<String> categorias = sistema.listarNombresCategorias();

        for (String categoria : categorias) {
            modelo.addElement(categoria);
        }

        listaCategorias.setModel(modelo);
        listaCategorias.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        );
    }

    private void confirmarAlta() {
        try {
            List<String> nombresCategorias = listaCategorias.getSelectedValuesList();

            sistema.altaEvento(
                    campoNombre.getText().trim(),
                    campoDescripcion.getText().trim(),
                    campoSiglas.getText().trim(),
                    convertirAFecha(campoFecha),
                    nombresCategorias
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Evento dado de alta correctamente.",
                    "Alta de Evento",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error en el alta de evento",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private DTFecha convertirAFecha(JSpinner selector) {
        Date fechaSeleccionada = (Date) selector.getValue();

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fechaSeleccionada);

        return new DTFecha(
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH) + 1,
                calendario.get(Calendar.DAY_OF_MONTH)
        );
    }
}
```

<a id="archivo-14"></a>

### 14. logica/Presentacion/AltaEdicionInternalFrame.java

Listas de DTEvento y DTUsuario. El alta envia ID de evento y nickname de organizador.

```java
package logica.Presentacion;

import logica.DataTypes.DTEvento;
import logica.DataTypes.DTUsuario;
import logica.DataTypes.DTFecha;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AltaEdicionInternalFrame extends JInternalFrame {
    private final ISistema sistema;
    private JLabel txtEventos;
    private JList<DTEvento> listaEventos;
    private JPanel eventosListados;
    private JButton altaEdicionButton;
    private JPanel panelFormularioEdicion;
    private JList<DTUsuario> listaOrganizadores;
    private JTextField txtNombreEdicion;
    private JTextField txtSiglaEdicion;
    private JSpinner txtFechaAlta;
    private JSpinner txtFechaInicio;
    private JSpinner txtFechaFin;
    private JTextField txtCiudad;
    private JTextField txtPais;
    private DTEvento eventoSeleccionado;
    private DTUsuario organizadorSeleccionado;

    public AltaEdicionInternalFrame(ISistema sistema){
        super("Alta de edición de evento", true, true, true, true);
        this.sistema = sistema;

        crearPantallaInicial();
        setContentPane(eventosListados);

        cargarEventos();
        cargarOrganizadores();
        altaEdicionButton.addActionListener(e -> seleccionarEventoyOrganizador());

        pack();
        setLocation(100, 80);

    }

    private void cargarEventos() {
        DefaultListModel<DTEvento> modelo = new DefaultListModel<>();
        List<DTEvento> eventos = sistema.listarEventos();
        for (DTEvento e : eventos) {
            modelo.addElement(e);
        }
        listaEventos.setModel(modelo);
    }

    private void cargarOrganizadores() {
        DefaultListModel<DTUsuario> modelo = new DefaultListModel<>();
        List<DTUsuario> organizadores = sistema.listarOrganizadores();
        for (DTUsuario o : organizadores) {
            modelo.addElement(o);
        }
        listaOrganizadores.setModel(modelo);
    }

    private void seleccionarEventoyOrganizador() {
        eventoSeleccionado = listaEventos.getSelectedValue();
        organizadorSeleccionado = listaOrganizadores.getSelectedValue();
        if (eventoSeleccionado == null || organizadorSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, seleccione un evento y un organizador.",
                    "Evento no Seleccionado o Organizador no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        mostrarFormularioEdicion();
    }

    private void mostrarFormularioEdicion() {
        txtNombreEdicion = new JTextField();
        txtSiglaEdicion = new JTextField();
        txtFechaInicio = crearSelectorFecha();
        txtFechaFin = crearSelectorFecha();
        txtFechaAlta = crearSelectorFecha();
        txtCiudad = new JTextField();
        txtPais = new JTextField();

        JPanel datos = new JPanel(new GridLayout(7, 2, 10, 10));

        datos.add(new JLabel("Nombre edición:"));
        datos.add(txtNombreEdicion);

        datos.add(new JLabel("Sigla edición:"));
        datos.add(txtSiglaEdicion);

        datos.add(new JLabel("Fecha de inicio:"));
        datos.add(txtFechaInicio);

        datos.add(new JLabel("Fecha fin:"));
        datos.add(txtFechaFin);

        datos.add(new JLabel("Fecha de alta:"));
        datos.add(txtFechaAlta);

        datos.add(new JLabel("Ciudad:"));
        datos.add(txtCiudad);

        datos.add(new JLabel("País:"));
        datos.add(txtPais);

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> altaEdicion());
        btnCancelar.addActionListener(e -> ocultarFormularioEdicion());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnAceptar);
        botones.add(btnCancelar);

        panelFormularioEdicion.removeAll();
        panelFormularioEdicion.setLayout(new BorderLayout(10, 10));
        panelFormularioEdicion.add(
                new JLabel("Alta de edicion para el evento: " + eventoSeleccionado.getNombre() + " - Organizador: " + organizadorSeleccionado.nombre()),
                BorderLayout.NORTH
        );
        panelFormularioEdicion.add(datos, BorderLayout.CENTER);
        panelFormularioEdicion.add(botones, BorderLayout.SOUTH);
        panelFormularioEdicion.setVisible(true);
        altaEdicionButton.setVisible(false);

        panelFormularioEdicion.revalidate();
        panelFormularioEdicion.repaint();
        pack();
    }

    private void ocultarFormularioEdicion() {
        if (panelFormularioEdicion != null) {
            panelFormularioEdicion.removeAll();
            panelFormularioEdicion.setVisible(false);
            altaEdicionButton.setVisible(true);
            panelFormularioEdicion.revalidate();
            panelFormularioEdicion.repaint();
            pack();
        }
    }

    private void altaEdicion() {
        if (eventoSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un evento antes de crear la edición.",
                    "Evento no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (organizadorSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un Organizador antes de crear la edicion del evento",
                    "Organizador no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String nombreEdicion = txtNombreEdicion.getText().trim();
        String siglaEdicion = txtSiglaEdicion.getText().trim();
        String ciudad = txtCiudad.getText().trim();
        String pais = txtPais.getText().trim();

        DTFecha fechaInicio = convertirAFecha(txtFechaInicio);
        DTFecha fechaFin = convertirAFecha(txtFechaFin);
        DTFecha fechaAlta = convertirAFecha(txtFechaAlta);
        if (nombreEdicion.isBlank() || siglaEdicion.isBlank()
                || fechaInicio == null || fechaFin == null || fechaAlta == null
                || ciudad.isBlank() || pais.isBlank()) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (esAnterior(fechaFin, fechaInicio)) {
            JOptionPane.showMessageDialog(this, "La fecha de fin no puede ser anterior a la fecha de inicio.",
                    "Fechas inválidas", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (esAnterior(fechaInicio, fechaAlta)) {
            JOptionPane.showMessageDialog(this, "La fecha de alta no puede ser posterior a la fecha de inicio.",
                    "Fechas inválidas", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            sistema.altaEdicion(
                    eventoSeleccionado.getId(), organizadorSeleccionado.nickname(), nombreEdicion,
                    siglaEdicion, fechaInicio, fechaFin, fechaAlta, ciudad, pais);
            JOptionPane.showMessageDialog(this, "Edición dada de alta correctamente.");
            dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error en el alta de edición",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private JSpinner crearSelectorFecha() {
        JSpinner selector = new JSpinner(new SpinnerDateModel());
        selector.setEditor(new JSpinner.DateEditor(selector, "dd/MM/yyyy"));
        return selector;
    }

    private DTFecha convertirAFecha(JSpinner selector) {
        Date fechaSeleccionada = (Date) selector.getValue();
        if (fechaSeleccionada == null) {
            return null;
        }
        Calendar fecha = Calendar.getInstance();
        fecha.setTime(fechaSeleccionada);
        return new DTFecha(
                fecha.get(Calendar.YEAR),
                fecha.get(Calendar.MONTH) + 1,
                fecha.get(Calendar.DAY_OF_MONTH));
    }

    private boolean esAnterior(DTFecha primera, DTFecha segunda) {
        if (primera.getAnio() != segunda.getAnio()) {
            return primera.getAnio() < segunda.getAnio();
        }
        if (primera.getMes() != segunda.getMes()) {
            return primera.getMes() < segunda.getMes();
        }
        return primera.getDia() < segunda.getDia();
    }

    private void crearPantallaInicial() {
        eventosListados = new JPanel(new BorderLayout(10, 10));

        txtEventos = new JLabel(
                "Seleccione un evento y un organizador.",
                SwingConstants.CENTER
        );

        listaEventos = new JList<>();
        listaOrganizadores = new JList<>();
        altaEdicionButton = new JButton("Alta edición");

        panelFormularioEdicion = new JPanel();
        panelFormularioEdicion.setVisible(false);

        JPanel panelListas = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel panelEventos = new JPanel(new BorderLayout());
        panelEventos.add(new JLabel("Eventos disponibles"), BorderLayout.NORTH);
        panelEventos.add(new JScrollPane(listaEventos), BorderLayout.CENTER);

        JPanel panelOrganizadores = new JPanel(new BorderLayout());
        panelOrganizadores.add(
                new JLabel("Organizadores"),
                BorderLayout.NORTH
        );
        panelOrganizadores.add(
                new JScrollPane(listaOrganizadores),
                BorderLayout.CENTER
        );

        panelListas.add(panelEventos);
        panelListas.add(panelOrganizadores);

        JPanel panelSur = new JPanel(new BorderLayout(10, 10));
        panelSur.add(altaEdicionButton, BorderLayout.NORTH);
        panelSur.add(panelFormularioEdicion, BorderLayout.CENTER);

        eventosListados.add(txtEventos, BorderLayout.NORTH);
        eventosListados.add(panelListas, BorderLayout.CENTER);
        eventosListados.add(panelSur, BorderLayout.SOUTH);
    }

}
```

<a id="archivo-15"></a>

### 15. logica/Presentacion/AltaTipoRegistroInternalFrame.java

Los dialogos seleccionan DTO. Guardar envia el ID de la edicion.

```java
package logica.Presentacion;

import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTEvento;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.util.List;

import java.awt.*;

public class AltaTipoRegistroInternalFrame extends JInternalFrame {
    private JPanel principalPanel;
    private JLabel lblEdicion;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtCosto;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JTextField txtCupo;
    private final ISistema sistema;
    private DTEdicion edicionSeleccionada;

    public AltaTipoRegistroInternalFrame(ISistema sistema) {
        super("Alta de tipos de registro", true, true, true, true);

        if (sistema == null) {
            throw new IllegalArgumentException("El sistema no puede ser null");
        }

        this.sistema = sistema;

        crearFormulario();
        setContentPane(principalPanel);

        edicionSeleccionada = seleccionarEdicion();
        if (edicionSeleccionada == null) {
            // El llamador agrega y hace visible el InternalFrame después
            // de construirlo; por eso se cierra en el siguiente evento.
            SwingUtilities.invokeLater(this::dispose);
            return;
        }

        lblEdicion.setText(
                "Tipos de registro para la edición: "
                        + edicionSeleccionada.getNombre());

        btnGuardar.addActionListener(e -> guardarTipoRegistro());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        pack();
        setLocation(140, 100);
    }

    private void crearFormulario() {
        principalPanel = new JPanel(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(6, 2, 10, 10));

        lblEdicion = new JLabel("Tipos de registro");
        txtNombre = new JTextField(20);
        txtDescripcion = new JTextField(20);
        txtCosto = new JTextField(20);
        txtCupo = new JTextField(20);

        btnGuardar = new JButton("Guardar");
        btnLimpiar = new JButton("Limpiar");

        formulario.add(lblEdicion);
        formulario.add(new JLabel());

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("Descripción:"));
        formulario.add(txtDescripcion);

        formulario.add(new JLabel("Costo:"));
        formulario.add(txtCosto);

        formulario.add(new JLabel("Cupo:"));
        formulario.add(txtCupo);

        formulario.add(btnGuardar);
        formulario.add(btnLimpiar);

        principalPanel.add(formulario, BorderLayout.CENTER);
    }

    private DTEdicion seleccionarEdicion() {
        List<DTEvento> eventos = sistema.listarEventos();
        if (eventos.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay eventos registrados.",
                    "Alta de tipo de registro",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return null;
        }

        DTEvento evento = (DTEvento) JOptionPane.showInputDialog(
                this,
                "Seleccione un evento:",
                "Evento",
                JOptionPane.QUESTION_MESSAGE,
                null,
                eventos.toArray(),
                eventos.get(0)
        );

        if (evento == null) {
            return null;
        }

        List<DTEdicion> ediciones = sistema.listarEdiciones(evento.getId());
        if (ediciones.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "El evento seleccionado no tiene ediciones.",
                    "Alta de tipo de registro",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return null;
        }

        return (DTEdicion) JOptionPane.showInputDialog(
                this,
                "Seleccione una edición:",
                "Edición",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ediciones.toArray(),
                ediciones.get(0)
        );
    }



    private void guardarTipoRegistro() {
        try {
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();

            float costo = Float.parseFloat(txtCosto.getText().trim());
            int cupo = Integer.parseInt(txtCupo.getText().trim());

            sistema.altaTipoRegistro(
                    edicionSeleccionada.getId(),
                    nombre,
                    descripcion,
                    costo,
                    cupo
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Tipo de registro guardado correctamente. Puede cargar otro.",
                    "Alta de tipo de registro",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarCampos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "El costo y el cupo deben ser valores numéricos válidos.",
                    "Datos inválidos",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Datos inválidos",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtCosto.setText("");
        txtCupo.setText("");
        txtNombre.requestFocusInWindow();
    }
}
```

<a id="archivo-16"></a>

### 16. logica/Presentacion/AltaRegistroInternalFrame.java

Listas de DTO de evento, edicion y tipo. El asistente sigue con DTUsuario; confirmar envia nickname e IDs.

```java
package logica.Presentacion;

import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTEvento;
import logica.DataTypes.DTTipoRegistro;
import logica.DataTypes.DTUsuario;
import logica.sistema01.ISistema;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.List;

public class AltaRegistroInternalFrame extends JInternalFrame {

    private final ISistema sistema;
    private JPanel panelPrincipal;
    private JList<DTEvento> listaEventos;
    private JList<DTEdicion> listaEdiciones;
    private JList<DTTipoRegistro> listaTiposRegistro;
    private JTextField txtCodigoPatrocinio;
    private JList<DTUsuario> listaAsistentes;
    private JButton btnRegistrar;
    private JButton btnCancelar;

    public AltaRegistroInternalFrame(ISistema sistema) {
        super("Registro a edición", true, true, true, true);

        if (sistema == null) {
            throw new IllegalArgumentException("El sistema no puede ser null.");
        }

        this.sistema = sistema;
        configurarInterfaz();
        cargarEventos();

        setContentPane(panelPrincipal);
        pack();
        setSize(850, 500);
        setLocation(25, 25);
    }

    private void configurarInterfaz() {
        listaEventos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarEdiciones();
            }
        });

        listaEdiciones.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarTiposRegistro();
            }
        });

        btnRegistrar.addActionListener(e -> registrarAsistente());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void cargarEventos() {
        DefaultListModel<DTEvento> modelo = new DefaultListModel<>();
        List<DTEvento> eventos = sistema.listarEventos();

        for (DTEvento evento : eventos) {
            modelo.addElement(evento);
        }

        listaEventos.setModel(modelo);
    }

    private void cargarEdiciones() {
        DefaultListModel<DTEdicion> modelo = new DefaultListModel<>();
        DTEvento evento = listaEventos.getSelectedValue();

        if (evento != null) {
            for (DTEdicion edicion : sistema.listarEdiciones(evento.getId())) {
                modelo.addElement(edicion);
            }
        }

        listaEdiciones.setModel(modelo);
        listaTiposRegistro.setModel(new DefaultListModel<>());
    }

    private void cargarTiposRegistro() {
        DefaultListModel<DTTipoRegistro> modelo = new DefaultListModel<>();
        DTEdicion edicion = listaEdiciones.getSelectedValue();

        if (edicion != null) {
            for (DTTipoRegistro tipoRegistro : sistema.listarTiposRegistro(edicion.getId())) {
                modelo.addElement(tipoRegistro);
            }
        }

        listaTiposRegistro.setModel(modelo);
        cargarAsistentes();
    }

    private void cargarAsistentes() {
        DefaultListModel<DTUsuario> modelo = new DefaultListModel<>();

        for (DTUsuario asistente : sistema.listarAsistentes()) {
            modelo.addElement(asistente);
        }

        listaAsistentes.setModel(modelo);
    }

    private void registrarAsistente() {
        DTEvento evento = listaEventos.getSelectedValue();
        DTEdicion edicion = listaEdiciones.getSelectedValue();
        DTTipoRegistro tipoRegistro = listaTiposRegistro.getSelectedValue();
        DTUsuario asistente = listaAsistentes.getSelectedValue();

        if (evento == null || edicion == null
                || tipoRegistro == null || asistente == null) {
            mostrarAdvertencia(
                    "Debe seleccionar evento, edición, tipo de registro y asistente.");
            return;
        }

        try {
            sistema.altaRegistro(
                    asistente.nickname(),
                    edicion.getId(),
                    tipoRegistro.getId(),
                    txtCodigoPatrocinio.getText()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "El asistente fue registrado correctamente.",
                    "Registro a edición",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "No se pudo realizar el registro",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Registro a edición",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
```

<a id="archivo-17"></a>

### 17. logica/Presentacion/AltaPatrocinio.java

Los listados usan DTO y el combo usa el enum compartido. Confirmar envia IDs; las instituciones siguen siendo nombres.

```java
package logica.Presentacion;

import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTEvento;
import logica.DataTypes.NivelPatrocinio;
import logica.DataTypes.DTTipoRegistro;
import logica.DataTypes.DTFecha;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.awt.Component;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AltaPatrocinio extends JInternalFrame {
    private final ISistema sistema;
    private JPanel principalJpanel;
    private JPanel eventosDisponibles;
    private JList<DTEvento> listaEventos;
    private JLabel TituloEventos;
    private JButton btnConfirmarEvento;
    private JList<DTEdicion> listaEdiciones;
    private JLabel TituloEdiciones;
    private JPanel edicionesDisponibles;
    private JButton btnConfirmarEdicion;
    private JPanel formularioPatrocinio;
    private JList<DTTipoRegistro> listaTiposRegistro;
    private JComboBox<String> comboInstituciones;
    private JComboBox<NivelPatrocinio> comboNivelPatrocinio;
    private JTextField txtMontoAportado;
    private JTextField txtCantRegistros;
    private JTextField txtCodigo;
    private JSpinner spinnerFechaAlta;
    private JButton btnGuardarPatrocinio;
    private JButton btnCancelarPatrocinio;
    private DTEvento eventoSeleccionado;
    private DTEdicion edicionSeleccionada;

    public AltaPatrocinio(ISistema sistema) {
        super("Alta de patrocinio", true, true, true, true);

        if (sistema == null) {
            throw new IllegalArgumentException("El sistema no puede ser null.");
        }

        this.sistema = sistema;

        configurarPantallaInicial();
        configurarRenderizadores();
        configurarFecha();
        configurarNivelesPatrocinio();
        cargarEventos();

        btnConfirmarEvento.addActionListener(e -> confirmarEvento());
        btnConfirmarEdicion.addActionListener(e -> confirmarEdicion());
        btnGuardarPatrocinio.addActionListener(e -> guardarPatrocinio());
        btnCancelarPatrocinio.addActionListener(e -> dispose());

        setContentPane(eventosDisponibles);
        pack();
        setLocation(100, 80);
    }

    private void configurarPantallaInicial() {
        edicionesDisponibles.setVisible(false);
        formularioPatrocinio.setVisible(false);
        listaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEdiciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTiposRegistro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void configurarRenderizadores() {
        listaEventos.setCellRenderer((lista, evento, indice, seleccionado, foco) -> {
            JLabel etiqueta = etiquetaLista(lista, evento, indice, seleccionado, foco);

            if (evento != null) {
                etiqueta.setText("ID " + evento.getId() + " - "
                        + evento.getNombre() + " - "
                        + evento.getDescripcion());
            }

            return etiqueta;
        });

        listaEdiciones.setCellRenderer((lista, edicion, indice, seleccionado, foco) -> {
            JLabel etiqueta = etiquetaLista(lista, edicion, indice, seleccionado, foco);

            if (edicion != null) {
                etiqueta.setText("ID " + edicion.getId() + " - "
                        + edicion.getNombre());
            }

            return etiqueta;
        });

        listaTiposRegistro.setCellRenderer((lista, tipoRegistro, indice, seleccionado, foco) -> {
            JLabel etiqueta = etiquetaLista(lista, tipoRegistro, indice, seleccionado, foco);

            if (tipoRegistro != null) {
                etiqueta.setText("ID " + tipoRegistro.getId() + " - "
                        + tipoRegistro.getNombre() + " - "
                        + tipoRegistro.getDescripcion() + " - $"
                        + tipoRegistro.getCosto());
            }

            return etiqueta;
        });
    }

    private JLabel etiquetaLista(
            JList<?> lista,
            Object valor,
            int indice,
            boolean seleccionado,
            boolean foco
    ) {
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        Component componente = renderer.getListCellRendererComponent(
                lista,
                valor,
                indice,
                seleccionado,
                foco
        );

        return (JLabel) componente;
    }

    private void configurarFecha() {
        spinnerFechaAlta.setModel(new SpinnerDateModel());
        spinnerFechaAlta.setEditor(
                new JSpinner.DateEditor(spinnerFechaAlta, "dd/MM/yyyy")
        );
    }

    private void configurarNivelesPatrocinio() {
        comboNivelPatrocinio.setModel(
                new DefaultComboBoxModel<>(NivelPatrocinio.values())
        );
    }

    private void cargarEventos() {
        DefaultListModel<DTEvento> modelo = new DefaultListModel<>();
        List<DTEvento> eventos = sistema.listarEventos();

        for (DTEvento evento : eventos) {
            modelo.addElement(evento);
        }

        listaEventos.setModel(modelo);
    }

    private void confirmarEvento() {
        eventoSeleccionado = listaEventos.getSelectedValue();

        if (eventoSeleccionado == null) {
            mostrarAdvertencia("Debe seleccionar un evento.");
            return;
        }

        cargarEdiciones();
        cambiarPanel(edicionesDisponibles);
    }

    private void cargarEdiciones() {
        DefaultListModel<DTEdicion> modelo = new DefaultListModel<>();
        List<DTEdicion> ediciones = sistema.listarEdiciones(eventoSeleccionado.getId());

        for (DTEdicion edicion : ediciones) {
            modelo.addElement(edicion);
        }

        listaEdiciones.setModel(modelo);
    }

    private void confirmarEdicion() {
        edicionSeleccionada = listaEdiciones.getSelectedValue();

        if (edicionSeleccionada == null) {
            mostrarAdvertencia("Debe seleccionar una edición.");
            return;
        }

        cargarTiposRegistro();
        cargarInstituciones();
        cambiarPanel(formularioPatrocinio);
    }

    private void cargarTiposRegistro() {
        DefaultListModel<DTTipoRegistro> modelo = new DefaultListModel<>();
        List<DTTipoRegistro> tiposRegistro = sistema.listarTiposRegistro(edicionSeleccionada.getId());

        for (DTTipoRegistro tipoRegistro : tiposRegistro) {
            modelo.addElement(tipoRegistro);
        }

        listaTiposRegistro.setModel(modelo);
    }

    private void cargarInstituciones() {
        comboInstituciones.removeAllItems();

        for (String nombreInstitucion : sistema.listarNombresInstituciones()) {
            comboInstituciones.addItem(nombreInstitucion);
        }
    }

    private void guardarPatrocinio() {
        try {
            DTTipoRegistro tipoRegistro = listaTiposRegistro.getSelectedValue();
            String nombreInstitucion = (String) comboInstituciones.getSelectedItem();
            NivelPatrocinio nivelPatrocinio = (NivelPatrocinio) comboNivelPatrocinio.getSelectedItem();

            if (tipoRegistro == null) {
                mostrarAdvertencia("Debe seleccionar un tipo de registro.");
                return;
            }

            if (nombreInstitucion == null || nombreInstitucion.isBlank()) {
                mostrarAdvertencia("Debe seleccionar una institución.");
                return;
            }

            sistema.altaPatrocinio(
                    edicionSeleccionada.getId(),
                    nombreInstitucion,
                    tipoRegistro.getId(),
                    nivelPatrocinio,
                    leerMontoAportado(),
                    leerCantidadRegistros(),
                    txtCodigo.getText().trim(),
                    convertirAFecha(spinnerFechaAlta)
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Patrocinio dado de alta correctamente.",
                    "Alta de Patrocinio",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (NumberFormatException e) {
            mostrarError("El monto aportado y la cantidad de registros deben ser números válidos.");
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    private float leerMontoAportado() {
        return Float.parseFloat(txtMontoAportado.getText().trim());
    }

    private int leerCantidadRegistros() {
        return Integer.parseInt(txtCantRegistros.getText().trim());
    }

    private DTFecha convertirAFecha(JSpinner selector) {
        Date fechaSeleccionada = (Date) selector.getValue();

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fechaSeleccionada);

        return new DTFecha(
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH) + 1,
                calendario.get(Calendar.DAY_OF_MONTH)
        );
    }

    private void cambiarPanel(JPanel panel) {
        panel.setVisible(true);
        setContentPane(panel);
        revalidate();
        repaint();
        pack();
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Alta de Patrocinio",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error en el alta de patrocinio",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
```

<a id="archivo-18"></a>

### 18. logica/Presentacion/ConsultaEventoInternalFrame.java

Lee el detalle y las categorias del DTO. El detalle de edicion se prepara en presentacion.

```java
package logica.Presentacion;

import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTEvento;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.awt.*;

public class ConsultaEventoInternalFrame extends JInternalFrame {

    private final ISistema sistema;

    private final CardLayout cardLayout;
    private final JPanel panelPrincipal;

    private final JList<DTEvento> listaEventos;
    private final JTextArea txtDatosEvento;
    private final JList<String> listaCategorias;
    private final JList<DTEdicion> listaEdiciones;

    private DTEvento eventoSeleccionado;

    public ConsultaEventoInternalFrame(ISistema sistema) {
        super("Consulta de evento", true, true, true, true);

        this.sistema = sistema;

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        listaEventos = new JList<>();
        txtDatosEvento = new JTextArea(6, 30);
        listaCategorias = new JList<>();
        listaEdiciones = new JList<>();

        panelPrincipal.add(crearPanelSeleccion(), "SELECCION");
        panelPrincipal.add(crearPanelDetalle(), "DETALLE");

        setContentPane(panelPrincipal);

        cargarEventos();

        pack();
        setLocation(100, 80);
    }

    private JPanel crearPanelSeleccion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "Seleccione un evento para consultar",
                SwingConstants.CENTER
        );

        JButton btnConsultar = new JButton("Consultar evento");
        JButton btnCerrar = new JButton("Cerrar");

        JPanel botones = new JPanel();
        botones.add(btnConsultar);
        botones.add(btnCerrar);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(listaEventos), BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        btnConsultar.addActionListener(e -> consultarEvento());
        btnCerrar.addActionListener(e -> dispose());

        return panel;
    }

    private JPanel crearPanelDetalle() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        txtDatosEvento.setEditable(false);
        txtDatosEvento.setLineWrap(true);
        txtDatosEvento.setWrapStyleWord(true);

        JPanel panelDatos = new JPanel(new BorderLayout());
        panelDatos.setBorder(
                BorderFactory.createTitledBorder("Datos del evento")
        );
        panelDatos.add(new JScrollPane(txtDatosEvento), BorderLayout.CENTER);

        JPanel panelCategorias = new JPanel(new BorderLayout());
        panelCategorias.setBorder(
                BorderFactory.createTitledBorder("Categorías")
        );
        panelCategorias.add(new JScrollPane(listaCategorias), BorderLayout.CENTER);

        JPanel panelEdiciones = new JPanel(new BorderLayout());
        panelEdiciones.setBorder(
                BorderFactory.createTitledBorder("Ediciones")
        );
        panelEdiciones.add(new JScrollPane(listaEdiciones), BorderLayout.CENTER);

        JPanel panelListas = new JPanel(new GridLayout(1, 2, 10, 10));
        panelListas.add(panelCategorias);
        panelListas.add(panelEdiciones);

        JButton btnVerEdicion = new JButton("Ver detalle de edición");
        JButton btnVolver = new JButton("Volver");

        JPanel botones = new JPanel();
        botones.add(btnVerEdicion);
        botones.add(btnVolver);

        panel.add(panelDatos, BorderLayout.NORTH);
        panel.add(panelListas, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        btnVerEdicion.addActionListener(e -> mostrarDetalleEdicion());
        btnVolver.addActionListener(e ->
                cardLayout.show(panelPrincipal, "SELECCION")
        );

        return panel;
    }

    private void cargarEventos() {
        DefaultListModel<DTEvento> modelo = new DefaultListModel<>();

        for (DTEvento evento : sistema.listarEventos()) {
            modelo.addElement(evento);
        }

        listaEventos.setModel(modelo);
        listaEventos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
    }

    private void consultarEvento() {
        eventoSeleccionado = listaEventos.getSelectedValue();

        if (eventoSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un evento.",
                    "Consulta de evento",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        cargarDatosEvento();
        cargarCategorias();
        cargarEdiciones();

        cardLayout.show(panelPrincipal, "DETALLE");
    }

    private void cargarDatosEvento() {
        txtDatosEvento.setText(
                "Nombre: " + eventoSeleccionado.getNombre() + "\n" +
                        "Sigla: " + eventoSeleccionado.getSigla() + "\n" +
                        "Descripción: " + eventoSeleccionado.getDescripcion() + "\n" +
                        "Fecha de alta: " + eventoSeleccionado.getFechaAlta()
        );
    }

    private void cargarCategorias() {
        DefaultListModel<String> modelo = new DefaultListModel<>();

        for (String categoria : eventoSeleccionado.getCategorias()) {
            modelo.addElement(categoria);
        }

        listaCategorias.setModel(modelo);
    }

    private void cargarEdiciones() {
        DefaultListModel<DTEdicion> modelo = new DefaultListModel<>();

        for (DTEdicion edicion : sistema.listarEdiciones(eventoSeleccionado.getId())) {
            modelo.addElement(edicion);
        }

        listaEdiciones.setModel(modelo);
        listaEdiciones.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
    }

    private void mostrarDetalleEdicion() {
        DTEdicion edicionSeleccionada = listaEdiciones.getSelectedValue();

        if (edicionSeleccionada == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar una edición.",
                    "Consulta de evento",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                FormatoDetalles.edicion(edicionSeleccionada),
                "Detalle de edición",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
```

<a id="archivo-19"></a>

### 19. logica/Presentacion/ConsultaEdiciones.java

Usa DTO para las listas. Ya no recorre entidades para armar el detalle.

```java
package logica.Presentacion;

import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTEvento;
import logica.DataTypes.DTPatrocinio;
import logica.DataTypes.DTRegistroEdicion;
import logica.DataTypes.DTTipoRegistro;
import logica.sistema01.ISistema;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.List;

public class ConsultaEdiciones extends JInternalFrame {

    private final ISistema sistema;
    private final CardLayout cardLayout = new CardLayout();

    private JPanel panelPrincipal;
    private JPanel panelEventos;
    private JPanel panelEdiciones;
    private JPanel panelDetalleEdicion;
    private JPanel panelDetalleSeleccionado;

    private JList<DTEvento> listaEventos;
    private JList<DTEdicion> listaEdiciones;
    private JList<DTTipoRegistro> listaTiposRegistro;
    private JList<DTPatrocinio> listaPatrocinios;

    private JLabel lblEventoSeleccionado;
    private JLabel lblEdicionSeleccionada;
    private JLabel lblDetalleSeleccionado;
    private JTextArea txtDetalleEdicion;
    private JTextArea txtRegistros;
    private JTextArea txtDetalleSeleccionado;

    private JButton btnSeleccionarEvento;
    private JButton btnCerrarEventos;
    private JButton btnSeleccionarEdicion;
    private JButton btnVolverEventos;
    private JButton btnDetalleTipoRegistro;
    private JButton btnDetallePatrocinio;
    private JButton btnVolverEdiciones;
    private JButton btnVolverDetalleEdicion;
    private JButton btnCerrarDetalleSeleccionado;

    private DTEvento eventoSeleccionado;
    private DTEdicion edicionSeleccionada;

    public ConsultaEdiciones(ISistema sistema) {
        super("Consulta Ediciones", true, true, true, true);
        this.sistema = sistema;

        configurarInterfaz();
        configurarRenderizadores();
        cargarEventos();

        setContentPane(panelPrincipal);
        pack();
        setLocation(100, 80);
    }

    private void configurarInterfaz() {
        panelPrincipal.removeAll();
        panelPrincipal.setLayout(cardLayout);
        panelPrincipal.add(panelEventos, "EVENTOS");
        panelPrincipal.add(panelEdiciones, "EDICIONES");
        panelPrincipal.add(panelDetalleEdicion, "DETALLE_EDICION");
        panelPrincipal.add(panelDetalleSeleccionado, "DETALLE_SELECCIONADO");

        txtDetalleEdicion.setEditable(false);
        txtRegistros.setEditable(false);
        txtDetalleSeleccionado.setEditable(false);

        listaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEdiciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTiposRegistro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPatrocinios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        btnSeleccionarEvento.addActionListener(e -> seleccionarEvento());
        btnCerrarEventos.addActionListener(e -> dispose());
        btnSeleccionarEdicion.addActionListener(e -> seleccionarEdicion());
        btnVolverEventos.addActionListener(e -> cardLayout.show(panelPrincipal, "EVENTOS"));
        btnDetalleTipoRegistro.addActionListener(e -> mostrarDetalleTipoRegistro());
        btnDetallePatrocinio.addActionListener(e -> mostrarDetallePatrocinio());
        btnVolverEdiciones.addActionListener(e -> cardLayout.show(panelPrincipal, "EDICIONES"));
        btnVolverDetalleEdicion.addActionListener(e -> cardLayout.show(panelPrincipal, "DETALLE_EDICION"));
        btnCerrarDetalleSeleccionado.addActionListener(e -> dispose());
    }

    private void configurarRenderizadores() {
        listaEventos.setCellRenderer((lista, evento, indice, seleccionado, foco) -> {
            JLabel etiqueta = etiquetaLista(lista, evento, indice, seleccionado, foco);

            if (evento != null) {
                etiqueta.setText("ID " + evento.getId() + " - "
                        + evento.getNombre() + " - "
                        + evento.getDescripcion());
            }

            return etiqueta;
        });

        listaEdiciones.setCellRenderer((lista, edicion, indice, seleccionado, foco) -> {
            JLabel etiqueta = etiquetaLista(lista, edicion, indice, seleccionado, foco);

            if (edicion != null) {
                etiqueta.setText("ID " + edicion.getId() + " - "
                        + edicion.getNombre());
            }

            return etiqueta;
        });

        listaTiposRegistro.setCellRenderer((lista, tipo, indice, seleccionado, foco) -> {
            JLabel etiqueta = etiquetaLista(lista, tipo, indice, seleccionado, foco);

            if (tipo != null) {
                etiqueta.setText("ID " + tipo.getId() + " - "
                        + tipo.getNombre() + " - $"
                        + tipo.getCosto());
            }

            return etiqueta;
        });

        listaPatrocinios.setCellRenderer((lista, patrocinio, indice, seleccionado, foco) -> {
            JLabel etiqueta = etiquetaLista(lista, patrocinio, indice, seleccionado, foco);

            if (patrocinio != null) {
                etiqueta.setText(
                        patrocinio.getCodigo() + " - "
                                + patrocinio.getNombreInstitucion() + " - "
                                + patrocinio.getNivelPatrocinio()
                );
            }

            return etiqueta;
        });
    }

    private JLabel etiquetaLista(
            JList<?> lista,
            Object valor,
            int indice,
            boolean seleccionado,
            boolean foco
    ) {
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        Component componente = renderer.getListCellRendererComponent(
                lista,
                valor,
                indice,
                seleccionado,
                foco
        );

        return (JLabel) componente;
    }

    private void cargarEventos() {
        DefaultListModel<DTEvento> modelo = new DefaultListModel<>();

        for (DTEvento evento : sistema.listarEventos()) {
            modelo.addElement(evento);
        }

        listaEventos.setModel(modelo);
    }

    private void seleccionarEvento() {
        eventoSeleccionado = listaEventos.getSelectedValue();

        if (eventoSeleccionado == null) {
            mostrarAdvertencia("Debe seleccionar un evento.");
            return;
        }

        DefaultListModel<DTEdicion> modelo = new DefaultListModel<>();
        List<DTEdicion> ediciones = sistema.listarEdiciones(eventoSeleccionado.getId());

        for (DTEdicion edicion : ediciones) {
            modelo.addElement(edicion);
        }

        listaEdiciones.setModel(modelo);
        lblEventoSeleccionado.setText(
                "Evento seleccionado: " + eventoSeleccionado.getNombre()
        );

        cardLayout.show(panelPrincipal, "EDICIONES");
    }

    private void seleccionarEdicion() {
        edicionSeleccionada = listaEdiciones.getSelectedValue();

        if (edicionSeleccionada == null) {
            mostrarAdvertencia("Debe seleccionar una edición.");
            return;
        }

        lblEdicionSeleccionada.setText(
                "Edición seleccionada: " + edicionSeleccionada.getNombre()
        );
        txtDetalleEdicion.setText(FormatoDetalles.edicion(edicionSeleccionada));
        cargarTiposRegistro();
        cargarRegistros();
        cargarPatrocinios();

        cardLayout.show(panelPrincipal, "DETALLE_EDICION");
    }

    private void cargarTiposRegistro() {
        DefaultListModel<DTTipoRegistro> modelo = new DefaultListModel<>();

        for (DTTipoRegistro tipoRegistro : sistema.listarTiposRegistro(edicionSeleccionada.getId())) {
            modelo.addElement(tipoRegistro);
        }

        listaTiposRegistro.setModel(modelo);
    }

    private void cargarRegistros() {
        List<DTRegistroEdicion> registros = sistema.listarRegistrosEdicion(edicionSeleccionada.getId());

        if (registros.isEmpty()) {
            txtRegistros.setText("No hay registros para esta edición.");
            return;
        }

        StringBuilder texto = new StringBuilder();
        texto.append("Registros:\n\n");

        for (DTRegistroEdicion registro : registros) {
            texto.append("Id: ")
                    .append(registro.getId())
                    .append(" - Fecha: ")
                    .append(registro.getFecha())
                    .append(" - Asistente: ")
                    .append(registro.getNicknameAsistente())
                    .append(" - Tipo: ")
                    .append(registro.getNombreTipoRegistro())
                    .append(" - Costo: ")
                    .append(registro.getCosto())
                    .append(" - Patrocinado: ")
                    .append(registro.isPatrocinado() ? "Si" : "No")
                    .append("\n");
        }

        txtRegistros.setText(texto.toString());
    }

    private void cargarPatrocinios() {
        DefaultListModel<DTPatrocinio> modelo = new DefaultListModel<>();

        for (DTPatrocinio patrocinio : sistema.listarPatrocinios(edicionSeleccionada.getId())) {
            modelo.addElement(patrocinio);
        }

        listaPatrocinios.setModel(modelo);
    }

    private void mostrarDetalleTipoRegistro() {
        DTTipoRegistro tipoSeleccionado = listaTiposRegistro.getSelectedValue();

        if (tipoSeleccionado == null) {
            mostrarAdvertencia("Debe seleccionar un tipo de registro.");
            return;
        }

        lblDetalleSeleccionado.setText("Detalle del tipo de registro:");
        txtDetalleSeleccionado.setText(FormatoDetalles.tipoRegistro(tipoSeleccionado));

        cardLayout.show(panelPrincipal, "DETALLE_SELECCIONADO");
    }

    private void mostrarDetallePatrocinio() {
        DTPatrocinio patrocinio = listaPatrocinios.getSelectedValue();

        if (patrocinio == null) {
            mostrarAdvertencia("Debe seleccionar un patrocinio.");
            return;
        }

        lblDetalleSeleccionado.setText("Detalle del patrocinio:");
        txtDetalleSeleccionado.setText(FormatoDetalles.patrocinio(patrocinio));

        cardLayout.show(panelPrincipal, "DETALLE_SELECCIONADO");
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Consulta de edición",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
```

<a id="archivo-20"></a>

### 20. logica/Presentacion/ConsultaTipoRegistroInternalFrame.java

Selecciona DTO y muestra el detalle mediante FormatoDetalles.

```java
package logica.Presentacion;

import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTEvento;
import logica.DataTypes.DTTipoRegistro;
import logica.sistema01.ISistema;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import java.awt.CardLayout;
import java.util.List;

public class ConsultaTipoRegistroInternalFrame extends JInternalFrame {

    private final ISistema sistema;
    private final CardLayout cardLayout = new CardLayout();

    private JPanel panelPrincipal;
    private JPanel panelEventos;
    private JPanel panelEdiciones;
    private JPanel panelTipos;
    private JPanel panelDetalles;

    private JList<DTEvento> listaEventos;
    private JList<DTEdicion> listaEdiciones;
    private JList<DTTipoRegistro> listaTiposRegistro;

    private JLabel lblEventoSeleccionado;
    private JLabel lblEdicionSeleccionada;
    private JTextArea txtDetalles;

    private JButton btnSeleccionarEvento;
    private JButton btnCerrarEventos;
    private JButton btnSeleccionarEdicion;
    private JButton btnVolverEventos;
    private JButton btnVerDetalle;
    private JButton btnVolverEdiciones;
    private JButton btnVolverTipos;
    private JButton btnCerrarDetalles;

    private DTEvento eventoSeleccionado;
    private DTEdicion edicionSeleccionada;

    public ConsultaTipoRegistroInternalFrame(ISistema sistema) {
        super("Consulta de tipo de registro", true, true, true, true);

        this.sistema = sistema;

        configurarInterfaz();
        cargarEventos();

        setContentPane(panelPrincipal);
        pack();
        setLocation(140, 80);
    }

    private void configurarInterfaz() {
        panelPrincipal.removeAll();
        panelPrincipal.setLayout(cardLayout);
        panelPrincipal.add(panelEventos, "EVENTOS");
        panelPrincipal.add(panelEdiciones, "EDICIONES");
        panelPrincipal.add(panelTipos, "TIPOS");
        panelPrincipal.add(panelDetalles, "DETALLES");

        txtDetalles.setEditable(false);
        listaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEdiciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTiposRegistro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        btnSeleccionarEvento.addActionListener(e -> seleccionarEvento());
        btnCerrarEventos.addActionListener(e -> dispose());
        btnSeleccionarEdicion.addActionListener(e -> seleccionarEdicion());
        btnVolverEventos.addActionListener(e -> cardLayout.show(panelPrincipal, "EVENTOS"));
        btnVerDetalle.addActionListener(e -> mostrarDetalle());
        btnVolverEdiciones.addActionListener(e -> cardLayout.show(panelPrincipal, "EDICIONES"));
        btnVolverTipos.addActionListener(e -> cardLayout.show(panelPrincipal, "TIPOS"));
        btnCerrarDetalles.addActionListener(e -> dispose());
    }

    private void cargarEventos() {
        DefaultListModel<DTEvento> modelo = new DefaultListModel<>();

        for (DTEvento evento : sistema.listarEventos()) {
            modelo.addElement(evento);
        }

        listaEventos.setModel(modelo);
    }

    private void seleccionarEvento() {
        eventoSeleccionado = listaEventos.getSelectedValue();

        if (eventoSeleccionado == null) {
            mostrarAdvertencia("Debe seleccionar un evento.");
            return;
        }

        DefaultListModel<DTEdicion> modelo = new DefaultListModel<>();

        for (DTEdicion edicion : sistema.listarEdiciones(eventoSeleccionado.getId())) {
            modelo.addElement(edicion);
        }

        listaEdiciones.setModel(modelo);
        lblEventoSeleccionado.setText(
                "Evento seleccionado: " + eventoSeleccionado.getNombre()
        );

        cardLayout.show(panelPrincipal, "EDICIONES");
    }

    private void seleccionarEdicion() {
        edicionSeleccionada = listaEdiciones.getSelectedValue();

        if (edicionSeleccionada == null) {
            mostrarAdvertencia("Debe seleccionar una edición.");
            return;
        }

        DefaultListModel<DTTipoRegistro> modelo = new DefaultListModel<>();
        List<DTTipoRegistro> tiposRegistro =
                sistema.listarTiposRegistro(edicionSeleccionada.getId());

        for (DTTipoRegistro tipo : tiposRegistro) {
            modelo.addElement(tipo);
        }

        listaTiposRegistro.setModel(modelo);
        lblEdicionSeleccionada.setText(
                "Edición seleccionada: " + edicionSeleccionada.getNombre()
        );

        cardLayout.show(panelPrincipal, "TIPOS");
    }

    private void mostrarDetalle() {
        DTTipoRegistro tipoSeleccionado =
                listaTiposRegistro.getSelectedValue();

        if (tipoSeleccionado == null) {
            mostrarAdvertencia(
                    "Debe seleccionar un tipo de registro."
            );
            return;
        }

        txtDetalles.setText(FormatoDetalles.tipoRegistro(tipoSeleccionado));

        cardLayout.show(panelPrincipal, "DETALLES");
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Consulta de tipo de registro",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
```

<a id="archivo-21"></a>

### 21. logica/Presentacion/ConsultaPatrocinioInternalFrame.java

El renderizador y el detalle leen los nombres del DTO, sin recorrer relaciones JPA.

```java
package logica.Presentacion;

import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTEvento;
import logica.DataTypes.DTPatrocinio;
import logica.sistema01.ISistema;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.List;

public class ConsultaPatrocinioInternalFrame extends JInternalFrame {

    private final ISistema sistema;
    private final CardLayout cardLayout = new CardLayout();

    private JPanel panelPrincipal;
    private JPanel panelEventos;
    private JPanel panelEdiciones;
    private JPanel panelPatrocinios;
    private JPanel panelDetalles;

    private JList<DTEvento> listaEventos;
    private JList<DTEdicion> listaEdiciones;
    private JList<DTPatrocinio> listaPatrocinios;

    private JLabel lblEventoSeleccionado;
    private JLabel lblEdicionSeleccionada;
    private JTextArea txtDetalles;

    private JButton btnSeleccionarEvento;
    private JButton btnCerrarEventos;
    private JButton btnSeleccionarEdicion;
    private JButton btnVolverEventos;
    private JButton btnDetallePatrocinio;
    private JButton btnVolverEdiciones;
    private JButton btnVolverPatrocinios;
    private JButton btnCerrarDetalles;

    private DTEvento eventoSeleccionado;
    private DTEdicion edicionSeleccionada;

    public ConsultaPatrocinioInternalFrame(ISistema sistema) {
        super("Consulta de patrocinio", true, true, true, true);

        this.sistema = sistema;

        configurarInterfaz();
        configurarRenderizadores();
        cargarEventos();

        setContentPane(panelPrincipal);
        pack();
        setLocation(140, 80);
    }

    private void configurarInterfaz() {
        panelPrincipal.removeAll();
        panelPrincipal.setLayout(cardLayout);
        panelPrincipal.add(panelEventos, "EVENTOS");
        panelPrincipal.add(panelEdiciones, "EDICIONES");
        panelPrincipal.add(panelPatrocinios, "PATROCINIOS");
        panelPrincipal.add(panelDetalles, "DETALLES");

        txtDetalles.setEditable(false);
        listaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEdiciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPatrocinios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        btnSeleccionarEvento.addActionListener(e -> seleccionarEvento());
        btnCerrarEventos.addActionListener(e -> dispose());
        btnSeleccionarEdicion.addActionListener(e -> seleccionarEdicion());
        btnVolverEventos.addActionListener(e -> cardLayout.show(panelPrincipal, "EVENTOS"));
        btnDetallePatrocinio.addActionListener(e -> mostrarDetalle());
        btnVolverEdiciones.addActionListener(e -> cardLayout.show(panelPrincipal, "EDICIONES"));
        btnVolverPatrocinios.addActionListener(e -> cardLayout.show(panelPrincipal, "PATROCINIOS"));
        btnCerrarDetalles.addActionListener(e -> dispose());
    }

    private void configurarRenderizadores() {
        listaEventos.setCellRenderer((lista, evento, indice, seleccionado, foco) -> {
            JLabel etiqueta = etiquetaLista(lista, evento, indice, seleccionado, foco);

            if (evento != null) {
                etiqueta.setText("ID " + evento.getId() + " - "
                        + evento.getNombre() + " - "
                        + evento.getDescripcion());
            }

            return etiqueta;
        });

        listaEdiciones.setCellRenderer((lista, edicion, indice, seleccionado, foco) -> {
            JLabel etiqueta = etiquetaLista(lista, edicion, indice, seleccionado, foco);

            if (edicion != null) {
                etiqueta.setText("ID " + edicion.getId() + " - "
                        + edicion.getNombre());
            }

            return etiqueta;
        });

        listaPatrocinios.setCellRenderer((lista, patrocinio, indice, seleccionado, foco) -> {
            JLabel etiqueta = etiquetaLista(lista, patrocinio, indice, seleccionado, foco);

            if (patrocinio != null) {
                etiqueta.setText(
                        patrocinio.getCodigo() + " - "
                                + patrocinio.getNombreInstitucion() + " - "
                                + patrocinio.getNivelPatrocinio()
                );
            }

            return etiqueta;
        });
    }

    private JLabel etiquetaLista(
            JList<?> lista,
            Object valor,
            int indice,
            boolean seleccionado,
            boolean foco
    ) {
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        Component componente = renderer.getListCellRendererComponent(
                lista,
                valor,
                indice,
                seleccionado,
                foco
        );

        return (JLabel) componente;
    }

    private void cargarEventos() {
        DefaultListModel<DTEvento> modelo = new DefaultListModel<>();

        for (DTEvento evento : sistema.listarEventos()) {
            modelo.addElement(evento);
        }

        listaEventos.setModel(modelo);
    }

    private void seleccionarEvento() {
        eventoSeleccionado = listaEventos.getSelectedValue();

        if (eventoSeleccionado == null) {
            mostrarAdvertencia("Debe seleccionar un evento.");
            return;
        }

        DefaultListModel<DTEdicion> modelo = new DefaultListModel<>();

        for (DTEdicion edicion : sistema.listarEdiciones(eventoSeleccionado.getId())) {
            modelo.addElement(edicion);
        }

        listaEdiciones.setModel(modelo);
        lblEventoSeleccionado.setText(
                "Evento seleccionado: " + eventoSeleccionado.getNombre()
        );

        cardLayout.show(panelPrincipal, "EDICIONES");
    }

    private void seleccionarEdicion() {
        edicionSeleccionada = listaEdiciones.getSelectedValue();

        if (edicionSeleccionada == null) {
            mostrarAdvertencia("Debe seleccionar una edición.");
            return;
        }

        DefaultListModel<DTPatrocinio> modelo = new DefaultListModel<>();
        List<DTPatrocinio> patrocinios =
                sistema.listarPatrocinios(edicionSeleccionada.getId());

        for (DTPatrocinio patrocinio : patrocinios) {
            modelo.addElement(patrocinio);
        }

        listaPatrocinios.setModel(modelo);
        lblEdicionSeleccionada.setText(
                "Edición seleccionada: " + edicionSeleccionada.getNombre()
        );

        cardLayout.show(panelPrincipal, "PATROCINIOS");
    }

    private void mostrarDetalle() {
        DTPatrocinio patrocinio = listaPatrocinios.getSelectedValue();

        if (patrocinio == null) {
            mostrarAdvertencia("Debe seleccionar un patrocinio.");
            return;
        }

        txtDetalles.setText(FormatoDetalles.patrocinio(patrocinio));

        cardLayout.show(panelPrincipal, "DETALLES");
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Consulta de patrocinio",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
```

<a id="archivo-22"></a>

### 22. logica/Presentacion/ConsultaUsuarioInternalFrame.java

Conserva el perfil y registros con sus DTO actuales. Solicita a Sistema las ediciones del organizador.

```java
package logica.Presentacion;

import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTRegistro;
import logica.DataTypes.DTRegistroMin;
import logica.DataTypes.DTUsuario;
import logica.DataTypes.DTUsuarioAsist;
import logica.DataTypes.DTUsuarioOrg;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.util.List;
import java.util.Set;

public class ConsultaUsuarioInternalFrame extends JInternalFrame {
    private static final int ANCHO_VENTANA = 500;
    private static final int ALTO_VENTANA = 380;

    private final ISistema sistema;
    private JPanel JPanelPrincipal;
    private JPanel PanelUsuarios;
    private JList<String> listUsuarios;
    private JLabel txtUsuarios;
    private JButton btnSelecUsuario;
    private JLabel txtEspecifico;
    private JTextPane paneEspecifico;
    private JList<Object> listEspecifico;
    private JButton btnEspecifico;
    private JButton btnDetalleEdicion;
    private JButton btnVolverUsuarios;
    private JPanel panelEspecifico;
    private JTextPane paneSeleccionado;
    private JPanel panelSeleccionado;
    private JLabel txtSeleccionado;
    private JButton btnVolverEspecifico;
    private String nicknameSeleccionado;

    public ConsultaUsuarioInternalFrame(ISistema sistema) {
        super("Consulta Usuario", true, true, true, true);
        this.sistema = sistema;

        configurarPantallaInicial();
        cargarListadoUsuarios();

        btnSelecUsuario.addActionListener(e -> seleccionarUsuario());
        btnEspecifico.addActionListener(e -> mostrarDetalleRegistro());
        btnDetalleEdicion.addActionListener(e -> mostrarDetalleEdicion());
        btnVolverUsuarios.addActionListener(e -> volverAUsuarios());
        btnVolverEspecifico.addActionListener(e -> mostrarPanel(panelEspecifico));

        setContentPane(PanelUsuarios);
        setTitle("Consulta usuario");
        ajustarTamanoVentana();
    }

    private void configurarPantallaInicial() {
        panelEspecifico.setVisible(false);
        panelSeleccionado.setVisible(false);
        paneEspecifico.setEditable(false);
        paneSeleccionado.setEditable(false);
        listUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listEspecifico.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        configurarRenderizadorListaEspecifica();
    }

    private void configurarRenderizadorListaEspecifica() {
        listEspecifico.setCellRenderer((lista, valor, indice, seleccionado, foco) -> {
            DefaultListCellRenderer renderer = new DefaultListCellRenderer();
            JLabel etiqueta = (JLabel) renderer.getListCellRendererComponent(
                    lista,
                    valor,
                    indice,
                    seleccionado,
                    foco
            );

            if (valor instanceof DTRegistroMin registro) {
                etiqueta.setText(registro.toString());
            } else if (valor instanceof DTEdicion edicion) {
                etiqueta.setText("ID edición: " + edicion.getId()
                        + " - " + edicion.getNombre()
                        + " (" + edicion.getSigla() + ")");
            }

            return etiqueta;
        });
    }

    private void cargarListadoUsuarios() {
        Set<DTUsuario> usuarios = sistema.listarUsuarios();
        DefaultListModel<String> modelo = new DefaultListModel<>();

        if (usuarios.isEmpty()) {
            listUsuarios.setVisible(false);
            txtUsuarios.setText("No hay usuarios aun.");
            return;
        }

        for (DTUsuario usuario : usuarios) {
            modelo.addElement(usuario.nickname());
        }

        listUsuarios.setModel(modelo);
        listUsuarios.setVisible(true);
    }

    private void seleccionarUsuario() {
        nicknameSeleccionado = listUsuarios.getSelectedValue();

        if (nicknameSeleccionado == null || nicknameSeleccionado.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor seleccione un usuario de la lista.",
                    "Usuario no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        cargarEspecifico();
        mostrarPanel(panelEspecifico);
    }

    private void cargarEspecifico() {
        try {
            DTDatosUsuario datos = sistema.mostrarDatosUsuario(nicknameSeleccionado);
            DefaultListModel<Object> modeloLista = new DefaultListModel<>();

            if (datos instanceof DTUsuarioAsist asistente) {
                cargarDatosAsistente(asistente, modeloLista);
            } else if (datos instanceof DTUsuarioOrg organizador) {
                cargarDatosOrganizador(organizador, modeloLista);
            }

            listEspecifico.setModel(modeloLista);
            panelSeleccionado.setVisible(false);

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los datos del usuario: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarDatosAsistente(
            DTUsuarioAsist asistente,
            DefaultListModel<Object> modeloLista
    ) {
        txtEspecifico.setText("Información del Asistente (registros a ediciones):");
        paneEspecifico.setText(
                "Nickname: " + asistente.getNickname() + "\n" +
                        "Nombre: " + asistente.getNombre() + " " + asistente.getApellido() + "\n" +
                        "Correo: " + asistente.getCorreo() + "\n" +
                        "Fecha de Nacimiento: " + asistente.getFechaNacimiento()
        );

        List<DTRegistroMin> registros = sistema.listarRegistrosAsistente(asistente.getNickname());

        for (DTRegistroMin registro : registros) {
            modeloLista.addElement(registro);
        }

        if (registros.isEmpty()) {
            paneEspecifico.setText(
                    paneEspecifico.getText()
                            + "\n\nNo se encuentra registrado en ninguna edición."
            );
        }

        btnEspecifico.setVisible(true);
        btnEspecifico.setText("Ver detalle registro");
        btnDetalleEdicion.setVisible(true);
        btnDetalleEdicion.setText("Ver detalle edición");
    }

    private void cargarDatosOrganizador(
            DTUsuarioOrg organizador,
            DefaultListModel<Object> modeloLista
    ) {
        txtEspecifico.setText("Información del Organizador (ediciones asociadas):");
        paneEspecifico.setText(
                "Nickname: " + organizador.getNickname() + "\n" +
                        "Nombre: " + organizador.getNombre() + "\n" +
                        "Correo: " + organizador.getCorreo() + "\n" +
                        "Descripción: " + organizador.getDescripcion() + "\n" +
                        "Sitio Web / Enlace: " + (organizador.getEnlace() != null
                        ? organizador.getEnlace()
                        : "No especificado")
        );

        for (DTEdicion edicion : sistema.listarEdicionesOrganizador(
                organizador.getNickname())) {
            modeloLista.addElement(edicion);
        }

        if (modeloLista.isEmpty()) {
            paneEspecifico.setText(
                    paneEspecifico.getText()
                            + "\n\nNo tiene ediciones asociadas actualmente."
            );
        }

        btnEspecifico.setVisible(false);
        btnDetalleEdicion.setVisible(true);
        btnDetalleEdicion.setText("Ver detalle edición");
    }

    private void mostrarDetalleRegistro() {
        Object seleccionado = listEspecifico.getSelectedValue();

        if (!(seleccionado instanceof DTRegistroMin registro)) {
            mostrarAdvertencia("Debe seleccionar un registro.");
            return;
        }

        try {
            DTRegistro detalle = sistema.mostrarDatosRegistro(
                    nicknameSeleccionado,
                    registro.getId()
            );

            txtSeleccionado.setText("Detalle del registro:");
            paneSeleccionado.setText(
                    "Fecha: " + detalle.getFecha() + "\n" +
                            "Edición: " + detalle.getNombreEdicion() + "\n" +
                            "Tipo de registro: " + detalle.getNombreTipoRegistro() + "\n" +
                            "Descripción del tipo: " + detalle.getDescripcionTipoRegistro() + "\n" +
                            "Costo: " + detalle.getCosto() + "\n" +
                            "Patrocinado: " + (detalle.isPatrocinado() ? "Sí" : "No")
            );

            mostrarPanel(panelSeleccionado);
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
    }

    private void mostrarDetalleEdicion() {
        Object seleccionado = listEspecifico.getSelectedValue();

        if (seleccionado == null) {
            mostrarAdvertencia("Debe seleccionar una edición o un registro.");
            return;
        }

        Long idEdicion = obtenerIdEdicion(seleccionado);

        if (idEdicion == null) {
            mostrarAdvertencia("No se pudo identificar la edición seleccionada.");
            return;
        }

        try {
            txtSeleccionado.setText("Detalle de la edición:");
            DTEdicion datos = sistema.mostrarDatosEdicion(idEdicion);
            paneSeleccionado.setText(FormatoDetalles.edicion(datos));
            mostrarPanel(panelSeleccionado);
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
    }

    private Long obtenerIdEdicion(Object seleccionado) {
        if (seleccionado instanceof DTRegistroMin registro) {
            return registro.getIdEdicion();
        }

        if (seleccionado instanceof DTEdicion edicion) {
            return edicion.getId();
        }

        return null;
    }

    private void mostrarPanel(JPanel panel) {
        panel.setVisible(true);
        setContentPane(panel);
        revalidate();
        repaint();
        ajustarTamanoVentana();
    }

    private void volverAUsuarios() {
        nicknameSeleccionado = null;
        listEspecifico.clearSelection();
        setContentPane(PanelUsuarios);
        revalidate();
        repaint();
        ajustarTamanoVentana();
    }

    private void ajustarTamanoVentana() {
        pack();
        setSize(
                Math.max(getWidth(), ANCHO_VENTANA),
                Math.max(getHeight(), ALTO_VENTANA)
        );
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Consulta usuario",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Consulta usuario",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
```

<a id="archivo-23"></a>

### 23. logica/Clases/Patrocinio.java

Solo agrega el import del enum. No cambia anotaciones JPA, relaciones ni columnas.

```java
package logica.Clases;

import logica.DataTypes.NivelPatrocinio;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import logica.DataTypes.DTFecha;
import logica.DataTypes.DTFechaConverter;

@Entity
@Table(name = "patrocinio")
public class Patrocinio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String codigo;

    @Convert(converter = DTFechaConverter.class)
    @Column(nullable = false)
    private DTFecha fecha;

    @Column(nullable = false)
    private float montoAportado;

    @Column(nullable = false)
    private int cantRegistros;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NivelPatrocinio nivelPatrocinio;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "institucion_id", nullable = false)
    private Institucion institucion;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "edicion_id", nullable = false)
    private Edicion edicion;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_registro_id", nullable = false)
    private TipoRegistro tipoRegistro;

    protected Patrocinio() {
    }

    public Patrocinio(
            String codigo,
            DTFecha fecha,
            float montoAportado,
            int cantRegistros,
            NivelPatrocinio nivelPatrocinio,
            Institucion institucion,
            Edicion edicion,
            TipoRegistro tipoRegistro
    ) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.montoAportado = montoAportado;
        this.cantRegistros = cantRegistros;
        this.nivelPatrocinio = nivelPatrocinio;
        this.institucion = institucion;
        this.edicion = edicion;
        this.tipoRegistro = tipoRegistro;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public DTFecha getFecha() {
        return fecha;
    }

    public float getMontoAportado() {
        return montoAportado;
    }

    public int getCantRegistros() {
        return cantRegistros;
    }

    public NivelPatrocinio getNivelPatrocinio() {
        return nivelPatrocinio;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public Edicion getEdicion() {
        return edicion;
    }

    public TipoRegistro getTipoRegistro() {
        return tipoRegistro;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setFecha(DTFecha fecha) {
        this.fecha = fecha;
    }

    public void setMontoAportado(float montoAportado) {
        this.montoAportado = montoAportado;
    }

    public void setCantRegistros(int cantRegistros) {
        this.cantRegistros = cantRegistros;
    }

    public void setNivelPatrocinio(NivelPatrocinio nivelPatrocinio) {
        this.nivelPatrocinio = nivelPatrocinio;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public void setEdicion(Edicion edicion) {
        this.edicion = edicion;
    }

    public void setTipoRegistro(TipoRegistro tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }
}
```

<a id="archivo-24"></a>

### 24. logica/Persistencia/DatosIniciales.java

Solo actualiza el import del enum de la precarga.

```java
package logica.Persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import logica.Clases.Asistente;
import logica.Clases.Categoria;
import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.Clases.Institucion;
import logica.DataTypes.NivelPatrocinio;
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
```

## 8. Estado de verificacion de la propuesta

Se compilaron todas las fuentes de la propuesta en una copia temporal, con Java 25 y las dependencias JPA/Hibernate instaladas: sin errores de compilacion. Tambien se verifico que ISistema y las pantallas propuestas no importan Clases ni Persistencia.

Esta comprobacion valida tipos, constructores, firmas y llamadas Java. No instrumenta los .form de IntelliJ ni prueba los casos de uso contra PostgreSQL. La verificacion de interfaz y persistencia sigue pendiente para cuando se decida aplicar la propuesta.
