package logica.Clases;

public class TipoRegistro {
    private String nombre;
    private String descripcion;
    private float costo;
    private int cupo;

    public TipoRegistro (
            String nombre,
            String descripcion,
            float costo,
            int cupo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        this.cupo = cupo;
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
    public float getCosto() {
        return costo;
    }
    public void setCosto(float costo) {
        this.costo = costo;
    }
    public int getCupo() {
        return cupo;
    }
    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    @Override
    public String toString() {
        return nombre;
    }
//    public int getCantCupos() {
//        return cantCupos;
//    }
//    public void setCantCupos(int cantCupos) {
//        this.cantCupos = cantCupos;
//    }
}
