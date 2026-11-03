package logica.sistema01;

import logica.DataTypes.DTRegistroEdicion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static logica.sistema01.Fixtures.*;
import static org.junit.jupiter.api.Assertions.*;

class RegistroEdicionTest {

    private Long idEdicion;
    private Long idTipo;
    private String institucion;

    @BeforeEach
    void preparar() {
        idEdicion = edicionAceptada(evento(), organizador());
        idTipo = tipoRegistro(idEdicion, 100f, 10);
        institucion = institucion();
    }

    private DTRegistroEdicion registroDe(String nickname) {
        return SISTEMA.listarRegistrosEdicion(idEdicion).stream()
                .filter(r -> r.getNicknameAsistente().equals(nickname))
                .findFirst().orElseThrow();
    }


    @Test
    void registroGeneralCobraElCostoDelTipo() {
        String asistente = asistente(null);
        SISTEMA.altaRegistro(asistente, idEdicion, idTipo, null);

        DTRegistroEdicion registro = registroDe(asistente);
        assertEquals(100.0, registro.getCosto(), 0.001);
        assertFalse(registro.isPatrocinado());
    }

    @Test
    void asistenteYaRegistradoFalla() {
        String asistente = asistente(null);
        SISTEMA.altaRegistro(asistente, idEdicion, idTipo, null);
        assertThrows(IllegalArgumentException.class,
                () -> SISTEMA.altaRegistro(asistente, idEdicion, idTipo, null));
    }

    @Test
    void cupoAgotadoFalla() {
        Long tipoChico = tipoRegistro(idEdicion, 100f, 1);
        SISTEMA.altaRegistro(asistente(null), idEdicion, tipoChico, null);
        String segundo = asistente(null);
        assertThrows(IllegalArgumentException.class,
                () -> SISTEMA.altaRegistro(segundo, idEdicion, tipoChico, null));
    }

    @Test
    void edicionNoAceptadaFalla() {
        Long ingresada = edicion(evento(), organizador());
        Long tipo = tipoRegistro(ingresada, 100f, 10);
        String asistente = asistente(null);
        assertThrows(IllegalArgumentException.class,
                () -> SISTEMA.altaRegistro(asistente, ingresada, tipo, null));
    }

    @Test
    void codigoValidoDejaCostoCero() {
        String codigo = patrocinio(idEdicion, institucion, idTipo, 2);
        String asistente = asistente(institucion);
        SISTEMA.altaRegistro(asistente, idEdicion, idTipo, codigo);

        DTRegistroEdicion registro = registroDe(asistente);
        assertEquals(0.0, registro.getCosto(), 0.001);
        assertTrue(registro.isPatrocinado());
    }

    @Test
    void codigoDeOtraInstitucionFalla() {
        String codigo = patrocinio(idEdicion, institucion, idTipo, 2);
        String deOtra = asistente(institucion());
        assertThrows(IllegalArgumentException.class,
                () -> SISTEMA.altaRegistro(deOtra, idEdicion, idTipo, codigo));
    }

    @Test
    void codigoDeOtroTipoFalla() {
        String codigo = patrocinio(idEdicion, institucion, idTipo, 2);
        Long otroTipo = tipoRegistro(idEdicion, 100f, 10);
        String asistente = asistente(institucion);
        assertThrows(IllegalArgumentException.class,
                () -> SISTEMA.altaRegistro(asistente, idEdicion, otroTipo, codigo));
    }

    @Test
    void codigoSinUsosFalla() {
        String codigo = patrocinio(idEdicion, institucion, idTipo, 1);
        SISTEMA.altaRegistro(asistente(institucion), idEdicion, idTipo, codigo);
        String segundo = asistente(institucion);
        assertThrows(IllegalArgumentException.class,
                () -> SISTEMA.altaRegistro(segundo, idEdicion, idTipo, codigo));
    }

    @Test
    void asistenteSinInstitucionNoPuedeUsarCodigo() {
        String codigo = patrocinio(idEdicion, institucion, idTipo, 2);
        String sinInstitucion = asistente(null);
        assertThrows(IllegalArgumentException.class,
                () -> SISTEMA.altaRegistro(sinInstitucion, idEdicion, idTipo, codigo));
    }

    @Test
    void codigoInexistenteFalla() {
        String asistente = asistente(institucion);
        assertThrows(IllegalArgumentException.class,
                () -> SISTEMA.altaRegistro(asistente, idEdicion, idTipo, "NO-EXISTE"));
    }
}