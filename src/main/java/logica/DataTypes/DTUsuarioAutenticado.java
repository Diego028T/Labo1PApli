package logica.DataTypes;

public record DTUsuarioAutenticado(
        String nickname,
        String nombre,
        RolUsuario rol
) {
}