package logica.sistema01;

import logica.Clases.Edicion;
import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTRegistro;
import logica.DataTypes.DTRegistroMin;
import logica.DataTypes.DTUsuario;
import logica.DataTypes.EstadoAltaUsuario;
import logica.Clases.Evento;
import logica.Clases.Organizador;
import logica.Clases.Patrocinio;
import logica.Clases.Registro;
import logica.Clases.TipoRegistro;
import logica.DataTypes.DTFecha;
import logica.Clases.Categoria;
import logica.Clases.NivelPatrocinio;
import logica.Clases.Edicion;
import logica.Clases.TipoRegistro;

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

    void altaTipoRegistro(
            Edicion edicion,
            String nombre,
            String descripcion,
            float costo,
            int cupo
    );

    void altaEvento(
            String nombre,
            String descripcion,
            String sigla,
            DTFecha fechaAlta,
            List<String> nombresCategorias
    );

    void altaEdicion(
            Evento evento,
            Organizador organizador,
            String nombre,
            String sigla,
            DTFecha fechaInicio,
            DTFecha fechaFin,
            DTFecha fechaAlta,
            String ciudad,
            String pais
    );

    void altaPatrocinio(
            Edicion edicion,
            String nombreInstitucion,
            TipoRegistro tipoRegistro,
            NivelPatrocinio nivelPatrocinio,
            float montoAportado,
            int cantRegistros,
            String codigo,
            DTFecha fechaAlta
    );

    List<Edicion> listarEdiciones(Evento evento);

    String mostrarDatosEdicion(Long idEdicion);

    List<TipoRegistro> listarTiposRegistro(Edicion edicion);

    List<Patrocinio> listarPatrocinios(Edicion edicion);

    List<Registro> listarRegistrosEdicion(Edicion edicion);

    Set<DTUsuario> listarUsuarios();

    Set<DTUsuario> listarAsistentes();

    DTDatosUsuario mostrarDatosUsuario(String nickname);

    void modificarDatosUsuario(DTDatosUsuario datos);

    List<DTRegistroMin> listarRegistrosAsistente(String nickname);

    DTRegistro mostrarDatosRegistro(String nickname, Long idRegistro);

    void altaRegistro(
            String nicknameAsistente,
            Edicion edicion,
            TipoRegistro tipoRegistro,
            String codigoPatrocinio
    );

    List<Evento> listarEventos();

    List<Organizador> listarOrganizadores();

    List<Categoria> listarNombresCategorias();

    void altaCategoria(String nombre);

}
