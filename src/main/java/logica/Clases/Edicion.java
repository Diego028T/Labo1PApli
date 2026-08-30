package logica.Clases;
import logica.DataTypes.DTFecha;

import java.util.List;
import java.util.ArrayList;


public class Edicion{
    private String nombre;
    private String sigla;
    private DTFecha fechaAlta;
    private DTFecha fechaFin;
    private String ciudad;
    private String pais;
    private List<TipoRegistro> tiposRegistro;

    public Edicion(String nombre, String sigla, DTFecha fechaAlta, DTFecha fechaFin, String ciudad, String pais){
        this.nombre = nombre;
        this.sigla = sigla;
        this.fechaAlta = fechaAlta;
        this.fechaFin = fechaFin;
        this.ciudad = ciudad;
        this.pais = pais;
        this.tiposRegistro = new ArrayList<>();
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
