package presentacionweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logica.DataTypes.DTEdicion;
import logica.sistema01.ISistema;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet("/alta-tipo-registro")
public class AltaTipoRegistroServlet extends HttpServlet {

    private static final String VISTA = "/WEB-INF/vistas/altaTipoRegistro.jsp";
    private static final String TOKEN = "csrfAltaTipoRegistro";
    private static final String MENSAJE = "mensajeAltaTipoRegistro";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = obtenerSesionOrganizador(request, response);
        if (sesion == null) {
            return;
        }

        String nickname = (String) sesion.getAttribute("nicknameUsuario");
        try {
            prepararVista(request, obtenerSistema(), nickname);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (IllegalArgumentException | SecurityException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("mensajeExito", sesion.getAttribute(MENSAJE));
        sesion.removeAttribute(MENSAJE);
        mostrarVista(request, response, sesion);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = obtenerSesionOrganizador(request, response);
        if (sesion == null) {
            return;
        }

        Object token = sesion.getAttribute(TOKEN);
        if (!(token instanceof String) || !token.equals(request.getParameter("csrf"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Long idEdicion;
        try {
            idEdicion = Long.valueOf(request.getParameter("idEdicion"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ISistema sistema = obtenerSistema();
        String nickname = (String) sesion.getAttribute("nicknameUsuario");
        try {
            prepararVista(request, sistema, nickname);
        } catch (IllegalArgumentException | SecurityException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            float costo = Float.parseFloat(texto(request, "costo"));
            int cupo = Integer.parseInt(texto(request, "cupo"));
            sistema.altaTipoRegistro(
                    nickname, idEdicion, texto(request, "nombre"),
                    texto(request, "descripcion"), costo, cupo);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "El costo y el cupo deben ser numeros validos.");
            conservarCampos(request);
            mostrarVista(request, response, sesion);
            return;
        } catch (SecurityException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            conservarCampos(request);
            mostrarVista(request, response, sesion);
            return;
        }

        sesion.setAttribute(MENSAJE, "Tipo de registro creado correctamente.");
        response.sendRedirect(response.encodeRedirectURL(
                request.getContextPath() + "/alta-tipo-registro"));
    }

    private HttpSession obtenerSesionOrganizador(
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession sesion = request.getSession(false);
        Object nickname = sesion == null ? null : sesion.getAttribute("nicknameUsuario");
        if (!(nickname instanceof String valor) || valor.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        if (!"ORGANIZADOR".equals(sesion.getAttribute("rolUsuario"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        return sesion;
    }

    private ISistema obtenerSistema() throws ServletException {
        Object sistema = getServletContext().getAttribute("sistema");
        if (!(sistema instanceof ISistema instancia)) {
            throw new ServletException("La logica no esta inicializada.");
        }
        return instancia;
    }

    private void prepararVista(
            HttpServletRequest request, ISistema sistema, String nickname) {

        List<DTEdicion> ediciones = sistema.listarEdicionesOrganizador(nickname);
        request.setAttribute("ediciones", ediciones);

        String parametro = request.getParameter("idEdicion");
        if (parametro != null) {
            Long idEdicion = Long.valueOf(parametro);
            DTEdicion seleccionada = ediciones.stream()
                    .filter(edicion -> edicion.getId().equals(idEdicion))
                    .findFirst()
                    .orElseThrow(() -> new SecurityException("Edicion no disponible."));
            request.setAttribute("edicionSeleccionada", seleccionada);
        }
    }

    private void mostrarVista(
            HttpServletRequest request, HttpServletResponse response, HttpSession sesion)
            throws ServletException, IOException {

        if (sesion.getAttribute(TOKEN) == null) {
            sesion.setAttribute(TOKEN, UUID.randomUUID().toString());
        }
        request.setAttribute("csrf", sesion.getAttribute(TOKEN));
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    private String texto(HttpServletRequest request, String campo) {
        String valor = request.getParameter(campo);
        return valor == null ? "" : valor.trim();
    }

    private void conservarCampos(HttpServletRequest request) {
        for (String campo : List.of("nombre", "descripcion", "costo", "cupo")) {
            request.setAttribute(campo, request.getParameter(campo));
        }
    }
}