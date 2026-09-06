package logica.Clases;
import jakarta.persistence.*;
import logica.DataTypes.DTFecha;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "edicion")
public class Edicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 10)
    private String sigla;

    @Column(nullable = false)
    private DTFecha fechaInicio;

    @Column(nullable = false)
    private DTFecha fechaFin;

    @Column(nullable = false)
    private DTFecha fechaAlta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organizador_id", nullable = false)
    private Organizador organizador;

    @Column(nullable = false, length = 50)
    private String ciudad;

    @Column(nullable = false, length = 50)
    private String pais;

    @OneToMany(mappedBy = "edicion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TipoRegistro> tiposRegistro;

    public Edicion() {
        this.tiposRegistro = new ArrayList<>();
    }

    public Edicion(
            String nombre,
            String sigla,
            DTFecha fechaInicio,
            DTFecha fechaFin,
            DTFecha fechaAlta,
            String ciudad,
            String pais,
            Organizador organizador
    ) {
        this.nombre = nombre;
        this.sigla = sigla;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaAlta = fechaAlta;
        this.ciudad = ciudad;
        this.pais = pais;
        this.organizador = organizador;
        this.tiposRegistro = new ArrayList<>();
    }

        public Long getId() {
            return id;
        }

        public String obtenerDetalles() {
            return "Nombre: " + nombre
                    + "\nSigla: " + sigla
                    + "\nFecha de inicio: " + fechaInicio
                    + "\nFecha de fin: " + fechaFin
                    + "\nFecha de alta: " + fechaAlta
                    + "\nCiudad: " + ciudad
                    + "\nPaís: " + pais
                    + "\nOrganizador: "
                    + (organizador == null ? "Sin asignar" : organizador.getNombre());
        }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getSigla() {
        return sigla;
    }
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
    public DTFecha getFechaAlta() {
        return fechaAlta;
    }
    public void setFechaAlta(DTFecha fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public DTFecha getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(DTFecha fechaFin) {
        this.fechaFin = fechaFin;
    }
    public String getCiudad() {
        return ciudad;
    }
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    public String getPais() {
        return pais;
    }
    public void setPais(String pais) {
        this.pais = pais;
    }

    public DTFecha getFechaInicio() {
        return fechaInicio;
    }

    public Organizador getOrganizador() {
        return organizador;
    }

    public List<TipoRegistro> getTiposRegistro() {
        if (tiposRegistro == null) {
            tiposRegistro = new ArrayList<>();
        }

        return new ArrayList<>(tiposRegistro);
    }

    public void agregarTipoRegistro(TipoRegistro tipoRegistro) {
        if (tipoRegistro == null) {
            throw new IllegalArgumentException("El tipo de registro no puede ser null");
        }

        if (tiposRegistro == null) {
            tiposRegistro = new ArrayList<>();
        }

        String nombreNuevo = tipoRegistro.getNombre();

        if (nombreNuevo == null || nombreNuevo.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        boolean yaExiste = tiposRegistro.stream()
                .anyMatch(tipo -> tipo.getNombre().equalsIgnoreCase(nombreNuevo.trim()));

        if (yaExiste) {
            throw new IllegalArgumentException(
                    "Ya existe un tipo de registro con ese nombre para esta edición"
            );
        }

        tipoRegistro.setEdicion(this);
        tiposRegistro.add(tipoRegistro);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
