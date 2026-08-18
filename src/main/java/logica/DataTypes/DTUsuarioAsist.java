package logica

public class DTUsuarioAsist {
    private String apellido;
    private DTFecha fechaNacimiento;


    public DTUsuarioAsist(String apellido, DTFecha fechaNacimiento) {
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getApellido() {
        return apellido;
    }

    public DTFecha getfechaNacimiento() {
        return fechaNacimiento;
    }

}