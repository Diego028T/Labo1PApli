package logica.sistema01;

import logica.DataTypes.EstadoEdicion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static logica.sistema01.Fixtures.*;
import static org.junit.jupiter.api.Assertions.*;

class EdicionEstadoTest {

    private Long idEvento;
    private String organizador;

    @BeforeEach
    void preparar() {
        idEvento = evento();
        organizador = organizador();
    }

    @Test
    void edicionNuevaQuedaIngresada() {
        Long id = edicion(idEvento, organizador);
        assertEquals(EstadoEdicion.INGRESADA, SISTEMA.mostrarDatosEdicion(id).getEstado());
    }

    @Test
    void ingresadaPasaAAceptada() {
        Long id = edicion(idEvento, organizador);
        SISTEMA.modificarEstadoEdicion(id, EstadoEdicion.APROBADA);
        assertEquals(EstadoEdicion.APROBADA, SISTEMA.mostrarDatosEdicion(id).getEstado());
    }

    @Test
    void ingresadaPasaARechazada() {
        Long id = edicion(idEvento, organizador);
        SISTEMA.modificarEstadoEdicion(id, EstadoEdicion.RECHAZADA);
        assertEquals(EstadoEdicion.RECHAZADA, SISTEMA.mostrarDatosEdicion(id).getEstado());
    }

    @Test
    void aceptadaNoPuedeCambiar() {
        Long id = edicionAceptada(idEvento, organizador);
        assertThrows(IllegalArgumentException.class,
                () -> SISTEMA.modificarEstadoEdicion(id, EstadoEdicion.RECHAZADA));
    }

    @Test
    void rechazadaNoPuedeVolverAIngresada() {
        Long id = edicion(idEvento, organizador);
        SISTEMA.modificarEstadoEdicion(id, EstadoEdicion.RECHAZADA);
        assertThrows(IllegalArgumentException.class,
                () -> SISTEMA.modificarEstadoEdicion(id, EstadoEdicion.INGRESADA));
    }

    @Test
    void listadosFiltranPorEstado() {
        Long ingresada = edicion(idEvento, organizador);
        Long aceptada = edicionAceptada(idEvento, organizador);

        assertTrue(SISTEMA.listarEdicionesIngresadas(idEvento).stream()
                .anyMatch(e -> e.getId().equals(ingresada)));
        assertFalse(SISTEMA.listarEdicionesIngresadas(idEvento).stream()
                .anyMatch(e -> e.getId().equals(aceptada)));
        assertTrue(SISTEMA.listarEdicionesAceptadas(idEvento).stream()
                .anyMatch(e -> e.getId().equals(aceptada)));
        assertFalse(SISTEMA.listarEdicionesAceptadas(idEvento).stream()
                .anyMatch(e -> e.getId().equals(ingresada)));
    }

    @Test
    void edicionInexistenteFalla() {
        assertThrows(IllegalArgumentException.class,
                () -> SISTEMA.modificarEstadoEdicion(-1L, EstadoEdicion.APROBADA));
    }
}