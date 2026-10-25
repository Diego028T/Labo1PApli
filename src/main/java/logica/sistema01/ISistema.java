package logica.sistema01;

import logica.DataTypes.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ISistema {

    void altaInstitucion(String nombre, String descripcion, String sitioWeb);

    List<String> listarNombresInstituciones();

    EstadoAltaUsuario chequearUsuario(String nickname, String correo);

    void altaAsistente(String nickname, String nombre, String correo, String apellido, LocalDate fechaNacimiento, String nombreInstitucion, char[] contrasena, char[] confirmacionContrasena);

    void altaOrganizador(String nickname, String nombre, String correo, String descripcion, String enlace, char[] contrasena, char[] confirmacionContrasena);

    void altaEvento(String nombre, String descripcion, String sigla, DTFecha fechaAlta, List<String> nombresCategorias);

    void altaEvento(String nombre, String descripcion, String sigla, DTFecha fechaAlta, List<String> nombresCategorias, byte[] imagen, String tipoImagen);

    DTImagen obtenerImagenEvento(Long idEvento);

    void altaEdicion(Long idEvento, String nicknameOrganizador, String nombre, String sigla, DTFecha fechaInicio, DTFecha fechaFin, DTFecha fechaAlta, String ciudad, String pais);

    // La vieja: utilizada por el administrador desde Swing.
    void altaTipoRegistro(Long idEdicion, String nombre, String descripcion, float costo, int cupo);

    // La nueva: incorpora la identidad del organizador. JAVA puede distinguir aunque tengan el mismo nombre
    void altaTipoRegistro(String nicknameOrganizador, Long idEdicion, String nombre, String descripcion, float costo, int cupo);

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

    DTUsuarioAutenticado autenticar(String identificador, char[] contrasena);

    Set<DTUsuario> listarUsuarios();

    Set<DTUsuario> listarAsistentes();

    DTDatosUsuario mostrarDatosUsuario(String nickname);

    void modificarDatosUsuario(DTDatosUsuario datos, char[] nuevaContrasena, char[] confirmacionNuevaContrasena);

    List<DTRegistroMin> listarRegistrosAsistente(String nickname);

    DTRegistro mostrarDatosRegistro(String nickname, Long idRegistro);

    List<DTCategoria> listarCategoriasJerarquicas();

    void altaCategoria(String nombre, Long idPadre);

    List<DTEvento> listarEventosPorCategoria(String categoria);
    DTEvento mostrarDatosEvento(Long idEvento);
}
