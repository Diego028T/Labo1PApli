package logica.Clases;
import  jakarta.persistence.*;


@Entity
@Table(name = "categoria")

public class Categoria{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    protected Categoria() {
    }

    public Categoria(String nombre){
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return  nombre;
    }
}