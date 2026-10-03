package logica.DataTypes;

public class DTRegistroEdicion {

    private final Long id;
    private final DTFecha fecha;
    private final String nicknameAsistente;
    private final String nombreTipoRegistro;
    private final double costo;
    private final boolean patrocinado;

    public DTRegistroEdicion(
            Long id,
            DTFecha fecha,
            String nicknameAsistente,
            String nombreTipoRegistro,
            double costo,
            boolean patrocinado) {
        this.id = id;
        this.fecha = fecha;
        this.nicknameAsistente = nicknameAsistente;
        this.nombreTipoRegistro = nombreTipoRegistro;
        this.costo = costo;
        this.patrocinado = patrocinado;
    }

    public Long getId() {
        return id;
    }

    public DTFecha getFecha() {
        return fecha;
    }

    public String getNicknameAsistente() {
        return nicknameAsistente;
    }

    public String getNombreTipoRegistro() {
        return nombreTipoRegistro;
    }

    public double getCosto() {
        return costo;
    }

    public boolean isPatrocinado() {
        return patrocinado;
    }
}