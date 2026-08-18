package logica;

public enum NivelPatrocinio {PLATINO, ORO, PLATA, BRONCE}

public class Patrocinio {
    private String codigo;
    private DTFecha fecha;
    private float montoAportado;
    private int cantRegistros;
    private NivelPatrocinio nivelPatrocinio;

    public Patrocinio(String codigo, DTFecha fecha, float montoAportado, int cantRegistros, NivelPatrocinio nivelPatrocinio) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.montoAportado = montoAportado;
        this.cantRegistros = cantRegistros;
        this.nivelPatrocinio = nivelPatrocinio;
    }

    public String getCodigo() { return codigo; }
    public DTFecha getFecha() { return fecha; }
    public float getMontoAportado() { return montoAportado; }
    public int getCantRegistros() { return cantRegistros; }
    public NivelPatrocinio getNivelPatrocinio() { return nivelPatrocinio; }
}