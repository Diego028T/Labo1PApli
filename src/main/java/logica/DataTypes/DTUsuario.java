package logica.DataTypes;

/*el record en java es similar a reemplazar el datatype con todo los campos, record hace solo todo, los getters y etc*/
public record DTUsuario(String nickname, String nombre) {
}

/*package logica.DataTypes;

public class DTUsuario {
    private String nickname;
    private String nombre;
    private String correo;

    public DTUsuario(String nickname, String nombre, String correo) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.correo = correo;
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
}*/