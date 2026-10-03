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