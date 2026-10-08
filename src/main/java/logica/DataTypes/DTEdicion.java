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
