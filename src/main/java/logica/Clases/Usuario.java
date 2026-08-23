package logica.Clases;

import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTUsuario;

public abstract class Usuario {

    private String nombre;
    private String nickname;
    private String correo;

    public Usuario(String nombre, String nickname, String correo) {
        this.nombre = nombre;
        this.nickname = nickname;
        this.correo = correo;
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

    // Para listar usuarios: nickname + nombre
    public DTUsuario getDTUsuario() {
        return new DTUsuario(nickname, nombre);
    }

    // Cada subtipo arma su propio DT detallado
    public abstract DTDatosUsuario getDTUsuarioDetallado();
}