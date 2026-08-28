package logica.sistema01;

import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTUsuario;
import logica.Clases.Evento;
import logica.Clases.Organizador;

import java.util.List;
import java.util.Set;

public interface ISistema {

    void altaInstitucion(
            String nombre,
            String descripcion,
            String sitioWeb
    );

    List<String> listarNombresInstituciones();

    Set<DTUsuario> listarUsuarios();

    DTDatosUsuario mostrarDatosUsuario(String nickname);

    void modificarDatosUsuario(DTDatosUsuario datos);

    List<Evento> listarEventos();

    List<Organizador> listarOrganizadores();
}
