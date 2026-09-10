package logica.Clases;

import jakarta.persistence.*;
import logica.DataTypes.DTFecha;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "organizador_id", nullable = false)
    private Organizador organizador;

    @Column(nullable = false, length = 50)
    private String ciudad;

    @Column(nullable = false, length = 50)
    private String pais;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

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

    public Edicion(
            String nombre,
            String sigla,
            DTFecha fechaInicio,
            DTFecha fechaFin,
            DTFecha fechaAlta,
            String ciudad,
            String pais,
            Organizador organizador,
            Evento evento
    ) {
        this(nombre, sigla, fechaInicio, fechaFin, fechaAlta, ciudad, pais, organizador);
        this.evento = evento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setFechaInicio(DTFecha fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Organizador getOrganizador() {
        return organizador;
    }

    public void setOrganizador(Organizador organizador) {
        this.organizador = organizador;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public List<TipoRegistro> getTiposRegistro() {
        if (tiposRegistro == null) {
            return new ArrayList<>();
        }
        try {
            return new ArrayList<>(tiposRegistro);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void setTiposRegistro(List<TipoRegistro> tiposRegistro) {
        this.tiposRegistro = tiposRegistro != null ? tiposRegistro : new ArrayList<>();
    }

    public void agregarTipoRegistro(TipoRegistro tipoRegistro) {
        if (tipoRegistro == null) {
            throw new IllegalArgumentException("El tipo de registro no puede ser null");
        }

        String nombreNuevo = tipoRegistro.getNombre();

        if (nombreNuevo == null || nombreNuevo.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        try {
            if (tiposRegistro != null) {
                boolean yaExiste = tiposRegistro.stream()
                        .anyMatch(tipo -> tipo.getNombre().equalsIgnoreCase(nombreNuevo.trim()));

                if (yaExiste) {
                    throw new IllegalArgumentException(
                            "Ya existe un tipo de registro con ese nombre para esta edición"
                    );
                }
            }
        } catch (org.hibernate.LazyInitializationException e) {
            // Colección no inicializada fuera de sesión JPA activa
        }

        tipoRegistro.setEdicion(this);
        try {
            if (tiposRegistro != null) {
                tiposRegistro.add(tipoRegistro);
            }
        } catch (org.hibernate.LazyInitializationException e) {
            // Colección no inicializada fuera de sesión JPA activa
        }
    }

    @Override
    public String toString() {
        return nombre;
    }
}
