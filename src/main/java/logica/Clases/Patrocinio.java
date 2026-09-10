package logica.Clases;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "patrocinio")
public class Patrocinio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String codigo;

    @Convert(converter = DTFechaConverter.class)
    @Column(nullable = false)
    private DTFecha fecha;

    @Column(nullable = false)
    private float montoAportado;

    @Column(nullable = false)
    private int cantRegistros;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NivelPatrocinio nivelPatrocinio;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "institucion_id", nullable = false)
    private Institucion institucion;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "edicion_id", nullable = false)
    private Edicion edicion;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_registro_id", nullable = false)
    private TipoRegistro tipoRegistro;

    protected Patrocinio() {
    }

    public Patrocinio(
            String codigo,
            DTFecha fecha,
            float montoAportado,
            int cantRegistros,
            NivelPatrocinio nivelPatrocinio,
            Institucion institucion,
            Edicion edicion,
            TipoRegistro tipoRegistro
    ) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.montoAportado = montoAportado;
        this.cantRegistros = cantRegistros;
        this.nivelPatrocinio = nivelPatrocinio;
        this.institucion = institucion;
        this.edicion = edicion;
        this.tipoRegistro = tipoRegistro;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public DTFecha getFecha() {
        return fecha;
    }

    public float getMontoAportado() {
        return montoAportado;
    }

    public int getCantRegistros() {
        return cantRegistros;
    }

    public NivelPatrocinio getNivelPatrocinio() {
        return nivelPatrocinio;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public Edicion getEdicion() {
        return edicion;
    }

    public TipoRegistro getTipoRegistro() {
        return tipoRegistro;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setFecha(DTFecha fecha) {
        this.fecha = fecha;
    }

    public void setMontoAportado(float montoAportado) {
        this.montoAportado = montoAportado;
    }

    public void setCantRegistros(int cantRegistros) {
        this.cantRegistros = cantRegistros;
    }

    public void setNivelPatrocinio(NivelPatrocinio nivelPatrocinio) {
        this.nivelPatrocinio = nivelPatrocinio;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public void setEdicion(Edicion edicion) {
        this.edicion = edicion;
    }

    public void setTipoRegistro(TipoRegistro tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }
}