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
import logica.DataTypes.DTCategoria;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ISistema {

    void altaInstitucion(String nombre, String descripcion, String sitioWeb);

    List<String> listarNombresInstituciones();

    EstadoAltaUsuario chequearUsuario(String nickname, String correo);

    void altaAsistente(String nickname, String nombre, String correo, String apellido, LocalDate fechaNacimiento, String nombreInstitucion);

    void altaOrganizador(String nickname, String nombre, String correo, String descripcion, String enlace);

    void altaEvento(String nombre, String descripcion, String sigla, DTFecha fechaAlta, List<String> nombresCategorias);

    void altaEdicion(Long idEvento, String nicknameOrganizador, String nombre, String sigla, DTFecha fechaInicio, DTFecha fechaFin, DTFecha fechaAlta, String ciudad, String pais);

    void altaTipoRegistro(Long idEdicion, String nombre, String descripcion, float costo, int cupo);

    void altaPatrocinio(Long idEdicion, String nombreInstitucion, Long idTipoRegistro, NivelPatrocinio nivelPatrocinio, float montoAportado, int cantRegistros, String codigo, DTFecha fechaAlta);

    void altaRegistro(String nicknameAsistente, Long idEdicion, Long idTipoRegistro, String codigoPatrocinio);

    List<DTEvento> listarEventos();

    List<DTEdicion> listarEdiciones(Long idEvento);

    List<DTEdicion> listarEdicionesOrganizador(String nickname);

    DTEdicion mostrarDatosEdicion(Long idEdicion);

    List<DTTipoRegistro> listarTiposRegistro(Long idEdicion);

    List<DTPatrocinio> listarPatrocinios(Long idEdicion);

    List<DTRegistroEdicion> listarRegistrosEdicion(Long idEdicion);

    List<DTUsuario> listarOrganizadores();

    List<String> listarNombresCategorias();

    Set<DTUsuario> listarUsuarios();

    Set<DTUsuario> listarAsistentes();

    DTDatosUsuario mostrarDatosUsuario(String nickname);

    void modificarDatosUsuario(DTDatosUsuario datos);

    List<DTRegistroMin> listarRegistrosAsistente(String nickname);

    DTRegistro mostrarDatosRegistro(String nickname, Long idRegistro);

    List<DTCategoria> listarCategoriasJerarquicas();

    void altaCategoria(String nombre, Long idPadre);
}
