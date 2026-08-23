package logica.DataTypes;

public class DTUsuarioOrg implements DTDatosUsuario {

    private String nickname;
    private String nombre;
    private String correo;
    private String descripcion;
    private String enlace;

    public DTUsuarioOrg(
            String nickname,
            String nombre,
            String correo,
            String descripcion,
            String enlace) {

        this.nickname = nickname;
        this.nombre = nombre;
        this.correo = correo;
        this.descripcion = descripcion;
        this.enlace = enlace;
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

    public String getDescripcion() {
        return descripcion;
    }

    public String getEnlace() {
        return enlace;
    }
}
