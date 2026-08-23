package logica.Clases;

import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTUsuarioAsist;

import java.time.LocalDate;

public class Asistente extends Usuario {

    private String apellido;
    private LocalDate fechaNacimiento;

    public Asistente(
            String nombre,
            String nickname,
            String correo,
            String apellido,
            LocalDate fechaNacimiento) {

        super(nombre, nickname, correo);

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

    @Override
    public DTDatosUsuario getDTUsuarioDetallado() {

        return new DTUsuarioAsist(
                getNickname(),
                getNombre(),
                getCorreo(),
                apellido,
                fechaNacimiento
        );
    }
}