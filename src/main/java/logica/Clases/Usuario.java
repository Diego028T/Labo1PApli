package logica;

public abstract class Usuario {
    private String nombre;
    private String nickname;
    private String correo;
    private TipoUsuario tipoUsuario;

    public Usuario(String nombre, String nickname, String correo, TipoUsuario tipoUsuario) {
        this.nombre = nombre;
        this.nickname = nickname;
        this.correo = correo;
        this.tipoUsuario = tipoUsuario;
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

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
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

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}