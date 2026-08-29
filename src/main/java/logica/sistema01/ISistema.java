package logica.sistema01;

import logica.Clases.Edicion;
import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTUsuario;
import logica.DataTypes.EstadoAltaUsuario;
import logica.Clases.Evento;
import logica.Clases.Organizador;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ISistema {

    void altaInstitucion(
            String nombre,
            String descripcion,
            String sitioWeb
    );

    List<String> listarNombresInstituciones();

    EstadoAltaUsuario chequearUsuario(String nickname, String correo);

    void altaAsistente(
            String nickname,
            String nombre,
            String correo,
            String apellido,
            LocalDate fechaNacimiento,
            String nombreInstitucion
    );

    void altaOrganizador(
            String nickname,
            String nombre,
            String correo,
            String descripcion,
            String enlace
    );

    List<Edicion> listarEdiciones(Evento evento);

    Set<DTUsuario> listarUsuarios();

    DTDatosUsuario mostrarDatosUsuario(String nickname);

    void modificarDatosUsuario(DTDatosUsuario datos);

    List<Evento> listarEventos();

    List<Organizador> listarOrganizadores();
}
