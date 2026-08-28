package logica.Clases;

import java.util.ArrayList;
import java.util.List;

public class Evento {
    private String nombre;
    private String descripcion;
    private String sigla;
    private final List<Edicion> ediciones;

    public Evento(String nombre, String descripcion, String sigla) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.sigla = sigla;
        this.ediciones = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setEdiciones(Edicion edicion) {
        if (edicion == null) {
            throw new IllegalArgumentException("La edición no puede ser null");
        }
        ediciones.add(edicion);
    }

    public List<Edicion> getEdiciones() {
        return new ArrayList<>(ediciones);
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


    @Override
    public String toString() {
        return nombre;
    }
}
