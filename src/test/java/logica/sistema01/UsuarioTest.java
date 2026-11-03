package logica.sistema01;

import logica.DataTypes.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static logica.sistema01.Fixtures.*;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void altaAsistenteGuardaSusDatos() {
        String nick = asistente(null);
        DTDatosUsuario datos = SISTEMA.mostrarDatosUsuario(nick);

        DTUsuarioAsist asistente = assertInstanceOf(DTUsuarioAsist.class, datos);
        assertEquals("Asis", asistente.getNombre());
        assertEquals("Test", asistente.getApellido());
    }

    @Test
    void altaOrganizadorGuardaSusDatos() {
        String nick = organizador();
        assertInstanceOf(DTUsuarioOrg.class, SISTEMA.mostrarDatosUsuario(nick));
    }

    @Test
    void nicknameRepetidoFalla() {
        String nick = asistente(null);
        assertThrows(IllegalArgumentException.class, () ->
                SISTEMA.altaOrganizador(nick, "Otro", unico("c") + "@test.uy", "d", "", CLAVE, CLAVE));
    }

    @Test
    void correoRepetidoFalla() {
        String nick = asistente(null);
        assertThrows(IllegalArgumentException.class, () ->
                SISTEMA.altaOrganizador(unico("org"), "Otro", nick + "@test.uy", "d", "", CLAVE, CLAVE));
    }

    @Test
    void contrasenasDistintasFallan() {
        assertThrows(IllegalArgumentException.class, () ->
                SISTEMA.altaOrganizador(unico("org"), "Org", unico("c") + "@test.uy", "d", "",
                        "Clave123!".toCharArray(), "Otra456!".toCharArray()));
    }

    @Test
    void fechaNacimientoFuturaFalla() {
        assertThrows(IllegalArgumentException.class, () ->
                SISTEMA.altaAsistente(unico("asis"), "A", unico("c") + "@test.uy", "T",
                        LocalDate.now().plusDays(1), null, CLAVE, CLAVE));
    }

    @Test
    void chequearUsuarioDetectaRepetidos() {
        String nick = asistente(null);
        String correo = nick + "@test.uy";

        assertEquals(EstadoAltaUsuario.OK, SISTEMA.chequearUsuario(unico("n"), unico("c") + "@test.uy"));
        assertEquals(EstadoAltaUsuario.NICKNAME_REPETIDO, SISTEMA.chequearUsuario(nick, unico("c") + "@test.uy"));
        assertEquals(EstadoAltaUsuario.CORREO_REPETIDO, SISTEMA.chequearUsuario(unico("n"), correo));
        assertEquals(EstadoAltaUsuario.NICKNAME_Y_CORREO_REPETIDOS, SISTEMA.chequearUsuario(nick, correo));
    }


    @Test
    void autenticarPorNicknameYPorCorreo() {
        String nick = organizador();

        DTUsuarioAutenticado porNick = SISTEMA.autenticar(nick, CLAVE);
        assertNotNull(porNick);
        assertEquals(nick, porNick.nickname());
        assertEquals(RolUsuario.ORGANIZADOR, porNick.rol());

        assertNotNull(SISTEMA.autenticar(nick + "@test.uy", CLAVE));
    }

    @Test
    void autenticarConContrasenaIncorrectaDevuelveNull() {
        String nick = asistente(null);
        assertNull(SISTEMA.autenticar(nick, "Incorrecta1!".toCharArray()));
    }

    @Test
    void autenticarUsuarioInexistenteODatosVaciosDevuelveNull() {
        assertNull(SISTEMA.autenticar("no-existe-" + unico("x"), CLAVE));
        assertNull(SISTEMA.autenticar("", CLAVE));
        assertNull(SISTEMA.autenticar(null, CLAVE));
    }


    @Test
    void modificarDatosCambiaNombreYContrasena() {
        String nick = asistente(null);
        char[] nueva = "Nueva789!".toCharArray();

        SISTEMA.modificarDatosUsuario(
                new DTUsuarioAsist(nick, "Nombre Nuevo", nick + "@test.uy", "Apellido Nuevo",
                        LocalDate.of(1999, 5, 5)),
                nueva, nueva);

        DTUsuarioAsist datos = (DTUsuarioAsist) SISTEMA.mostrarDatosUsuario(nick);
        assertEquals("Nombre Nuevo", datos.getNombre());
        assertNotNull(SISTEMA.autenticar(nick, "Nueva789!".toCharArray()));
        assertNull(SISTEMA.autenticar(nick, CLAVE));
    }

    @Test
    void modificarConConfirmacionDistintaFalla() {
        String nick = asistente(null);
        assertThrows(IllegalArgumentException.class, () ->
                SISTEMA.modificarDatosUsuario(
                        new DTUsuarioAsist(nick, "N", nick + "@test.uy", "A", LocalDate.of(1999, 5, 5)),
                        "Nueva789!".toCharArray(), "Otra000!".toCharArray()));
    }

    @Test
    void listarUsuariosIncluyeLosNuevos() {
        String nick = organizador();
        assertTrue(SISTEMA.listarUsuarios().stream().anyMatch(u -> u.getNickname().equals(nick)));
    }
}