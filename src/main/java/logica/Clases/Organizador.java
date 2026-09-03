package logica.Clases;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTUsuarioOrg;

@Entity
@Table(name = "organizador")
public class Organizador extends Usuario {

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(length = 200)
    private String enlace;

    protected Organizador() {
    }

    public Organizador(
            String nombre,
            String nickname,
            String correo,
            String descripcion,
            String enlace) {

        super(nombre, nickname, correo);
        this.descripcion = descripcion;
        this.enlace = enlace;
    }

    public String getNombre() {
        return super.getNombre();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    @Override
    public String toString() {
        return super.getNombre();
    }

    @Override
    public DTDatosUsuario getDTUsuarioDetallado() {
        return new DTUsuarioOrg(
                getNickname(),
                getNombre(),
                getCorreo(),
                descripcion,
                enlace
        );
    }
}
