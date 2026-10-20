package presentacionweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logica.DataTypes.DTEvento;
import logica.sistema01.Fabrica;
import logica.sistema01.ISistema;

import java.io.IOException;
import java.util.List;

@WebServlet({"/eventos", "/evento"})
    public class ConsultaEventoServlet extends HttpServlet {

    private final ISistema sistema = Fabrica.getInstancia().getISistema();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("/eventos".equals(request.getServletPath())) {
            String categoria = request.getParameter("categoria");
            List<DTEvento> eventos = (categoria == null || categoria.isBlank())
                    ? sistema.listarEventos()
                    : sistema.listarEventosPorCategoria(categoria);
            request.setAttribute("eventos", eventos);
            request.setAttribute("categoria", categoria);
            request.getRequestDispatcher("/WEB-INF/vistas/eventos/eventos.jsp").forward(request, response);
            return;
        }

        // /evento?id=N
        Long id;
        try {
            id = Long.valueOf(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        DTEvento evento;
            try {
            evento = sistema.mostrarDatosEvento(id);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("evento", evento);
        request.setAttribute("ediciones", sistema.listarEdiciones(id));
        request.getRequestDispatcher("/WEB-INF/vistas/eventos/evento_detalle.jsp").forward(request, response);
    }
}