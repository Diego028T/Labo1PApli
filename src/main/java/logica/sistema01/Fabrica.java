package logica.sistema01;

public final class Fabrica {

    private static final Fabrica instancia = new Fabrica();

    private Fabrica() {
    }

    public static Fabrica getInstancia() {
        return instancia;
    }

    public ISistema getISistema() {
        return Sistema.getInstancia();
    }
}
