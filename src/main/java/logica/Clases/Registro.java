package logica.Clases;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import logica.DataTypes.DTFecha;
import logica.DataTypes.DTFechaConverter;

@Entity
@Table(name = "registro")
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = DTFechaConverter.class)
    @Column(nullable = false)
    private DTFecha fecha;

    @Column(nullable = false)
    private double costo;

    @Column(nullable = false)
    private boolean patrocinado;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "asistente_id", nullable = false)
    private Asistente asistente;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_registro_id", nullable = false)
    private TipoRegistro tipoRegistro;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "edicion_id", nullable = false)
    private Edicion edicion;

    protected Registro() {
    }

    public Registro(
            DTFecha fecha,
            double costo,
            boolean patrocinado,
            Asistente asistente,
            TipoRegistro tipoRegistro,
            Edicion edicion
    ) {
        this.fecha = fecha;
        this.costo = costo;
        this.patrocinado = patrocinado;
        this.asistente = asistente;
        this.tipoRegistro = tipoRegistro;
        this.edicion = edicion;
    }

    public Long getId() {
        return id;
    }

    public DTFecha getFecha() {
        return fecha;
    }

    public void setFecha(DTFecha fecha) {
        this.fecha = fecha;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public boolean isPatrocinado() {
        return patrocinado;
    }

    public void setPatrocinado(boolean patrocinado) {
        this.patrocinado = patrocinado;
    }

    public Asistente getAsistente() {
        return asistente;
    }

    public void setAsistente(Asistente asistente) {
        this.asistente = asistente;
    }

    public TipoRegistro getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(TipoRegistro tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public Edicion getEdicion() {
        return edicion;
    }

    public void setEdicion(Edicion edicion) {
        this.edicion = edicion;
    }
}