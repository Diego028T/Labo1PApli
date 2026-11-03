package logica.sistema01;

import logica.DataTypes.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

final class Fixtures {

    static final ISistema SISTEMA = Fabrica.getInstancia().getISistema();
    static final char[] CLAVE = "Demo2026!".toCharArray();

    private Fixtures() {
    }

    static String unico(String prefijo) {
        return prefijo + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    static DTFecha fecha(int anio, int mes, int dia) {
        return new DTFecha(anio, mes, dia);
    }

    static DTFecha hoy() {
        LocalDate h = LocalDate.now();
        return new DTFecha(h.getYear(), h.getMonthValue(), h.getDayOfMonth());
    }

    static String organizador() {
        String nick = unico("org");
        SISTEMA.altaOrganizador(nick, "Org Test", nick + "@test.uy", "desc", "", CLAVE, CLAVE);
        return nick;
    }

    static String asistente(String institucion) {
        String nick = unico("asis");
        SISTEMA.altaAsistente(nick, "Asis", nick + "@test.uy", "Test",
                LocalDate.of(2000, 1, 1), institucion, CLAVE, CLAVE);
        return nick;
    }

    static String institucion() {
        String nombre = unico("Test-Inst");
        SISTEMA.altaInstitucion(nombre, "desc", "https://test.uy");
        return nombre;
    }

    static Long evento() {
        String nombre = unico("Test-Ev");
        SISTEMA.altaEvento(nombre, "desc", "TST", hoy(), List.of("Tecnología"));
        return SISTEMA.listarEventos().stream()
                .filter(e -> e.getNombre().equals(nombre))
                .findFirst().orElseThrow().getId();
    }

    // Edición nueva: queda INGRESADA
    static Long edicion(Long idEvento, String nickOrganizador) {
        String nombre = unico("Test-Ed");
        SISTEMA.altaEdicion(idEvento, nickOrganizador, nombre, "TED",
                fecha(2027, 5, 1), fecha(2027, 5, 3), hoy(), "Montevideo", "Uruguay");
        return SISTEMA.listarEdiciones(idEvento).stream()
                .filter(e -> e.getNombre().equals(nombre))
                .findFirst().orElseThrow().getId();
    }

    static Long edicionAceptada(Long idEvento, String nickOrganizador) {
        Long id = edicion(idEvento, nickOrganizador);
        SISTEMA.modificarEstadoEdicion(id, EstadoEdicion.APROBADA);
        return id;
    }

    static Long tipoRegistro(Long idEdicion, float costo, int cupo) {
        String nombre = unico("Tipo");
        SISTEMA.altaTipoRegistro(idEdicion, nombre, "desc", costo, cupo);
        return SISTEMA.listarTiposRegistro(idEdicion).stream()
                .filter(t -> t.getNombre().equals(nombre))
                .findFirst().orElseThrow().getId();
    }

    static String patrocinio(Long idEdicion, String institucion, Long idTipo, int cantRegistros) {
        String codigo = unico("COD");
        SISTEMA.altaPatrocinio(idEdicion, institucion, idTipo, NivelPatrocinio.ORO,
                100000f, cantRegistros, codigo, hoy());
        return codigo;
    }
}