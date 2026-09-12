package logica.DataTypes;

public class DTRegistroMin {

    private Long id;
    private Long idEdicion;
    private DTFecha fecha;
    private String nombreEdicion;
    private String nombreTipoRegistro;

    public DTRegistroMin(
            Long id,
            Long idEdicion,
            DTFecha fecha,
            String nombreEdicion,
            String nombreTipoRegistro) {
        this.id = id;
        this.idEdicion = idEdicion;
        this.fecha = fecha;
        this.nombreEdicion = nombreEdicion;
        this.nombreTipoRegistro = nombreTipoRegistro;
    }

    public Long getId() {
        return id;
    }

    public Long getIdEdicion() {
        return idEdicion;
    }

    public DTFecha getFecha() {
        return fecha;
    }

    public String getNombreEdicion() {
        return nombreEdicion;
    }

    public String getNombreTipoRegistro() {
        return nombreTipoRegistro;
    }

    @Override
    public String toString() {
        return "Id registro: " + id
                + " - Fecha: " + fecha
                + " - Edición: " + nombreEdicion
                + " - Tipo: " + nombreTipoRegistro;
    }
}
