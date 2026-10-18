package logica.DataTypes;

public class DTImagen {
    final byte[] imagen;
    final String imagenTipo;

    public DTImagen(byte[] imagen, String imagenTipo){
        this.imagen = imagen;
        this.imagenTipo = imagenTipo;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public String getImagenTipo() {
        return imagenTipo;
    }

}
