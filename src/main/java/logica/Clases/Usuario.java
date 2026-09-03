package logica.Clases;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTUsuario;

@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(nullable = false, unique = true, length = 120)
    private String correo;

    protected Usuario() {
    }

    public Usuario(String nombre, String nickname, String correo) {
        this.nombre = nombre;
        this.nickname = nickname;
        this.correo = correo;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCorreo() {
        return correo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public DTUsuario getDTUsuario() {
        return new DTUsuario(nickname, nombre);
    }

    public abstract DTDatosUsuario getDTUsuarioDetallado();
}