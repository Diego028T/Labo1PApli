package logica.DataTypes;

import java.util.List;

public class DTEvento {

    private final Long id;
    private final String nombre;
    private final String descripcion;
    private final String sigla;
    private final DTFecha fechaAlta;
    private final List<String> categorias;
    private final boolean tieneImagen;

    public DTEvento(
            Long id,
            String nombre,
            String descripcion,
            String sigla,
            DTFecha fechaAlta,
            List<String> categorias,
            boolean tieneImagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.sigla = sigla;
        this.fechaAlta = fechaAlta;
        this.categorias = List.copyOf(categorias);
        this.tieneImagen = tieneImagen;
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

    public String getSigla() {
        return sigla;
    }

    public DTFecha getFechaAlta() {
        return fechaAlta;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public boolean isTieneImagen() {
        return tieneImagen;
    }

    @Override
    public String toString() {
        return nombre;
    }
}