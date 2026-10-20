package presentacionweb;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logica.DataTypes.DTImagen;
import logica.sistema01.Fabrica;
import logica.sistema01.ISistema;

import java.io.IOException;

@WebServlet("/imagen")
public class ImagenServlet extends HttpServlet {

    private final ISistema sistema = Fabrica.getInstancia().getISistema();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        DTImagen imagen = null;
        try {
            Long id = Long.valueOf(request.getParameter("id"));
            // ACA TIENEN QUE AGREGAR LA ENTIDAD QUE VAN A USAR PARA MOSTRAR SU IMAGEN
            if ("evento".equals(request.getParameter("tipo"))) {
                imagen = sistema.obtenerImagenEvento(id);
            }
        } catch (IllegalArgumentException e) { // id ausente o inexistente se trata como no imagen

        }

        if (imagen == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.setContentType(imagen.getImagenTipo());
        response.setContentLength(imagen.getImagen().length);
        response.getOutputStream().write(imagen.getImagen());
    }
}