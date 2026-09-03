package logica.Clases;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTUsuarioAsist;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "asistente")
public class Asistente extends Usuario {

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    // Provisorio: no se persiste hasta terminar el mapeo de Institucion.
    @Transient
    private Institucion institucion;

    // Provisorio: no se persiste hasta terminar el mapeo de Registro.
    @Transient
    private List<Registro> registros = new ArrayList<>();

    protected Asistente() {
    }

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
