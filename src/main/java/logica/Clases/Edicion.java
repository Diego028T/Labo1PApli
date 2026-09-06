package logica.Clases;
import logica.DataTypes.DTFecha;

import java.util.List;
import java.util.ArrayList;


public class Edicion{
    private String nombre;
    private String sigla;
    private DTFecha fechaInicio;
    private DTFecha fechaFin;
    private DTFecha fechaAlta;
    private Organizador organizador;
    private String ciudad;
    private String pais;
    private List<TipoRegistro> tiposRegistro;

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

    public String obtenerDetalles() {
        return "Nombre: " + nombre
                + "\nSigla: " + sigla
                + "\nFecha de inicio: " + fechaInicio
                + "\nFecha de fin: " + fechaFin
                + "\nFecha de alta: " + fechaAlta
                + "\nCiudad: " + ciudad
                + "\nPaís: " + pais
                + "\nOrganizador: " + organizador.getNombre();
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
        return new ArrayList<>(tiposRegistro);
    }

    public void agregarTipoRegistro(TipoRegistro tipoRegistro) {
        if (tipoRegistro == null) {
            throw new IllegalArgumentException("El tipo de registro no puede ser null");
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

        tiposRegistro.add(tipoRegistro);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
