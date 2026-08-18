package logica;

public class Organizador extends Usuario {
    private String descripcion;
    private String enlace;

    public Organizador(String nombre, String apellido, String correo, TipoUsuario tipoUsuario,
                       String descripcion, String enlace) {
        super(nombre, apellido, correo, tipoUsuario);
        this.descripcion = descripcion;
        this.enlace = enlace;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }
}