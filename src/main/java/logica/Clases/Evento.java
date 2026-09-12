package logica.Clases;

import jakarta.persistence.*;
import logica.DataTypes.DTFecha;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String descripcion;

    @Column(nullable = false, length = 10)
    private String sigla;

    @Column(nullable = false)
    private DTFecha fechaAlta;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "evento_categoria",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria")
    )
    private final List<Categoria> categorias;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Edicion> ediciones;

    public Evento(){
        this.categorias = new ArrayList<>();
        this.ediciones = new ArrayList<>();
    }

    public Evento(
            String nombre,
            String descripcion,
            String sigla,
            DTFecha fechaAlta
    ) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.sigla = sigla;
        this.fechaAlta = fechaAlta;
        this.categorias = new ArrayList<>();
        this.ediciones = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getSigla() {
        return sigla;
    }

    public DTFecha getFechaAlta() {
        return fechaAlta;
    }

    public List<Categoria> getCategorias() {
        if (categorias == null) {
            return new ArrayList<>();
        }
        try {
            return new ArrayList<>(categorias);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<Edicion> getEdiciones() {
        if (ediciones == null) {
            return new ArrayList<>();
        }
        try {
            return new ArrayList<>(ediciones);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void agregarCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser null.");
        }

        try {
            boolean yaExiste = categorias.stream()
                    .anyMatch(c -> c.getNombre()
                            .equalsIgnoreCase(categoria.getNombre()));

            if (yaExiste) {
                throw new IllegalArgumentException(
                        "La categoría ya está asociada al evento."
                );
            }

            categorias.add(categoria);
        } catch (org.hibernate.LazyInitializationException e) {
            // Colección no inicializada fuera de sesión JPA activa
        }
    }

    public void reemplazarCategorias(List<Categoria> categoriasNuevas) {
        categorias.clear();
        categorias.addAll(categoriasNuevas);
    }

    public void agregarEdicion(Edicion edicion) {
        if (edicion == null) {
            throw new IllegalArgumentException("La edición no puede ser null.");
        }

        edicion.setEvento(this);
        try {
            ediciones.add(edicion);
        } catch (org.hibernate.LazyInitializationException e) {
            // Colección no inicializada fuera de sesión JPA activa
        }
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public void setFechaAlta(DTFecha fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public Long getId() {
        return id;
    }
}