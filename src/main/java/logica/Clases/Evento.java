package logica;

public class Evento {
    private String nombre;
    private String descripcion;
    private Sring sigla;

    public Evento(String nombre, String descripcion, String sigla) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.sigla = sigla;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}