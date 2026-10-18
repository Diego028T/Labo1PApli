package logica.sistema01;

import logica.DataTypes.DTEvento;
import logica.DataTypes.DTFecha;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AltaEventoTest {

    private final ISistema sistema = Fabrica.getInstancia().getISistema();
    private final DTFecha hoy = new DTFecha(2026, 10, 18);
    private final List<String> categorias = List.of("Tecnología");

    // Nombres "Test-..." para poder limpiarlos de la base antes de la defensa
    private String nombreUnico() {
        return "Test-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private DTEvento buscar(String nombre) {
        return sistema.listarEventos().stream()
                .filter(e -> e.getNombre().equals(nombre))
                .findFirst()
                .orElseThrow();
    }

    @Test
    void altaSinImagen() {
        String nombre = nombreUnico();
        sistema.altaEvento(nombre, "desc", "TST", hoy, categorias);

        DTEvento evento = buscar(nombre);
        assertFalse(evento.isTieneImagen());
        assertNull(sistema.obtenerImagenEvento(evento.getId()));
    }

    @Test
    void altaConImagen() {
        String nombre = nombreUnico();
        byte[] bytes = {1, 2, 3};
        sistema.altaEvento(nombre, "desc", "TST", hoy, categorias, bytes, "image/png");

        DTEvento evento = buscar(nombre);
        assertTrue(evento.isTieneImagen());
        assertArrayEquals(bytes, sistema.obtenerImagenEvento(evento.getId()).getImagen());
        assertEquals("image/png", sistema.obtenerImagenEvento(evento.getId()).getImagenTipo());
    }

    @Test
    void tipoNoImagenFalla() {
        assertThrows(IllegalArgumentException.class, () ->
                sistema.altaEvento(nombreUnico(), "desc", "TST", hoy, categorias,
                        new byte[]{1}, "application/pdf"));
    }

    @Test
    void imagenMayorA5MBFalla() {
        byte[] grande = new byte[5 * 1024 * 1024 + 1];
        assertThrows(IllegalArgumentException.class, () ->
                sistema.altaEvento(nombreUnico(), "desc", "TST", hoy, categorias,
                        grande, "image/png"));
    }

    @Test
    void nombreRepetidoFalla() {
        String nombre = nombreUnico();
        sistema.altaEvento(nombre, "desc", "TST", hoy, categorias);
        assertThrows(IllegalArgumentException.class, () ->
                sistema.altaEvento(nombre, "otra", "TS2", hoy, categorias));
    }
}