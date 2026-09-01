package logica.DataTypes;

public class DTRegistro {

    private DTFecha fecha;
    private double costo;
    private boolean patrocinado;
    private String nombreTipoRegistro;
    private String descripcionTipoRegistro;
    private String nombreEdicion;

    public DTRegistro(
            DTFecha fecha,
            double costo,
            boolean patrocinado,
            String nombreTipoRegistro,
            String descripcionTipoRegistro,
            String nombreEdicion) {
        this.fecha = fecha;
        this.costo = costo;
        this.patrocinado = patrocinado;
        this.nombreTipoRegistro = nombreTipoRegistro;
        this.descripcionTipoRegistro = descripcionTipoRegistro;
        this.nombreEdicion = nombreEdicion;
    }

    public DTFecha getFecha() {
        return fecha;
    }

    public double getCosto() {
        return costo;
    }

    public boolean isPatrocinado() {
        return patrocinado;
    }

    public String getNombreTipoRegistro() {
        return nombreTipoRegistro;
    }

    public String getDescripcionTipoRegistro() {
        return descripcionTipoRegistro;
    }

    public String getNombreEdicion() {
        return nombreEdicion;
    }
}
