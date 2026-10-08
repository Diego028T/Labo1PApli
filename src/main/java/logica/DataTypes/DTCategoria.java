package logica.DataTypes;

/** Datos de categoría usados por la interfaz para reconstruir la jerarquía. */
public record DTCategoria(Long id, String nombre, Long idPadre) {
    @Override
    public String toString() {
        return nombre;
    }
}
