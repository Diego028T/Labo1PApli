package logica.Clases;

import logica.DataTypes.DTFecha;

import java.util.ArrayList;
import java.util.List;

public class Evento {

    private String nombre;
    private String descripcion;
    private String sigla;
    private DTFecha fechaAlta;

    private final List<Categoria> categorias;
    private final List<Edicion> ediciones;

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
        return new ArrayList<>(categorias);
    }

    public List<Edicion> getEdiciones() {
        return new ArrayList<>(ediciones);
    }

    public void agregarCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser null.");
        }

        boolean yaExiste = categorias.stream()
                .anyMatch(c -> c.getNombre()
                        .equalsIgnoreCase(categoria.getNombre()));

        if (yaExiste) {
            throw new IllegalArgumentException(
                    "La categoría ya está asociada al evento."
            );
        }

        categorias.add(categoria);
    }

    public void agregarEdicion(Edicion edicion) {
        if (edicion == null) {
            throw new IllegalArgumentException("La edición no puede ser null.");
        }

        ediciones.add(edicion);
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
}