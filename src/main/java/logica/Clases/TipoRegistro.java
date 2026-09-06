package logica.Clases;

import  jakarta.persistence.*;

@Entity
@Table(name = "tipo_registro")
@Inheritance(strategy = InheritanceType.JOINED)

public class TipoRegistro{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(nullable = false)
    private float costo;

    @Column(nullable = false)
    private int cupo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "edicion_id", nullable = false)
    private Edicion edicion;

    protected TipoRegistro(){

    }

    public TipoRegistro (String nombre, String descripcion, float costo, int cupo) {
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
    public Long getId() {
        return id;
    }

    public void setEdicion(Edicion edicion) {
        this.edicion = edicion;
    }

    public Edicion getEdicion() {
        return edicion;
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
