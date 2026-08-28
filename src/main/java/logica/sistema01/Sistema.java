package logica.sistema01;

import logica.Clases.*;
import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTUsuario;
import logica.DataTypes.DTUsuarioAsist;
import logica.DataTypes.DTUsuarioOrg;

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
    private final List<Usuario> usuarios;
    private final List<Evento> eventos;

    private Sistema() {
        instituciones = new HashMap<>();
        usuarios = new ArrayList<>();
        eventos = new ArrayList<>();

        cargarDatosIniciales();
    }

    public static Sistema getInstancia() {
        return instancia;
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

        for (Usuario u : usuarios) {
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
        for (Usuario usuario : usuarios) {
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
        usuarios.add(new Asistente(
                "Matias",
                "MatiB",
                "matiasbragiotorres@gmail.com",
                "Bragio",
                LocalDate.of(2000, 5, 10)
        ));

        usuarios.add(new Organizador(
                "Juancito",
                "juanchi",
                "juancito@gmail.com",
                "Organizador de conferencias",
                "https://orgconf.com"
        ));

        eventos.add(new Evento("Conferencia Java", "Conferencia sobre Java", "JV2026"));
        eventos.add(new Evento("Conferencia Python", "Conferencia sobre Python", "PY2026"));
    }

    private Usuario buscarPorNickname(String nickname) {
        for (Usuario u : usuarios) {
            if (u.getNickname().equals(nickname)) {
                return u;
            }
        }

        throw new RuntimeException(
                "No existe un usuario con el nickname: " + nickname
        );
    }
}
