package logica.Clases;
import  jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "categoria")

public class Categoria{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_padre_id")
    private Categoria padre;

    @OneToMany(mappedBy = "padre")
    private List<Categoria> hijas = new ArrayList<>();

    @ManyToMany(mappedBy = "categorias", fetch = FetchType.LAZY)
    private List<Evento> eventos = new ArrayList<>();

    protected Categoria() {
        this.eventos = new ArrayList<>();
        this.hijas = new ArrayList<>();
    }

    public Categoria(String nombre){
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public Categoria getPadre() {
        return padre;
    }

    public void setPadre(Categoria padre) {
        this.padre = padre;
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
