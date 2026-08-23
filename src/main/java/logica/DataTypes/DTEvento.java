package logica.DataTypes;

public class DTEvento {
    private String nombre;
    private String descripcion;
    private String sigla;
    private DTFecha fechaAlta;

    public DTEvento(String nombre, String descripcion, String sigla, DTFecha fechaAlta) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.sigla = sigla;
        this.fechaAlta = fechaAlta;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getSigla() {
        return sigla;
    }

    public DTFecha getFechaAlta() {
        return fechaAlta;
    }
}