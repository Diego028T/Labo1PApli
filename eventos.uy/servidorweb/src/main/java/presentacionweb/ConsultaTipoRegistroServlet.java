package presentacionweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logica.DataTypes.DTEvento;
import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTTipoRegistro;
import logica.sistema01.ISistema;

import java.io.IOException;
import java.util.List;

@WebServlet("/consulta-tipo-registro")
public class ConsultaTipoRegistroServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        ISistema sistema =
                (ISistema) getServletContext().getAttribute("sistema");

        if (sistema == null) {
            throw new ServletException("La logica no esta inicializada.");
        }

        String parametroEvento = request.getParameter("idEvento");

        try {
            if (parametroEvento == null) {
                request.setAttribute("eventos", sistema.listarEventos());
            } else {
                Long idEvento = Long.valueOf(parametroEvento);
                // Provisorio: sustituir por la consulta de ediciones Aceptadas.
                List<DTEdicion> ediciones = sistema.listarEdiciones(idEvento);
                request.setAttribute("idEventoSeleccionado", idEvento);
                request.setAttribute("ediciones", ediciones);

                String parametroEdicion = request.getParameter("idEdicion");
                if (parametroEdicion != null) {
                    Long idEdicion = Long.valueOf(parametroEdicion);
                    boolean disponible = ediciones.stream()
                            .anyMatch(edicion -> edicion.getId().equals(idEdicion));
                    if (!disponible) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    List<DTTipoRegistro> tipos = sistema.listarTiposRegistro(idEdicion);
                    request.setAttribute("tiposRegistro", tipos);
                    request.setAttribute("idEdicionSeleccionada", idEdicion);

                    String parametroTipo = request.getParameter("idTipoRegistro");
                    if (parametroTipo != null) {
                        Long idTipo = Long.valueOf(parametroTipo);
                        DTTipoRegistro seleccionado = tipos.stream()
                                .filter(tipo -> tipo.getId().equals(idTipo))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Tipo no disponible."));
                        request.setAttribute("tipoSeleccionado", seleccionado);
                    }
                }
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.getRequestDispatcher(
                "/WEB-INF/vistas/consultaTipoRegistro.jsp"
        ).forward(request, response);
    }
}