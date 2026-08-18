package logica;

import java.time.LocalDate;

public class Asistente extends Usuario {
    private String apellido;
    private LocalDate fechaNacimiento;

    public Asistente(String nombre, String nickname, String correo, TipoUsuario tipoUsuario, String apellido, LocalDate fechaNacimiento) {
        super(nombre, nickname, correo, tipoUsuario);
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getApellido() {
        return apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}