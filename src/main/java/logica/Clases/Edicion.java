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
    private  DTFecha fechaAlta;

    @Column(nullable = false)
    private  DTFecha fechaFin;

    @Column(nullable = false, length = 50)
    private String ciudad;

    @Column(nullable = false, length = 50)
    private String pais;

    @OneToMany(mappedBy = "edicion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TipoRegistro> tiposRegistro;

    public  Edicion(){
        this.tiposRegistro = new ArrayList<>();
    }

    public Edicion(String nombre, String sigla, DTFecha fechaAlta, DTFecha fechaFin, String ciudad, String pais){
        this.nombre = nombre;
        this.sigla = sigla;
        this.fechaAlta = fechaAlta;
        this.fechaFin = fechaFin;
        this.ciudad = ciudad;
        this.pais = pais;
        this.tiposRegistro = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String obtenerDetalles(){
        return "Nombre: " + nombre + "\nSigla: " + sigla + "\nFecha de alta: " + fechaAlta + "\nFecha de fin: " + fechaFin + "\nCiudad: " + ciudad + "\nPais: " + pais;
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
