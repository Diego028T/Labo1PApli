package logica.sistema01;

import logica.Clases.PasswordService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordServiceTest {

    @Test
    void contrasenaCorrectaEsAceptada() {
        // Preparación: generar lo que se guardaría en la base.
        String hash = PasswordService.hash(
                "Demo2026!".toCharArray()
        );

        // Ejecución: comprobar la contraseña ingresada.
        boolean resultado = PasswordService.verify(
                "Demo2026!".toCharArray(),
                hash
        );

        // Verificación: debe aceptarla.
        assertTrue(resultado);
    }

    @Test
    void contrasenaIncorrectaEsRechazada() {
        String hash = PasswordService.hash(
                "Demo2026!".toCharArray()
        );

        boolean resultado = PasswordService.verify(
                "OtraContrasena!".toCharArray(),
                hash
        );

        assertFalse(resultado);
    }

    @Test
    void noPermiteCrearHashDeContrasenaNula() {
        assertThrows(
                IllegalArgumentException.class,
                () -> PasswordService.hash(null)
        );
    }

    @Test
    void noPermiteCrearHashDeContrasenaVacia() {
        assertThrows(
                IllegalArgumentException.class,
                () -> PasswordService.hash(new char[0])
        );
    }

    @Test
    void hashConFormatoInvalidoEsRechazado() {
        boolean resultado = PasswordService.verify(
                "Demo2026!".toCharArray(),
                "esto-no-es-un-hash-valido"
        );

        assertFalse(resultado);
    }
}