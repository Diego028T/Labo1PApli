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

    @ManyToMany(mappedBy = "categorias", fetch = FetchType.LAZY)
    private List<Evento> eventos = new ArrayList<>();

    protected Categoria() {
        this.eventos = new ArrayList<>();
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