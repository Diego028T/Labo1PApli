package logica

public class DTUsuarioOrg {
    private String descripcion;
    private String enlace;


    public DTUsuarioOrg(String descripcion, String enlace) {
        this.descripcion = descripcion;
        this.enlace = enlace;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEnlace() {
        return enlace;
    }
