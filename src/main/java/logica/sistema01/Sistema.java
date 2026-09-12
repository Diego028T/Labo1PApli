package logica.sistema01;

import logica.Clases.*;
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
        //cargarDatosIniciales();
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
    public List<Categoria> listarNombresCategorias() {
        return CategoriaDAO.listarCategorias();
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
            Edicion edicion,
            TipoRegistro tipoRegistro,
            String codigoPatrocinio
    ) {
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
    public List<Evento> listarEventos() {
        try {
            List<Evento> dbEventos = EventoDAO.listarEventos();
            if (dbEventos != null && !dbEventos.isEmpty()) {
                return dbEventos;
            }
        } catch (Exception ignored) {
        }
        return new ArrayList<>(eventos);
    }

    @Override
    public void altaEdicion(
            Evento evento,
            Organizador organizador,
            String nombre,
            String sigla,
            DTFecha fechaInicio,
            DTFecha fechaFin,
            DTFecha fechaAlta,
            String ciudad,
            String pais
    ) {
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
    public List<Edicion> listarEdiciones(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("Debe seleccionar un evento.");
        }

        if (evento.getId() != null) {
            try {
                return EdicionDAO.listarPorEvento(evento.getId());
            } catch (Exception ignored) {
            }
        }

        return evento.getEdiciones();
    }

    @Override
    public List<TipoRegistro> listarTiposRegistro(Edicion edicion) {
        if (edicion == null) {
            throw new IllegalArgumentException("Debe seleccionar una edición.");
        }

        if (edicion.getId() == null) {
            return edicion.getTiposRegistro();
        }

        return tipoRegistroDAO.listarPorEdicion(edicion);
    }

    @Override
    public List<Patrocinio> listarPatrocinios(Edicion edicion) {
        if (edicion == null) {
            throw new IllegalArgumentException("Debe seleccionar una edición.");
        }

        if (edicion.getId() == null) {
            return List.of();
        }

        return patrocinioDAO.listarPorEdicion(edicion);
    }

    @Override
    public List<Organizador> listarOrganizadores() {
        List<Organizador> resultado = new ArrayList<>();

        for (Usuario usuario : usuarioDAO.listarUsuarios()) {
            if (usuario instanceof Organizador organizador) {
                resultado.add(organizador);
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
            Edicion edicion,
            String nombreInstitucion,
            TipoRegistro tipoRegistro,
            NivelPatrocinio nivelPatrocinio,
            float montoAportado,
            int cantRegistros,
            String codigo,
            DTFecha fechaAlta
    ) {
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

}
