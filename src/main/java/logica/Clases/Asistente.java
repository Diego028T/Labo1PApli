package logica.Clases;

import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTUsuarioAsist;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Asistente extends Usuario {

    private String apellido;
    private LocalDate fechaNacimiento;
    private Institucion institucion; // se usa en caso de que ese asistente tenga vinculo con una institucion
    private List<Registro> registros;

    public Asistente(
            String nombre,
            String nickname,
            String correo,
            String apellido,
            LocalDate fechaNacimiento) {

        super(nombre, nickname, correo);

        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.registros = new ArrayList<>();
    }

    public String getApellido() {
        return apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public List<Registro> getRegistros() {
        return new ArrayList<>(registros);
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public void agregarRegistro(Registro registro) {
        if (registro == null) {
            throw new IllegalArgumentException("El registro no puede ser null");
        }

        registros.add(registro);
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
