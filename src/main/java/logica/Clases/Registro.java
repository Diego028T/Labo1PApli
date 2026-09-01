package logica.Clases;
import logica.DataTypes.DTFecha;


public class Registro {
    private int id;
    private DTFecha fecha;
    private double costo;
    private boolean patrocinado;
    private TipoRegistro tipoRegistro;
    private Edicion edicion;

    // Constructor
    public Registro(
            int id,
            DTFecha fecha,
            double costo,
            boolean patrocinado,
            TipoRegistro tipoRegistro,
            Edicion edicion) {
        this.id = id;
        this.fecha = fecha;
        this.costo = costo;
        this.patrocinado = patrocinado;
        this.tipoRegistro = tipoRegistro;
        this.edicion = edicion;
    }

    public int getId() { return id; }
    public DTFecha getFecha() { return fecha; }
    public void setFecha(DTFecha fecha) { this.fecha = fecha; }

    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }

    public boolean isPatrocinado() { return patrocinado; }
    public void setPatrocinado(boolean patrocinado) { this.patrocinado = patrocinado; }

    public TipoRegistro getTipoRegistro() { return tipoRegistro; }
    public void setTipoRegistro(TipoRegistro tipoRegistro) { this.tipoRegistro = tipoRegistro; }

    public Edicion getEdicion() { return edicion; }
    public void setEdicion(Edicion edicion) { this.edicion = edicion; }
}
