package logica.DataTypes;

public class DTRegistroMin {

    private Long id;
    private DTFecha fecha;
    private String nombreEdicion;
    private String nombreTipoRegistro;

    public DTRegistroMin(
            Long id,
            DTFecha fecha,
            String nombreEdicion,
            String nombreTipoRegistro) {
        this.id = id;
        this.fecha = fecha;
        this.nombreEdicion = nombreEdicion;
        this.nombreTipoRegistro = nombreTipoRegistro;
    }

    public Long getId() {
        return id;
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
        return fecha + " - " + nombreEdicion + " - " + nombreTipoRegistro;
    }
}
