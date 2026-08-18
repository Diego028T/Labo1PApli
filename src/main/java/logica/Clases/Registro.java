package logica;

public class Registro {
    private DTFecha fecha;
    private double costo;
    private DTFecha fechaAlta;

    // Constructor
    public Registro(DTFecha fecha, double costo, DTFecha fechaAlta) {
        this.fecha = fecha;
        this.costo = costo;
        this.fechaAlta = fechaAlta;
    }

    public DTFecha getFecha() { return fecha; }
    public void setFecha(DTFecha fecha) { this.fecha = fecha; }

    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }

    public DTFecha getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(DTFecha fechaAlta) { this.fechaAlta = fechaAlta; }
}