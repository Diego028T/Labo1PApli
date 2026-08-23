package logica.DataTypes;

import java.time.LocalDate;

public class DTUsuarioAsist implements DTDatosUsuario {

    private String nickname;
    private String nombre;
    private String correo;
    private String apellido;
    private LocalDate fechaNacimiento;

    public DTUsuarioAsist(
            String nickname,
            String nombre,
            String correo,
            String apellido,
            LocalDate fechaNacimiento) {

        this.nickname = nickname;
        this.nombre = nombre;
        this.correo = correo;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNickname() {
        return nickname;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getApellido() {
        return apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
}