package logica.DataTypes;

public class DTTipoRegistro {

    private final Long id;
    private final String nombre;
    private final String descripcion;
    private final float costo;
    private final int cupo;

    public DTTipoRegistro(
            Long id,
            String nombre,
            String descripcion,
            float costo,
            int cupo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        this.cupo = cupo;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public float getCosto() {
        return costo;
    }

    public int getCupo() {
        return cupo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}