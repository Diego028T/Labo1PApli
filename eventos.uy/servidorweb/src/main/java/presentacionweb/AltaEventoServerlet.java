package presentacionweb;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logica.sistema01.Fabrica;
import logica.sistema01.ISistema;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import logica.DataTypes.DTEvento;
import logica.DataTypes.DTFecha;
import java.time.LocalDate;
import java.util.List;

import java.io.IOException;

@WebServlet("/eventos/nuevo")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class AltaEventoServerlet extends HttpServlet {

    private final ISistema sistema = Fabrica.getInstancia().getISistema();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("categorias", sistema.listarNombresCategorias());
        request.getRequestDispatcher("/WEB-INF/vistas/eventos/evento_nuevo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String nombre = request.getParameter("nombre");
            String[] seleccion = request.getParameterValues("categorias");
            List<String> categorias = seleccion == null ? List.of() : List.of(seleccion);

            byte[] imagen = null;
            String tipoImagen = null;
            Part parte = request.getPart("imagen");
            if (parte != null && parte.getSize() > 0) {
                imagen = parte.getInputStream().readAllBytes();
                tipoImagen = parte.getContentType();
            }

            LocalDate hoy = LocalDate.now();
            sistema.altaEvento(nombre, request.getParameter("descripcion"), request.getParameter("sigla"), new DTFecha(hoy.getYear(), hoy.getMonthValue(), hoy.getDayOfMonth()), categorias, imagen, tipoImagen);

            Long id = sistema.listarEventos().stream().filter(e -> e.getNombre().equalsIgnoreCase(nombre.trim())).map(DTEvento::getId).findFirst().orElseThrow();
            response.sendRedirect(request.getContextPath() + "/evento?id=" + id);

        } catch (IllegalArgumentException | IllegalStateException e) {
            String mensaje = e instanceof IllegalStateException
                    ? "La imagen no puede superar los 5 MB."
                    : e.getMessage();
            request.setAttribute("error", mensaje);
            request.setAttribute("seleccionadas", request.getParameterValues("categorias"));
            doGet(request, response);
        }
    }
}
