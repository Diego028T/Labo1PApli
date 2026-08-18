package logica

public class DTEdicion {
    private String nombre;
    private String descripcion;
    private String sigla;
    private DTFecha fechaAlta;

    public DTEdicion(String nombre, String descripcion, String sigla, DTFecha fechaAlta) {
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

    public String getCorreo() {
        return sigla;
    }

    public DTFecha getFechaAlta() {
        return sigla;
    }
}