package logica.sistema01;

import logica.DataTypes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static logica.sistema01.Fixtures.*;
import static org.junit.jupiter.api.Assertions.*;

class AltasYConsultasTest {

    private String organizador;
    private Long idEvento;
    private Long idEdicion;
    private Long idTipo;

    @BeforeEach
    void preparar() {
        organizador = organizador();
        idEvento = evento();
        idEdicion = edicionAceptada(idEvento, organizador);
        idTipo = tipoRegistro(idEdicion, 100f, 10);
    }


    @Test
    void patrocinioQueSuperaElVeintePorCientoFalla() {
        assertThrows(IllegalArgumentException.class, () ->
                SISTEMA.altaPatrocinio(idEdicion, institucion(), idTipo, NivelPatrocinio.PLATA,
                        100000f, 300, unico("COD"), hoy()));
        // 300 registros * 100 = 30.000 y es mayo al  20 % de 100.000
    }

    @Test
    void institucionQueYaPatrocinaFalla() {
        String inst = institucion();
        patrocinio(idEdicion, inst, idTipo, 1);
        assertThrows(IllegalArgumentException.class, () -> patrocinio(idEdicion, inst, idTipo, 1));
    }

    @Test
    void codigoDePatrocinioRepetidoFalla() {
        String codigo = patrocinio(idEdicion, institucion(), idTipo, 1);
        assertThrows(IllegalArgumentException.class, () ->
                SISTEMA.altaPatrocinio(idEdicion, institucion(), idTipo, NivelPatrocinio.BRONCE,
                        100000f, 1, codigo, hoy()));
    }

    @Test
    void aporteOCantidadInvalidosFallan() {
        assertThrows(IllegalArgumentException.class, () ->
                SISTEMA.altaPatrocinio(idEdicion, institucion(), idTipo, NivelPatrocinio.ORO,
                        0f, 1, unico("COD"), hoy()));
        assertThrows(IllegalArgumentException.class, () ->
                SISTEMA.altaPatrocinio(idEdicion, institucion(), idTipo, NivelPatrocinio.ORO,
                        1000f, 0, unico("COD"), hoy()));
    }

    @Test
    void listarPatrociniosDevuelveSusDatos() {
        String inst = institucion();
        String codigo = patrocinio(idEdicion, inst, idTipo, 2);

        DTPatrocinio p = SISTEMA.listarPatrocinios(idEdicion).get(0);
        assertEquals(codigo, p.getCodigo());
        assertEquals(inst, p.getNombreInstitucion());
        assertEquals(NivelPatrocinio.ORO, p.getNivelPatrocinio());
        assertEquals(2, p.getCantRegistros());
        assertNotNull(p.getNombreTipoRegistro());
        assertNotNull(p.getFecha());
    }


    @Test
    void tipoRegistroConCupoCeroFalla() {
        assertThrows(IllegalArgumentException.class, () -> tipoRegistro(idEdicion, 100f, 0));
    }

    @Test
    void tipoRegistroRepetidoEnLaEdicionFalla() {
        SISTEMA.altaTipoRegistro(idEdicion, "General", "d", 50f, 5);
        assertThrows(IllegalArgumentException.class, () ->
                SISTEMA.altaTipoRegistro(idEdicion, "General", "d", 50f, 5));
    }

    @Test
    void tipoRegistroPorOtroOrganizadorFalla() {
        String otro = organizador();
        assertThrows(SecurityException.class, () ->
                SISTEMA.altaTipoRegistro(otro, idEdicion, unico("T"), "d", 50f, 5));
    }

    @Test
    void tipoRegistroPorSuOrganizadorFunciona() {
        SISTEMA.altaTipoRegistro(organizador, idEdicion, "Propio", "d", 50f, 5);
        assertTrue(SISTEMA.listarTiposRegistro(idEdicion).stream()
                .anyMatch(t -> t.getNombre().equals("Propio") && t.getCupo() == 5));
    }


    @Test
    void institucionRepetidaFalla() {
        String inst = institucion();
        assertThrows(IllegalArgumentException.class, () ->
                SISTEMA.altaInstitucion(inst, "d", "https://test.uy"));
    }

    @Test
    void institucionConSitioWebInvalidoFalla() {
        assertThrows(IllegalArgumentException.class, () ->
                SISTEMA.altaInstitucion(unico("Test-Inst"), "d", "sin-protocolo"));
    }

    @Test
    void institucionConImagenSePuedeRecuperar() {
        String nombre = unico("Test-Inst");
        SISTEMA.altaInstitucion(nombre, "d", "https://test.uy", new byte[]{1, 2}, "image/png");
        assertArrayEquals(new byte[]{1, 2}, SISTEMA.obtenerImagenInstitucion(nombre).getImagen());
        assertTrue(SISTEMA.listarNombresInstituciones().contains(nombre));
    }

    @Test
    void categoriaRepetidaOPadreInexistenteFallan() {
        String nombre = unico("test-cat");
        SISTEMA.altaCategoria(nombre, null);
        assertThrows(IllegalArgumentException.class, () -> SISTEMA.altaCategoria(nombre, null));
        assertThrows(IllegalArgumentException.class, () -> SISTEMA.altaCategoria(unico("Test-Cat"), -1L));
        assertTrue(SISTEMA.listarNombresCategorias().contains(nombre));
        assertFalse(SISTEMA.listarCategoriasJerarquicas().isEmpty());
    }


    @Test
    void eventoConCategoriaInexistenteFalla() {
        assertThrows(IllegalArgumentException.class, () ->
                SISTEMA.altaEvento(unico("Test-Ev"), "d", "T", hoy(), List.of("No-Existe-" + unico("c"))));
    }

    @Test
    void eventosPorCategoriaYDetalle() {
        String cat = unico("test-cat");
        SISTEMA.altaCategoria(cat, null);
        String nombre = unico("Test-Ev");
        SISTEMA.altaEvento(nombre, "d", "T", hoy(), List.of(cat));

        List<DTEvento> eventos = SISTEMA.listarEventosPorCategoria(cat);
        assertEquals(1, eventos.size());
        assertEquals(nombre, SISTEMA.mostrarDatosEvento(eventos.get(0).getId()).getNombre());
        assertThrows(IllegalArgumentException.class, () -> SISTEMA.mostrarDatosEvento(-1L));
    }

    @Test
    void edicionConNombreRepetidoFalla() {
        String nombre = SISTEMA.mostrarDatosEdicion(idEdicion).getNombre();
        assertThrows(IllegalArgumentException.class, () ->
                SISTEMA.altaEdicion(idEvento, organizador, nombre, "S",
                        fecha(2027, 1, 1), fecha(2027, 1, 2), hoy(), "C", "P"));
    }

    @Test
    void edicionConFinAnteriorAlInicioFalla() {
        assertThrows(IllegalArgumentException.class, () ->
                SISTEMA.altaEdicion(idEvento, organizador, unico("Test-Ed"), "S",
                        fecha(2027, 5, 10), fecha(2027, 5, 1), hoy(), "C", "P"));
    }

    @Test
    void edicionesDelOrganizadorYConImagen() {
        SISTEMA.altaEdicion(idEvento, organizador, unico("Test-Ed"), "S",
                fecha(2027, 6, 1), fecha(2027, 6, 2), hoy(), "C", "P", new byte[]{9}, "image/png");

        List<DTEdicion> suyas = SISTEMA.listarEdicionesOrganizador(organizador);
        assertEquals(2, suyas.size());
        DTEdicion conImagen = suyas.stream().filter(DTEdicion::isTieneImagen).findFirst().orElseThrow();
        assertArrayEquals(new byte[]{9}, SISTEMA.obtenerImagenEdicion(conImagen.getId()).getImagen());
    }


    @Test
    void consultaDeRegistroDelAsistente() {
        String asistente = asistente(null);
        SISTEMA.altaRegistro(asistente, idEdicion, idTipo, null);

        List<DTRegistroMin> registros = SISTEMA.listarRegistrosAsistente(asistente);
        assertEquals(1, registros.size());
        assertEquals(idEdicion, registros.get(0).getIdEdicion());

        DTRegistro detalle = SISTEMA.mostrarDatosRegistro(asistente, registros.get(0).getId());
        assertEquals(100.0, detalle.getCosto(), 0.001);
        assertNotNull(detalle.getNombreEdicion());
        assertNotNull(detalle.getNombreTipoRegistro());
    }


    @Test
    void organizadorConImagenSePuedeRecuperar() {
        String nick = unico("org");
        SISTEMA.altaOrganizador(nick, "O", nick + "@test.uy", "d", "", CLAVE, CLAVE,
                new byte[]{7}, "image/png");
        assertArrayEquals(new byte[]{7}, SISTEMA.obtenerImagenUsuario(nick).getImagen());
        assertNull(SISTEMA.obtenerImagenUsuario(organizador()));
    }
}