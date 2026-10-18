package presentacionweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logica.DataTypes.DTEvento;
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

        List<DTEvento> eventos = sistema.listarEventos();

        request.setAttribute("eventos", eventos);

        request.getRequestDispatcher(
                "/WEB-INF/vistas/consulta_tipo_registro.jsp"
        ).forward(request, response);
    }
}