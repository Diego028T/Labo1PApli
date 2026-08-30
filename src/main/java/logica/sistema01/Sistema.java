package logica.sistema01;

import logica.Clases.*;
import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTUsuario;
import logica.DataTypes.DTUsuarioAsist;
import logica.DataTypes.DTUsuarioOrg;
import logica.DataTypes.DTFecha;
import logica.DataTypes.EstadoAltaUsuario;

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

    private Sistema() {
        instituciones = new HashMap<>();
        usuariosPorNickname = new HashMap<>();
        usuariosPorCorreo = new HashMap<>();
        eventos = new ArrayList<>();

        cargarDatosIniciales();
    }

    public static Sistema getInstancia() {
        return instancia;
    }

    @Override
    public EstadoAltaUsuario chequearUsuario(String nickname, String correo) {
        boolean nicknameRepetido = usuariosPorNickname.containsKey(nickname);
        boolean correoRepetido = usuariosPorCorreo.containsKey(correo);

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
        String clave = nombreInstitucion.trim().toLowerCase();
        Institucion institucion = instituciones.get(clave);

        if (institucion == null) {
            throw new IllegalArgumentException(
                    "No existe una institución con el nombre: " + nombreInstitucion
            );
        }

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

        EstadoAltaUsuario estado = chequearUsuario(nickname, correo);

        if (estado != EstadoAltaUsuario.OK) {
            throw new IllegalArgumentException("El nickname o correo ya están en uso.");
        }

        Asistente asistente = new Asistente(
                nombre,
                nickname,
                correo,
                apellido,
                fechaNacimiento
        );

        asignarInstitucion(asistente, nombreInstitucion);

        usuariosPorNickname.put(nickname, asistente);
        usuariosPorCorreo.put(correo, asistente);
    }

    @Override
    public void altaOrganizador(
            String nickname,
            String nombre,
            String correo,
            String descripcion,
            String enlace) {

        EstadoAltaUsuario estado = chequearUsuario(nickname, correo);

        if (estado != EstadoAltaUsuario.OK) {
            throw new IllegalArgumentException("El nickname o correo ya están en uso.");
        }

        Organizador organizador = new Organizador(
                nombre,
                nickname,
                correo,
                descripcion,
                enlace
        );

        usuariosPorNickname.put(nickname, organizador);
        usuariosPorCorreo.put(correo, organizador);
    }

    @Override
    public void altaInstitucion(
            String nombre,
            String descripcion,
            String sitioWeb) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la institución es obligatorio."
            );
        }

        String clave = nombre.trim().toLowerCase();

        if (instituciones.containsKey(clave)) {
            throw new IllegalArgumentException(
                    "Ya existe una institución con ese nombre."
            );
        }

        Institucion nuevaInstitucion = new Institucion(
                nombre.trim(),
                descripcion,
                sitioWeb
        );

        instituciones.put(clave, nuevaInstitucion);
    }

    @Override
    public List<String> listarNombresInstituciones() {
        List<String> nombres = new ArrayList<>();

        for (Institucion institucion : instituciones.values()) {
            nombres.add(institucion.getNombre());
        }

        return nombres;
    }

    @Override
    public Set<DTUsuario> listarUsuarios() {
        Set<DTUsuario> resultado = new HashSet<>();

        for (Usuario u : usuariosPorNickname.values()) {
            DTUsuario dt = u.getDTUsuario();
            resultado.add(dt);
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

            usuario.setNombre(datosAsistente.getNombre());

            Asistente asistente = (Asistente) usuario;
            asistente.setApellido(datosAsistente.getApellido());
            asistente.setFechaNacimiento(datosAsistente.getFechaNacimiento());

        } else if (datos instanceof DTUsuarioOrg datosOrganizador) {
            Usuario usuario = buscarPorNickname(datosOrganizador.getNickname());

            usuario.setNombre(datosOrganizador.getNombre());

            Organizador organizador = (Organizador) usuario;
            organizador.setDescripcion(datosOrganizador.getDescripcion());
            organizador.setEnlace(datosOrganizador.getEnlace());
        }
    }

    @Override
    public List<Evento> listarEventos() {
        return new ArrayList<>(eventos);
    }

    public List<Edicion> listarEdiciones(Evento evento){
        for (Edicion edicion : evento.getEdiciones()) {}
        return new ArrayList<>(evento.getEdiciones());
    }

    @Override
    public List<Organizador> listarOrganizadores() {
        List<Organizador> resultado = new ArrayList<>();
        for (Usuario usuario : usuariosPorNickname.values()) {
            if (usuario instanceof Organizador organizador) {
                resultado.add(organizador);
            }
        }
        return resultado;
    }

    private void cargarDatosIniciales() {
        altaInstitucion(
                "UTEC",
                "Universidad Tecnológica del Uruguay",
                "https://utec.edu.uy"
        );

        altaInstitucion(
                "ANTEL",
                "Empresa nacional de telecomunicaciones",
                "https://www.antel.com.uy"
        );

        //datos para probar
        altaAsistente(
                "MatiB",
                "Matias",
                "matiasbragiotorres@gmail.com",
                "Bragio",
                LocalDate.of(2000, 5, 10),
                ""
        );

        altaOrganizador(
                "juanchi",
                "Juancito",
                "juancito@gmail.com",
                "Organizador de conferencias",
                "https://orgconf.com"
        );

        Evento conferenciaJava = new Evento(
                "Conferencia Java",
                "Conferencia sobre Java",
                "JV2026"
        );

        conferenciaJava.setEdiciones(new Edicion(
                "Java 2026",
                "JV26",
                new DTFecha(2026, 1, 15),
                new DTFecha(2026, 11, 12),
                "Montevideo",
                "Uruguay"
        ));

        eventos.add(conferenciaJava);

        eventos.add(new Evento("Conferencia Python", "Conferencia sobre Python", "PY2026"));
    }

    private Usuario buscarPorNickname(String nickname) {
        Usuario usuario = usuariosPorNickname.get(nickname);

        if (usuario == null) {
            throw new RuntimeException(
                    "No existe un usuario con el nickname: " + nickname
            );
        }

        return usuario;
    }

    @Override
    public void altaTipoRegistro(
            Edicion edicion,
            String nombre,
            String descripcion,
            float costo,
            int cupo) {

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

        TipoRegistro tipo = new TipoRegistro(
                nombre.trim(),
                descripcion.trim(),
                costo,
                cupo
        );

        edicion.agregarTipoRegistro(tipo);

    }
}
